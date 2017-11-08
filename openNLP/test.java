import java.io.IOException;

import pasty.nlp.answertypeclassifier.AnswerTypeClassifier;

public class test {

    private final static String QUESTION = "Who was the king of Siam in the 16th century?";

    private static String models = "./resources/models";
    private static String wordNet = "./resources/WordNet-3.0/dict/";
    private static String trainingQuestions = "./resources/train/training-questions.txt";
    private static String answerTypeMap = "./resources/train/answer-type-map.txt";

    public static void main(String[] args) throws IOException {
        AnswerTypeClassifier atc = new AnswerTypeClassifier (models, wordNet, trainingQuestions, answerTypeMap);
        String[] response = atc.computeAnswerType(QUESTION);
        System.out.println("\n" + QUESTION);
        System.out.println("Answer type: " + response[0]);
        System.out.println("Probability: " + response[1]);
    }
}
