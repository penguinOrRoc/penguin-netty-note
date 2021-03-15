package com.penguin.netty.http;

public class HttpMain {
    public static void main(String[] args) {
        /**
         *
         */
        HttpServer httpServer = new HttpServer();
        try {
            httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
