package dev.ahwar;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
public class Main {


    public static void main(String[] args) {
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            String model_path = "C:\\Users\\pakle\\IdeaProjects\\onnx-rt\\src\\main\\java\\dev\\ahwar\\roberta-sequence-classification-9.onnx";
            OrtSession session = env.createSession(model_path, new OrtSession.SessionOptions());
            System.out.println(session.getInputInfo());
            System.out.println(session.getInputNames());

            // TODO: how to do BertTokenizer? In Python:
            // from transformers import BertTokenizerFast
            // need this for Java...
            // Inference with Java BERT NLP Deep Learning and ONNX Runtime
            // session.run(null);
        } catch (OrtException e) {
            e.printStackTrace();
        }

        System.out.println("Hello world!");
    }
}