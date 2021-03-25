package com.penguin.netty.simple;

import com.penguin.netty.simple.server.NettyServer;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyPush {
    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        NettyServer nettyServer = new NettyServer();

        //3.给客户端推送消息
        threadPool.execute(() -> {
            try {
                while (true) {
                    //给每个通道发送消息
                    ConcurrentHashMap<Integer, SocketChannel> channelHashMap = nettyServer.channelHashMap;
                    if (channelHashMap.size() > 0) {
                        System.out.println("开始推送~");
                        for (Integer key : channelHashMap.keySet()) {
                            SocketChannel socketChannel = channelHashMap.get(key);
                            socketChannel.eventLoop().execute(() -> {
                                System.out.println(key + "-----客户端推送的消息");
                                socketChannel.writeAndFlush(key + "-----客户端推送的消息");
                            });
                        }
                        System.out.println("推送完毕~");
                        break;

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

}
