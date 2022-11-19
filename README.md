# Running ONNX NLP Model using Java


## Download files

These files are required to run the project  

1. ONNX model 
2. `tokenizer.json` file

**Download the model**  


Model used in this project (`RoBERTa-BASE`) was downloaded from this [link](https://github.com/onnx/models/raw/main/text/machine_comprehension/roberta/model/roberta-base-11.onnx)

Download the model from the link and save in `raw-files` directory


Tokenzer file `tokenizer.json` was taken from this [huggingface repo](https://huggingface.co/roberta-base)  
Download the `tokenizer.json` from the [link](https://huggingface.co/roberta-base/raw/main/tokenizer.json) and save in `raw-files` directory


## Installation:
Open Project folder in Java IDE (`Recommended: IntelliJ IDE`) with gradle support and Build the project


### Requirements:
1. Java Development Kit JDK version: 11
2. Gradle version 7+

### Building project
Build the project using This button

![how to build project](images/building-project.jpg)

## Run the Code

Open the `Main.java` file and click the play button as shown in the red box in the below image  
![how to run project](images/run-code.jpg)
