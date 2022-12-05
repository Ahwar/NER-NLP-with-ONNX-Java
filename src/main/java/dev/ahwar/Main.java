package dev.ahwar;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;

import java.io.IOException;
import java.util.Map;

public class Main {

    static long[] inputIds;
    static long[] inputAttentionMask;
    static Tokenizer tokenizer;

    public static void main(String[] args) {

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
                tokenizer = new Tokenizer("raw-files/tokenizer.json");
                tokenizer.encode("James is working at Nasa");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


            /*
             * Calculate Inputs
             * then convert them to OnnxTensor
             * */

            // get Input Ids and Attention Mask
            inputIds = tokenizer.getIds(); // get Input Ids
            inputAttentionMask = tokenizer.getAttentionMask(); // get Attention mask


            // modify Encoded Ids according to the model requirement
            // from [input_ids] to [[input_ids]]
            long[][] newInputIds = new long[1][inputIds.length];
            System.arraycopy(inputIds, 0, newInputIds[0], 0, inputIds.length);


            // modify Attention Mask according to the model requirement
            // from [attention_mask] to [[attention_mask]]
            long[][] newAttentionMask = new long[1][inputAttentionMask.length];
            System.arraycopy(inputAttentionMask, 0, newAttentionMask[0], 0, inputAttentionMask.length);


            // create OnnxTensor
            OnnxTensor idsTensor = OnnxTensor.createTensor(env, newInputIds);
            OnnxTensor maskTensor = OnnxTensor.createTensor(env, newAttentionMask);


            // map input Tensor according to model Input
            // key is layer name, and value is value you want to pass
            var model_inputs = Map.of("input_ids", idsTensor, "attention_mask", maskTensor);


            /*
             * Running the inference on the model
             * */
            Result result = session.run(model_inputs);

            /*
             * Handling the inference output
             * */
            float[][][] logits = (float[][][]) result.get(0).getValue();

        } catch (OrtException e) {
            e.printStackTrace();
        }

    }
}