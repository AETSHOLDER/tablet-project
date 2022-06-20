package com.example.paperlessmeeting_demo.tool.ScreenTools.utils;

/**
 * @author cyj
 * @createDate 5/6/22
 * @ClassName: SocketCmd
 * @Description: 静态常量类(替换enum) socket 指令码
 * @Version: 1.0
 */

public class SocketCmd {
    public static final int SocketCmd_Default = -1;
    public static final int SocketCmd_ReqReceiveScreen = 1000; //请求接收屏幕
    public static final int SocketCmd_ReqSendScreen    = 1001; //请求发送屏幕
    public static final int SocketCmd_RepAccept        = 1002; //回复确认
    public static final int SocketCmd_RepReject_001    = 1003; //回复拒绝，正在接收屏幕信息;
    public static final int SocketCmd_ScreentData      = 1004; //屏幕数据
    public static final int SocketCmd_Other            = 1005;
}
