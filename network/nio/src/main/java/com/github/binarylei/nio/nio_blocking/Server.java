package com.github.binarylei.nio.nio_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 阻塞模式，要实现非阻塞，必须要注册到Selector中
 */
public class Server {

    final static int PORT = 9898;
    
    public static void main(String[] args) {
        ServerSocketChannel ssChannel = null;
        SocketChannel sChannel = null;
        ByteBuffer buf = null;
        FileChannel fChannel = null;

        try {
            //1. 获取服务器端通道
            ssChannel = ServerSocketChannel.open();

            //2. 绑定端口号
            ssChannel.bind(new InetSocketAddress(PORT));
            System.out.printf("服务器正在监听%s", PORT);

            //3. 获取客户端通道
            sChannel = ssChannel.accept();

            //4. 分配缓冲区
            buf = ByteBuffer.allocate(1024);

            //5. 将从客户端接收的文件写入本地
            String url = "F:\\2.txt";
            fChannel = FileChannel.open(Paths.get(url), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            while (sChannel.read(buf) != -1) {
                buf.flip();
                fChannel.write(buf);
                buf.clear();
            }

            //6. 向客户端发送数据 要flip()再发送
            buf.put("中华人民共和国".getBytes()).flip();
            sChannel.write(buf);
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
            if (ssChannel != null) {
                try {
                    ssChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
