package com.github.binarylei.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {

	@Override
	public void completed(AsynchronousSocketChannel asChannel, Server attachment) {
		// 当有下一个客户端接入时，调用Server的accept方法，这样反复执行下去，就可以处理多个客户端
		attachment.assChannel.accept(attachment, this);
		read(asChannel);
	}

	private void read(final AsynchronousSocketChannel asChannel) {
		//读取数据
		ByteBuffer buf = ByteBuffer.allocate(1024);
		asChannel.read(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer resultSize, ByteBuffer attachment) {
				//获得读取的字节数
				System.out.println("Server -> " + "收到客户端的数据长度为:" + resultSize);

				//1. 获取读取的数据
                attachment.flip();
				String resultData = new String(attachment.array()).trim();
				System.out.println("Server -> " + "收到客户端的数据信息为:" + resultData);

				String response = "服务器响应, 收到了客户端发来的数据: " + resultData;
				write(asChannel, response);
			}
			@Override
			public void failed(Throwable e, ByteBuffer attachment) {
				e.printStackTrace();
			}
		});
	}
	
	private void write(AsynchronousSocketChannel asChannel, String response) {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(response.getBytes()).flip();
		asChannel.write(buf);
	}
	
	@Override
	public void failed(Throwable e, Server attachment) {
		e.printStackTrace();
	}

}
