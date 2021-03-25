package com.penguin.netty.simple.server;

import com.penguin.netty.simple.client.NettyChannelInitialalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class NettyServer {
    //定义一个HashMap，保存客户端与通道的关系
    public static ConcurrentHashMap<Integer, SocketChannel> channelHashMap = new ConcurrentHashMap<>();
    public static void main(String[] args) throws Exception {
        NettyServer nettyServer = new NettyServer();
        nettyServer.run();

    }

    public void run() throws Exception {
        /**
         * 说明：
         * 1.创建两个线程组，BossGroup、WorkerGroup
         * 2.BossGroup负责处理连接请求，WorkerGroup负责和客户端进行业务处理
         * 3.BossGroup、WorkerGroup都是无限循环
         * 4.BossGroup、WorkerGroup，含有的子线程NioEventLoop的个数 = CPU核数*2
         */

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(2);
        try {
            //创建服务器端启动对象，，配置参数
            //使用链式编程来进行设置
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossEventLoopGroup, workerEventLoopGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new NettyChannelInitialalizer());
            System.out.println("Server is ready……");
            //绑定一个端口并同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(8000).sync();
            //对关闭通道的操作进行监听   
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }
}
