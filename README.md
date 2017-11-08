# answer-type.js

A module for using NLP to determine the answer type of a given question.

This module uses the `OpenNLP` library (v1.5.3).

<img src="https://opennlp.apache.org/img/opennlp-logo.png" width="450" alt="opennlp-logo" />

## Installation
##### Using NPM
```node
npm install answer-type
```

## Usage
##### Require the module
```javascript
let answerType = require('answer-type');
```

##### Train the classifier
```javascript
answerType.trainAnswerTypeClassifier();
```

### Get answer type
```javascript
// Returns answer type and probability (where 1.0 = 100%)

answerType.getAnswerType('Who was king of France in the late 17th century?');
// => { type: 'person', prob: 0.9904253513937077 }
```

---

## Using custom training data
You have the option to use custom training data for this module. To do so you need two files:

1. A set of training questions
2. A mapping file which connects the answer type letter to a word(s) that describes what it actually represents

##### Answer type map
In the training data, rather than have an entire word representing answer type it is more neat and concise to simply use a letter to represent the answer type. However, this means that when you classify the answer type the model will simply return a letter. In order to actually determine what answer type this letter is connected to, we can use a map file. This is simply the letter, a dash and what the answer type is, like so:

```
<letter>-<description>
```

Here's an example of a map file with 3 answer types:

```
P-person
L-location
A-amount
```

##### Training questions
This contains all the questions the model will be trained on. In order to get a usable level of accuracy you will need quite a lot of **unique** questions. Insufficient questions or poor training data (such as overlapping answer types or duplicated questions) will produce an inaccurate model that will perform poorly on unseen questions. The default classifier of this module has 1888 questions and 13 answer types, which is around 145 questions per answer type. Around 100 questions per answer type would be a good starting point.

The format of training question files is as follows:

```
<A letter representing an answer type> <The question>
```

Here's an example of a training questions file (with answer types from the sample map file above):

```
P Which French monarch reinstated the divine right of the monarchy to France?
L Which island in The Visayas (in central Phillipines) is the biggest?
A How many sides does a heptagon have?
```

Once you have your training questions and answer type map file, you need to pass them to the answer type classifier during the training command. The questions file is the first parameter and the map file is the second:

```javascript
answerType.trainAnswerTypeClassifier(
    'files/training/custom-questions.txt',
    'files/map/custom-answer-type-map.txt'
);
```

Once it is trained, you can then use the `getAnswerType()` function to apply the trained model to a question. To view the training and map files for the default classifier in this module, have a look in the `openNLP/resources/train` folder.

---

## Customising the model training and answer type classification
This part is done in the `AnswerTypeClassifier.java` file, located in `openNLP/src/pasty/nlp/answertypeclassifier`. Documentation about that file and its processes can be found in the java file itself so they won't be duplicated here.

Once you've made changes you'll need to rebuild the jar. Gradle is used for java compilation, navigate into the `openNLP` folder and run `gradle fatJar` to create a jar with the dependencies embedded.

---

## Background
The answer classification functionality in this module is inspired by the book 'Taming Text'. In chapter 8 section 4 (may vary based on book version) it shows how to make a simple question/answer system using Solr. The first part of this is using OpenNLP to train a model on questions and using that model to determine the answer type of unseen questions. The questions used to train this module's default classifier are from the Taming Text ebook data and consist of 1888 questions manually tagged with their answer type by Tom Morton for his PhD thesis in 2005.

In the book, they have a command to train the model on questions and then export a .bin file representing the model. This .bin file can be loaded by an answer type classifier, meaning you don't have to train the model every time you launch the classifier. For reference, training the model takes about 10-15 seconds on a modern computer. On slower computers with less processing power (e.g. a basic laptop or a small AWS EC2 instance) this might take longer. So what are the advantages of exporting this .bin file?

1. To instantiate an answer type classifier you can just use the .bin file (which is close to instant) rather than training for each classifier instance
2. You can just port the .bin file over to your production environment, you don't need to take all the training files
3. You could make a variation of this module wherein the answer type classifier java instance is created each time you compute the answer type and deleted after. This means the answer type classifier class instance wouldn't have to be permanently sitting in memory whilst your server is running, it could be created and destroyed only as it's needed

Ultimately in a production environment it would be cleaner just to have the model, however since it only trains once (at start up) and the training doesn't take long on most competent computers and the class instance doesn't take up much space in memory then implementing the ability to export to a .bin for portability and quick loading isn't a priority. However, down the line it may be implemented to make things a little cleaner and a little easier for less powerful computers.

### License
MIT
