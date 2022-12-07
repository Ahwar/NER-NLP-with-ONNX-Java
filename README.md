# Run HuggingFace NER (NLP) Model as Java using ONNX    


## Download files

These files are required to run the project  

1. ONNX model 
2. `tokenizer.json` file

**Convert the model**  

To convert HuggingFace NER model to ONNX Open this [Google Colaboratory Notebook](https://colab.research.google.com/drive/1kZx9XOnExVfPoAGHhHRUrdQnioiLloBW#revisionId=0BwKss6yztf4KS0NKaWRiQjc0RGRvQkd6ZFp3OUFhR1lTclBNPQ) run the code as below shown image and follow all the steps  

![run colab code cell](images\run-colab.jpeg)  



(the code for above purpose is also saved in jupyter notebook in the file `convert Huggingface model to ONNX.ipynb`. you can run the code using [Jupyter notebook](https://jupyter.org/install))

Tokenzer file `tokenizer.json` was taken from this [huggingface repo](https://huggingface.co/xlm-roberta-large-finetuned-conll03-english)  
Download the `tokenizer.json` from the [link](https://huggingface.co/xlm-roberta-large-finetuned-conll03-english/raw/main/tokenizer.json) and save in `raw-files` directory


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
