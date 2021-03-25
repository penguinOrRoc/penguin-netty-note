package com.penguin.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyBuffer {
    public static void main(String[] args){
        ByteBuf byteBuf = Unpooled.buffer(10);
    }
}
