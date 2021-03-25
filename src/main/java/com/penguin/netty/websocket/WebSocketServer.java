package com.penguin.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

    public static void main(String[] args) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);//设置两个线程组
            serverBootstrap.channel(NioServerSocketChannel.class); //使用NioSocketChannel作为服务器的通道实现
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //因为基于http协议，所以使用http的编码、解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写，添加ChunkeWrite处理器
                            pipeline.addLast(new ChunkedWriteHandler());


                            /**
                             * 1.http协议在传输过程中分段，HttpObjectAggregator，就是可以将多个段聚合；
                             * 2.基于上述原理，当浏览器发送大量数据时，会发出多次请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 1.对于websocket，他的数据是以帧（frame）的形式传递
                             * 2.WebSocketFrame有6个子类
                             * 3.浏览器请求时 ws://ip:port/uri
                             * 4.WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/uri"));
                            /**
                             * 自定义的Handler，处理业务逻辑
                             */
                            pipeline.addLast(new WebSocketHandler());

                        }
            });
            System.out.println("netty 服务器启动");
            //绑定一个端口并同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            //对关闭通道的操作进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //监听关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


}
