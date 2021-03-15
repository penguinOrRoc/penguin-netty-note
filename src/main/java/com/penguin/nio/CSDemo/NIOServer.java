package com.penguin.nio.CSDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        //1.创建ServerSocketChannel   类似于BIO中的ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.绑定服务器端端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        //3.设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //4.得到selector多路复用器
        Selector selector = Selector.open();
        //5.把ServerSocketChannel 注册到 selector关心事件为OP_ACCEPT  （当受到客户端连接时）
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6.循环等待客户端的连接
        while (true) {
            if (selector.select(1000) == 0) {
               // System.out.println("服务器等待1000毫秒，无连接~");
                continue;
            } else {//如果 >0,表示已经获取到关注事件了
                //返回关注事件的集合，获取到selectionKey集合
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                //通过selectionKey可以反向获取通道
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    //获取到selecttionKey，并进行对应的处理
                    SelectionKey selectionKey = iterator.next();
                    System.out.println("selectionKey = "+selectionKey);
                    System.out.println("selector.keys().size() = "+selector.keys().size());
                    if (selectionKey.isAcceptable()) {//有新的客户端连接
                        //为该客户端创建一个socketChannel
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //将客户端设置为非阻塞
                        socketChannel.configureBlocking(false);
                        //将socketChannel注册到selector，关注事件为OP_READ,同时为socketChannel关联一个Buffer
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        System.out.println("isAcceptable_客户端连接成功:"+socketChannel);
                    }
                    if (selectionKey.isReadable()) {//发生OP_READ
                        //通过key，反向获取到对应channel
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        //获取到该channel关联的buffer
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        channel.read(byteBuffer);
                        System.out.println("isReadable_客户端发送信息:"+channel);
                        System.out.println("信息内容是"+new String(byteBuffer.array()));
                    }
                    //手动从集合中移除当前的selectionoKey,防止多线程下的重复操作
                    iterator.remove();

                }
            }


        }

    }
}


