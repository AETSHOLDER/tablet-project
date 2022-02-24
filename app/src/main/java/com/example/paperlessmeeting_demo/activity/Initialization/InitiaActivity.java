package com.example.paperlessmeeting_demo.activity.Initialization;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.InitiaBean.InitiaRoomBean;
import com.example.paperlessmeeting_demo.socket.InitSocketManager;
import com.example.paperlessmeeting_demo.tool.FLLog;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.fingerth.supdialogutils.SYSDiaLogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.helloworld.library.MiddleDialogConfig;
import com.helloworld.library.utils.DialogEnum;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.socket.client.Ack;

import static com.fingerth.supdialogutils.SYSDiaLogUtils.handler;

public class InitiaActivity extends BaseActivity {
    private InetAddress address;
    private Integer port = 12345;
    @BindView(R.id.dianji)
    TextView dianji;

    private String[] infoArr = {"正在查找服务器...", "正在连接服务器...", "正在查找会议室列表...", "正在注册..."};
    private String TAG = "InitiaActivity";
    private Handler mHandler;
    private static String MulticastHost = "224.1.1.2";
    private static int PORT = 15000;
    private boolean isfirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if(wm != null) {
//            WifiManager.MulticastLock multicastLock = wm
//                    .createMulticastLock("mylock");
//            multicastLock.acquire();
//        }
        Log.d(TAG, "mac===" + FLUtil.getMacAddress());
        FLUtil.findSerialPortInfo();
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            Log.v(TAG, "run...");
            try {
//                sendMultiBroadcast();
                connection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

//    Handler mHander = new Handler() {
//
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            switch (msg.what) {
//                case 1:
//                    String str = (String) msg.obj;
////                    ipStr = str.substring(4, str.length() - 4);
//             /*       if (isFirst) {
//                        Log.d("ewerrr", ipStr);
//                        Hawk.put("ip", ipStr.trim());
//                        isFirst = false;
//                    } else {
//                        if (Hawk.contains("ip")) {
//                            String ip = Hawk.get("ip");
//                            Log.d("ewerrr222", ipStr);
//                            if (!ipStr.equals(ip)) {
//                                Log.d("ewerrr333", ipStr);
//                                Hawk.put("ip", ipStr.trim());
//                                setChangeIp();
//                            }
//                        }
//
//                    }*/
//                    break;
//
//            }
//        }
//    };

    //  开启组播
    private void sendMultiBroadcast() throws IOException {
//        InetAddress inetAddress = InetAddress.getByName(MulticastHost);//多点广播地址组
//        MulticastSocket multicastSocket = new MulticastSocket(PORT);//多点广播套接字
//        multicastSocket.setInterface(InetAddress.getLocalHost());
//        multicastSocket.setTimeToLive(1);
//        multicastSocket.setBroadcast(true);
//        multicastSocket.joinGroup(inetAddress);
//
//
//
//        byte[] bytes = new byte[512];
//        DatagramPacket receivePacket = new DatagramPacket(bytes, bytes.length);
//        multicastSocket.receive(receivePacket);
//        String ip = receivePacket.getAddress().getHostAddress();
//        int port = receivePacket.getPort();
//        String msg = new String(bytes, 0, receivePacket.getLength());
//        //    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//        Log.d("getserver2222", ip + "\t port: " + port + "\tmsg: " + msg);
//        //   multicastSocket.close();
//        Log.e("UdpService", "start multcast socket");


//        UrlConstant.initSocketUrl = "http://"+ip+"/innerPlatform";
        /**
         *  step2:连接服务器
         * */
        Message msg1 = new Message();
        msg1.obj = infoArr[1];
        handler.sendMessage(msg1);
        connection();
    }

    @Override
    protected void initView() {
        dianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtils.init().isClickFast()) {
                    return;
                }
                /**
                 *  step1:组播查找服务器
                 * */
                SYSDiaLogUtils.showProgressDialog(InitiaActivity.this, SYSDiaLogUtils.SYSDiaLogType.IosType, infoArr[0], false, null);
                //通过Handler启动线程
                HandlerThread handlerThread = new HandlerThread("MultiSocketA");
                handlerThread.start();
                mHandler = new Handler(handlerThread.getLooper());
                mHandler.post(mRunnable);
            }
        });

    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_initia;
    }

    private void connection() {
        // 如果socket已连接，直接请求，未连接再连接
        if(InitSocketManager.socket.connected() ){
            doregister();
        }else {
            InitSocketManager.SocketCallBack callBack = new InitSocketManager.SocketCallBack() {
                @Override
                public void success() {
                    doregister();
                }

                @Override
                public void fail() {
                    Log.d(TAG, "连接失败!!!");
                }
            };
            InitSocketManager.connect(callBack);
        }

    }


    private void doregister() {
        FLLog.i("socket连接成功");
        /**
         *  step3:查找会议室
         * */
        Message msg = new Message();
        msg.obj = infoArr[2];
        handler.sendMessage(msg);
        // 查找会议室
        InitSocketManager.socket.emit("find meeting room", new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject msg = (JSONObject) args[0];
                BasicResponse<ArrayList<InitiaRoomBean>> roomListBean;
                Gson gson = new Gson();
                try {
                    roomListBean = gson.fromJson(msg.toString(), new TypeToken<BasicResponse<ArrayList<InitiaRoomBean>>>() {
                    }.getType());
                    if (roomListBean.getCode() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                SYSDiaLogUtils.showErrorDialog(InitiaActivity.this, "错误警告", "查找会议室错误，请重试！", "取消", false);
                            }
                        });
                        return;
                    }
                    //  do soming
                    ArrayList<InitiaRoomBean> list = roomListBean.getData();
                    Log.d(TAG, "find meeting room" + msg.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SYSDiaLogUtils.dismissProgress();
                            showMeetingRoom(list);
                        }
                    });
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *  step4:展示选择会议室
     * */
    private void showMeetingRoom(List<InitiaRoomBean> data) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            InitiaRoomBean bean = data.get(i);
            list.add(bean.getName());
        }

        new MiddleDialogConfig().builder(InitiaActivity.this)
                .setDialogStyle(DialogEnum.LIST)
                .setTitle("请选择会议室")
                .setDatas(list)
                .setLeftRightVis()
                .setItemSlidingCount(6, 0.65)
                .setRightCallBack(null)
                .setLeftCallBack(null)
                .setItemCallBack(new MiddleDialogConfig.ItemCallBackListener() {
                    @Override
                    public void item(String str) {
                        for (String ss : list) {
                            if (ss.equals(str)) {
                                int index = list.indexOf(ss);
                                InitiaRoomBean bean = data.get(index);
                                Hawk.put("meetingId", bean.getId());//保存当前设备所属会议室ID，和会议发布APP会议相匹配。
                                Hawk.put("meetingRoomName", bean.getName());//保存当前设备所属会议室名字，和会议发布APP会议相匹配。
                                //  step5:填写本机机号
                                fillMacNo(data.get(index));
                                break;
                            }
                        }
                    }
                })
                .show();
    }

    /**
     *  step5:填写本机机号
     * */
    private void fillMacNo(InitiaRoomBean data) {
        new MiddleDialogConfig().builder(InitiaActivity.this)
                .setDialogStyle(DialogEnum.EDIT)
                .setTitle("请填写本机机号")
                .setEditHint("1")
                .setLeftVis(false)
                .setRight("确定")
                .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                    @Override
                    public void rightBtn(String cont) {
                        /**
                         *  step6:是否有麦克风
                         * */
                        Log.d(TAG, "cont===" + cont);

                        withMic(data, cont);
                        Hawk.put(constant.myNumber, cont);
                    }
                }).show();
    }

    /**
     *  step6:是否有麦克风
     * */
    private void withMic(InitiaRoomBean data, String no) {
        new MiddleDialogConfig().builder(InitiaActivity.this)
                .setDialogStyle(DialogEnum.BASIC)
                .setTitle("是否有麦克风?")
                .setContent("")
                .setLeft("是的")
                .setRight("否")
                .setLeftCallBack(new MiddleDialogConfig.LeftCallBack() {
                    @Override
                    public void leftBtn(String cont) {
                        register(data, no, true);
                    }
                })
                .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                    @Override
                    public void rightBtn(String cont) {
                        /**
                         *  step6:完成注册
                         * */
                        register(data, no, false);
                    }
                }).show();
    }

    /**
     *  step6:完成注册
     * */
    private void register(InitiaRoomBean data, String no, boolean withMic) {
        JSONObject ppl = new JSONObject();
        JSONObject mic = new JSONObject();
        String meetingId = data.getId();
        String meetingRoomName = data.getName();
//                if (Hawk.contains(constant.micMac) && Hawk.contains(constant.micIP) && Hawk.contains(constant.micNumber)) {
        try {
            ppl.put("mac", FLUtil.getMacAddress());
            ppl.put("number", no);

//                        mic.put("mac", Hawk.get(constant.micMac));
//                        mic.put("number", Integer.parseInt(Hawk.get(constant.micNumber)));
//                        mic.put("group", 1);
//                        mic.put("ip", Hawk.get(constant.micIP));
//                        mic.put("withMic", withMic);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        InitSocketManager.socket.emit("register", new Object[]{meetingId, ppl, null}, new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject msg = (JSONObject) args[0];
                Log.d(TAG, msg.toString());

                Gson gson = new Gson();
                try {
                    BasicResponse dataBean = gson.fromJson(msg.toString(), BasicResponse.class);
                    if (dataBean.getCode() == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Hawk.put(constant.isFirstInit, false);
                                SYSDiaLogUtils.dismissProgress();
                                SYSDiaLogUtils.showSuccessDialog(InitiaActivity.this, "", "注册成功！", "确定", false);
                                SYSDiaLogUtils.dismissProgress();
//                                            Intent intent = new Intent(InitiaActivity.this, LoginActivity.class);
//                                            startActivity(intent);

                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Hawk.put(constant.isFirstInit, true);
                                SYSDiaLogUtils.dismissProgress();
                                SYSDiaLogUtils.showErrorDialog(InitiaActivity.this, "", "注册失败！", "确定", false);
                                isfirst = true;
                            }
                        });
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }
        });
//                } else {
//                    SYSDiaLogUtils.dismissProgress();
//                    SYSDiaLogUtils.showErrorDialog(InitiaActivity.this, "", "请查看串口连接！", "确定", false);
//                    return;
//                }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
