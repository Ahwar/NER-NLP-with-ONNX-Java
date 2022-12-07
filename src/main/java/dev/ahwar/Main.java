package dev.ahwar;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class Main {

    static long[] inputIds;
    static long[] inputAttentionMask;
    static Tokenizer tokenizer;
    static String persons = "";
    static String locations = "";
    static String organizations = "";
    static String misc = "";

    public static int findMaxIndex(float[] arr) {
        /*
         * find the index of maximum number in array
         * reference_link: https://qr.ae/pr89bV
         * */
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static void post(int class_, String token, String persons_,
                            String locations_, String organizations_, String misc_) {
    /*
    seperates tokens into arrays according to class ids


    below is the relation from class id to the label
    "id2label": {
    "0": "B-LOC",
    "1": "B-MISC",
    "2": "B-ORG",
    "3": "I-LOC",
    "4": "I-MISC",
    "5": "I-ORG",
    "6": "I-PER",
    "7": "O"
    * */
        if (class_ == 6) persons = persons_ + token;
        else if (class_ == 2 || class_ == 5) organizations = organizations_ + token;
        else if (class_ == 3 || class_ == 0) locations = locations_ + token;
        else if (class_ == 1 || class_ == 4) misc = misc_ + token;
    }

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
                tokenizer.encode("Ahwar wants to work at Google in london. EU rejected German call to boycott British lamb.");
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
            // get output results
            float[][][] logits = (float[][][]) result.get(0).getValue();
            String[] tokens = tokenizer.getTokens(); // tokenize the text

            for (int i = 0; i < logits[0].length; i++) {
                try {
                    // find class ids for all the tokens of the text
                    // and separate them in the arrays
                    post(findMaxIndex(logits[0][i]), tokens[i], persons, locations, organizations, misc);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            /*
            * Showing the results
            * */

            String tokensSpecialChar = String.valueOf(tokenizer.getTokens()[1].charAt(0)); // word seperators in tokens
            System.out.println("All persons in the text: " + Arrays.toString(persons.split(tokensSpecialChar)));
            System.out.println("All Organizations in the text: " + Arrays.toString(organizations.split(tokensSpecialChar)));
            System.out.println("All Locations in the text: " + Arrays.toString(locations.split(tokensSpecialChar)));
            System.out.println("All Miscellanous entities in the text: " + Arrays.toString(misc.split(tokensSpecialChar)));

        } catch (OrtException e) {
            e.printStackTrace();
        }

    }
}