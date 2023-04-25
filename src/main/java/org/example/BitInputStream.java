package org.example;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {
    private InputStream in;
    private int buffer;
    private int bufferPos;

    public BitInputStream(InputStream in) {
        this.in = in;
        this.buffer = 0;
        this.bufferPos = 0;
    }

    public int read() throws IOException {
        if (bufferPos == 0) {
            buffer = in.read();
            if (buffer == -1) {
                return -1;
            }
            bufferPos = 8;
        }
        bufferPos--;
        return (buffer >> bufferPos) & 1;
    }

    public void close() throws IOException {
        in.close();
    }
}