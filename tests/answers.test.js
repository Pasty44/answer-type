/**
 * Each of the answer types supproted by the classifier
 */

// Increase timeout to allow time for model to train
 jasmine.DEFAULT_TIMEOUT_INTERVAL = 30000;

 const answerType = require('..');
 answerType.trainAnswerTypeClassifier();

 describe('classification', () => {
     test('person', () => {
         let at = answerType.getAnswerType("Who was king of France in the late 17th century?");
         expect(at.type).toEqual('person');
     });
     test('location', () => {
         let at = answerType.getAnswerType("In what country is the longest river located?");
         expect(at.type).toEqual('location');
     });
     test('organization', () => {
         let at = answerType.getAnswerType("What company invented the Macintosh?");
         expect(at.type).toEqual('organization');
     });
     test('timepoint', () => {
         let at = answerType.getAnswerType("When was the wheel invented?");
         expect(at.type).toEqual('timepoint');
     });
     test('duration', () => {
         let at = answerType.getAnswerType("How long does the average person spend sleeping?");
         expect(at.type).toEqual('duration');
     });
     test('money', () => {
         let at = answerType.getAnswerType("What is the cost of a 2 bedroom house in Los Angeles?");
         expect(at.type).toEqual('money');
     });
     test('percentage', () => {
         let at = answerType.getAnswerType("What percent of peoplein America are Hispanic?");
         expect(at.type).toEqual('percentage');
     });
     test('amount', () => {
         let at = answerType.getAnswerType("How many grandslams has Roger Federer won?");
         expect(at.type).toEqual('amount');
     });
     test('distance', () => {
         let at = answerType.getAnswerType("How far is it from Sydney to Bangkok?");
         expect(at.type).toEqual('distance');
     });
     test('description', () => {
         let at = answerType.getAnswerType("What is a winter solstice?");
         expect(at.type).toEqual('description');
     });
     test('title', () => {
         let at = answerType.getAnswerType("What is the name of the movie containing the quote 'Run Forest, Run!'?");
         expect(at.type).toEqual('title');
     });
     test('definition', () => {
         let at = answerType.getAnswerType("What does HTML stand for?");
         expect(at.type).toEqual('definition');
     });
     test('other', () => {
         let at = answerType.getAnswerType("Why do wolves howl at the moon?");
         expect(at.type).toEqual('other');
     });
 });
