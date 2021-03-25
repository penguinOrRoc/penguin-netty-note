package com.penguin.netty.simple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public void run() throws Exception {
        //客户端需要一个事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            //创建客户端对象  Bootstrap  ,注意与服务端ServerBootStrap不同
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(eventLoopGroup) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());//加入自己的处理器
                        }
                    });
            System.out.println("client is ready……");
            //启动客户端去链接服务器端,知识点：netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).sync();
            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭
            eventLoopGroup.shutdownGracefully();
        }
    }
}
