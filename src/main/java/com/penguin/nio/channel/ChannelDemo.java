package com.penguin.nio.channel;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelDemo {
    public static void main(String[] args) throws IOException {

        picCopyByChannel();

    }
    public static  void picCopyByChannel() throws IOException {
        //创建输入输出流
        FileInputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Pictures\\2.jpg");
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator\\Pictures\\2copy.jpg");
       //创建输入输出通道
        FileChannel inFileChannel = inputStream.getChannel();
        FileChannel outFileChannel = outputStream.getChannel();
        //通过通道拷贝数据
        outFileChannel.transferFrom(inFileChannel,0,inFileChannel.size());
        //关闭流
        outFileChannel.close();
        inFileChannel.close();
        outputStream.close();
        inputStream.close();
    }

        //使用一个buffer完成文件的读取
    public static  void readAndWrite() throws IOException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\input.txt");
        FileChannel inFileChannel = inputStream.getChannel();
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\output.txt");
        FileChannel outFileChannel = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while(true){
            byteBuffer.clear();
            int read = inFileChannel.read(byteBuffer);
            if(read ==-1){
                break;
            }
            //
            byteBuffer.flip();
            outFileChannel.write(byteBuffer);
        }

    }
    public static void readFromFile() throws IOException {
        //创建文件的输入流
        File file = new File("C:\\Users\\Administrator\\Desktop\\channeldemo.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //将通道数据读入到buffer
        fileChannel.read(byteBuffer);
        //将byteBuffer的字节数据转换成String
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();

    }


    public static void writeToFile()throws IOException {
        String str = "channel demo";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\channeldemo.txt");
        //通过filechannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入byteBuffer
        byteBuffer.put(str.getBytes());
        //对bytebuffer进行反转
        byteBuffer.flip();
//        byteBuffer.position(2);
        //将bytebuffer写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();

    }
}


