package org.example;

import java.io.*;
import java.util.HashMap;

import static org.example.Huffman.*;

public class HuffmanCompressing {
    public static void compress(String inputFile,String output) throws IOException {
        String input = readFile(inputFile);

        // Encode the input data using Huffman coding
        HashMap<Character,String> codes = huffmanEncoding(input);

        // Write the encoded data to a binary file
        BitOutput out = new BitOutput(new FileOutputStream(output));
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            String code = codes.get(c);
            for (int j = 0; j < code.length(); j++) {
                out.writeBit(code.charAt(j) == '1' ? 1 : 0);
            }
        }
        out.flush();
        out.close();
    }

    public static void decompress(String source,String output) throws IOException {
        HashMap<String, Character> codes = readDictionaryFromFile("dictionary.dict");

        // Read the encoded data from the binary file
        BitInput  in = new BitInput(new FileInputStream(source));
        StringBuilder sb = new StringBuilder();
        String currCode = "";
        while (true) {
            int bit = in.readBit();
            if (bit == -1) {
                break;
            }
            currCode += (bit == 1 ? '1' : '0');
            if (codes.containsKey(currCode)) {
                sb.append(codes.get(currCode));
                currCode = "";
            }
        }
        in.close();
        //write file
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        writer.write(sb.toString().trim());
        writer.close();
    }
}
