package pasty.nlp.answertypeclassifier;

/**
*   The AnswerTypeClassifier class uses OpenNLP modules and the WordNet dictionary
*   to do any parsing and syntactic analysis of the question.
*
*   It uses OpenNLP's maxent model and the input training files to create the
*   answer type classification model. This model can then be applied to unseen
*   questions. When doing so, it calculates the probability of each answer type
*   for that question. The one with the highest probability is the one chosen
*   as that question's answer type
*
*   Default values for the training files can be found in the answer-type NPM
*   module's index.js file
*
*   @author Andrew Dinihan
*
*/

import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.model.MaxentModel;
import opennlp.model.TwoPassDataIndexer;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class AnswerTypeClassifier {

    /*
        OpenNLP model .bin files
    */
    private final String CHUNKER_MODEL = "en-chunker.bin";
    private final String MAXENT_MODEL = "en-pos-maxent.bin";

    /*
        Training iterations - how many times the training algorithm refines its model
        More iterations make it more accurate but increase training time
        250 is a good balance between accuracy and training speed
    */
    private final int TRAINING_ITERATIONS = 250;

    /*
        Whether or not to print the output from OpenNLP model training
        It normally prints out the outcome of each training iteration
        This can be good to see the result of the training algorithm,
        which is useful if you've implemented a custom set of training data
        and want to see how the model is performing, but it can output
        hundreds of lines to the console which can be annoying
    */
    private final boolean PRINT_MODEL_TRAINING_OUTPUT = false;

    /********************************************************************/

    private MaxentModel model;
    private double[] probs;
    private AnswerTypeContextGenerator atcg;
    private Parse parse;
    private Parser parser;
    private HashMap<String, String> answerTypes;

    public AnswerTypeClassifier(String models, String wordNet, String trainingFile, String answerTypeMapFile) throws IOException {
        // Load all the modules for parsing the question
        File modelsDir = new File(models);
        InputStream chunkerStream = new FileInputStream(new File(modelsDir,CHUNKER_MODEL));
        ChunkerModel chunkerModel = new ChunkerModel(chunkerStream);
        ChunkerME chunker = new ChunkerME(chunkerModel);
        InputStream posStream = new FileInputStream(new File(modelsDir,MAXENT_MODEL));
        POSModel posModel = new POSModel(posStream);
        POSTaggerME tagger =  new POSTaggerME(posModel);
        this.parser = new ChunkParser(chunker, tagger);
        this.atcg = new AnswerTypeContextGenerator(new File(wordNet));
        AnswerTypeEventStream es = new AnswerTypeEventStream(trainingFile,atcg, parser);

        // Train the model
        if (PRINT_MODEL_TRAINING_OUTPUT == true) {
            this.model = GIS.trainModel(TRAINING_ITERATIONS, new TwoPassDataIndexer(es, 3));
        }
        else {
            PrintStream psSave = new PrintStream(System.out);
            System.setOut(new PrintStream(new OutputStream() {public void write(int b) {}}));
            this.model = GIS.trainModel(TRAINING_ITERATIONS, new TwoPassDataIndexer(es, 3));
            System.setOut(psSave);
        }

        // Instantiate array used to hold probability of each answer type for a question
        this.probs = new double[model.getNumOutcomes()];

        // Create map representing answer types
        loadAnswerTypes(answerTypeMapFile);
    }

    public String[] computeAnswerType(String question) {
        this.parse = ParserTool.parseLine(question, this.parser, 1)[0];
        double[] probs = computeAnswerTypeProbs();
        return new String[] {answerTypes.get(model.getBestOutcome(probs)), String.valueOf(Arrays.stream(probs).max().getAsDouble())};
    }

    private double[] computeAnswerTypeProbs() {
        String[] context = atcg.getContext(this.parse);
        return model.eval(context, probs);
    }

    private void loadAnswerTypes(String answerTypeMapFile) throws IOException {
        answerTypes = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader(new FileReader(answerTypeMapFile));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("-");
            if (parts.length != 2) {
                throw new IOException("Invalid answer type mapping in '" + answerTypeMapFile +"', format should be <letter>-<answer type>\nSee answer-type NPM module documentation for examples");
            }
            String key = parts[0];
            String value = parts[1];
            answerTypes.put(key, value);
        }
    }

}
