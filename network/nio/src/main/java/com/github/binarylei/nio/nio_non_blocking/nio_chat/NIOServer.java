package com.github.binarylei.nio.nio_non_blocking.nio_chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 网络多客户端聊天室
 * 功能1： 客户端通过Java NIO连接到服务端，支持多客户端的连接
 * 功能2：客户端初次连接时，服务端提示输入昵称，如果昵称已经有人使用，提示重新输入，如果昵称唯一，则登录成功，之后发送消息都需要按照规定格式带着昵称发送消息
 * 功能3：客户端登录后，发送已经设置好的欢迎信息和在线人数给客户端，并且通知其他客户端该客户端上线
 * 功能4：服务器收到已登录客户端输入内容，转发至其他登录客户端。
 * <p>
 * TODO 客户端下线检测
 */
public class NIOServer {

    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");
    //用来记录在线人数，以及昵称
    private static HashSet<String> users = new HashSet<String>();

    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILIT = "#@#";

    private Selector selector = null;


    public NIOServer(int port) throws IOException {
        this.port = port;

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
        System.out.println("服务已启动，监听端口是：" + this.port);
    }


    public void listener() throws IOException {
        while (true) {
            // 1. 阻塞到至少有一个通道在你注册的事件上就绪，返回的 int 值表示有多少通道已经就绪
            int wait = selector.select();
            if (wait == 0) continue;

            // 2. 遍历多路复用器上已经准备就绪的通道
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                process(key);
            }
        }
    }

    public void process(SelectionKey key) throws IOException {
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
            sChannel.write(charset.encode("请输入你的昵称"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) throws IOException {
        // 返回该 SelectionKey 对应的 Channel，其中有数据需要读取
        SocketChannel socket = (SocketChannel) key.channel();
        new NIOServerHandler(socket, key).handler();
    }

    private void write(SelectionKey key) {
        //ServerSocketChannel ssc =  (ServerSocketChannel) key.channel();
        //ssc.register(this.seletor, SelectionKey.OP_WRITE);
    }

    private class NIOServerHandler {
        private SocketChannel socket;
        private SelectionKey key;

        public NIOServerHandler(SocketChannel socket, SelectionKey key) {
            this.socket = socket;
            this.key = key;
        }

        public void handler() throws IOException {
            //往缓冲区读数据
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try {
                while (socket.read(buff) > 0) {
                    buff.flip();
                    content.append(charset.decode(buff));

                }
                System.out.println("从IP地址为：" + socket.getRemoteAddress() + "的获取到消息: " + content);
                // 将此对应的 channel 设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);
            } catch (IOException io) {
                key.cancel();
                if (socket != null) {
                    socket.close();
                }
            }
            if (content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if (arrayContent != null && arrayContent.length == 1) {
                    String nickName = arrayContent[0];
                    if (users.contains(nickName)) {
                        socket.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int onlineCount = onlineCount();
                        String message = "欢迎 " + nickName + " 进入聊天室! 当前在线人数:" + onlineCount;
                        broadCast(null, message);
                    }
                }
                //注册完了，发送消息
                else if (arrayContent != null && arrayContent.length > 1) {
                    String nickName = arrayContent[0];
                    String message = content.substring(nickName.length() + USER_CONTENT_SPILIT.length());
                    message = nickName + " 说： " + message;
                    if (users.contains(nickName)) {
                        //不回发给发送此内容的客户端
                        broadCast(socket, message);
                    }
                }
            }
        }

        private void broadCast(SocketChannel client, String content) throws IOException {
            //广播数据到所有的SocketChannel中
            for (SelectionKey key : selector.keys()) {
                Channel targetchannel = key.channel();
                //如果client不为空，不回发给发送此内容的客户端
                if (targetchannel instanceof SocketChannel && targetchannel != client) {
                    SocketChannel target = (SocketChannel) targetchannel;
                    target.write(charset.encode(content));
                }
            }
        }

        // TODO 要是能检测下线，就不用这么统计了
        private int onlineCount() {
            int res = 0;
            for (SelectionKey key : selector.keys()) {
                Channel target = key.channel();

                if (target instanceof SocketChannel) {
                    res++;
                }
            }
            return res;
        }
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(8080).listener();
    }
}
