package com.penguin.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * TextWebSocketFrame表示一个文本帧（frame）
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息："+msg.text());
        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("server time:"+ LocalDateTime.now()+"\n"+msg.text()));
    }

    /**
     * 当web客户端连接后，就会触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("浏览器连接:" + ctx.channel().id().asLongText());
    }

    /**
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("浏览器失联:" + ctx.channel().id().asLongText());
    }

    /**
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息:" + cause.getMessage());
        ctx.close();
    }
}
