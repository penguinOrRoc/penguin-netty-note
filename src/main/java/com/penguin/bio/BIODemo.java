package com.penguin.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 实例说明
 * 1.使用BIO模型编写一个服务器端，监听6666端口，当有客户端连接时，就启动一个线程与之通讯
 * 2.使用线程池机制进行改善，可以连接多个客户端
 * 3.服务端可以接收客户端发送的数据
 */
public class BIODemo {
    public static void main(String[] args) throws Exception {
        //1.创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        //2.创建一个ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            //3.监听，等待客户端连接
            System.out.println("创建连接~");
            System.out.println("main:"+Thread.currentThread().getId()+"\t"+Thread.currentThread().getName());
            final Socket socket = serverSocket.accept();
            //4.创建一个线程，并进行通讯
            executorService.execute(() -> {
                System.out.println("son:"+Thread.currentThread().getId()+"\t"+Thread.currentThread().getName());
                handler(socket);
            });
        }


    }

    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //输出客户端发送的数据
                    System.out.print(new String(bytes, 0, read));
                } else {
//                    OutputStream out=System.out;
//                    byte[] bs="我收到了\n".getBytes();
//                    out.write(bs);
//                    out.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("连接"+Thread.currentThread().getId()+"已经关闭~");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}


