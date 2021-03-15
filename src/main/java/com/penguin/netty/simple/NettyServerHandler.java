package com.penguin.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明
 * 1.自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 * 2.自定义Handler，才能称之为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据实际（读取客户端发送的消息）
     * @param ctx 上下文对象，含有管道（pipeline）、通道（channel）、地址
     * @param msg 客户端发送的数据，默认时Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 模拟一个耗时业务
         * Thread.sleep(10*1000);
         * ctx.writeAndFlush(Unpooled.copiedBuffer("hello , client~ from channelRead",CharsetUtil.UTF_8));
         */
        /**
         * 解决方案1：用户程序自定义的普通任务
         * 耗时任务-》异步执行-》提交channel 对应的NIOEventLoop的taskQueue
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(10*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello , client~ from channelRead ~ method1",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常："+ e.getMessage());
            }
        });
         */
        /**
         * 解决方案2：用户自定义定时任务

        ctx.channel().eventLoop().schedule(()->{
                    try {
                        Thread.sleep(10*1000);
                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , client~ from channelRead ~ method2",CharsetUtil.UTF_8));
                    } catch (InterruptedException e) {
                        System.out.println("发生异常："+ e.getMessage());
                    }
                },
                5,
                TimeUnit.SECONDS);  */
        /**channel和pipeline两者的关系，相互包含
        Channel channel = ctx.channel();
        ChannelPipeline pipleline = ctx.pipeline();
         */
        //将msg数据转换成ByteBuf(Netty的)
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("server msg:"+byteBuf.toString(CharsetUtil.UTF_8));

        /**
        System.out.println("Thread name:"+Thread.currentThread().getName());
        System.out.println("server ctx:"+ctx);
        System.out.println("server address:"+ctx.channel().remoteAddress());
         */

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush ： write + flush
        //将数据写入到缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , client~ from channelReadComplete",CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}








