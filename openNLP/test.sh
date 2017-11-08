#!/bin/bash
LG='\033[1;32m'
NC='\033[0m'

echo
echo -e "${LG}Compiling files...${NC}"
javac -cp .:resources/opennlp-tools-1.5.3.jar:resources/jwnl-1.3.3.jar:build/libs/OpenNLPAnswerTypeClassifier-1.0.0.jar *.java
echo
echo -e "${LG}Training...${NC}"
echo
java -cp .:resources/opennlp-tools-1.5.3.jar:resources/jwnl-1.3.3.jar:build/libs/OpenNLPAnswerTypeClassifier-1.0.0.jar test
echo
echo "Finished"
echo
rm -f *.class
