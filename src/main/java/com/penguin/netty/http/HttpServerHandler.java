package com.penguin.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler是ChannelInboundHandler
 * HttpObject 客户端和服务器端相互通讯的数据封装成HttpObject
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.out.println("Client Address:" + ctx.channel().remoteAddress());
            System.out.println("Pipeline hashcode:" + ctx.channel().pipeline().hashCode());
            System.out.println("Channel hashcode:" + ctx.channel().hashCode());

            //1.获取到请求
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.getUri());
            if ("/favicon.ico".equals(uri.getPath())){
                //1.1根据请求信息，过滤指定资源
                System.out.println("Your request 'favicon.ico' is not response~");
                return ;
            }
            //2.回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("Hello,I am server~", CharsetUtil.UTF_8);
            //3.构建http响应 httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //4.将构建好的response返回
            ctx.writeAndFlush(response);

        }

    }
}
