package com.example.paperlessmeeting_demo.tool.TempMeetingTools.im;

import android.util.Log;

import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ByteUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


public class MyWebSocketServer extends WebSocketServer {
    private String TAG = "MyWebSocketServer";

    public MyWebSocketServer(){
        super();
    }

    public MyWebSocketServer(InetSocketAddress host){
        super(host);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "onOpen()一个客户端连接成功："+conn.getRemoteSocketAddress());
    }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "onClose()与client端连接断开");
        ServerManager.getInstance().UserLeave(conn);
    }
    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, "onMessage()client端来的消息->"+message);
        // add信息
        if (message.contains(constant.ADD)) {
            try {
                TempWSBean wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean>() {
                }.getType());

                //  收到添加客户端的websocket的信息add
                if (wsebean != null) {
                    if (wsebean.getPackType().equals(constant.ADD)) {
                        ServerManager.getInstance().UserLogin(wsebean.getUserMac_id(),wsebean.getUserMac_number(), conn);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ServerManager.getInstance().MsgReceive(message,conn);
    }
    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        // 接收到的是Byte数据，需要转成文本数据
//        String strMsg = ByteUtil.byteBufferToString(message);
//        Log.d(TAG, "onMessage()接收到ByteBuffer的数据->"+ strMsg);
//        if(!strMsg.contains(constant.CVI_PAPER_SCREEN_DATA)){
//            return;
//        }
        ServerManager.getInstance().MsgReceiveByte(message,conn);
    }
    @Override
    public void onError(WebSocket conn, Exception ex) {
        // 异常  经常调试的话会有缓存，导致下一次调试启动时，端口未关闭,多等待一会儿
        // 可以在这里回调处理，关闭连接，开个线程重新连接调用startMyWebsocketServer()
        Log.e(TAG, "->onError()出现异常："+ex);
        ServerManager.getInstance().reconnect();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()，WebSocket服务端启动成功");
        ServerManager.getInstance().serverOnStart();
    }


}
