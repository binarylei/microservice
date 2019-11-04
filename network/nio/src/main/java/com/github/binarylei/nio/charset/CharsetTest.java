package com.github.binarylei.nio.charset;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.SortedMap;

public class CharsetTest {

    //输出有效的字符集
    public void test1 () {
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        for (Map.Entry<String, Charset> me : charsets.entrySet()) {
            System.out.println(me.getKey() + ": " + me.getValue());
        }
    }

    //编码与解码
    public void test2 () throws CharacterCodingException {
        Charset cs = Charset.forName("gbk");

        //编码器和解码器
        CharsetEncoder encoder = cs.newEncoder();
        CharsetDecoder decoder = cs.newDecoder();

        CharBuffer cBuf = CharBuffer.allocate(1024);
        cBuf.put("中华人民共和国");
        cBuf.flip();

        //编码
        ByteBuffer bBuf = encoder.encode(cBuf);

        try {
            System.out.println(new String(bBuf.array(), "gbk"));
        } catch (UnsupportedEncodingException e) {
            ;
        }

        //解码
        CharBuffer cBuf2 = decoder.decode(bBuf);
        System.out.println(cBuf2.limit());
        System.out.println(cBuf2.toString());

    }

    public static void main(String[] args) throws CharacterCodingException {

        CharsetTest t = new CharsetTest();
        t.test2();
    }
}
