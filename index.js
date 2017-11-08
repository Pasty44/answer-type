/**
 * answer-type
 *
 * An NPM module for determining the answer type of a question
 *
 * Copyright(c) 2017 Andrew Dinihan
 * MIT Licensed
 */

 var answerTypeClassifier;

/**
 * Defaults
 */

var TRAINING_QUESTIONS = __dirname + '/openNLP/resources/train/training-questions.txt';
var ANSWER_TYPE_MAP = __dirname + '/openNLP/resources/train/answer-type-map.txt';
var OPENNLP_ANSWER_TYPE_CLASSIFICATION_JAR = __dirname + '/openNLP/build/libs/OpenNLPAnswerTypeClassifier-1.0.0.jar';
var ANSWER_TYPE_CLASSIFICATION_CLASS = 'pasty.nlp.answertypeclassifier.AnswerTypeClassifier';
var BIN_MODELS_FOLDER = __dirname + '/openNLP/resources/models/';
var WORDNET_DICTIONARY = __dirname + '/openNLP/resources/WordNet-3.0/dict/';

/**
 * Dependencies
 */

const java = require('java');

/**
 * Train the model
 *
 * @param {String} [trainingQuestions = TRAINING_QUESTIONS] - path of the training questions file
 * @param {String} [answerTypeMap = ANSWER_TYPE_MAP] - path of the answer type map file
 * @api public
 */

exports.trainAnswerTypeClassifier = function (trainingQuestions=TRAINING_QUESTIONS, answerTypeMap=ANSWER_TYPE_MAP) {
    java.classpath.push(OPENNLP_ANSWER_TYPE_CLASSIFICATION_JAR);
    java.import(ANSWER_TYPE_CLASSIFICATION_CLASS);
    answerTypeClassifier = java.newInstanceSync(
        ANSWER_TYPE_CLASSIFICATION_CLASS,
        BIN_MODELS_FOLDER,
        WORDNET_DICTIONARY,
        trainingQuestions,
        answerTypeMap
    );
    exports.answerTypeClassifier = answerTypeClassifier;
}

/**
 * Compute the answer type for a particular question
 *
 * @param {String} question
 * @api public
 */

exports.getAnswerType = function (question) {
    let response = answerTypeClassifier.computeAnswerTypeSync(question);
    return ({ type: response[0], prob: response[1] });
}
