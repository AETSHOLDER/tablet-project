package com.example.paperlessmeeting_demo.activity;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.activity.Sign.SignActivity;
import com.example.paperlessmeeting_demo.activity.Sign.SignListActivity;
import com.example.paperlessmeeting_demo.adapter.WuHuListAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.adapter.PagerAdapter;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.fragment.WuHuFragment;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.sharefile.BroadcastUDPFileService;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WuHuActivity  extends BaseActivity implements View.OnClickListener,WuHuListAdapter.saveSeparatelyInterface{
    @BindView(R.id.edit_ll)
    RelativeLayout edit_ll;
    @BindView(R.id.edit_rl)
    RelativeLayout edit_rl;
    @BindView(R.id.edit_name_rl)
    RelativeLayout edit_name_rl;
    @BindView(R.id.vote_ll)
    RelativeLayout vote_ll;
    @BindView(R.id.consult_ll)
    RelativeLayout consult_ll;

    @BindView(R.id.finish_ll)
    RelativeLayout finish_ll;

    private Dialog dialog;
    @BindView(R.id.comfirm)
    ImageView comfirm;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView timeTv;
    private List<String> stringsIp = new ArrayList<>();//临时会议存储各个设备Ip
  /*  @BindView(R.id.tittle2)*/
  private   EditText tittle2;
   /* @BindView(R.id.company_name)*/
  private   EditText company_name;
    private MyListView myListView;
    private WuHuListAdapter wuHuListAdapter;
    private  int  k=0;
    private View inflate;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList=new ArrayList<>();

    private RelativeLayout  add_topic_rl;
    private RelativeLayout  dialg_rl_root;
    private RelativeLayout sava_all;
    private Button mBtnDelete;
    private Button mBtnAdd;
    private ViewPager mViewPager;
    private SparseArray<WuHuFragment>mTestFragments;
    private PagerAdapter mPagerAdapter;
    private int key;
    private int mCurPos;
    private int fragmentPos=0;
     private MyBroadcastReceiver myBroadcastReceiver;
    private MyBroadcastReceiver myRefreshBroadcastReceiver;
    private InetAddress address;
    private boolean isBind = false;
    /*
     * 临时会议分享文件，service和InetAddress
     * */
    private BroadcastUDPFileService broadcastUDPFileService;
    private BroadcastUDPFileService.BroadcastUDPFileServiceBinder binder;
    private  WuHuEditBean wuHuEditBean;
    //表示红线的flag
    private String lineFlag;
    //标识主题的颜色
    private String themFlag;
    private boolean  isEditRl=false;
    private SocketShareFileManager socketShareFileManager;
    private ServiceConnection serviceConnection;
    private Handler handler ;
    private Runnable runnable;
    /**
     * 点击返回时间
     */
    private long time = 2000;
    private long first_time = 0;
    private String meetingTime ;//会议-月日时分
    private  String week = "";
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            String selfIp = "";
            String stIp = "";
            switch (msg.what) {

                case 2:
                    Toast.makeText(WuHuActivity.this, "正在接受文件", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(WuHuActivity.this, "文件已成功接收，请在文件管理页面查看", Toast.LENGTH_SHORT).show();
                    Log.e("hahahahahahaa","case 3");
                    int flag= msg.arg1;
                    int flag2=msg.arg2;
                    String filePath1=(String) msg.obj;
                    Intent intent1 = new Intent();
                    Bundle bundle1=new Bundle();
                    bundle1.putString("flag","1");
                    bundle1.putString("filePath",filePath1);
                    intent1.putExtras(bundle1);
                    intent1.setAction(constant.SHARE_FILE_BROADCAST);
                    sendBroadcast(intent1);
                    break;
                case 4:
                    Toast.makeText(WuHuActivity.this, "文件接收失败", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(WuHuActivity.this, "推送文件已成功接收，请在文件管理页面查看", Toast.LENGTH_SHORT).show();
                    Log.e("hahahahahahaa","case 8");
                    String filePath=(String) msg.obj;
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putString("flag","2");
                    bundle.putString("filePath",filePath);
                    intent.putExtras(bundle);
                    intent.setAction(constant.SHARE_FILE_BROADCAST);
                    sendBroadcast(intent);

                    break;
                case 119:
                    String adressIp = (String) msg.obj;
                    String strIp = "";
                    if (Hawk.contains("SelfIpAddress")) {
                        strIp = Hawk.get("SelfIpAddress");
                    }
                    if (!adressIp.equals(strIp)) {
                        stringsIp.add(adressIp);
                    }
                    for (int i = 0; i < stringsIp.size() - 1; i++) {
//                        Log.d("gdhh222", stringsIp.get(i) + "");
                        for (int j = stringsIp.size() - 1; j > i; j--) {
                            if (stringsIp.get(i).equals(stringsIp.get(j)))
                                stringsIp.remove(j);
                        }
                    }
                    Hawk.put("stringsIp", stringsIp);
                    break;

            }


        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.ADD_FRAGMENT_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);

      /*  myRefreshBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(constant.REFRESH_BROADCAST);
        registerReceiver(myRefreshBroadcastReceiver, filter2);*/

        // 如果是临时会议,判断是否是主席
        if (UserUtil.isTempMeeting) {
            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageClient)) {
                //  客户端默认非主席
                UserUtil.ISCHAIRMAN = false;
                String ipUrl = getIntent().getStringExtra("ip");
                if (!StringUtils.isEmpty(ipUrl)) {
                    UrlConstant.TempWSIPString = "ws://" + ipUrl + ":" + UrlConstant.port;
                    Log.e("临时会议", "临时会议的server IP ======" + UrlConstant.TempWSIPString);
                }

                //  连接websocket(server，client均要连)
                JWebSocketClientService.initSocketClient();

            } else if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                UserUtil.ISCHAIRMAN = true;
                String code = getIntent().getStringExtra("code");
              //  alertCodeDialog(code);

                //  广播四位数code以及IP地址
                UDPBroadcastManager.getInstance().sendUDPWithCode(code);
                // 开启socket服务器
                ServerManager.getInstance().startMyWebsocketServer(UrlConstant.port);
                UrlConstant.TempWSIPString = "ws://" + FLUtil.getIPAddress() + ":" + UrlConstant.port;
                // 主席的client在收到server创建成功后连接
            }
        }
        EventBus.getDefault().register(this);
        wuHuEditBeanList.clear();
        if (UserUtil.ISCHAIRMAN) {
            edit_ll.setVisibility(View.VISIBLE);
            finish_ll.setVisibility(View.VISIBLE);
            if (Hawk.contains("WuHuFragmentData")){
                wuHuEditBean= Hawk.get("WuHuFragmentData");
                wuHuEditBean.setTopics("2022年临时会议");
                wuHuEditBean.setTopic_type("会议记录");
                WuHuEditBean.EditListBean editListBean=new WuHuEditBean.EditListBean();
                editListBean.setSubTopics("总结2022年");
                editListBean.setAttendeBean("某某，某某，某某，某");
                wuHuEditBeanList.add(editListBean);
                Hawk.put("WuHuFragmentData",wuHuEditBean);
            }
        }else {
            edit_ll.setVisibility(View.GONE);
            finish_ll.setVisibility(View.GONE);
        }

        wuHuListAdapter=new WuHuListAdapter(WuHuActivity.this,wuHuEditBeanList);
        wuHuListAdapter.setSaveSeparatelyInterface(this);
        /*
         *
         * 当是临时会议时，开启Servive随时监听各参会人人员分享的文件。正常网络会议时监听同屏关闭操作
         * */
        socketShareFileManager = new SocketShareFileManager(mHander);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (BroadcastUDPFileService.BroadcastUDPFileServiceBinder) service;
                broadcastUDPFileService = binder.getService();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] bytes = new byte[1024];
                        try {
                            address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        DatagramSocket datagramSocket = null;
                        try {
                            datagramSocket = new DatagramSocket(constant.EXTRAORDINARY_MEETING_PORT);
                            datagramSocket.setBroadcast(true);
                            DatagramPacket datagramPacket = new DatagramPacket(bytes,
                                    bytes.length);
                            while (true) {
                                String strMsg = null;
                                int p = datagramPacket.getPort();
                                // 准备接收数据
                                try {
                                    // datagramSocket.setSoTimeout(20000);
                                    datagramSocket.receive(datagramPacket);
                                    strMsg = new String(datagramPacket.getData(), 0, datagramPacket.getData().length, "UTF-8");
                                    if (strMsg.contains(constant.SHARE_FILE_IP)) {
                                        String ip = datagramPacket.getAddress().getHostAddress();
                                        Message message = new Message();
                                        message.what = 119;
                                        message.obj = ip;
                                        mHander.sendMessage(message);
                                    } else if (strMsg.contains(constant.TEMP_MEETINGSHARE_FILE)) {
                                        /*
                                         * 接收分享文件
                                         * */
                                        Log.e("5555555","接收分享文件");
                                        socketShareFileManager.SendFlag("1");
                                    }
                                    else if (strMsg.contains(constant.TEMP_MEETINGPUSH_FILE)) {
                                        /*
                                         * 接收推送文件
                                         * */
                                        Log.e("5555555","接收推送文件");
                                        socketShareFileManager.SendFlag("2");
                                    }

                                    else if (strMsg.contains(constant.FINISH_SHARE_SCEEN)) {
                                        /*
                                         * 结束同频，通知其他设备自动关闭同屏接收界面
                                         * */
                                        String ip = datagramPacket.getAddress().getHostAddress();
                                        Message mes = new Message();
                                        mes.what = 911;
                                        mes.obj = ip;
                                        mHander.sendMessage(mes);
                                    } else if (strMsg.contains(constant.START_SHARE_SCEEN)) {
                                        String ip = datagramPacket.getAddress().getHostAddress();
                                        /*
                                         * 开始同屏，通知其他设备打开同频界面,如果是自身IP 则不跳转
                                         * */

                                        Message mes = new Message();
                                        mes.what = 120;
                                        mes.obj = ip;
                                        mHander.sendMessage(mes);
                                  /*  Intent intent = new Intent(MainActivity.this, ScreenReceiveActivity2.class);
                                    startActivity(intent);*/
                                    } else if (strMsg.contains(constant.INIATE_ENDORSEMENT)) {
                                        String ip = datagramPacket.getAddress().getHostAddress();
                                        Message mes = new Message();
                                        mes.what = 102;
                                        mes.obj = ip;
                                        mHander.sendMessage(mes);

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //  String str = ByteToString.byteArrayToHexString(datagramPacket.getData());
                                /*    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = dataBean;
                                    mHander.sendMessage(msg);*/
                                //    createFileWithByte(strMsg.getBytes());
                            }

                        } catch (SocketException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

                broadcastUDPFileService.setListener(new BroadcastUDPFileService.errorMsgListener() {
                    @Override
                    public void getErrorMsg(String msg) {
                        Log.d("gsfgdgg", msg.toString());
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                broadcastUDPFileService = null;
            }
        };

        Intent intent = new Intent(this, BroadcastUDPFileService.class);
        isBind = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        Log.d("gsfgdgg3333", isBind + "");


    }

    @Override
    protected int getLayoutId() {

        if (UserUtil.ISCHAIRMAN) {
            return R.layout.wuhu_main;
        }else {
            return R.layout.wuhu_main_attend;
        }

    }

    @Override
    protected void initView() {
        handler = new Handler();
        if ( Hawk.contains(constant.myNumber)){
            String  n=Hawk.get(constant.myNumber);
            name.setText(n);
        }
        meetingTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA13);//会议-月日时分
        Calendar c1 = Calendar.getInstance();
        int day = c1.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        timeTv.setText(meetingTime+" "+week);
         runnable = new Runnable(){
            @Override
            public void run() {
                meetingTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA13);//会议-月日时分
                timeTv.setText(meetingTime+" "+week);
                handler.postDelayed(this, 50);// 50是延时时长
            }
        };

        handler.postDelayed(runnable, 1000 * 60);// 打开定时器，执行操作

        edit_rl.setOnClickListener(this);
        edit_ll.setOnClickListener(this);
        comfirm.setOnClickListener(this);
        vote_ll.setOnClickListener(this);
        consult_ll.setOnClickListener(this);
        finish_ll.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mBtnDelete = (Button) findViewById(R.id.btn_delete);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        edit_name_rl.setVisibility(View.GONE);
        mTestFragments = new SparseArray<>();
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;

      /*  mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;
        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
        fragmentPos++;*/
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTestFragments);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos = position;
                Log.d("sort:", "onPageSelected: " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestFragments.removeAt(mCurPos);
                mPagerAdapter.notifyDataSetChanged();
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("aa","aa");
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);

            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case  R.id.consult_ll:
                 intent = new Intent(WuHuActivity.this, SignListActivity.class);
                startActivity(intent);

                break;
            case R.id.edit_rl:
                if (!isEditRl){
                    edit_name_rl.setVisibility(View.VISIBLE);
                    isEditRl=true;
                }else {
                    edit_name_rl.setVisibility(View.GONE);
                    isEditRl=false;
                }

                break;
            case R.id.edit_ll:
                showRightDialog();
                break;
            case R.id.comfirm:
                edit_name_rl.setVisibility(View.GONE);
                if ( edit_name.getText().toString().isEmpty()){
                    Toast.makeText(WuHuActivity.this,"名字不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                Hawk.put(constant.user_name, edit_name.getText().toString());
                UserUtil.user_name = edit_name.getText().toString();
                name.setText(edit_name.getText().toString());

                break;
            case R.id.vote_ll:
                intent=new Intent(WuHuActivity.this, WuHuVoteActivity.class);
                startActivity(intent);
                break;
            case R.id.finish_ll:
                showFinishMeetingDialog();
                break;

        }

    }
    //保存单个数据
    @Override
    public void saveData(int position) {
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBean.setTopics(company_name.getText().toString());
            wuHuEditBean.setTopic_type(tittle2.getText().toString());
            wuHuEditBean.setLine_color(lineFlag);
            wuHuEditBean.setThem_color(themFlag);
            wuHuEditBean.setPosition(position+"");
            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
            Hawk.put("WuHuFragmentData",wuHuEditBean);
            //更新单个数据
            wsUpdata(wuHuEditBean,constant.REFRASHWuHUSIGLEDATA);
        }
    }

    //结束会议
    private void showFinishMeetingDialog() {
        CVIPaperDialogUtils.showCustomDialog(WuHuActivity.this, "确定要结束会议？", "请保存/上传好会议文件!!!", "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {

                    if (UserUtil.isTempMeeting) {
                        if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                            // 如果是服务端，关掉服务，关停广播
                            String code = getIntent().getStringExtra("code");
                            UDPBroadcastManager.getInstance().sendDestroyCode(code);
                            ServerManager.getInstance().StopMyWebsocketServer();
                            UDPBroadcastManager.getInstance().removeUDPBroastcast();
                        }
                        JWebSocketClientService.closeConnect();

                        if(Hawk.contains(constant._id)){
                            String _id = Hawk.contains(constant._id) ? Hawk.get(constant._id) : "";
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", _id);
                            //绑定生命周期
                            NetWorkManager.getInstance().getNetWorkApiService().finishWUHUMeeting(map).compose(WuHuActivity.this.<BasicResponse>bindToLifecycle())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultObserver<BasicResponse>() {
                                        @Override
                                        protected void onSuccess(BasicResponse response) {
                                            if (response != null) {

                                                UserUtil.user_id = "";
                                                UserUtil.meeting_record_id = "";
                                                if(Hawk.contains(constant._id)){
                                                    Hawk.delete(constant._id);
                                                }
                                                if(Hawk.contains(constant.user_id)){
                                                    Hawk.delete(constant.user_id);
                                                }
                                            }
                                        }
                                    });
                        }
                        try {
                            Thread.sleep(100);
                        }catch (Exception e){

                        }

                        finish();
                    } else {
                        String _id = UserUtil.meeting_record_id;
                        String c_id = Hawk.contains(constant.c_id) ? Hawk.get(constant.c_id) : "";
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", _id);
                        map.put("status", "FINISH");
                        map.put("c_id", c_id);
                        //绑定生命周期
                        NetWorkManager.getInstance().getNetWorkApiService().finishMeeting(map).compose(WuHuActivity.this.<BasicResponse>bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<BasicResponse>() {
                                    @Override
                                    protected void onSuccess(BasicResponse response) {
                                        if (response != null) {
                                            JWebSocketClientService.closeConnect();

                                            UserUtil.user_id = "";
                                            UserUtil.meeting_record_id = "";
                                            UserUtil.user_name = "";

                                            Hawk.delete(constant._id);
                                            Hawk.delete(constant.user_id);
                                            Hawk.delete(constant.user_name);
                                            finish();
                                        }
                                    }
                                });
                    }

                }
            }
        });
    }

    /**
     * 收到websocket 信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg0000: " , message.toString());

        if (UserUtil.isTempMeeting) {
            //  名称已经确定
            if (message.getMessage().contains(constant.SURENAME)) {
                UserUtil.user_name = Hawk.get(constant.myNumber);
               // ttendeesName.setText(Hawk.get(constant.myNumber) + "号   您好");
            }

            if (UserUtil.ISCHAIRMAN) {
                // 服务端开启成功 服务端上的client只可以在此连接
                if (message.getMessage().equals(constant.SERVERSTART)) {
                    //  连接websocket(server，client均要连)
                    JWebSocketClientService.initSocketClient();
                }
            }

        }
        //  client端接收到信息
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
            // 查询投票信息
            if (message.getMessage().contains(constant.WUHUADDFRAGMENT)) {
                Log.e("onReceiveMsg11111: " , message.toString());
                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        WuHuEditBean     wuHuEditBean = wsebean.getBody();
                        Log.d("onReceiveMsg11111大小=", fragmentPos+ "");
                        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
                        fragmentPos++;
                        mPagerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(message.getMessage().contains(constant.REFRASHWuHUSIGLEDATA)){
                Log.e("onReceiveMsg2222: " , message.toString());

            }

        }
    }
    public void showRightDialog( ) {
        //自定义dialog显示布局
        inflate = LayoutInflater.from(WuHuActivity.this).inflate(R.layout.dialog_edit_wuhu, null);
        //自定义dialog显示风格
        dialog = new Dialog(WuHuActivity.this, R.style.DialogRight);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(true);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        View line=inflate.findViewById(R.id.line);
        RadioGroup line_colors=inflate.findViewById(R.id.line_colors);
        RadioGroup theme_colors=inflate.findViewById(R.id.theme_colors);
        myListView=inflate.findViewById(R.id.myList_view);
        company_name=inflate.findViewById(R.id.company_name);
        tittle2=inflate.findViewById(R.id.tittle2);

        add_topic_rl=inflate.findViewById(R.id.add_topic_rl);
        dialg_rl_root=inflate.findViewById(R.id.dialg_rl_root);
        sava_all=inflate.findViewById(R.id.sava_all);
        Log.d("reyeyrty222",UserUtil.ISCHAIRMAN+"");

        myListView.setAdapter(wuHuListAdapter);
        wuHuListAdapter.notifyDataSetChanged();

        line_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                switch (checkedId){

                    case R.id.color_rb1:
                        bundle.putString("refreshType","1");
                        line.setBackgroundColor(Color.parseColor("#EA4318"));
                        lineFlag="1";
                        break;
                    case R.id.color_rb2:
                        bundle.putString("refreshType","2");
                        line.setBackgroundColor(Color.parseColor("#1D1D1D"));
                        lineFlag="2";
                        break;

                }
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);
            }
        });

        theme_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();

                switch (checkId){
                    case R.id.color_rb3:
                        bundle.putString("refreshType","3");
                        themFlag="3";

                        break;
                    case R.id.color_rb4:
                        bundle.putString("refreshType","4");
                        themFlag="4";

                        break;
                    case R.id.color_rb5:
                        bundle.putString("refreshType","5");
                        themFlag="5";

                        break;
                    case R.id.color_rb6:
                        bundle.putString("refreshType","6");
                        themFlag="6";

                        break;
                    case R.id.color_rb7:
                        bundle.putString("refreshType","7");
                        themFlag="7";
                        break;

                }
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);
            }
        });


        add_topic_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WuHuEditBean.EditListBean editListBean=new WuHuEditBean.EditListBean();
                editListBean.setSubTopics(wuHuEditBeanList.get(wuHuEditBeanList.size()-1).getSubTopics());
                editListBean.setAttendeBean(wuHuEditBeanList.get(wuHuEditBeanList.size()-1).getAttendeBean());
                wuHuEditBeanList.add(editListBean);
                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                wuHuListAdapter.notifyDataSetChanged();

                if (Hawk.contains("WuHuFragmentData")){
                    WuHuEditBean wuHuEditBean= Hawk.get("WuHuFragmentData");
                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData",wuHuEditBean);
                    //通知其他设备增加fragment
                    wsUpdata(wuHuEditBean,constant.WUHUADDFRAGMENT);
                }
                //自己增加frgament
             /*   mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
                fragmentPos++;
                mPagerAdapter.notifyDataSetChanged();*/

              /*  Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("aa","filePath");
                intent.putExtras(bundle);
                intent.setAction(constant.REFRESH_BROADCAST);
                  sendBroadcast(intent);*/

            }
        });
        //获取当前Activity所在的窗体
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity= Gravity.RIGHT;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Point size=new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.4);//设置宽
        wlp.height = 1600;//设置宽
        window.setAttributes(wlp);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver!=null){
            unregisterReceiver(myBroadcastReceiver);
        }
        if (serviceConnection!=null){
            unbindService(serviceConnection);

        }
        if (handler!=null&&runnable!=null) {
            handler.removeCallbacks(runnable);
        }
    }
    /**
     * websocket发送数据至其他设备
     */
    private void wsUpdata(Object obj, String packType) {
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(packType);
        bean.setBody(obj);
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
    }
    @Override
    protected void initData() {
      /*  if (!UserUtil.ISCHAIRMAN) {
            edit_ll.setVisibility(View.GONE);
        }*/
        Log.d("reyeyrty333",UserUtil.ISCHAIRMAN+"");
    }
    @Override
    public void onBackPressed() {
        if (!UserUtil.ISCHAIRMAN) {
            return;
        } else {
            showFinishMeetingDialog();
            if (handler!=null&&runnable!=null) {
                handler.removeCallbacks(runnable);
            }
        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            if (in.getAction().equals(constant.ADD_FRAGMENT_BROADCAST)){
                mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos+""));
                fragmentPos++;
                mPagerAdapter.notifyDataSetChanged();

            }else if(in.getAction().equals(constant.REFRESH_BROADCAST)){



            }


        }
    }
}
