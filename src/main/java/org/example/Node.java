package org.example;

public class Node {
    private char sym;
    private int freq;
    private Node left;
    private Node right;

    public Node(char sym, int freq, Node left, Node right) {
        this.sym = sym;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    private Node() {
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public char getSym() {
        return sym;
    }

    public int getFreq() {
        return freq;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setSym(char sym) {
        this.sym = sym;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
