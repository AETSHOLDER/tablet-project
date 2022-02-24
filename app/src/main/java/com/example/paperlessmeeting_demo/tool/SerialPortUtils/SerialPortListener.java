package com.example.paperlessmeeting_demo.tool.SerialPortUtils;

public interface SerialPortListener {
    public final static byte STATUS_CONNECT_SUCCESS = 1;//连接成功

    public final static byte STATUS_CONNECT_CLOSED = 0;//关闭连接

    public final static byte STATUS_CONNECT_ERROR = 0;//连接失败


    /**
     * 当接收到系统消息
     * isComplete 是否完成
     */
    void onMessageResponse(String msg, boolean isComplete);

    /**
     * 当连接状态发生变化时调用
     */
//    public void onPortStatusConnectChanged(int statusCode);
}
