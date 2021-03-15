package com.penguin.nio.CSDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOClient {
    public  static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();//1池 n线程  执行很多短期异步任务
        executorService.execute(()->{
            for (int i=0;i<10;i++){
                try {
                    connect("clint:"+i);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        System.in.read();



    }
    public static void connect(String requestMsg)throws IOException {
        //1.得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //2.设置为非阻塞
        socketChannel.configureBlocking(false);
        //3.提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8888);
        //4.连接服务器
        if(!socketChannel.connect(inetSocketAddress)){//连接失败
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以进行其他工作~");
            }
        }
        //连接成功，进行的操作
        ByteBuffer byteBuffer = ByteBuffer.wrap(requestMsg.getBytes());
        //5.将buffer的数据写入到channel
        socketChannel.write(byteBuffer);
        //6.模拟一个阻塞等待
//        System.in.read();
    }

}


