package com.example.paperlessmeeting_demo.socket;


import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
//import com.example.paperlessmeeting.activity.VideoChatActivity;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.enums.MessageEventType;
import com.example.paperlessmeeting_demo.enums.SocketConnectStatus;
import com.example.paperlessmeeting_demo.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by fengli on 2018/2/6.
 */

public class SocketManager {

    private static final String TAG = "SocketManager:";
    public static Socket socket;

    public static SocketConnectStatus connectStatus = SocketConnectStatus.SocketDisconnected;

    private static SocketCallBack callBack;

    public static void connect(String token, final SocketCallBack callBack) {

        SocketManager.callBack = callBack;
        if (socket == null) {
            initSocket(token);
        } else {
            socket = null;
            initSocket(token);
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


    private static void initSocket(String token) {
        IO.Options opts = new IO.Options();
        opts.forceNew = false;
        opts.reconnection = true;
        opts.reconnectionDelay = 2000;      //延迟
        opts.reconnectionDelayMax = 6000;
        opts.reconnectionAttempts = -1;
        opts.timeout = 6000;
        opts.query = "zzl=" + token;
        try {
            socket = IO.socket(UrlConstant.baseUrl, opts);
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
                connectStatus = SocketConnectStatus.SocketDisconnected;
                EventBus.getDefault().post(new MessageEvent(MessageEventType.EventConnectStatus, SocketConnectStatus.SocketDisconnected));
            }
        });


//        socket.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.i(TAG, "连接中");
//
//                connectStatus = SocketConnectStatus.SocketConnecting;
//                EventBus.getDefault().post(new MessageEvent(MessageEventType.EventConnectStatus, SocketConnectStatus.SocketConnecting));
//            }
//        });


     /*   socket.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接中");

                connectStatus = SocketConnectStatus.SocketConnecting;
                EventBus.getDefault().post(new MessageEvent(MessageEventType.EventConnectStatus, SocketConnectStatus.SocketConnecting));
            }
        });*/




        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接失败");
                connectStatus = SocketConnectStatus.SocketConnectError;
                EventBus.getDefault().post(new MessageEvent(MessageEventType.EventConnectStatus, SocketConnectStatus.SocketConnectError));
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接成功");
                connectStatus = SocketConnectStatus.SocketConnected;
                EventBus.getDefault().post(new MessageEvent(MessageEventType.EventConnectStatus, SocketConnectStatus.SocketConnected));
            }
        });

      /*  // 收到信消息
        socket.on("chat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "收到消息");
                Ack ack = (Ack) args[1];
                ack.call("我已收到消息");
                JSONObject msg = (JSONObject) args[0];


                byte[] file = null;
                try {
                    JSONObject msgBody = msg.getJSONObject("bodies");

                    file = (byte[]) msgBody.get("fileData");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final MessageModel message = JSON.parseObject(msg.toString(), MessageModel.class);

                if (file != null) {

                    String fileName = message.getBodies().getFileName();
                    String savePath = "";
                    switch (message.getBodies().getType()) {
                        case img:
                            savePath = FLUtil.imageSavePath() + fileName;
                            break;
                        case audio:
                            savePath = FLUtil.audioSavePath() + fileName;
                            break;
                    }
                    File imageFile = new File(savePath);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        outputStream.write(file);
                        outputStream.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                // 消息保存数据库
                DbManager.insertMessage(message);

                // 更新会话
                DbManager.insertOrUpdateConversation(message);

                // 发送事件
                MessageEvent event = new MessageEvent(MessageEventType.EventMessage, message);
                EventBus.getDefault().post(event);
            }
        });*/

        // 视频通话请求
        socket.on("videoChat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONObject dataObject = (JSONObject) args[0];

                try {
                    String fromUser = dataObject.getString("from_user");
                    String room = dataObject.getString("room");

                    Bundle bundle = new Bundle();
                    bundle.putString("fromUser", fromUser);
                    bundle.putString("toUser", ClientManager.currentUserId);
                    bundle.putString("room", room);
                    bundle.putInt("type", 1);

                    BaseActivity topActivity = (BaseActivity) ActivityUtils.getTopActivity();

                    //   topActivity.openActivity(VideoChatActivity.class, bundle);
                    if (topActivity != null) {

                    } else {
                        Log.i(TAG, "获取栈顶activity失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        // 用户上线
        socket.on("onLine", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONObject msg = (JSONObject) args[0];
                try {
                    MessageEvent event = new MessageEvent(MessageEventType.EventUserOnline, msg.getString("user"));
                    EventBus.getDefault().post(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        // 用户下线
        socket.on("offLine", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject msg = (JSONObject) args[0];
                try {
                    MessageEvent event = new MessageEvent(MessageEventType.EventUserOffLine, msg.getString("user"));
                    EventBus.getDefault().post(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // 连接状态改变
        socket.on("statusChange", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "连接状态确实改变了......");
            }
        });


        socket.on("zzl", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            /*    JSONObject msg = (JSONObject) args[0];
                try {
                    MessageEvent event = new MessageEvent(MessageEventType.EventUserOffLine, msg.getString("user"));
                    EventBus.getDefault().post(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }


}
