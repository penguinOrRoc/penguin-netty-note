package com.penguin.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

//MappedByteBuffer可以让文件直接在堆外内存修改，无需拷贝
public class MappedByteBufferDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\input.txt", "rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
        //读写模式、可以修改的起始位置，映射到内存的大小
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'a');
        mappedByteBuffer.put(1, (byte) 'b');
        mappedByteBuffer.put(2, (byte) 'c');
        mappedByteBuffer.put(3, (byte) 'd');
        mappedByteBuffer.put(4, (byte) 'e');
        randomAccessFile.close();


    }
}


