package huffmanish;


import java.io.*;
import java.util.BitSet;

public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("ERROR: No arguments.");
        }
        String compressedFilename = args[0];
        if (!compressedFilename.endsWith(".huffmanish")) {
            System.out.println("ERROR: Must supply a valid .huffmanish file as argument.");
        }
        String filename = compressedFilename.substring(0, compressedFilename.length() - 11);

        try {
            FileInputStream inputStream = new FileInputStream(compressedFilename);
            ObjectInputStream objectStream = new ObjectInputStream(inputStream);
            HuffmanTree tree = (HuffmanTree) objectStream.readObject();
            BitSet bitSet = (BitSet) objectStream.readObject();

            StringBuilder decoded = tree.decode(bitSet);
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(decoded.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: inputted file not found.");
        } catch (IOException e) {
            System.out.println("ERROR with the inputted file: IO Exception.");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR:" + e.getMessage());
        }
    }
}
