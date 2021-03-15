package com.penguin.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个netty提供的httpServerCoder ->[coder - decoder]
        //1.HttpServerCodec时netty提供的处理http请求的编码、解码器
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        //2.添加自定义处理器
        pipeline.addLast("HttpServerHandler",new HttpServerHandler());
    }
}
