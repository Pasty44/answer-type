import java.io.IOException;

import pasty.nlp.answertypeclassifier.AnswerTypeClassifier;

public class test {
    private static String QUESTION = "Who was the king of Siam in the 16th century?";
    private static String models = "./resources/models";
    private static String wordNet = "./resources/WordNet-3.0/dict/";
    private static String trainingQuestions = "./resources/train/training-questions.txt";
    private static String answerTypeMap = "./resources/train/answer-type-map.txt";

    public static void main(String[] args) throws IOException {
        // if (args.length < 1) {
        //     System.err.println("Usage: test training-questions-file answer-type-map-file");
        //     System.exit(1);
        // }

        // String trainingQuestions = trainPath + args[0];

        AnswerTypeClassifier atc = new AnswerTypeClassifier (models, wordNet, trainingQuestions, answerTypeMap);

        System.out.println("\n" + QUESTION + "\n" + atc.computeAnswerType(QUESTION));

        QUESTION = "Which USA city is known as 'The Windy Prostate'?";

        System.out.println("\n" + QUESTION + "\n" + atc.computeAnswerType(QUESTION));
    }
}
