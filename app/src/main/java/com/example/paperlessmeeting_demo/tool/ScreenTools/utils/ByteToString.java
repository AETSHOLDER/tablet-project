package com.example.paperlessmeeting_demo.tool.ScreenTools.utils;

import java.util.Locale;

public class ByteToString {
	/**
     * 16进制表示的字符串转换为字节数组
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        if((len&1)==1){
        	len =len-1;
        }
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i+1), 16));
        }
        return bytes;
    }
	
    /**
     * 16进制的字符串表示转成字节数组
     * @param hexString 16进制格式的字符串            
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }
    
    /**
     * byte[]数组转换为16进制的字符串
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    /**
     * byte[]数组转换为16进制的字符串
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toLowerCase(Locale.getDefault());
    }
    
    /**
     * 字节数组转成16进制表示格式的字符串
     * @param byteArray 要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");
     
        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }
	
    /**
     * 字节转成16进制表示格式的字符串
     * @param tByte 要转换的字节
     * @return 16进制表示格式的字符串
     **/
	public static String mbyteToString(byte tByte){
		String mbyteToString= Integer.toBinaryString((tByte & 0xFF) + 0x100).substring(1);
		return mbyteToString;
	}
	
	/**
     * 16进制表示格式的字符串转成字节数组
     * @param str 要转换的16进制表示格式字符串
     * @return 字节数组
     **/
	public static byte[] hexToByteArray(String str){
//		str = str.replace(" ", "");
//		str = str.replace(",", "");
    	String[] newStr = str.split(" ");
    	byte[] array = new byte[newStr.length];
    	for(int i =0;i<newStr.length;i++){
    		int a = Integer.parseInt(newStr[i],16);
    		byte bb = (byte) a;
    		array[i]=bb;
    		System.out.println("bb-"+bb);
    	}
    	return array;
	}

    /**
     * 16进制表示格式的字符串转成字符串
     * @return 字节数组
     **/
    public static String hexStrTostr(String hexStr){
        byte[] bytes = ByteToString.hexStringToByteArray(hexStr);
        int tRecvCount = bytes.length;
        StringBuffer tStringBuf = new StringBuffer();
        char[] tChars = new char[tRecvCount];
        for (int i = 0; i < tRecvCount; i++) {
            tChars[i] = (char) bytes[i];
        }
        tStringBuf.append(tChars);
        return tStringBuf.toString();
    }
    /**
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }


    /**
     * 字节数组转化成为ASC
     * @param tByte
     * @return
     */
    public static String convertByteArrToASC(byte[] tByte){
        String hex = ByteToString.toHexString(tByte);
        return  ByteToString.convertHexToASC(hex);
    }

    //16进制字符串转ASCII码
    public static String convertHexToASC(String hex){
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for( int i=0; i<hex.length()-1; i+=2 ){
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

}
