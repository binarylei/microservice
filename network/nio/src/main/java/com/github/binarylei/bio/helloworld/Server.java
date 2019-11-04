package com.github.binarylei.bio.helloworld;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    final static int PROT = 8765;

    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            //1. 绑定端口
            server = new ServerSocket();
            server.bind(new InetSocketAddress((InetAddress) null, PROT), PROT);
            System.out.println("server listening on " + PROT);

            while (true) {
                //2. 获取客户端请求的Socket，没有请求就阻塞
                Socket socket = server.accept();
                //3. 开启一个线程执行客户端的任务
                new Thread(new ServerHandler(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server = null;
        }
    }
}
