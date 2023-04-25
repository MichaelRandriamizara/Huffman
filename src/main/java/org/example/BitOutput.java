package org.example;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutput extends FilterOutputStream {
    private int buffer;
    private int bitsInBuffer;

    public BitOutput(OutputStream out) {
        super(out);
        buffer = 0;
        bitsInBuffer = 0;
    }

    public void writeBit(int bit) throws IOException {
        buffer <<= 1;
        buffer |= bit;
        bitsInBuffer++;
        if (bitsInBuffer == 8) {
            super.write(buffer);
            buffer = 0;
            bitsInBuffer = 0;
        }
    }

    public void flush() throws IOException {
        if (bitsInBuffer > 0) {
            buffer <<= (8 - bitsInBuffer);
            super.write(buffer);
            buffer = 0;
            bitsInBuffer = 0;
        }
        super.flush();
    }
}
