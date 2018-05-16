package huffmanish;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class HuffmanNode<T> implements Serializable {
    protected int freq;
    protected T value;
    protected HuffmanNode<T> left, right;

    public HuffmanNode(T value, int frequency) {
        this.value = value;
        this.freq = frequency;
        this.left = null;
        this.right = null;
    }

    public HuffmanNode(HuffmanNode<T> left, HuffmanNode<T> right) {
        this.value = value;
        this.freq = left.freq + right.freq;
        this.left = left;
        this.right = right;
    }

    public Map<T, String> getCodes() {
        /* Finds the prefix-free codes for each character stored in the tree. */
        HashMap<T, String> code = new HashMap<T, String>();
        if (this.isLeaf()) {
            code.put(this.value, "0");
            return code;
        }
        StringBuilder codeBuilder = new StringBuilder();
        this.fillCodes(codeBuilder, code);
        return code;
    }

    /* Performs a depth first search, adding the prefix-free codes of each
     leaf node to the HashMap. */
    private void fillCodes(StringBuilder codeBuilder, Map<T, String> code) {
        if (this.isLeaf()) {
            code.put(this.value, codeBuilder.toString());
            return;
        }
        codeBuilder.append('0');
        this.left.fillCodes(codeBuilder, code);
        codeBuilder.deleteCharAt(codeBuilder.length() - 1);
        codeBuilder.append('1');
        this.right.fillCodes(codeBuilder, code);
        codeBuilder.deleteCharAt(codeBuilder.length() - 1);
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public int getFrequency() {
        return this.freq;
    }

}
