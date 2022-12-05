package dev.ahwar;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import java.io.IOException;
import java.nio.file.Paths;

import static ai.djl.huggingface.tokenizers.HuggingFaceTokenizer.newInstance;

public class Tokenizer {

    /*
    * Class for Tokenization and
    * */
    private Encoding encoding;
    private final HuggingFaceTokenizer tokenizer;

    public Tokenizer(String tokenizerJsonPath) throws IOException {
        /*
         * Initialize tokenizer
         */
        tokenizer = newInstance(Paths.get(tokenizerJsonPath));
    }

    public void encode(String inputText) {
        /*
         * Encode Text
         * */
        encoding = tokenizer.encode(inputText);
    }

    public long[] getIds() {
        /*
         * get Ids from encoded tokens
         * */
        return encoding.getIds();
    }

    public long[] getAttentionMask() {
        /*
         * get Attention mask from encoded tokens
         * */
        return encoding.getAttentionMask();
    }
}
