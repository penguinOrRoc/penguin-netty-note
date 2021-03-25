package com.penguin.netty.HeartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HeartBeatServer {
    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到的连接个数
                    .handler(new LoggingHandler(LogLevel.INFO)) //为bossgroup添加日志处理器
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供IdleStateHandler
                            /**
                             * 1.IdleStateHandler是netty提供的处理空闲状态的处理器
                             * 2.Long readerIdleTime 表示多长时间没有读，就会发送一个心跳检测包是否连接
                             * 3.Long writerIdleTime 表示多长时间没有写，就会发送一个心跳检测包是否连接
                             * 4.Long allIdleTime 表示多长时间没有读写，就会发送一个心跳检测包是否连接
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

                            /**
                             * 当IdleStateEvent触发后，就会传递给管道的下一个Handler处理
                             * 通过调用下一个Handler的userEventTiggered，在该方法中去处理IdleStateEvent(读空闲、写空闲、读写空闲)
                             */
                            //加入对空闲检测进一步处理的自定义Handler
                            pipeline.addLast(new HeartBeatServerHandler());
                        }
                    });
            System.out.println("netty 服务器启动");
            //绑定一个端口并同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(8000).sync();
            //对关闭通道的操作进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //监听关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
