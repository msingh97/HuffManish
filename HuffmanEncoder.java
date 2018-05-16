package huffmanish;

import java.io.*;
import java.util.*;


public class HuffmanEncoder {
    private static HashMap<Character, Integer> getFrequencies(String filename) throws FileNotFoundException,
            IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        HashMap<Character, Integer> frequencies = new HashMap<Character, Integer>();
        while (reader.ready()) {
            char c = (char) reader.read();
            if (frequencies.containsKey(c)) {
                frequencies.put(c, 1 + frequencies.get(c));
            } else {
                frequencies.put(c, 1);
            }
        }
        reader.close();
        return frequencies;
    }

    private static PriorityQueue<HuffmanNode> makePQ(HashMap<Character, Integer> frequencies) {
        // Reverse comparator - to ensure higher frequency items are removed from the priority queue first.
        Comparator<HuffmanNode> comparator = new Comparator<HuffmanNode>() {
            public int compare(HuffmanNode o1, HuffmanNode o2) {
                return o1.getFrequency() - o2.getFrequency();
            }
        };

        PriorityQueue<HuffmanNode> PQ = new PriorityQueue<>(frequencies.keySet().size(), comparator);
        for (Character c : frequencies.keySet()) {
            HuffmanNode<Character> node = new HuffmanNode<Character>(c, frequencies.get(c));
            PQ.add(node);
        }
        return PQ;
    }

    private static HuffmanNode<Character> makeTree(PriorityQueue<HuffmanNode> PQ) {
        while (PQ.size() > 1) {
            HuffmanNode a = PQ.remove();
            HuffmanNode b = PQ.remove();
            HuffmanNode c = new HuffmanNode(a, b);
            PQ.add(c);
        }
        return PQ.remove();
    }

    private static BitSet makeBitSet(String filename, Map<Character, Integer> frequencies,
                                     Map<Character, String> codes) throws IOException {
        // Calculate total number of bits to represent the characters.
        int totalBits = 0;
        for (char c : frequencies.keySet()) {
            totalBits += frequencies.get(c) * codes.get(c).length();
        }
        BitSet fileBitSet = new BitSet(totalBits);

        int currBitIndex = 0;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while (reader.ready()) {
            char current = (char) reader.read();
            String code = codes.get(current);
            for (int i = 0; i < code.length(); i++) {
                if (code.charAt(i) == '1') {
                    fileBitSet.set(currBitIndex + i);
                }
            }
            currBitIndex += code.length();
        }
        return fileBitSet;
    }

    public static void main( String[] args )
    {
        if (args.length < 1) {
            System.out.println("ERROR: Invalid arguments. Must specify read/write and filename as args.");
            return;
        }

        String filename = args[0];
        try {
            HashMap<Character, Integer> frequencies = getFrequencies(filename);
            PriorityQueue<HuffmanNode> PQ = makePQ(frequencies);
            if (PQ.size() == 0) {
                System.out.println("Found empty file, no need to compress.");
                return;
            }

            HuffmanTree tree = new HuffmanTree(makeTree(PQ));
            Map<Character, String> codes = tree.getCodes();
            BitSet fileBitSet = makeBitSet(filename, frequencies, codes);

            String compressedFileName = filename + ".huffmanish";
            FileOutputStream outputStream = new FileOutputStream(compressedFileName, false);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(tree);
            objectStream.writeObject(fileBitSet);
            objectStream.close();
            outputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found.");
        } catch (java.io.IOException e) {
            System.out.println("ERROR with inputted file: IO Exception.");
        }
    }
}
