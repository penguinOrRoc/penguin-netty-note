package com.penguin.netty.simple.ClientGroup;


import com.penguin.netty.simple.client.NettyClient;

public class Client01 {
    public static void main(String[] args) throws Exception{
        NettyClient c = new NettyClient();
        c.run();

    }
}
