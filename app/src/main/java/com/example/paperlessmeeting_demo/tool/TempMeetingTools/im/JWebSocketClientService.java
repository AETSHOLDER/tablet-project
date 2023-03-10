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
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.activity.LoginActivity;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity;
import com.example.paperlessmeeting_demo.activity.WhiteBoardActivity2;
import com.example.paperlessmeeting_demo.bean.PushBean;
import com.example.paperlessmeeting_demo.bean.TeamDisagree;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.meetingRecordId;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.AnalyticDataUtils;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.AutomaticVoteUtil;
import com.example.paperlessmeeting_demo.util.ByteUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class JWebSocketClientService {
    private static AnalyticDataUtils mAnalyticUtils = null;
    public static JWebSocketClient client;
    public static String TAG = "JWebSocketClientService";
    //?????????????????????
    private static int min = 1;
    private static int max = 50;

    private static List<Bitmap> bitmapList = new ArrayList<>();

    /**
     * ?????????websocket??????
     */
    public static void initSocketClient() {
        if (client != null && client.isOpen()) {
            return;
        }
        mAnalyticUtils = new AnalyticDataUtils();
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
                Log.d(TAG, "??????????????????" + message);
                // ????????????
                if (message.contains(constant.QUERYVOTE_WUHU_FRAGMENT)) {
                    EventMessage msg = new EventMessage(MessageReceiveType.MessageClient, message);
                    EventBus.getDefault().postSticky(msg);
                } else {
                    EventMessage msg = new EventMessage(MessageReceiveType.MessageClient, message);
                    EventBus.getDefault().post(msg);
                }

                Activity topActivity = ActivityUtils.getTopActivity();
                //  ????????????ws?????????????????????tempTeamReq
                if (message.contains("shareReq") || message.contains(constant.TEMPSHAREREQ)) {
                    String whiteName = "activity.WhiteBoardActivity2";
//                    Log.d("getLocalClassName", topActivity.getLocalClassName());

                    if (UserUtil.ISCHAIRMAN) {
                        return;
                    }
                    if (UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN) {
                        return;
                    }
                    if (topActivity != null) {
                        // ???????????????????????????????????????????????????
                        if (topActivity.getLocalClassName().contains(whiteName) || topActivity.getLocalClassName().contains("WhiteBoardActivity")) {
                            topActivity.finish();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Hawk.put(constant.team_share, "share");
                    Activity top2 = ActivityUtils.getTopActivity();
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
//                                CVIPaperDialogUtils.showCustomDialog(topActivity, "?????????????????????????????????!", "?????????????????????????", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
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
                                if (UserUtil.ISCHAIRMAN) {
                                    return;
                                }
                                if (UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN) {
                                    return;
                                }

                                CVIPaperDialogUtils.showCustomDialog(topActivity, "?????????????????????????????????!", "?????????????????????????", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                    @Override
                                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                        if (clickConfirm) {
                                            // ???????????????????????????????????????????????????
                                            String whiteName = "activity.WhiteBoardActivity2";
                                            if (topActivity.getLocalClassName().contains(whiteName) || topActivity.getLocalClassName().contains("WhiteBoardActivity")) {
                                                topActivity.finish();
                                                try {
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Activity top2 = ActivityUtils.getTopActivity();

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

                if (message.contains("shareFinish") || message.contains("teamFinish")) {
                    if (topActivity != null) {
                        String whiteName = "activity.WhiteBoardActivity";
                        // ?????????????????? ?????????????????????????????????
                        if (topActivity.getLocalClassName().contains(whiteName)) {
                            topActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CVIPaperDialogUtils.showCountDownConfirmDialog(topActivity, "??????|?????????????????????!", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                        @Override
                                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                            if (clickConfirm) {
                                                // ????????????????????????
                                                topActivity.finish();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }

                //  ????????????
                if (message.contains(constant.TEMPSHAREDIS) || message.contains(constant.TEMPTEAMDIS)) {
                    if (topActivity != null) {
                        String whiteName = "activity.WhiteBoardActivity2";
                        // ?????????????????? ?????????????????????????????????
                        if (topActivity.getLocalClassName().contains(whiteName)) {
                            topActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CVIPaperDialogUtils.showCountDownConfirmDialog(topActivity, "??????|?????????????????????!", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                        @Override
                                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                            if (clickConfirm) {
                                                // ????????????????????????
                                                topActivity.finish();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
                //  ????????????????????????????????????????????????
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

                //  ????????????fragment
                if (message.contains("tinajiyiti") || message.contains(constant.WUHUADDFRAGMENT)) {
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
                //  ??????????????????fragment
                if (message.contains("deletelwuhufragment") || message.contains(constant.DELETE_WUHU_FRAGMENT)) {
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

                //  ??????????????????fragment
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
                //  ??????????????????fragment????????????????????????
                if (message.contains("qufenliebiao") || message.contains(constant.REFRESH_WUHU_FILE_FRAGMENT)) {
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
                //  ??????????????????fragment
                if (message.contains("saveALL") || message.contains(constant.REFRASHWuHUALL)) {
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
                //??????????????????????????????
                if (message.contains(constant.PUSH_FILE_WEBSOCK)) {

                    TempWSBean<PushBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<PushBean>>() {
                    }.getType());
                    PushBean pushBean = wsebean.getBody();

                }
                //??????????????????
                if (message.contains(constant.CHANGE_COLOR_BG)) {

                    TempWSBean<String> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<String>>() {
                    }.getType());
                    String str = wsebean.getBody();

                }
                //  ????????????


                if (message.contains(constant.MEETINGFINISH)) {
                    try {
                        WSBean<meetingRecordId> wsebean = new Gson().fromJson(message, new TypeToken<WSBean<meetingRecordId>>() {
                        }.getType());
                        //  ??????vote???websocket?????????
                        if (wsebean != null) {
                            meetingRecordId meetingRecordId = wsebean.getBody();
                            if (meetingRecordId.getMeetingRecordId().equals(UserUtil.meeting_record_id)) { // ?????????????????????
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
                                            if (!UserUtil.ISCHAIRMAN) {
                                                if (Hawk.contains("WuHuFragmentData")) {
                                                    Hawk.delete("WuHuFragmentData");
                                                    if (Hawk.contains("wuhulocal")) {
                                                        Hawk.delete("wuhulocal");

                                                    }
                                                }
                                            }

                                            Intent intent = new Intent(topActivity, LoginActivity.class);
                                            topActivity.startActivity(intent);
                                            topActivity.finish();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Activity top2 = ActivityUtils.getTopActivity();
                                                    if (top2 != null) {
                                                        top2.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                CVIPaperDialogUtils.showCountDownConfirmDialog(top2, "???????????????!", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                                                    @Override
                                                                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                                                        if (clickConfirm) {

                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            }, 100);

                                        }
                                    });
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!UserUtil.ISCHAIRMAN) {
                    // ????????????????????????????????????
                    if (message.contains(constant.NEWVOTE)) {
                        TempWSBean<VoteListBean.VoteBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<VoteListBean.VoteBean>>() {
                        }.getType());
                        VoteListBean.VoteBean voteBean = wsebean.getBody();
                        String flag = wsebean.getFlag();
                        AutomaticVoteUtil.voteAlert(voteBean, flag);

                    }
                    // ???????????? ????????????
//                    if(message.contains("meeting_vote_id")){
//                        try {
//                            WSBean wsebean = new Gson().fromJson(message, WSBean.class);
//                            //  ??????vote???websocket?????????
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
              /*  //  ????????????
                if (message.contains(constant.MINUTE_MEETING)) {
                    if (topActivity != null) {
                        String whiteName = "activity.ActivityMinuteMeeting";
                        // ?????????????????? ?????????????????????????????????
                        if (topActivity.getLocalClassName().contains(whiteName)) {
                            topActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        WSBean wsebean = new Gson().fromJson(msg.getMessage(), WSBean.class);
                                        //  ??????vote???websocket?????????pushSign

                                        if (wsebean != null) {
                                            if (constant.MINUTE_MEETING.equals(wsebean.getPackType())) {
                                                HandWritingBean handWritingBean = new HandWritingBean();
                                                TempWSBean<HandWritingBean> wb = new Gson().fromJson(msg.getMessage(), new TypeToken<TempWSBean<HandWritingBean>>() {
                                                }.getType());
                                                if (wb != null) {
                                                    handWritingBean = wb.getBody();
                                                    Log.d("onReceiveMsg22222", "??????????????????" + handWritingBean.getSendUserId() + "==" + handWritingBean.getBaseData().length());
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
                // ??????????????????add
                if (message.contains(constant.RESETNAME)) {
                    sendMsg(creatAddMsg(constant.RESETNAME));
                }
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
//                super.onMessage(bytes);
//                Log.e("222","?????????client ???????????????????????????");
//                String strMsg = ByteUtil.byteBufferToString(bytes);
//                if(!strMsg.contains(constant.CVI_PAPER_SCREEN_DATA)){
//                    return;
//                }
                dealWithScreenData(bytes);
            }


            /**
             *  ??????????????????add
             * */
            private String creatAddMsg(String infoMsg) {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                /**
                 * ????????????????????????????????????????????????????????????????????????
                 * ?????????????????????????????????????????????id
                 * */
                String myNumber = "";
                if (infoMsg.equals(constant.RESETNAME)) {
                    Random random = new Random();
                    myNumber = (random.nextInt(max - min + 1) + min) + "";
                    Hawk.put(constant.myNumber, myNumber);
                } else {
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
                //?????????????????????????????????????????????add????????????????????????????????????add??????
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
                Log.d(TAG, "websocket????????????");
                mHandler.post(heartBeatRunnable);
            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
                ex.printStackTrace();

                Activity topActivity = ActivityUtils.getTopActivity();
                if (topActivity != null) {
                    Intent intent = new Intent(topActivity, LoginActivity.class);
                    topActivity.startActivity(intent);
                    topActivity.finish();
                }


                Looper.prepare();
                MeetingAPP.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity top2 = ActivityUtils.getTopActivity();
                        if (top2 != null) {

                            CVIPaperDialogUtils.showCountDownConfirmDialog(top2, "???????????????!", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if (clickConfirm) {

                                    }
                                }
                            });
                        }
                    }
                }, 100);
                Looper.loop();

            }
        };
        client.setConnectionLostTimeout(0);
        connect();
    }

    /**
     * ??????websocket
     */
    private static void connect() {
        new Thread() {
            @Override
            public void run() {
                if (!client.isOpen()) {
                    if (client.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                        try {
                            //connectBlocking?????????????????????????????????????????????????????????????????????????????????
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
     * ????????????
     *
     * @param msg
     */
    public static void sendMsg(String msg) {
        if (null != client && client.isOpen()) {
            Log.d("JWebSocketClientService", "??????????????????" + msg);
            client.send(msg);
        }
    }

    /**
     * ??????????????????,??????????????????????????????????????????
     *
     * @param bytes
     */
    public static void sendByteBuffer(ByteBuffer bytes) {
        if (null != client && client.isOpen()) {
//            Log.d("JWebSocketClientService", "??????????????????");

            byte[] aa = com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil.decodeValue(bytes);

//            String strMsg = ByteUtil.byteBufferToString(bytes);
            Log.d(TAG, "??????????????????->" + aa);
            client.send(bytes);
        }
    }

    public static void sendByteArr(byte[] bytes) {
        if (null != client && client.isOpen()) {
//            Log.d("JWebSocketClientService", "??????????????????");

            client.send(bytes);
        }
    }

    /**
     * ???????????????????????????
     */
    public static void dealWithScreenData(ByteBuffer bytes) {
        if (UserUtil.ISCHAIRMAN) {
            return;
        }
        try {
            final ReceiveData receiveData = mAnalyticUtils.analyticData(bytes);
            Activity topActivity = ActivityUtils.getTopActivity();
            if (topActivity != null) {
                topActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  ???????????????
                        EventScreenMessage eventScreenMessage = new EventScreenMessage(MessageReceiveType.MessageScreenData, receiveData);
                        EventBus.getDefault().postSticky(eventScreenMessage);
                    }
                });
            }

        } catch (IOException e) {

        }
    }

    /**
     * ????????????
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

    //    -------------------------------------websocket????????????------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//??????10??????????????????????????????????????????
    private static Handler mHandler = new Handler();
    private static int reconnectCount = 1;
    private static Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "???????????????websocket????????????");
            if (client != null) {
                if (client.isClosed()) {
                    if (UserUtil.isTempMeeting) {
                        //  ??????3???
                        if (reconnectCount > 0) {
                            reconnectWs();
                        } else {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                topActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CVIPaperDialogUtils.showCountDownConfirmDialog(topActivity, "???????????????!", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                            @Override
                                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                                if (clickConfirm) {
                                                    /**
                                                     * ????????????launcherMode?????????singleTask
                                                     * ???????????????????????????activity????????????
                                                     * ???LoginActivity?????????activity??????????????????
                                                     * ??????????????????????????????
                                                     * */
                                                    JWebSocketClientService.closeConnect();
                                                    if (UserUtil.isTempMeeting) {
                                                        Log.d(TAG, "???????????????111111");
                                                        if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                                                            // ????????????????????????????????????????????????
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
                    // ????????????
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
                //??????client?????????????????????????????????
                client = null;
                initSocketClient();
            }
            //????????????????????????????????????????????????????????????
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * ????????????
     */
    private static void reconnectWs() {
        reconnectCount--;
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "????????????");
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
