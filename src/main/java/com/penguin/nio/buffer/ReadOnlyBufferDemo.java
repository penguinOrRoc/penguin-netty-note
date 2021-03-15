package com.penguin.nio.buffer;

import java.nio.ByteBuffer;

public class ReadOnlyBufferDemo {
    public static void main(String[] args) {

        bufferReadOnly();
    }

    public static void bufferReadOnly() {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 60; i++) {
            buffer.put((byte) i);
        }
        buffer.flip();
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer);
        System.out.println("往readOnlyBuffer中写东西~");
        readOnlyBuffer.put((byte) 100);

        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }


    }

}