package com.penguin.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class BufferDemo {
    public static void main(String[] args) {

    }



    public static void budderDemo02() {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(10);
        buffer.putLong(9);
        buffer.putChar('c');
        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        //System.out.println(buffer.getChar());
    }

    public static void budderDemo01() {
        //初始化Buffer，并存放数据
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i * 2);
        }
        //从buffer读取数据
        //将buffer装换，读写转换
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}


