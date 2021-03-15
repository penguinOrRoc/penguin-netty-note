package com.penguin.nio.NioChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    public static void main(String[] args)  {
        //启动服务端
        GroupChatServer server = new GroupChatServer();
        server.listen();

    }
    //1.定义服务器端信息
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //2.构造器，初始化工作
    public GroupChatServer() {
        try {
            //创建多路复用器
            selector = Selector.open();
            //创建ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置为非阻塞
            listenChannel.configureBlocking(false);
            //将该listener注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //3.监听
    public void listen() {
        try {
            //循环监听处理
            while (true) {
                //阻塞指定时间后返回，也可不设置
                int count = selector.select(1000);
                if (count > 0) {//有事件处理
                    //遍历得到所有的selectionKey
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {//监听到客户端连接
                            SocketChannel socketChannel = listenChannel.accept();
                            //设置通道为非阻塞
                            socketChannel.configureBlocking(false);
                            //注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //进行广播通知定义自定义业务
                            System.out.println(socketChannel.getRemoteAddress() + "上线了");

                        }
                        if (selectionKey.isReadable()) {//通道发送read事件。通道为可读状态
                            readClientMessage(selectionKey);
                        }
                        //移除key，避免重复消费
                        iterator.remove();
                    }

                } else {
                    //System.out.println("等着吧");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取客户端信息的方法
    private void readClientMessage(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;

        try {
            //根据selectionKey获取关联的channel
            socketChannel = (SocketChannel) selectionKey.channel();
            //创建buffer读取数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {//读取到数据

                System.out.println("服务端接收消息：" + new String(byteBuffer.array()));
                //向其他客户端转发消息
                sendInfoToOther(new String(byteBuffer.array()), socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress()+"下线了~");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //转发消息
    private void sendInfoToOther(String message, SocketChannel socketChannel) throws IOException {
        System.out.println("服务器转发消息~");
        //遍历，排除自己
        for (SelectionKey selectionKey : selector.keys()) {
            Channel targetChannel = selectionKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != socketChannel) {
                SocketChannel targetSocketChannel = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
                //将消息写入到其他通道
                targetSocketChannel.write(byteBuffer);
            }

        }


    }


}


