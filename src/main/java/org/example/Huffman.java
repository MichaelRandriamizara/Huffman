package org.example;

import java.io.*;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
    public static HashMap<Character,String> huffmanEncoding(String source) throws IOException {
        HashMap<Character,Integer> freq = new HashMap<>();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if(freq.containsKey(c)){
                freq.put(c,freq.get(c)+1);
            }else{
                freq.put(c,1);
            }
        }
        Node root = buildTree(freq);
        HashMap<Character,String> codes = new HashMap<>();
        encode(root,"",codes);
        writeDictionaryToFile(codes,"dictionary.dict");
        return codes;
    }
    // Build the Huffman tree based on the frequency of characters
    private static Node buildTree(HashMap<Character,Integer> freq) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getFreq));

        // Create a leaf node for each character and add it to the priority queue
        for (char ch : freq.keySet()) {
            int f = freq.get(ch);
            pq.add(new Node(ch, f, null, null));
        }

        // Build the tree by merging nodes with the lowest frequency
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.getFreq() + right.getFreq(), left, right);
            pq.add(parent);
        }

        // The remaining node in the queue is the root of the tree
        return pq.poll();
    }

    // Recursively encode each character in the tree with its corresponding code
    private static void encode(Node node, String code, HashMap<Character,String> codes) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            codes.put(node.getSym(), code);
            return;
        }
        encode(node.getLeft(), code + "0", codes);
        encode(node.getRight(), code + "1", codes);
    }

    public static void writeDictionaryToFile(HashMap<Character,String> codes, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        int i=0;
        for (char ch : codes.keySet()) {
            writer.write(ch + "|" + codes.get(ch) + "|");
            i++;
            if(i<codes.size()){
                writer.newLine();
            }
        }
        writer.close();
    }

//    public static void writeDictionaryToFile(HashMap<Character,String> codes, String filename) throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
//        for (char ch : codes.keySet()) {
//            writer.write(ch + "|" + codes.get(ch) + "|");
//        }
//        writer.close();
//    }

    public static String huffmanDecoding(String encoded, String codeFile) throws IOException {
        HashMap<String, Character> codes = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(codeFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            codes.put(parts[1].trim(), parts[0].charAt(0));
        }
        reader.close();

        StringBuilder sb = new StringBuilder();
        String currCode = "";
        for (int i = 0; i < encoded.length(); i++) {
            currCode += encoded.charAt(i);
            if (codes.containsKey(currCode)) {
                sb.append(codes.get(currCode));
                currCode = "";
            }
        }
        return sb.toString();
    }

//    public static void compress(String inputFile, String outputFile) throws IOException {
//        // Read the input file
//        String input = readFile(inputFile);
//
//        // Encode the input data using Huffman coding
//        HashMap<Character,String> codes = huffmanEncoding(input);
//
//
//        // Encode the data and write it to a binary file
//        BitSet bitset = new BitSet();
//        int bitIndex = 0;
//        for (int i = 0; i < input.length(); i++) {
//            char ch = input.charAt(i);
//            String code = codes.get(ch);
//            for (int j = 0; j < code.length(); j++) {
//                if (code.charAt(j) == '1') {
//                    bitset.set(bitIndex);
//                }
//                bitIndex++;
//            }
//        }
//        byte[] bytes = bitset.toByteArray();
//        FileOutputStream fos = new FileOutputStream(outputFile);
//        fos.write(bytes);
//        fos.close();
//    }

    public static void compress(String inputFile, String outputFile) throws IOException {
        // Read the input file
        String input = readFile(inputFile);

        // Encode the input data using Huffman coding
        HashMap<Character,String> codes = huffmanEncoding(input);


        // Encode the data and write it to a binary file
        BitSet bitset = new BitSet();
        int bitIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            String code = codes.get(ch);
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '1') {
                    bitset.set(bitIndex);
                }
                bitIndex++;
            }
        }
        byte[] bytes = bitset.toByteArray();
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(bytes);
        fos.close();
    }

    // ...

    public static String readFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static HashMap<String,Character> readDictionaryFromFile(String filename) throws IOException {
        HashMap<String,Character> codes = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                line = reader.readLine();
                String[] parts = line.split("\\|");
                codes.put(parts[1].trim(), '\n');
                continue; // Skip empty lines
            }
            String[] parts = line.split("\\|");
            codes.put(parts[1].trim(), parts[0].charAt(0));
        }
        reader.close();
        return codes;
    }

    public static void decompress(String inputFile, String outputFile) throws IOException {
        // Read the dictionary file
        HashMap<String, Character> codes = Huffman.readDictionaryFromFile("dictionary.dict");
                /*new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader("dictionnary.dict"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                line = reader.readLine();
                String[] parts = line.split("\\|");
                codes.put(parts[0], '\n');
                continue; // Skip empty lines
            }
            String[] parts = line.split("\\|");
            codes.put(parts[1].trim(), parts[0].charAt(0));
        }
        reader.close();*/

        // Read the encoded data
        BitInputStream bis = new BitInputStream(new FileInputStream(inputFile));
        StringBuilder sb = new StringBuilder();
        String currCode = "";
        while (true) {
            int bit = bis.read();
            if (bit == -1) {
                break;
            }
            currCode += bit;
            if (codes.containsKey(currCode)) {
                sb.append(codes.get(currCode));
                currCode = "";
            }
        }
        bis.close();

        // Write the decoded data to the output file
        Writer writer = new FileWriter(outputFile);
        writer.write(sb.toString());
        writer.close();
    }


//    public static void decompress(String inputFile, String outputFile) throws IOException {
//        // Read the dictionary file
//        HashMap<String, Character> codes = new HashMap<>();
//        BufferedReader reader = new BufferedReader(new FileReader("dictionnary.dict"));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            if (line.isEmpty()) {
//                line=reader.readLine();
//                String[] parts = line.split("\\|");
//                codes.put(parts[0], '\n');
//                continue; // Skip empty lines
//            }
//            String[] parts = line.split("\\|");
//            codes.put(parts[1].trim(), parts[0].charAt(0));
//        }
//        reader.close();
//
//        // Read the encoded data
//        FileInputStream fis = new FileInputStream(inputFile);
//        byte[] bytes = fis.readAllBytes();
//        BitSet bitset = BitSet.valueOf(bytes);
//
//        // Decode the data
//        StringBuilder sb = new StringBuilder();
//        String currCode = "";
//        for (int i = 0; i < bitset.length(); i++) {
//            if (bitset.get(i)) {
//                currCode += "1";
//            } else {
//                currCode += "0";
//            }
//            if (codes.containsKey(currCode)) {
//                sb.append(codes.get(currCode));
//                currCode = "";
//            }
//        }
//
//        // Write the decoded data to the output file
//        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
//        writer.write(sb.toString());
//        writer.close();
//    }
}
