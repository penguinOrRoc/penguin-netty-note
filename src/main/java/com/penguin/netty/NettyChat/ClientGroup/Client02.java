package com.penguin.netty.NettyChat.ClientGroup;


import com.penguin.netty.NettyChat.MyChatClient;

public class Client02 {
    public static void main(String[] args)throws Exception{
        new MyChatClient("127.0.0.1",8080).run();
    }
}
