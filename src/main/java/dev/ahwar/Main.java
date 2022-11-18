package dev.ahwar;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;

import java.io.IOException;
import java.util.Map;

public class Main {


    public static void main(String[] args) throws IOException {
        OnnxTensor inputTensor;

        try {
            /*
             * Creating ONNX environment and session
             * */
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            String model_path = "src/main/java/dev/ahwar/roberta-base-11.onnx";
            // create an onnx-runtime session
            OrtSession session = env.createSession(model_path, new OrtSession.SessionOptions());


            /*
             * input and output layers Info
             * */
            System.out.printf("Model Input Names:  %s\nModel Input info:  %s\n" +
                            "Model Output Names:  %s\nModel Output info:  %s",
                            session.getInputNames(), session.getInputInfo(),
                            session.getOutputNames(), session.getOutputInfo());

            /*
             * Create dummy input and convert to OnnxTensor
             * */
            // dummy inputs
            long[][] inputArray = new long[1][20];
            // create tensor
            inputTensor = OnnxTensor.createTensor(env, inputArray);
            // map inputTensor according to model Input
            var model_inputs = Map.of("input_ids", inputTensor);


            /*
             * Running the inference on the model
             * */
            Result output = session.run(model_inputs);

            /*
            * Handling the output
            * */
            float[][][] output_1 = (float[][][]) output.get(0).getValue();
            float[][] output_2 = (float[][]) output.get(1).getValue();

        } catch (OrtException e) {
            e.printStackTrace();
        }

    }
}