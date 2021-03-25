package com.penguin.netty.HeartBeat;


import com.penguin.netty.NettyChat.MyChatClient;

public class ClientDemo {
    public static void main(String[] args) throws Exception{
        new MyChatClient("127.0.0.1",8000).run();
    }
}
