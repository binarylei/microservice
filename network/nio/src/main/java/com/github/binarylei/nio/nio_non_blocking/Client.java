package com.github.binarylei.nio.nio_non_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

//客户端
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel sChannel = null;
        try {
            //1. 获取通道
            sChannel = SocketChannel.open(
                    new InetSocketAddress("127.0.0.1", 8765));

            //2. 切换成非阻塞模式
            sChannel.configureBlocking(false);

            //3. 分配缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            //4. 把从控制台读到数据发送给服务器端
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String str = scanner.next();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                buf.put((dateFormat.format(new Date()) + "：" + str).getBytes());
                buf.flip();// 非阻塞模式下，write()方法在尚未写出任何内容时可能就返回了
                while (buf.hasRemaining()) {
                    sChannel.write(buf);
                }
                buf.clear();
            }
        } finally {
            sChannel.close();
        }
    }

}
