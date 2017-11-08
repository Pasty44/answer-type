/**
 * Instantiation of the answer type classifier
 */

// Increase timeout to allow time for model to train
jasmine.DEFAULT_TIMEOUT_INTERVAL = 30000;

const answerType = require('..');

describe('instantiation', () => {
    test('classifier initially undefined (not exported)', () => {
        expect(answerType.answerTypeClassifier).toEqual(undefined);
    });
    test('classifier defined after instantiation', done => {
        answerType.trainAnswerTypeClassifier();
        expect(answerType.answerTypeClassifier).not.toEqual(undefined);
        done();
    });
});
