package com.penguin.netty.NettyChat;

import com.penguin.netty.NettyChat.pojo.ChatUser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE    全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //时间工具
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //自定义hashmap对象，满足私聊场景
    public static Map<ChatUser,Channel> channelMap = new HashMap<>();
    public static List<Channel> channelList = new ArrayList<>();
    /**
     * handlerAdded 表示连接建立，一旦连接，第一个执行
     * @param ctx
     * @throws Exception
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //获取该用户通道信息
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //channelGroup.writeAndFlush会将channelGroup中所有的channel遍历，并发送消息
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress() +"加入聊天\n");
        //将channel加入到channelGroup
        channelGroup.add(channel);
        //建立用户与channel的关联，为私聊实现做准备
        //channelMap.put(new ChatUser(),channel);
        /**上面的方法没生效，改成手动遍历试试,手动遍历也不行，继续查问题
        channelList.add(channel);
        for(Channel ch : channelList){
            ch.writeAndFlush("[客户端]"+channel.remoteAddress() +"加入聊天\n");
        }
         */
    }
    /**
     * handlerRemoved 表示连接关闭，一旦关闭，第一个执行
     * @param ctx
     * @throws Exception
     */

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //获取该用户通道信息
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress() +"离开聊天了\n");
        System.out.println("channelGroup size" + channelGroup.size());
        //上方法会将channelGroup中所有的channel遍历，并发送消息
        //channelGroup.remove(channel);  //无需手动，方法会自动移除
    }

    /**
     * 表示channel处于活动状态，提示上线
     * @param ctx
     * @throws Exception
     */

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了~");
    }

    /**
     * 表示channel处于非活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"下线了~");
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("[客户端]"+ctx.channel().remoteAddress()+"说："+msg+"\n");
        //获取到当前channel，读取数据
        Channel channel = ctx.channel();
        //遍历channelGroup，根据不同情况，回复不同消息
        channelGroup.forEach(ch->{
            if(channel!=ch){
                //非当前通道，转发消息
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送消息:"+msg+"\n");
            }else{
                ch.writeAndFlush("[自己]发送了消息"+msg +"\n");
            }
        });
        /**

        for(Channel ch : channelList){
            if(channel!=ch){
                //非当前通道，转发消息
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送消息:"+msg+"\n");
            }else{
                ch.writeAndFlush("[自己]发送了消息"+msg +"\n");
            }
        }*/



    }


    /**
     * 捕获异常
     * @param ctx
     * @param cause
     * @throws Exception
     */

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
        System.out.println("客户端"+ctx.channel().remoteAddress()+"发生了异常:"+cause.getMessage());
    }
}
