package com.github.binarylei.nio.buffer;

import java.nio.ByteBuffer;

public class BufferTest {

    public static void main(String[] args) {
        //1. 分配一个指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(1024);
        System.out.println(buf1); // [pos=0 lim=1024 cap=1024]

        //2. 利用 put() 存放数据到缓冲区
        buf1.put("abc".getBytes());
        System.out.println(new String(buf1.array())); // abc
        System.out.println(buf1); // [pos=3 lim=1024 cap=1024]

        //3. buf1.put(0, (byte)'z')时position不变
        buf1.put(0, (byte) 'z');
        System.out.println(new String(buf1.array())); // zbc
        System.out.println(buf1); // [pos=3 lim=1024 cap=1024]

        //4. 切换到读模式
        buf1.flip();
        System.out.println(buf1); // [pos=0 lim=3 cap=1024]

        //5. get() 读数据，position + 1
        for (int i = 0; i < buf1.limit(); i++) {
            System.out.println(buf1.get());
        }
        System.out.println(buf1); // [pos=3 lim=3 cap=1024]

        //6. get(0) 读数据，position 不变
        buf1.get(1);
        System.out.println(buf1); // [pos=3 lim=3 cap=1024]

        //7. rewind() 重读模式，postion = 0，limit 不变
        buf1.rewind();
        System.out.println(buf1); // [pos=0 lim=3 cap=1024]

        //8. clear() 切换到时写模式
        buf1.clear();
        System.out.println(buf1); // [pos=0 lim=1024 cap=1024]

        //9. compact() 保留未读的部分切换到时写模式
        buf1.position(1).limit(3);
        System.out.println(buf1); // [pos=1 lim=3 cap=1024]
        buf1.compact();
        System.out.println(buf1); // [pos=2 lim=1024 cap=1024]

        //10. mark() 和 reset()
        buf1.mark();
        buf1.reset();
    }
}
