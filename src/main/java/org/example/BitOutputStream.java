package org.example;

import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream {
    private FileOutputStream fos;
    private byte buffer;
    private int bitIndex;

    public BitOutputStream(String fileName) throws IOException {
        this.fos = new FileOutputStream(fileName);
        this.buffer = 0;
        this.bitIndex = 0;
    }

    public void writeBit(boolean bit) throws IOException {
        if (bitIndex == 8) {
            flush();
        }
        if (bit) {
            buffer |= (1 << (7 - bitIndex));
        }
        bitIndex++;
    }

    private void flush() throws IOException {
        fos.write(buffer);
        buffer = 0;
        bitIndex = 0;
    }

    public void close() throws IOException {
        if (bitIndex > 0) {
            flush();
        }
        fos.close();
    }
}