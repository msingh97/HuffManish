package huffmanish;


import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class HuffmanTree implements Serializable {

    private HuffmanNode root;

    public HuffmanTree(HuffmanNode<Character> root) {
        this.root = root;
    }

    public Map<Character, String> getCodes() {
        return this.root.getCodes();
    }

    protected StringBuilder decode(BitSet bitSet) {
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;

        for (int i = 0; i < bitSet.length(); i++) {
            if (current.isLeaf()) {
                decoded.append(current.value);
                current = root;
            }
            if (bitSet.get(i)) {
                current = current.right;
            } else {
                current = current.left;
            }
        }

        return decoded;
    }
}
