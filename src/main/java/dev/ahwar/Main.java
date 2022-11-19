package dev.ahwar;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static ai.djl.huggingface.tokenizers.HuggingFaceTokenizer.newInstance;

public class Main {

    static long[] inputIds;

    public static long[] encodeText(String inputText, String tokenizerJsonPath) throws IOException {
        /*
         * Encode the text according to tokenizer
         * Returns encoded Ids
         * */
        try (HuggingFaceTokenizer tokenizer = newInstance(Paths.get(tokenizerJsonPath))) {
            Encoding encoding = tokenizer.encode(inputText, true);
            inputIds = encoding.getIds();
            return inputIds;
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            /*
             * Creating ONNX environment and session
             * */
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            String model_path = "raw-files/roberta-base-11.onnx";
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
             * Encode Text and convert to OnnxTensor
             * */

            // Encode Text
            try {
                inputIds = encodeText("Text to encode: Hello, World", "raw-files/tokenizer.json");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


            /*
             * Create dummy input and convert to OnnxTensor
             * */
            // modify Encoded Ids according to the model requirement
            long[][] inputArray = new long[1][inputIds.length];
            System.arraycopy(inputIds, 0, inputArray[0], 0, inputIds.length);
            // create OnnxTensor
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, inputArray);
            // map inputTensor according to model Input
            var model_inputs = Map.of("input_ids", inputTensor);


            /*
             * Running the inference on the model
             * */
            Result output = session.run(model_inputs);

            /*
             * Handling the inference output
             * */
            float[][][] output_1 = (float[][][]) output.get(0).getValue();
            float[][] output_2 = (float[][]) output.get(1).getValue();

        } catch (OrtException e) {
            e.printStackTrace();
        }

    }
}