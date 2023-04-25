package org.example;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        // Encode the input string using Huffman coding

        /*String input = "abracadabra";

        // Encode the input string using Huffman coding
        HashMap<Character, String> codes = Huffman.huffmanEncoding(input);

        // Print the character codes
        for (char ch : codes.keySet()) {
            System.out.println(ch + ": " + codes.get(ch));
        }

        // Print the encoded string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            sb.append(codes.get(ch));
        }
        String encoded = sb.toString();
        System.out.println("Encoded string: " + encoded);*/



        // Decode the encoded string using the codes
        /*String encoded="01101001110011110110100";
        String codes=Huffman.huffmanDecoding(encoded, "dictionnary.dict");
        // Print the decoded string
        System.out.println("Decoded string: " + codes);*/


        // Compress the input file using Huffman coding
        HuffmanCompressing.compress("texte.txt","texte.bin");

        // Decompress the output file using the codes
        HuffmanCompressing.decompress("texte.bin", "decompressed.txt");

    }
}