package com.example.paperlessmeeting_demo.socket;


import android.util.Log;

import com.example.paperlessmeeting_demo.enums.SocketConnectStatus;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.UrlConstant;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

//import com.example.paperlessmeeting.activity.VideoChatActivity;

/**
 * Created by fengli on 2018/2/6.
 * 获取中控IP用的Socket基础类
 */

public class SocketManagerUuId {

    private static final String TAG = "InitSocketManager:";
    public static Socket socket;

    public static SocketConnectStatus connectStatus = SocketConnectStatus.SocketDisconnected;

    private static SocketManagerUuId.SocketCallBack callBack;

    public static void connect(final SocketManagerUuId.SocketCallBack callBack) {

        SocketManagerUuId.callBack = callBack;
        if (socket == null) {
            initSocket();
        } else {
            socket = null;
            initSocket();
        }

        socket.once(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                callBack.success();
            }
        });
        socket.connect();
        addHandles();
    }


    private static void initSocket() {

        IO.Options opts = IO.Options.builder().setQuery("mac=" + FLUtil.getMacAddress()).build();
//        opts.forceNew = false;
//        opts.reconnection = true;
//        opts.reconnectionDelay = 2000;      //延迟
//        opts.reconnectionDelayMax = 6000;
//        opts.reconnectionAttempts = -1;
//        opts.timeout = 6000;
        try {
            socket = IO.socket(UrlConstant.initUuIdSocketUrl, opts);
        } catch (Exception e) {
        }
    }

    public static abstract class SocketCallBack {

        public abstract void success();

        public abstract void fail();
    }

    private static void addHandles() {
        /*
         * 断开连接
         * */
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接断开");
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接失败args" + args[0]);
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接成功");
            }
        });

        // 连接状态改变
        socket.on("statusChange", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接状态确实改变了......");
            }
        });
    }


}
