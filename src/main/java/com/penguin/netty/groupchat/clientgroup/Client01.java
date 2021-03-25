package com.penguin.netty.groupchat.clientgroup;

import com.penguin.netty.groupchat.GroupChatClient;

public class Client01 {
    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}
