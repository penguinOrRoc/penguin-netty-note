package com.penguin.netty.NettyChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class MyChatServer {
    private int port;

    public MyChatServer(int port) {
        this.port = port;
    }
    public static void main(String[] args) throws InterruptedException{
        new MyChatServer(8080).run();
    }

    /**
     * 编写run方法，处理客户端请求
     */
    public void run() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //channel连接时执行
                            //System.out.println("[客户端]"+ch.remoteAddress()+"连接了");
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向pipeline加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自己的业务处理handler
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            System.out.println("netty 服务器启动");
            //绑定一个端口并同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //对关闭通道的操作进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //监听关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
