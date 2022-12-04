package dev.ahwar;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.util.Pair;
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

    static Pair<long[], long[]> inputIds_mask;

    public static Pair<long[], long[]> encodeText(String inputText, String tokenizerJsonPath) throws IOException {
        /*
         * Encode the text according to tokenizer
         * Returns encoded Ids
         * */
        try (HuggingFaceTokenizer tokenizer = newInstance(Paths.get(tokenizerJsonPath))) {
            Encoding encoding = tokenizer.encode(inputText, true);
            return new Pair<>(encoding.getIds(), encoding.getAttentionMask());
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            /*
             * Creating ONNX environment and session
             * */
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            String model_path = "raw-files/onnx/model.onnx";
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
                inputIds_mask = encodeText("James is working at Nasa", "raw-files/tokenizer.json");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


            /*
             * Create dummy input and convert to OnnxTensor
             * */
            // modify Encoded Ids according to the model requirement
            // from [input_ids] to [[input_ids]]

            long[][] inputIds = new long[1][inputIds_mask.getKey().length];
            System.arraycopy(inputIds_mask.getKey(), 0, inputIds[0], 0, inputIds_mask.getKey().length);
            // modify Attention Mask according to the model requirement
            // from [attention_mask] to [[attention_mask]]
            long[][] attentionMask = new long[1][inputIds_mask.getValue().length];
            System.arraycopy(inputIds_mask.getValue(), 0, attentionMask[0], 0, inputIds_mask.getValue().length);
            // create OnnxTensor
            OnnxTensor idsTensor = OnnxTensor.createTensor(env, inputIds);
            OnnxTensor maskTensor = OnnxTensor.createTensor(env, attentionMask);
            // map input Tensor according to model Input
            // key is layer name, and value is value you want to pass
            var model_inputs = Map.of("input_ids", idsTensor, "attention_mask", maskTensor);


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