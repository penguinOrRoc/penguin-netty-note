package com.penguin.nio.NioChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupChatClient {
    //1.定义相关属性
    private final String HOST = "127.0.0.1";
    private final static int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String clientName;

    //2.构造器完成初始化工作
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open();
        //创建绑定对象,连接服务器
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        //注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到clientname
        clientName = socketChannel.getRemoteAddress().toString();
        System.out.println(clientName + " is ok ……");

    }
    //3.发送消息
    public void  sendInfo(String info) throws IOException {
        info = "用户"+clientName + "说:"+info;
        socketChannel.write(ByteBuffer.wrap(info.getBytes()));
    }
    //4.读取服务器回复的消息
    public void readInfo() throws IOException {
        int readChannels = selector.select();
        if(readChannels>0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isReadable()){
                    //遍历得到相关通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //得到一个buffer
                    ByteBuffer byteffer = ByteBuffer.allocate(1024);
                    //d读取
                    socketChannel.read(byteffer);
                    //将消息转换并输出
                    System.out.println(new String(new String(byteffer.array())));
                    iterator.remove();
                }else{
                    continue;
                }
            }
        }

    }
}


