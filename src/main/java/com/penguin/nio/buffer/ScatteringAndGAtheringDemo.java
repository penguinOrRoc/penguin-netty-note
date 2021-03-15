package com.penguin.nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGAtheringDemo {
    public static void main(String[] args) throws IOException {
        //Scattering将数据吸入到buffer时，可以采用buffer数组，依次写入    分散
        //Gathering从buffer读取数据时，可以采用buffer数组，依次读
        //创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[3];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(1);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 10;
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += 1;
                System.out.println("byteRead = " + byteRead);
                Arrays.asList(
                        Arrays.stream(byteBuffers).map(
                                buffer -> "position = " + buffer.position() + ",limit = " + buffer.limit()));

            }
            Arrays.asList(byteBuffers).forEach(
                    buffer -> buffer.flip()
            );
            //将数据输出到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += 1;
            }
            //将所有的buffer进行clean
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });
            System.out.println("byteRead = " + byteRead + ",byteWrite = " + byteWrite + ",messagelength = " + messageLength);
        }

    }
}


