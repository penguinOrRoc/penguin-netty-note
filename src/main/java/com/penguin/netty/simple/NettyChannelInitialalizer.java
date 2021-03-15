package com.penguin.netty.simple;

import com.penguin.netty.http.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelInitialalizer extends ChannelInitializer<SocketChannel> {
    //给我们的workerEventLoopGroup的EventLoop，对应的管道设置处理器
    //给Pipeline设置处理器
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        NettyServer nettyServer = new NettyServer();
        ConcurrentHashMap<Integer, SocketChannel> channelHashMap = nettyServer.channelHashMap;
        socketChannel.pipeline().addLast(new NettyServerHandler());
        //使用hashmap保存通道与客户端的关联
        System.out.println("socketChannel :" + socketChannel.hashCode());
        channelHashMap.put(socketChannel.hashCode(),socketChannel);
    }
}
