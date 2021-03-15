package com.penguin.nio.NioChat;

import java.io.IOException;
import java.util.Scanner;

public class GroupChatClientWrite {
    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient client = new GroupChatClient();
        //发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            client.sendInfo(scanner.nextLine());
        }

    }
}


