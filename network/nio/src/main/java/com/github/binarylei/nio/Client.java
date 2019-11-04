package com.github.binarylei.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author leigang
 * @version 2019-06-16
 * @since 2.0.0
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(8888));
        socketChannel.finishConnect();

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello world!".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        TimeUnit.SECONDS.sleep(1000);
    }
}
