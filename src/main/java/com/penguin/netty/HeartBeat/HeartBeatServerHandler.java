package com.penguin.netty.HeartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            //将evt向下转型IdleStateEvent
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            String eventType = null;
            switch (idleStateEvent.state()){
                case  READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() +"---超时---"+ eventType);
            System.out.println("可根据空闲状态，进行自定义业务的处理~");
        }
    }
}
