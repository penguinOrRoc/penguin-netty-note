package com.penguin.netty.NettyChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Scanner;

public class MyChatClient {
    //属性
    private final String host;
    private final int port;

    public MyChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new MyChatClient("127.0.0.1",8080).run();
    }

    public void run() throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline中加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向pipeline中加入编码器
                            pipeline.addLast("encoder", new StringDecoder());
                            //加入自己的业务处理handler
                            pipeline.addLast(new MyChatClientHandler());

                        }
                    });
            System.out.println("netty 客户端启动");
            //绑定一个端口并同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            //获取通道
            Channel channel = channelFuture.channel();
            //创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            //通过channel将消息发送到服务端
            while(scanner.hasNext()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg+"\r\n");
            }
            //对关闭通道的操作进行监听
            //channelFuture.channel().closeFuture().sync();
        } finally {
            //监听关闭
            clientGroup.shutdownGracefully();
        }

    }
}
