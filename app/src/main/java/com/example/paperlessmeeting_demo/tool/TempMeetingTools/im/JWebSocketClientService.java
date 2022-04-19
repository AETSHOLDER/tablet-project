package com.example.paperlessmeeting_demo.tool.TempMeetingTools.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import com.blankj.utilcode.util.ActivityUtils;
import com.example.paperlessmeeting_demo.activity.LoginActivity;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity2;
import com.example.paperlessmeeting_demo.bean.TeamDisagree;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.meetingRecordId;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.AutomaticVoteUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;
import org.greenrobot.eventbus.EventBus;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class JWebSocketClientService {
    public static JWebSocketClient client;
    public static String TAG = "JWebSocketClientService";
    //范围内的随机数
    private static int min = 1;
    private static int max = 50;

    private static List<Bitmap> bitmapList = new ArrayList<>();

    /**
     * 初始化websocket连接
     */
    public static void initSocketClient() {
        if(client != null && client.isOpen()){
            return;
        }
        URI uri = null;
        if (UserUtil.isTempMeeting) {
            uri = URI.create(UrlConstant.TempWSIPString);
        } else {
            uri = URI.create(UrlConstant.wsUrl);
        }
        reconnectCount = 2;
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.d(TAG, "收到的消息：" + message);
                // 发出消息
                EventMessage msg = new EventMessage(MessageReceiveType.MessageClient, message);
                EventBus.getDefault().post(msg);

                Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                //  处理通过ws收到的白板消息tempTeamReq
                if (message.contains("shareReq") || message.contains(constant.TEMPSHAREREQ)) {
                    String whiteName = "activity.WhiteBoardActivity2";
//                    Log.d("getLocalClassName", topActivity.getLocalClassName());

                    if(UserUtil.ISCHAIRMAN){
                        return;
                    }
                    if (UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN) {
                        return;
                    }
                    if (topActivity != null) {
                        // 如果是在白板内，先关闭白板，再进入
                        if(topActivity.getLocalClassName().contains(whiteName)|| topActivity.getLocalClassName().contains("WhiteBoardActivity")){
                            topActivity.finish();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Hawk.put(constant.team_share, "share");
                    Activity top2= (Activity) ActivityUtils.getTopActivity();
                    if (top2 != null) {
                        Intent intent;
                        if (UserUtil.isTempMeeting) {
                            intent = new Intent(top2, WhiteBoardActivity2.class);
                        } else {
                            intent = new Intent(top2, WhiteBoardActivity.class);
                        }
                        startActivity(intent);
                    }

//                    if (topActivity != null) {
//                        topActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                CVIPaperDialogUtils.showCustomDialog(topActivity, "您收到一个同屏白板请求!", "是否进入白板页面?", "进入", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
//                                    @Override
//                                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
//                                        if (clickConfirm) {
//
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    }
                }

                if (message.contains("teamReq") || message.contains(constant.TEMPTEAMREQ)) {
                    if (topActivity != null) {
                        topActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(UserUtil.ISCHAIRMAN){
                                    return;
                                }
                                if (UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN) {
                                    return;
                                }

                                CVIPaperDialogUtils.showCustomDialog(topActivity, "您收到一个协作白板请求!", "是否进入白板页面?", "进入", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                    @Override
                                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                        if (clickConfirm) {
                                            // 如果是在白板内，先关闭白板，再进入
                                            String whiteName = "activity.WhiteBoardActivity2";
                                            if(topActivity.getLocalClassName().contains(whiteName) || topActivity.getLocalClassName().contains("WhiteBoardActivity")){
                                                topActivity.finish();
                                                try {
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Activity top2 = (Activity) ActivityUtils.getTopActivity();

                                            Hawk.put(constant.team_share, "team");
                                            if (top2 != null) {
                                                Intent intent;
                                                if (UserUtil.isTempMeeting) {
                                                    intent = new Intent(top2, WhiteBoardActivity2.class);
                                                } else {
                                                    intent = new Intent(top2, WhiteBoardActivity.class);
                                                }
                                                startActivity(intent);
                                            }

                                        } else if (clickCancel) {
                                            Hawk.put(constant.team_share, "team");

                                            if (!Hawk.contains(constant.attendeIDList)) {
                                                return;
                                            }
                                            TeamDisagree disagree = new TeamDisagree();
                                            disagree.setPackType("teamAccept");
                                            disagree.setDevice("app");
                                            disagree.setUser_id(UserUtil.user_id);

                                            TeamDisagree.BodyBean body = new TeamDisagree.BodyBean();
                                            body.setResult("disagree");
                                            body.setUser_list(Hawk.get(constant.attendeIDList));

                                            disagree.setBody(body);
                                            String strJson = new Gson().toJson(disagree);
                                            sendMsg(strJson);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }

                if (message.contains("shareFinish") || message.contains("teamFinish")){
                    if (topActivity != null) {
                        String whiteName = "activity.WhiteBoardActivity";
                        // 在白板页面内 提示，出了白板不再提示
                        if (topActivity.getLocalClassName().contains(whiteName)) {
                            topActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CVIPaperDialogUtils.showCountDownConfirmDialog(topActivity, "同屏|协作白板已断开!", "确定", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                        @Override
                                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                            if (clickConfirm) {
                                                // 协作白板必须断开
                                                topActivity.finish();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }

                //  断开同屏
                if (message.contains(constant.TEMPSHAREDIS) || message.contains(constant.TEMPTEAMDIS)) {
                    if (topActivity != null) {
                        String whiteName = "activity.WhiteBoardActivity2";
                        // 在白板页面内 提示，出了白板不再提示
                        if (topActivity.getLocalClassName().contains(whiteName)) {
                            topActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CVIPaperDialogUtils.showCountDownConfirmDialog(topActivity, "同屏|协作白板已断开!", "确定", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                        @Override
                                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                            if (clickConfirm) {
                                                // 协作白板必须断开
                                                topActivity.finish();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
                //  服务器通知其他设备更新公私有文件
                if (message.contains(constant.RUSHFILELIST) || message.contains(constant.RUSHFILELIST)) {
                    if (topActivity != null) {
                     /*
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", fileType);
                        intent.putExtras(bundle);
                        intent.setAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
                        this.sendBroadcast(intent);*/

                    }
                }

                //  芜湖增加fragment
                if (message.contains("addfragments") || message.contains(constant.WUHUADDFRAGMENT)) {
                    if (topActivity != null) {
                     /*
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", fileType);
                        intent.putExtras(bundle);
                        intent.setAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
                        this.sendBroadcast(intent);*/

                    }
                }
                //  芜湖删除某个fragment
                if (message.contains("refreshdata") || message.contains(constant.DELETE_WUHU_FRAGMENT)) {
                    if (topActivity != null) {
                     /*
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", fileType);
                        intent.putExtras(bundle);
                        intent.setAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
                        this.sendBroadcast(intent);*/

                    }
                }

                //  芜湖更新某个fragment
                if (message.contains("refreshdata") || message.contains(constant.REFRASHWuHUSIGLEDATA)) {
                    if (topActivity != null) {
                     /*
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", fileType);
                        intent.putExtras(bundle);
                        intent.setAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
                        this.sendBroadcast(intent);*/

                    }
                }
                //  芜湖更新所有fragment
                if (message.contains("refreshdataALL") || message.contains(constant.REFRASHWuHUALL)) {
                    if (topActivity != null) {
                     /*
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", fileType);
                        intent.putExtras(bundle);
                        intent.setAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
                        this.sendBroadcast(intent);*/

                    }
                }
                //  会议结束
                if (message.contains(constant.MEETINGFINISH)) {
                    try {
                        WSBean<meetingRecordId> wsebean = new Gson().fromJson(message, new TypeToken<WSBean<meetingRecordId>>() {}.getType());
                        //  收到vote的websocket的信息
                        if (wsebean != null) {
                            meetingRecordId meetingRecordId = wsebean.getBody();
                            if(meetingRecordId.getMeetingRecordId().equals(UserUtil.meeting_record_id)){ // 如果是当前会议
                                if (topActivity != null) {
                                    topActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            JWebSocketClientService.closeConnect();
                                            UserUtil.user_id = "";
                                            UserUtil.meeting_record_id = "";
                                            UserUtil.user_name = "";

                                            Hawk.delete(constant._id);
                                            Hawk.delete(constant.user_id);
                                            Hawk.delete(constant.user_name);

                                            Intent intent = new Intent(topActivity, LoginActivity.class);
                                            topActivity.startActivity(intent);
                                            topActivity.finish();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Activity top2 = (Activity) ActivityUtils.getTopActivity();
                                                    if(top2!=null){
                                                        CVIPaperDialogUtils.showCountDownConfirmDialog(top2, "会议已结束!", "确定", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                                            @Override
                                                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                                                if (clickConfirm) {

                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            },100);

                                        }
                                    });
                                }
                            }
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!UserUtil.ISCHAIRMAN) {
                    // 收到临时会议新增投票信息
                    if (message.contains(constant.NEWVOTE)) {
                        TempWSBean<VoteListBean.VoteBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<VoteListBean.VoteBean>>(){}.getType());
                        VoteListBean.VoteBean voteBean = wsebean.getBody();
                        String flag= wsebean.getFlag();
                        AutomaticVoteUtil.voteAlert(voteBean,flag);

                    }
                    // 正式会议 投票信息
//                    if(message.contains("meeting_vote_id")){
//                        try {
//                            WSBean wsebean = new Gson().fromJson(message, WSBean.class);
//                            //  收到vote的websocket的信息
//                            if(wsebean!=null){
//                                if( wsebean.getPackType().equals("vote")){
//                                    // do something
//                                }
//                            }
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
              /*  //  秘书签批
                if (message.contains(constant.MINUTE_MEETING)) {
                    if (topActivity != null) {
                        String whiteName = "activity.ActivityMinuteMeeting";
                        // 在白板页面内 提示，出了白板不再提示
                        if (topActivity.getLocalClassName().contains(whiteName)) {
                            topActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        WSBean wsebean = new Gson().fromJson(msg.getMessage(), WSBean.class);
                                        //  收到vote的websocket的信息pushSign

                                        if (wsebean != null) {
                                            if (constant.MINUTE_MEETING.equals(wsebean.getPackType())) {
                                                HandWritingBean handWritingBean = new HandWritingBean();
                                                TempWSBean<HandWritingBean> wb = new Gson().fromJson(msg.getMessage(), new TypeToken<TempWSBean<HandWritingBean>>() {
                                                }.getType());
                                                if (wb != null) {
                                                    handWritingBean = wb.getBody();
                                                    Log.d("onReceiveMsg22222", "路过！！！！" + handWritingBean.getSendUserId() + "==" + handWritingBean.getBaseData().length());
                                                    bitmapList.add(base642Bitmap(handWritingBean.getBaseData()));
                                                    Hawk.put("handWritingBean", bitmapList);

                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.d("gfhhd", e.getMessage().toString());
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }*/
                // 有重名，重新add
                if (message.contains(constant.RESETNAME)) {
                    sendMsg(creatAddMsg(constant.RESETNAME));
                }
            }

            /**
             *  临时会议专用add
             * */
            private String creatAddMsg(String infoMsg) {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                /**
                 * 如果保存有本机机号，发送机号，否则随机并验证重名
                 * 验证重名，有重名不再发送以前的id
                 * */
                String myNumber = "";
                if(infoMsg.equals(constant.RESETNAME)){
                    Random random = new Random();
                    myNumber = (random.nextInt(max - min + 1) + min) + "";
                    Hawk.put(constant.myNumber, myNumber);
                }else {
                    if (Hawk.get(constant.myNumber) != null) {
                        myNumber = Hawk.get(constant.myNumber);
                    } else {
                        Random random = new Random();
                        myNumber = (random.nextInt(max - min + 1) + min) + "";
                        Hawk.put(constant.myNumber, myNumber);
                    }
                }

                bean.setUserMac_number(myNumber);
                bean.setPackType(constant.ADD);
                bean.setBody("");
                return new Gson().toJson(bean);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                //如果是临时会议，发送临时会议的add信息，正式会议发送正式的add信息
                String strJson = "";
                if (UserUtil.isTempMeeting) {
                    strJson = creatAddMsg("null");
                } else {
                    WSBean bean = new WSBean();
                    bean.setReqType(0);
                    bean.setUser_id(UserUtil.user_id);
                    bean.setPackType(constant.ADD);
                    bean.setDevice("app");
                    bean.setBody("");
                    strJson = new Gson().toJson(bean);
                }

                sendMsg(strJson);
                Log.d(TAG, "websocket连接成功");
                mHandler.post(heartBeatRunnable);
            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
                ex.printStackTrace();

                Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                if (topActivity != null) {
                    Intent intent = new Intent(topActivity, LoginActivity.class);
                    topActivity.startActivity(intent);
                    topActivity.finish();
                }


                Looper.prepare();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity top2 = (Activity) ActivityUtils.getTopActivity();
                        if(top2!=null){
                            CVIPaperDialogUtils.showCountDownConfirmDialog(top2, "会议已结束!", "确定", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if (clickConfirm) {

                                    }
                                }
                            });
                        }
                    }
                },100);
                Looper.loop();

            }
        };
        connect();
    }

    /**
     * 连接websocket
     */
    private static void connect() {
        new Thread() {
            @Override
            public void run() {
                if (!client.isOpen()) {
                    if (client.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                        try {
                            //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                            client.connectBlocking();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (client.getReadyState().equals(ReadyState.CLOSING) || client.getReadyState().equals(ReadyState.CLOSED)) {
                        client.reconnect();
                    }
                }
            }
        }.start();

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public static void sendMsg(String msg) {
        if (null != client && client.isOpen()) {
            Log.d("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg);
        }
    }

    /**
     * 断开连接
     */
    public static void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
            if (mHandler != null) {
                mHandler.removeCallbacks(heartBeatRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private static Handler mHandler = new Handler();
    private static int reconnectCount = 1;
    private static Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "心跳包检测websocket连接状态");
            if (client != null) {
                if (client.isClosed()) {
                    if (UserUtil.isTempMeeting) {
                        //  重连3次
                        if (reconnectCount > 0) {
                            reconnectWs();
                        } else {
                            Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                topActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CVIPaperDialogUtils.showCountDownConfirmDialog(topActivity, "会议已结束!", "退出", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                            @Override
                                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                                if (clickConfirm) {
                                                    /**
                                                     * 启动模式launcherMode设置为singleTask
                                                     * 使用的时候直接启动activity就可以啦
                                                     * 在LoginActivity之上的activity会自动被清除
                                                     * 符合栈的后进先出原则
                                                     * */
                                                    JWebSocketClientService.closeConnect();
                                                    if (UserUtil.isTempMeeting) {
                                                        Log.d(TAG, "是否先执行111111");
                                                        if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                                                            // 如果是服务端，关掉服务，关停广播
                                                            ServerManager.getInstance().StopMyWebsocketServer();
                                                            UDPBroadcastManager.getInstance().removeUDPBroastcast();
                                                        }
                                                    }
                                                    Intent intent = new Intent(topActivity, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }

                    } else {
                        reconnectWs();
                    }

                } else {
                    // 发送心跳
                    String strJson = "";
                    if (UserUtil.isTempMeeting) {
                        TempWSBean bean = new TempWSBean();
                        bean.setReqType(0);
                        bean.setUserMac_id(FLUtil.getMacAddress());
                        bean.setPackType(constant.HEART);
                        bean.setBody("");
                        strJson = new Gson().toJson(bean);
                        sendMsg(strJson);
                    } else {
                        WSBean bean = new WSBean();
                        bean.setReqType(0);
                        bean.setUser_id(UserUtil.user_id);
                        bean.setPackType(constant.HEART);
                        bean.setDevice("app");
                        bean.setBody("");
                        strJson = new Gson().toJson(bean);
                    }

                    sendMsg(strJson);

                }
            } else {
                //如果client已为空，重新初始化连接
                client = null;
                initSocketClient();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private static void reconnectWs() {
        reconnectCount--;
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static Bitmap base642Bitmap(String base64) {
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return mBitmap;
    }
}
