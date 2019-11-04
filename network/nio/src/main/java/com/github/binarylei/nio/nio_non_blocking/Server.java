package com.github.binarylei.nio.nio_non_blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

// 非阻塞模式
public class Server implements Runnable {

    public static void main(String[] args) {
        new Thread(new Server(8765)).start();
    }

    // 1 多路复用器（管理所有的通道）
    private Selector selector;
    // 2 建立缓冲区
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    public Server(int port) {
        try {
            //1. 获取多路复用器
            this.selector = Selector.open();

            //2. 获取服务器端通道
            ServerSocketChannel ssChannel = ServerSocketChannel.open();

            //3. 切换成非阻塞模式
            ssChannel.configureBlocking(false);

            //4. 绑定端口号
            ssChannel.bind(new InetSocketAddress(port));

            //5. 把服务器通道注册到多路复用器上，并且监听阻塞事件
            ssChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server start, port :" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                // 1. 阻塞到至少有一个通道在你注册的事件上就绪，返回的 int 值表示有多少通道已经就绪
                int wait = selector.select();
                if (wait == 0) continue;

                //2. 遍历多路复用器上已经准备就绪的通道
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            // 3.1 如果为 accept 状态，就获取客户端通道并注册到selector上
                            this.accept(key);
                        } else if (key.isReadable()) {
                            // 3.2 如果为可读状态
                            this.read(key);
                        } else if (key.isWritable()) {
                            // 3.3 写数据
                            this.write(key);
                        }
                    }

                    // 4. 注意必须在处理完通道时自己移除
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(SelectionKey key) {
        //ServerSocketChannel ssc =  (ServerSocketChannel) key.channel();
        //ssc.register(this.seletor, SelectionKey.OP_WRITE);
    }

    private void read(SelectionKey key) {
        try {
            //1. 清空缓冲区旧的数据
            this.readBuf.clear();

            //2. 获取之前注册的socket通道对象
            SocketChannel sChannel = (SocketChannel) key.channel();

            //3. 读数据
            int len = sChannel.read(this.readBuf);

            //4. 如果没有数据
            if (len == -1) {
                key.channel().close();
                key.cancel();
                return;
            }

            //5 有数据则进行读取 读取之前要flip()复位
            this.readBuf.flip();
            byte[] bytes = new byte[this.readBuf.remaining()];
            this.readBuf.get(bytes);

            System.out.println("Server : " + new String(bytes).trim());

            //6. 可以写回给客户端数据

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void accept(SelectionKey key) {
        try {
            //1. 获取服务通道ServerSocketChannel
            ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();

            //2. 获取客户端通道SocketChannel
            SocketChannel sChannel = ssChannel.accept();

            //3. 客户端通道SocketChannel也要设置成非阻塞模式
            sChannel.configureBlocking(false);

            //4 注册到多路复用器上，并设置读取标识
            sChannel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
