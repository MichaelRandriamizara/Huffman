package org.example;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInput extends FilterInputStream {
    private int buffer;
    private int bitsInBuffer;

    public BitInput(InputStream in) {
        super(in);
        buffer = 0;
        bitsInBuffer = 0;
    }

    public int readBit() throws IOException {
        if (bitsInBuffer == 0) {
            buffer = super.read();
            if (buffer == -1) {
                return -1;
            }
            bitsInBuffer = 8;
        }
        bitsInBuffer--;
        return (buffer >>> bitsInBuffer) & 1;
    }
}
