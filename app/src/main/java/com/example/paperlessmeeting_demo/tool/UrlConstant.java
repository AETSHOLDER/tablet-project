package com.example.paperlessmeeting_demo.tool;

/**
 * Created by fengli on 2018/2/5.
 */

public class UrlConstant {
    // public static final String baseUrl = "http://192.168.1.5:3000";
//    public static final String baseUrl = "http://192.168.11.39:3001";

    public static String baseUrl = "http://192.168.31.76:3006";
    public static String baseUuIdUrl = "http://192.168.8.141:6583";//请求中控摄像头的IP,勿改勿动

    public static final String login_url = "mobileLogin";
    public static final String register_url = "register";
    public static final String allUsers_url = "allUsers";

    public static  String wsUrl = "ws://192.168.1.1:8010";
    public static  String initSocketUrl = "http://192.168.8.141:13000/innerPlatform";
    public static String initUuIdSocketUrl = "http://192.168.8.141:13000/innerPlatform";
    public static String auth_token = "";
    public static String webUrl = "http://192.168.1.1:8888";
    public static String steamUrl = "rtmp://192.168.8.76:1935/live/123";
    public static String streamUuIdUrl = "rtmp://192.168.8.76:1935/live/123";
    public static String TempWSIPString = "";       //  临时会议服务server端IP
    public static final int port = 9090;     //  临时会议服务server端port
}
