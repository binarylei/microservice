package com.github.binarylei.nio.nio_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Client {

    final static int PORT = 9898;
    final static String IP = "127.0.0.1";

    public static void main(String[] args) {
        SocketChannel sChannel = null;
        ByteBuffer buf = null;
        FileChannel fChannel = null;

        try {
            //1. 获取通道
            sChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));

            //2. 分配缓冲区
            buf = ByteBuffer.allocate(1024);

            //3. 用通道读取本地文件发送给服务器
            String url = "F:\\1.txt";
            fChannel = FileChannel.open(Paths.get(url), StandardOpenOption.READ);
            while (fChannel.read(buf) != -1) {
                buf.flip();
                sChannel.write(buf);
                buf.clear();
            }

            //4. 告诉服务端写完成，否则服务端一直阻塞
            sChannel.shutdownOutput();

            //5. 接收服务端的响应
            int len = 0;
            while ((len = sChannel.read(buf)) != -1) {
                buf.flip();
                System.out.printf("client 接收 server 的数据：%s\n", new String(buf.array(), 0, len));
                buf.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4. 关闭资源
            if (fChannel != null) {
                try {
                    fChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sChannel != null) {
                try {
                    sChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
