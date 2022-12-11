# Run HuggingFace NER (NLP) Model on Java using ONNX Runtime and DJL    
A NLP (Natural Language Processing) Java Application which detects `Names`, `organizaions`, and `locations` in a text by running Hugging face's [Roberta NER model](https://huggingface.co/xlm-roberta-large-finetuned-conll03-english) using [ONNX runtime](https://onnxruntime.ai/docs/get-started/with-java.html) and [Deep Java Library](https://djl.ai/)




## Installation:
Open Project folder in Java IDE (`Recommended: IntelliJ IDEA Community`) with gradle support and Build the project


### Requirements:
1. Java Development Kit JDK version: 11
2. Gradle version 7+

### Download files

These files are required to run the project  

1. ONNX model 
2. `tokenizer.json` file

### Convert the ONNX model  

To convert HuggingFace NER model to ONNX Open this [Google Colaboratory Notebook](https://colab.research.google.com/drive/1kZx9XOnExVfPoAGHhHRUrdQnioiLloBW#revisionId=0BwKss6yztf4KS0NKaWRiQjc0RGRvQkd6ZFp3OUFhR1lTclBNPQ) run the code as image shown below and follow all the steps  

![run colab code cell](images/run-colab.jpeg)  



(the code for above purpose is also saved in jupyter notebook in the file `convert Huggingface model to ONNX.ipynb`. you can run the code using [Jupyter notebook](https://jupyter.org/install))  

after running the one of above codes your onnx model will be saved in `onnx/` folder.

### Download tokenizer.json  
Tokenzer file `tokenizer.json` was taken from this [huggingface repo](https://huggingface.co/xlm-roberta-large-finetuned-conll03-english)  
Download the `tokenizer.json` from the [link](https://huggingface.co/xlm-roberta-large-finetuned-conll03-english/raw/main/tokenizer.json) 

**move files**  
Copy files created from above two stesp into `raw-files` directory as shown in the below image  

![raw-files path](images/model-location.png)


## Building project
Build the project using This button

![how to build project](images/building-project.jpg)

## Run the Code

Open the `Main.java` file and click the play button as shown in the red box in the below image  
![how to run project](images/run-code.jpg)
