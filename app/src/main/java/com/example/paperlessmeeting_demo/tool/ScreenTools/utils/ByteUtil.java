package com.example.paperlessmeeting_demo.tool.ScreenTools.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author cyj
 * @createDate 5/6/22
 * @ClassName: ByteUtil
 * @Description: 描述
 * @Version: 1.0
 */

public class ByteUtil {
    /**
     * 将int转为长度为4的byte数组
     * 大端模式
     * @param length
     * @return
     */
    public static byte[] int2Bytes(int length) {
        byte[] result = new byte[4];
        result[3] = (byte) (length & 0xff);// 最低位
        result[2] = (byte) ((length >> 8) & 0xff);// 次低位
        result[1] = (byte) ((length >> 16) & 0xff);// 次高位
        result[0] = (byte) (length >>> 24);// 最高位,无符号右移。
        return result;
    }

    //转成2个字节
    public static byte[] short2Bytes(short size) {
        byte[] result = new byte[2];
        result[0] = (byte) size;
        result[1] = (byte) (size >> 8);
        return result;
    }
    /**
     * byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序，和intToBytes（）配套使用
     * 大端模式
     * @param src byte数组
     * @return int数值
     */
    public static int bytesToInt(byte[] src) {
        int value;
        value = (int) ((src[3] & 0xFF)
                | ((src[2] & 0xFF) << 8)
                | ((src[1] & 0xFF) << 16)
                | ((src[0] & 0xFF) << 24));
        return value;
    }

    // TODO  byte转short
    public static short bytesToShort(byte[] src) {
        short value;
        value = (short) ((src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8));
        return value;
    }
    // byte 转字符串 string
    public static String byteBufferToString(ByteBuffer buffer) {
        CharBuffer charBuffer = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
            buffer.flip();
            return charBuffer.toString();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获得校验码
     *
     * @param bytes 根据通讯协议的前12个字节
     * @return
     */
    public static byte getCheckCode(byte[] bytes) {
        byte b = 0x00;
        for (int i = 0; i < bytes.length; i++) {
            b ^= bytes[i];
        }
        return b;
    }

    /**
     *  byte数组定长分段截取
     * */
    public static byte[] byteSub(byte[] data, int start, int length) {
        byte[] bt = new byte[length];

        if(start + length > data.length) {
            bt = new byte[data.length-start];
        }

        for(int i = 0; i < length &&(i + start) < data.length; i++) {
            bt[i] = data[i + start];
        }
        return bt;
    }

    public static byte[] decodeValue(ByteBuffer bytes) {
//        int len = bytes.limit() - bytes.position();
//        byte[] bytes1 = new byte[len];
//        bytes.get(bytes1);


        byte[] b = new byte[bytes.remaining()];  //byte[] b = new byte[bb.capacity()]  is OK

        bytes.get(b, 0, b.length);

        return b;
    }



    public static ByteBuffer encodeValue(byte[] value) {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(value.length);
//        byteBuffer.clear();
//        byteBuffer.get(value, 0, value.length);


        ByteBuffer byteBuffer = ByteBuffer.wrap(value);
        return byteBuffer;
    }

}
