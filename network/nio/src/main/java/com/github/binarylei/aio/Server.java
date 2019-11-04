package com.github.binarylei.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JDK1.7 之后
 */
public class Server {
	private ExecutorService executorService;
	private AsynchronousChannelGroup threadGroup;
	public AsynchronousServerSocketChannel assChannel;
	
	public Server(int port){
		try {
			//1. 创建一个缓存池
			executorService = Executors.newCachedThreadPool();

			//2. 创建线程组
			threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);

			//3. 创建服务器通道
			assChannel = AsynchronousServerSocketChannel.open(threadGroup);

			//4. 绑定端口
			assChannel.bind(new InetSocketAddress(port));
			System.out.println("server listening on " + port);

			//5. 获取客户端通道，不阻塞
			assChannel.accept(this, new ServerCompletionHandler());

			//一直阻塞 不让服务器停止
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server(8765);
	}
	
}
