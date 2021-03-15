package com.penguin.netty.simple;

import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyTest {
    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newCachedThreadPool();

        NettyServer nettyServer = new NettyServer();
        //1.开启服务端
        threadPool.execute(() -> {
            try {
                nettyServer.ServerStart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //2.开启客户端 指定个数
        for (int i = 0; i < 3; i++) {
            threadPool.execute(() -> {
                try {
                    new NettyClient().ClientStart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

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
