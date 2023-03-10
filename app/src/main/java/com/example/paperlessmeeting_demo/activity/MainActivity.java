package com.example.paperlessmeeting_demo.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.Wheel3DView;
import com.cncoderx.wheelview.WheelView;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Note.AddContent;
import com.example.paperlessmeeting_demo.adapter.AttendeesVolumeAdapter;
import com.example.paperlessmeeting_demo.adapter.SameScreenAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.MeetingUserInfoBean;
import com.example.paperlessmeeting_demo.bean.MenuBean;
import com.example.paperlessmeeting_demo.bean.MicBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.meetingRecordId;
import com.example.paperlessmeeting_demo.db.NoteDb;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.fragment.FileFragment;
import com.example.paperlessmeeting_demo.fragment.InformationFragment;
import com.example.paperlessmeeting_demo.fragment.RigthContentFragment;
import com.example.paperlessmeeting_demo.fragment.SetingFragment;
import com.example.paperlessmeeting_demo.fragment.TempVoteListFragment;
import com.example.paperlessmeeting_demo.fragment.VoteListFragment;
import com.example.paperlessmeeting_demo.sharefile.BroadcastUDPFileService;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.SerialConstants;
import com.example.paperlessmeeting_demo.tool.SerialPortUtils.SerialPortListener;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.FloatPermissionManager;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.VideoEncoderUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.SerialPortUtil;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.CVIVolumeSeekBar;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.example.paperlessmeeting_demo.widgets.ThreeStateCheckbox;
import com.example.paperlessmeeting_demo.widgets.VerticalSeekBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heima.tabview.library.TabView;
import com.heima.tabview.library.TabViewChild;
import com.huxq17.floatball.libarary.FloatBallManager;
import com.huxq17.floatball.libarary.floatball.FloatBallCfg;
import com.huxq17.floatball.libarary.menu.FloatMenuCfg;
import com.huxq17.floatball.libarary.menu.MenuItem;
import com.huxq17.floatball.libarary.utils.BackGroudSeletor;
import com.huxq17.floatball.libarary.utils.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.badgeview.Badge;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;


/**
 * Created by ?????? on 2020/9/19.
 */

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, ServiceConnection, View.OnClickListener, View.OnTouchListener {
    @BindView(R.id.browser_image)
    ImageView browserImage;
    @BindView(R.id.browser_tv)
    TextView browserTv;
    @BindView(R.id.rl_browser)
    RelativeLayout rlBrowser;
    @BindView(R.id.meeting_services)
    ImageView meetingServices;
    @BindView(R.id.meeting_services_tv)
    TextView meetingServicesTv;
    @BindView(R.id.rl_meeting_services)
    RelativeLayout rlMeetingServices;
    @BindView(R.id.issues)
    TextView issues;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    TextView ttendeesName;
    Button delayBtn;
    @BindView(R.id.finish_btn)
    Button finishBtn;
    @BindView(R.id.delay_ll)
    LinearLayout delayLl;
    @BindView(R.id.meeting_microphone)
    ImageView meetingMicrophone;
    @BindView(R.id.meeting_microphone_tv)
    TextView meetingMicrophoneTv;
    @BindView(R.id.rl_microphone)
    RelativeLayout rlMicrophone;
    @BindView(R.id.sb_chandelier)
    VerticalSeekBar sbChandelier;
    @BindView(R.id.gesture_rl)
    RelativeLayout gestureRl;
    @BindView(R.id.kwon_tv)
    TextView kwonTv;
    @BindView(R.id.sb_chandelier_rl)
    RelativeLayout sbChandelierRl;
    @BindView(R.id.left_fragment)
    FrameLayout leftFragment;
    @BindView(R.id.rigth_fragment)
    FrameLayout rigthFragment;
    @BindView(R.id.aa)
    TextView aa;
    @BindView(R.id.tabView)
    TabView tabView;
    @BindView(R.id.tablayout)
    VerticalTabLayout tablayout;
    @BindView(R.id.tablayout_ll)
    LinearLayout tablayoutLl;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.seting_fragment)
    FrameLayout setingFragment;
    @BindView(R.id.all_fragment)
    LinearLayout allFragment;
    @BindView(R.id.fit_tablayout)
    VerticalTabLayout fitTablayout;
    @BindView(R.id.fittablayout_ll)
    LinearLayout fittablayoutLl;
    @BindView(R.id.fit_viewPager)
    ViewPager fitViewPager;
    @BindView(R.id.white_board_image)
    ImageView whiteBoardImage;
    @BindView(R.id.white_board_tv)
    TextView whiteBoardTv;
    @BindView(R.id.rl_white_board)
    RelativeLayout rlWhiteBoard;
    @BindView(R.id.meeting_summary)
    ImageView meeting_summary;//??????????????????????????????
    @BindView(R.id.theme_bg_ima)
    ImageView theme_bg_ima;
    @BindView(R.id.tittle_root)
    RelativeLayout tittle_root;

    private boolean hideWhite = false;
    private boolean hideBroad = false;
    private long exitTime = 0;
    private AlertDialog daleyTimeDialog;
    private AlertDialog volumeDialog;
    private AlertDialog sameScreenDialog;//?????????????????????
    //?????????-start+---------------
    private FloatBallManager mFloatballManager;
    private FloatPermissionManager mFloatPermissionManager;
    private ActivityLifeCycleListener mActivityLifeCycleListener = new ActivityLifeCycleListener();
    private int resumed;
    // ???????????????????????????
    private boolean micIsChecked = true;
    //?????????--end

    //??????-start
    private static final int ACTIVITY_RESULT_CODE = 110;
    //??????????????????
    private boolean isSameScreen = false;
    private MediaProjectionManager projectionManager;
    private VideoEncoderUtil videoEncoder;
    //??????--end
    private BroadcastReceiver mBroadcastReceiver;
    private CloseServiceBroadcastReceiver closeServiceBroadcastReceiver;
    //  ??????????????????
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Button mFloatingButton;
    private Cursor cursor;
    private NoteDb mNotedb;
    private SQLiteDatabase dbreader;
    private int statusHeight;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private float mLastX;
    private float mLastY;
    private float mStartX;
    private float mStartY;
    private long mDownTime;
    private long mUpTime;
    //   private LeftSidebaFragment leftSidebaFragment;
    private RigthContentFragment rigthContentFragment;
    private String path = "/storage/emulated/0/aa/??????????????????????????????V1.0.1.docx";
    private WhiteBoardServies myBinder;
     private BrowserServies browserServies;
    private MyReceiver myReceiver;
    private MyReceiver myBrowserReceiver;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragViewAdapter myTabAdapter;
    private FitFragViewAdapter mFitTabAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<MenuBean> menuBeanList = new ArrayList<>();
    private List<Fragment> fitFragmentList = new ArrayList<>();
    private List<MenuBean> fitMenuBeanList = new ArrayList<>();
    private final String TAG = "MainActivity777";
    private boolean isWhiteStart = false;
    private boolean isBroadStart = false;
    private AlertDialog serviceDialog;
    private AlertDialog results;
    private MeetingInfoBean meetingInfoBean;
    private String delayTime = "15";
    private List<AttendeBean> attendeBeanList = new ArrayList<>();
    private SameScreenAdapter sameScreenAdapter;
    private AttendeesVolumeAdapter attendeesVolumeAdapter;
    private String android_id;
    private String mVolume = "";

    private float posX;
    private float posY;

    private float curPosX;
    private float curPosY;
    private int checkNum = 0; // ???????????????????????????

    private List<MicBean> muMicList = new ArrayList<>();
    private ThreeStateCheckbox busYIdleState = null;  //  ????????????????????????????????????
    /*
    * ???????????????????????????service???InetAddress
    * */
    private BroadcastUDPFileService broadcastUDPFileService;
    private BroadcastUDPFileService.BroadcastUDPFileServiceBinder binder;
    private InetAddress address;
    private ServiceConnection serviceConnection;
    private boolean isBind = false;
    private SocketShareFileManager socketShareFileManager;
    private List<String> stringsIp = new ArrayList<>();//??????????????????????????????Ip
    private boolean isSecretaryId = false;//???????????????

    @Override

    public void onClick(View view) {
        if (view == mFloatingButton) {
            if (UserUtil.isFloating) {
                return;
            }
            UserUtil.isFloating = true;
            Intent intent = new Intent(getApplication(), AddContent.class);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                try {
                    intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                    intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                } catch (SQLException e) {
                    e.getLocalizedMessage();
                }
            }
            startActivity(intent);
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_white_board:
                if (UserUtil.isTempMeeting) {
                    intent = new Intent(MainActivity.this, WhiteBoardActivity2.class);
                } else {
                    intent = new Intent(MainActivity.this, WhiteBoardActivity.class);
                }
                startActivity(intent);
             /*   if (Hawk.contains("WhiteBoaord")) {
                    hideWhite = Hawk.get("WhiteBoaord");
                    Log.d("adfafaf", "00000" + hideWhite);
                    if (!hideWhite) {
                        //  closeServie();
                        startFloatingButtonService();
                        Log.d("adfafaf", "111111");
                    } else {
                        Log.d("adfafaf", "222222");
                        intent = new Intent();
                        intent.setAction(constant.SHOW_WHITEBOARD_BROADCAST);
                        sendBroadcast(intent);
                    }
                } else {
                    Log.d("adfafaf", "2222223333");
                    startFloatingButtonService();
                }*/
                break;
            case R.id.rl_browser:
             /*   intent = new Intent();
                intent.setAction(constant.MAX_BROWSER);
                sendBroadcast(intent);*/
                intent = new Intent(MainActivity.this, ActivityBrowser.class);
                startActivity(intent);
               /* if (Hawk.contains("BrowserServies")) {
                    hideBroad = Hawk.get("BrowserServies");
                    Log.d("adfafaf", "00000" + hideBroad);
                    if (!hideBroad) {
                        startFloatingBrowserService();
                    } else {
                        intent = new Intent();
                        intent.setAction(constant.SHOW_BROWSER);
                        sendBroadcast(intent);
                    }
                } else {
                    startFloatingBrowserService();
                }*/
                break;
            case R.id.rl_meeting_services:
                showDialog();
                break;
            case R.id.delay_btn:
                delayBtn.setVisibility(View.GONE);
                videoEncoder.stop();
                isSameScreen = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DatagramSocket socket = new DatagramSocket();
                            String str = constant.FINISH_SHARE_SCEEN;
                            byte[] sendStr = str.getBytes();
                            InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                            DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                            socket.send(packet);
                            socket.close();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                            /*    InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                                DatagramSocket socket = new DatagramSocket();
                                String sendData = "";
                                if (getDeviceId() != null) {
                                    sendData = getDeviceId();
                                }
                                DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, broadcastAddress, 1234);
                                socket.send(sendPacket);*/

                             /*   byte[] sendStr = android_id.getBytes();
                                InetAddress inet = InetAddress.getByName("255.255.255.255");
                                DatagramPacket packet = new DatagramPacket(sendStr, 0, inet, 6789);
                                DatagramSocket datagramSocket = new DatagramSocket();
                                datagramSocket.setSoTimeout(10000);
                                datagramSocket.send(packet);*/
                    }
                }).start();
             /*   intent = new Intent(MainActivity.this, ScreenReceiveActivity2.class);
                startActivity(intent);*/
              /*  Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);*/
                //  Meetingdelay();

            /*    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);*/
                //    Meetingdelay();
                break;
            case R.id.finish_btn:
                showFinishMeetingDialog();

                break;
            case R.id.rl_microphone:
                //  ?????????????????????????????????

                // ????????????????????????
                if (UserUtil.ISCHAIRMAN && !UserUtil.isTempMeeting) {
                    showVolumeDialog();
                } else {
                    if (micIsChecked) {
                        MeetingAPP.getInstance().getHexadecimal().sendCancelSpeech(new SerialPortListener() {
                            @Override
                            public void onMessageResponse(String msg, boolean isComplete) {
                                if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.CANCELSPEECH, msg)) {

                                    meetingMicrophone.setImageResource(R.drawable.volume_idle);
                                    micIsChecked = false;
                                    ToastUtil.makeText(MainActivity.this, "??????????????????!");
                                } else {
                                    meetingMicrophone.setImageResource(R.drawable.volume_checked);
                                    micIsChecked = true;
                                    ToastUtil.makeText(MainActivity.this, "??????????????????");
                                    Log.e(TAG, "??????????????????");
                                }
                            }
                        });

                    } else {
                        MeetingAPP.getInstance().getHexadecimal().sendApplySpeech(new SerialPortListener() {
                            @Override
                            public void onMessageResponse(String msg, boolean isComplete) {
                                if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.APPLYSPEECH, msg)) {

                                    micIsChecked = true;
                                    meetingMicrophone.setImageResource(R.drawable.volume_checked);
                                    ToastUtil.makeText(MainActivity.this, "??????????????????!");
                                } else {
                                    meetingMicrophone.setImageResource(R.drawable.volume_idle);
                                    micIsChecked = false;
                                    ToastUtil.makeText(MainActivity.this, "??????????????????!");
                                    Log.e(TAG, "??????????????????!");
                                }
                            }
                        });
                    }

                }

                break;
            case R.id.meeting_summary:
                /*
                * ??????????????????????????????
                * */
                intent = new Intent(MainActivity.this, ActivityMemberSsignApproval.class);
                startActivity(intent);
                break;
        }
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            String selfIp = "";
            String stIp = "";
            switch (msg.what) {
             /*   case 1:
                    Intent intent = new Intent(MainActivity.this, ScreenReceiveActivity2.class);
                    startActivity(intent);
                    break;*/

                case 2:
                    Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "??????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    Log.e("hahahahahahaa","case 3");
                    int flag= msg.arg1;
                    int flag2=msg.arg2;
                    String filePath1=(String) msg.obj;
                    Intent intent1 = new Intent();
                    Bundle bundle1=new Bundle();
                    bundle1.putString("filePath",filePath1);
                    intent1.putExtras(bundle1);
                    intent1.setAction(constant.SHARE_FILE_BROADCAST);
                    sendBroadcast(intent1);
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(MainActivity.this, "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    Log.e("hahahahahahaa","case 8");
                    String filePath=(String) msg.obj;
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
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
                case 120:
                    selfIp = (String) msg.obj;
                    if (Hawk.contains("SelfIpAddress")) {
                        stIp = Hawk.get("SelfIpAddress");
                        if (selfIp.equals(stIp)) {
                        //    delayBtn.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                    addFloatMenuItem("1");//?????????????????????????????????????????????????????????????????????????????????
                    String  isScrenn="";
                    if (Hawk.contains("isSameScreen")){
                        isScrenn=Hawk.get("isSameScreen");
                    }
                    if ("1".equals(isScrenn)){
//                        Intent inte = new Intent(MainActivity.this, ScreenReceiveActivity2.class);
//                        startActivity(inte);
                    }

                    break;
                case 911:
                    selfIp = (String) msg.obj;
                    if (Hawk.contains("SelfIpAddress")) {
                        stIp = Hawk.get("SelfIpAddress");
                        if (selfIp.equals(stIp)) {
                         //   delayBtn.setVisibility(View.GONE);
                            return;
                        }
                    }
                    addFloatMenuItem("0");//?????????????????????????????????????????????????????????????????????????????????
                    Intent in = new Intent();
                    in.setAction(constant.FINISH_SHARE_SCREEN_BROADCAST);
                    sendBroadcast(in);
                    break;
                case 201:
                    addFloatMenuItem("0");
                    break;
                case 102://????????????????????????????????????
                    selfIp = (String) msg.obj;
                    if (Hawk.contains("SelfIpAddress")) {
                        stIp = Hawk.get("SelfIpAddress");
                        Log.d("sdfgsgsg", stIp + "==?" + selfIp);
                        if (selfIp.equals(stIp)) {

                            return;
                        }
                    }
                    Intent intent2 = new Intent(MainActivity.this, ActivityMemberSsignApproval.class);
                    startActivity(intent2);
                    break;
            }


        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //????????????????????????????????????????????????????????????
        x = event.getRawX();
        y = event.getRawY() - statusHeight;   //statusHeight???????????????????????????
        WindowManager.LayoutParams lParams = (WindowManager.LayoutParams) v.getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                lParams.x = (int) (x - mTouchStartX);
                lParams.y = (int) (y - mTouchStartY);
                mWindowManager.updateViewLayout(mFloatingButton, mParams);
                break;
            case MotionEvent.ACTION_UP:
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                mUpTime = System.currentTimeMillis();
                //??????????????????????????????500??????,????????????????????????????????????20????????????????????????
                if (mUpTime - mDownTime < 500) {
                    if (Math.abs(mStartX - mLastX) < 20.0 && Math.abs(mStartY - mLastY) < 20.0) {
                        return false;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
         delayBtn=(Button) findViewById(R.id.delay_btn);
         ttendeesName=(TextView)findViewById(R.id.ttendees_name);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //?????????????????????fragment
        //   leftSidebaFragment = new LeftSidebaFragment(fragmentManager);
        rigthContentFragment = new RigthContentFragment("gsf", MainActivity.this);
        //??????????????????????????????????????????fragment????????????????????????
        //  fragmentTransaction.add(R.id.left_fragment, leftSidebaFragment);
        fragmentTransaction.add(R.id.rigth_fragment, rigthContentFragment);
        //???????????????????????????
        fragmentTransaction.commit();
        if (Hawk.contains("meetingInfoBean")) {
            meetingInfoBean = Hawk.get("meetingInfoBean");
            issues.setText(meetingInfoBean.getName());
            ImageLoader.getInstance().displayImage(UrlConstant.baseUrl + "/" + meetingInfoBean.getUser_list().get(0).getAvatar(), avatar);
            // ttendeesName.setText(meetingInfoBean.getUser_list().get(0).getUser_name() + "   ??????");
            Log.d("fgsgsg", UrlConstant.baseUrl + meetingInfoBean.getUser_list().get(0).getAvatar());
        }

        if (!StringUtils.isEmpty(UserUtil.user_name)) {
            ttendeesName.setText(UserUtil.user_name + "   ?????????");
        }
        finishBtn.setVisibility(View.INVISIBLE);
        delayBtn.setVisibility(View.GONE);
        gestureRl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        posX = event.getX();
                        posY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        curPosX = event.getX();
                        curPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((curPosX - posX > 0) && (Math.abs(curPosX - posX) > 25)) {
                            Log.v(TAG, "????????????");
                        } else if ((curPosX - posX < 0) && (Math.abs(curPosX - posX) > 25)) {
                            Log.v(TAG, "????????????");
                        }
                        if ((curPosY - posY > 0) && (Math.abs(curPosY - posY) > 25)) {
                            Log.v(TAG, "????????????");
                            sbChandelierRl.setVisibility(View.VISIBLE);
                        } else if ((curPosY - posY < 0) && (Math.abs(curPosY - posY) > 25)) {
                            Log.v(TAG, "????????????");
                            sbChandelierRl.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        // ????????????????????????
        if (message.getType().equals(MessageReceiveType.MessageRefreshMicSta)) {
            int micSta = ThreeStateCheckbox.NORMAL;
            String staStr = Hawk.get(constant.micSta);
            int sta = Integer.parseInt(staStr);
            // Sta=0x01?????????0x02?????????0x03??????
            if (sta == 1) {
                meetingMicrophone.setImageResource(R.drawable.volume_idle);
                micIsChecked = false;
                micSta = ThreeStateCheckbox.NORMAL;
            } else if (sta == 2) {
                meetingMicrophone.setImageResource(R.drawable.volume_checked);
                micIsChecked = true;
                micSta = ThreeStateCheckbox.SPEACH;
            } else if (sta == 3) {
                meetingMicrophone.setImageResource(R.drawable.volume_unchecked);
                micIsChecked = false;
                micSta = ThreeStateCheckbox.MUTE;
            }
            if (busYIdleState != null) {
                busYIdleState.setState(micSta);
            }
            return;
        }



        Log.e(TAG, "onReceiveMsg: " + message.toString());
        if (UserUtil.isTempMeeting) {
            //  ??????????????????
            if (message.getMessage().contains(constant.SURENAME)) {
                UserUtil.user_name = Hawk.get(constant.myNumber);
                ttendeesName.setText(Hawk.get(constant.myNumber) + "???   ??????");
            }

            if (UserUtil.ISCHAIRMAN) {
                // ????????????????????? ???????????????client?????????????????????
                if (message.getMessage().equals(constant.SERVERSTART)) {
                    //  ??????websocket(server???client?????????)
                    JWebSocketClientService.initSocketClient();
                }
            }

            attendeBeanList.clear();
            if(message.getType().equals(MessageReceiveType.MessageServer)){
                return;
            }
            if (message.getMessage().contains(constant.QUERYATTEND)) {
                try {
                    TempWSBean<ArrayList<AttendeBean>> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<AttendeBean>>>() {
                    }.getType());
                    attendeBeanList = wsebean.getBody();

                    if(attendeBeanList != null){
                        Hawk.put("TempMeetingAttende",attendeBeanList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //  ?????????????????????
            if (message.getMessage().contains(constant.GETMICSTATUS)) {
                String micMac = Hawk.get(constant.micMac);
                try {
                    WSBean<ArrayList<MicBean>> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<WSBean<ArrayList<MicBean>>>() {
                    }.getType());
                    muMicList = wsebean.getBody();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * ????????????????????????
     **/
    private void sendMicSta() {
        if (Hawk.contains(constant.micSta) && Hawk.contains(constant.micMac)) {
            String micMac = Hawk.get(constant.micMac);
            String micSta = Hawk.get(constant.micSta);

            // Sta=0x01?????????0x02?????????0x03??????
            MicBean micBean = new MicBean();
            micBean.setMeetingRecordId(UserUtil.meeting_record_id);
            micBean.setMac(micMac);
            micBean.setStatus(micSta);

            WSBean bean = new WSBean();
            bean.setReqType(0);
            bean.setUser_id(UserUtil.user_id);
            bean.setPackType(constant.SETMICSTATUS);
            bean.setDevice("app");
            bean.setBody(micBean);

            Type type = new TypeToken<WSBean<MicBean>>() {
            }.getType();

            String strJson = new Gson().toJson(bean, type);
            JWebSocketClientService.sendMsg(strJson);

            if (UserUtil.ISCHAIRMAN) {
                Type type2 = new TypeToken<WSBean<meetingRecordId>>() {
                }.getType();
                // ??????????????????
                meetingRecordId meetingRecordId = new meetingRecordId();
                meetingRecordId.setMeetingRecordId(UserUtil.meeting_record_id);

                WSBean bean2 = new WSBean();
                bean2.setReqType(0);
                bean2.setUser_id(UserUtil.user_id);
                bean2.setPackType(constant.GETMICSTATUS);
                bean2.setDevice("app");
                bean2.setBody(meetingRecordId);

                String strJson2 = new Gson().toJson(bean2, type2);
                Log.e("body===", strJson2);
                JWebSocketClientService.sendMsg(strJson2);

            }
        }
    }

    @Override
    protected void initData() {
        startFloatingBrowserService();
        /*
        * ????????????????????????????????????????????????
        * */
        tittle_root.getBackground().setAlpha(51);
        /**
         * ????????????????????????
         * */
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        if (Hawk.contains("background_img")) {
            String url = Hawk.get("background_img");

            Drawable drawable = MeetingAPP.getContext().getResources().getDrawable(R.mipmap.bg_main);
            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(drawable).showImageForEmptyUri(drawable).showImageOnFail(drawable).resetViewBeforeLoading(false).delayBeforeLoading(1000).
                    cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).handler(new Handler()).build();
            ImageLoader.getInstance().displayImage(url, theme_bg_ima, options);
        }
        if (Hawk.contains("meeting_issues")) {
            String string = Hawk.get("meeting_issues");
            issues.setText(string);
        }
        if (UserUtil.isTempMeeting) {
            issues.setText("????????????");
            meeting_summary.setVisibility(View.GONE);//??????????????????????????????????????????
        }

//        FLUtil.findSerialPortInfo();
        UDPBroadcastManager.getInstance().stopReceiveUDP();


        // ?????????????????????
        if (UserUtil.isTempMeeting) {
            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageClient)) {
                //  ????????????????????????
                UserUtil.ISCHAIRMAN = false;
                String ipUrl = getIntent().getStringExtra("ip");
                UserUtil.serverIP = ipUrl;
                if (!StringUtils.isEmpty(ipUrl)) {
                    UrlConstant.TempWSIPString = "ws://" + ipUrl + ":" + UrlConstant.port;
                    Log.e("????????????", "???????????????server IP ======" + UrlConstant.TempWSIPString);
                }

                //  ??????websocket(server???client?????????)
                JWebSocketClientService.initSocketClient();

            } else if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                UserUtil.ISCHAIRMAN = true;
                String code = getIntent().getStringExtra("code");
                alertCodeDialog(code);

                //  ???????????????code??????IP??????
                UDPBroadcastManager.getInstance().sendUDPWithCode(code);
                // ??????socket?????????
                ServerManager.getInstance().startMyWebsocketServer(UrlConstant.port);
                UrlConstant.TempWSIPString = "ws://" + FLUtil.getIPAddress() + ":" + UrlConstant.port;
                // ?????????client?????????server?????????????????????
            }
        }else {
            JWebSocketClientService.initSocketClient();
        }

        EventBus.getDefault().register(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMicSta();
            }
        }, 500);


        //  ??????????????????
        mNotedb = new NoteDb(this);
        dbreader = mNotedb.getReadableDatabase();
        statusHeight = getStatusHeight(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        //????????????????????????
        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        //?????????????????????
        mParams.x = 100;
        mParams.y = 100;
        //?????????Window???????????????????????????????????????????????????
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        Log.d("SecondActivity", "sdk:" + Build.VERSION.SDK_INT);
        //??????window ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//API Level 26
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }

        delayBtn.setOnClickListener(this);
        rlWhiteBoard.setOnClickListener(this);
        rlBrowser.setOnClickListener(this);
        rlMeetingServices.setOnClickListener(this);
        rlMicrophone.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        meeting_summary.setOnClickListener(this);
        // fittablayoutLl.getBackground().setAlpha(178);
        //  fitTablayout.getBackground().setAlpha(178);
        initViewPagerFragment();
        initFitFragment();
        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  FileDisplayActivity.show(MainActivity.this, path);
            }
        });

        /**
         *  ??????????????????
         * */
        sbChandelier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
//                Log.e("onStopTrackingTouch", Integer.toHexString(progress));
                String volume = Integer.toHexString(progress);
                if (mVolume.equals(volume)) {
                    return;
                }
//                vol =0x00~0x64
                MeetingAPP.getInstance().getHexadecimal().sendVolume(volume, new SerialPortListener() {
                    @Override
                    public void onMessageResponse(String msg, boolean isComplete) {
                        if (isComplete && SerialConstants.isSetOk(SerialConstants.SetOkEnum.volume, msg)) {
                            if (isComplete && SerialConstants.isSetOk(SerialConstants.SetOkEnum.volume, msg)) {
                                mVolume = volume;
                            } else {
                                ToastUtil.makeText(MainActivity.this, "??????????????????!");
                            }
                        }
                    }
                });
            }
        });

        String user_id = "";
        String m_id = "";
        if (Hawk.contains(constant.user_id)) {
            user_id = Hawk.get("user_id");
        }
        if (Hawk.contains(constant._id)) {
            m_id = Hawk.get("_id");
        }
        /**
         *  ????????????????????????,???????????????
         */
        if (!UserUtil.isTempMeeting) {
            NetWorkManager.getInstance().getNetWorkApiService().findMeetingUserInfo(user_id, m_id).compose(this.<BasicResponse<MeetingUserInfoBean>>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<MeetingUserInfoBean>>() {
                        @Override
                        protected void onSuccess(BasicResponse<MeetingUserInfoBean> response) {
                            MeetingUserInfoBean meetingInfobean = response.getData();
                            if (response != null) {
                                Hawk.put(constant.meeting_role, meetingInfobean.getRole());
                                UserUtil.ISCHAIRMAN = meetingInfobean.getRole().equals("0");
                                refreshTabUI();
//                                if (null == mFloatingButton && meetingInfobean.getRole().equals("1")) {
//                                    mFloatingButton = new Button(MainActivity.this);
//                                    mFloatingButton.setBackground(getApplication().getDrawable(R.mipmap.note));
//                                    mFloatingButton.setHeight(60);
//                                    mFloatingButton.setWidth(60);
//                                    mFloatingButton.setOnTouchListener(MainActivity.this);
//                                    mFloatingButton.setOnClickListener(MainActivity.this);
//                                    mWindowManager.addView(mFloatingButton, mParams);
//                                }
                            }
                        }
                    });
        }

        List<TabViewChild> tabViewChildList = new ArrayList<>();
        TabViewChild tabViewChild01 = new TabViewChild(R.mipmap.ic_file_sele, R.mipmap.ic_file_unsele, "??????", new FileFragment("??????", MainActivity.this));
        TabViewChild tabViewChild02 = new TabViewChild(R.mipmap.ic_vote_sele, R.mipmap.ic_vote_unsele, "??????", new FileFragment("??????", MainActivity.this));
        //   TabViewChild tabViewChild03 = new TabViewChild(R.mipmap.ic_service_sele, R.mipmap.ic_service_unsele, "??????", new FileFragment("??????", MainActivity.this));
        // TabViewChild tabViewChild04 = new TabViewChild(R.mipmap.ic_white_broad_sele, R.mipmap.ic_white_broad_unsele, "??????", FragmentCommon.newInstance("?????????"));
        TabViewChild tabViewChild05 = new TabViewChild(R.mipmap.ic_browser_sele, R.mipmap.ic_browser_unsele, "?????????", new FileFragment("??????", MainActivity.this));
        tabViewChildList.add(tabViewChild01);
        tabViewChildList.add(tabViewChild02);
        //  tabViewChildList.add(tabViewChild03);
        //  tabViewChildList.add(tabViewChild04);
        tabViewChildList.add(tabViewChild05);
        //end add data
        tabView.setTabViewChild(tabViewChildList, getSupportFragmentManager());
        tabView.setOnTabChildClickListener(new TabView.OnTabChildClickListener() {
            @Override
            public void onTabChildClick(int position, ImageView currentImageIcon, TextView currentTextView) {

            }
        });

    }

    /**
     * ????????????code?????????
     */
    private void alertCodeDialog(String code) {
        Dialog verify_invite_codeDialog = new Dialog(this, R.style.update_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.diaog_invite_code_tips, null);//?????????????????????
        TextView create_invite_codeview = view.findViewById(R.id.code);
        TextView ok_tv = view.findViewById(R.id.ok_tv);
        create_invite_codeview.setText(code);
        verify_invite_codeDialog.setContentView(view);//?????????????????????????????????
        verify_invite_codeDialog.setCancelable(false);// ?????????????????????????????????
        verify_invite_codeDialog.setCanceledOnTouchOutside(true);
        verify_invite_codeDialog.show();
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_invite_codeDialog.dismiss();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_invite_codeDialog.dismiss();
            }
        });
    }


    //????????????
    private void Meetingdelay() {

        showDaleyDialog();

    }

    private void setDaleyTime() {

        String _id = UserUtil.meeting_record_id;
     /*   if (Hawk.contains("_id")) {
            _id = Hawk.get("_id");
        }*/
        Map<String, Object> map = new HashMap<>();
        map.put("meeting_record_id", _id);
        map.put("delay_date", delayTime);

        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().delayMeeting(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        if (response != null) {
                            Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //????????????
    private void Finishdelay() {
        String _id = UserUtil.meeting_record_id;
      /*  if (Hawk.contains("_id")) {
            _id = Hawk.get("_id");
        }*/
        Map<String, Object> map = new HashMap<>();
        map.put("id", _id);
        map.put("status", "FINISH");

        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().finishMeeting(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onSuccess(BasicResponse response) {
                        if (response != null) {
                            Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * ??????menu UI
     * ??????????????????mic????????????
     */

    private void refreshTabUI() {
        fragmentList.clear();
        menuBeanList.clear();
        fragmentList.add(new RigthContentFragment("??????", MainActivity.this));
        // ????????????????????????
        if (UserUtil.ISCHAIRMAN) {
            Log.e("test","?????????");
            finishBtn.setVisibility(View.VISIBLE);
//            fragmentList.add(1, new NewFragmentCentralControl("??????", MainActivity.this));
        }
        Log.e("test","1111111111111");
        fragmentList.add(new InformationFragment("??????", MainActivity.this));
        if (UserUtil.isTempMeeting) {
            fragmentList.add(new TempVoteListFragment("??????", MainActivity.this));
        } else {
            fragmentList.add(new VoteListFragment("??????", MainActivity.this));
        }
        fragmentList.add(new SetingFragment("??????", MainActivity.this));


        menuBeanList.add(new MenuBean(0, 0, "??????"));
        if (UserUtil.ISCHAIRMAN) {
//            menuBeanList.add(1, new MenuBean(0, 0, "??????"));
        }
        menuBeanList.add(new MenuBean(0, 0, "??????"));
        menuBeanList.add(new MenuBean(0, 0, "??????"));
        menuBeanList.add(new MenuBean(0, 0, "??????"));


//        if (UserUtil.ISCHAIRMAN) {
//            fragmentList.add(1, new NewFragmentCentralControl("??????", MainActivity.this));
//        }
//        if (UserUtil.ISCHAIRMAN) {
//            menuBeanList.add(1, new MenuBean(0, 0, "??????"));
//        }
        myTabAdapter.notifyDataSetChanged();
        if (Hawk.contains(constant.micMac)) {
            //  ?????????????????????????????????????????????
            if (UserUtil.ISCHAIRMAN && !UserUtil.isTempMeeting) {

                if (Hawk.contains(constant.micMac)) {
                    if (UserUtil.ISCHAIRMAN) {
                        String micMac = Hawk.get(constant.micMac);
                        MeetingAPP.getInstance().getHexadecimal().setChairMan(micMac, new SerialPortListener() {
                            @Override
                            public void onMessageResponse(String msg, boolean isComplete) {
                                if (isComplete && SerialPortUtil.isChairManSetOK(micMac, msg)) {
                                    Log.e("setChairMan", "?????????????????????");

                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private void initViewPagerFragment() {
        fragmentList.clear();
        menuBeanList.clear();
        fragmentList.add(new RigthContentFragment("??????", MainActivity.this));
        // ????????????????????????
        if (UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN) {
            finishBtn.setVisibility(View.VISIBLE);
//            fragmentList.add(1, new NewFragmentCentralControl("??????", MainActivity.this));
        }
        fragmentList.add(new InformationFragment("??????", MainActivity.this));
        if (UserUtil.isTempMeeting) {
            fragmentList.add(new TempVoteListFragment("??????", MainActivity.this));
        } else {
            fragmentList.add(new VoteListFragment("??????", MainActivity.this));
        }
        fragmentList.add(new SetingFragment("??????", MainActivity.this));


        menuBeanList.add(new MenuBean(0, 0, "??????"));
//        if (UserUtil.isTempMeeting && UserUtil.ISCHAIRMAN) {
//            menuBeanList.add(new MenuBean(0, 0, "??????"));
//        }
        menuBeanList.add(new MenuBean(0, 0, "??????"));
        menuBeanList.add(new MenuBean(0, 0, "??????"));
        menuBeanList.add(new MenuBean(0, 0, "??????"));
        myTabAdapter = new FragViewAdapter(fragmentManager, fragmentList, menuBeanList);

        viewPager.setAdapter(myTabAdapter);
        tablayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(q.rorbin.verticaltablayout.widget.TabView tab, int position) {
             /*   fitViewPager.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);*/
                //  tab.setBackground(R.drawable.circle);
            }

            @Override
            public void onTabReselected(q.rorbin.verticaltablayout.widget.TabView tab, int position) {
                Intent intent = new Intent();
                //??????????????????fragment
                intent.setAction(constant.REMOVE_BROADCAST);
                //??????????????????
                sendBroadcast(intent);
                Log.d("fgsgg", "myTabAdapter??????");
             /*   myTabAdapter.notifyDataSetChanged();
                fitViewPager.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);*/
            }
        });
        //  tablayout.setupWithFragment(getChildFragmentManager(), R.id.rigth_fragment, fragmentList, myTabAdapter);
    }


    private void initFitFragment() {

      /*  fitFragmentList.add(new FileFragment("??????", MainActivity.this));
        fitFragmentList.add(new VoteListFragment("??????", MainActivity.this));
        fitMenuBeanList.add(new MenuBean(R.mipmap.ic_file_sele, R.mipmap.ic_file_unsele, "??????"));
        fitMenuBeanList.add(new MenuBean(R.mipmap.ic_vote_sele, R.mipmap.ic_vote_unsele, "??????"));
        mFitTabAdapter = new FitFragViewAdapter(fragmentManager, fitFragmentList, fitMenuBeanList);
        fitViewPager.setAdapter(mFitTabAdapter);
        fitTablayout.setupWithViewPager(fitViewPager);
        fitViewPager.setOffscreenPageLimit(fitFragmentList.size());
        mFitTabAdapter.notifyDataSetChanged();*/
        fitTablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(q.rorbin.verticaltablayout.widget.TabView tab, int position) {
                fitViewPager.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                // tab.setBackground(R.drawable.circle);
            }

            @Override
            public void onTabReselected(q.rorbin.verticaltablayout.widget.TabView tab, int position) {
                Log.d("fgsgg", "myTabAdapter??????");
                myTabAdapter.notifyDataSetChanged();
                fitViewPager.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
            }
        });
    }


    /**
     * ???assets??????????????????????????????
     *
     * @param assetFileName assets??????????????????
     * @param newFileName   ?????????/data/data/package_name/files/??????????????????
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        int buffsize = 1024;

        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[buffsize];
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void selectDb() {
        cursor = dbreader.query
                (NoteDb.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Hawk.put("isSameScreen","1");//??????????????????????????????:1-??????  0-?????????
        selectDb();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        if (Hawk.contains("WhiteBoaord")) {
            hideWhite = Hawk.delete("WhiteBoaord");
        }

        checkWritePermission1();
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
//        copy();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constant.CLOSE_WHITEBOARD_BROADCAST);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, intentFilter);


        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(constant.FRESH_TAB);
        registerReceiver(mBroadcastReceiver, intentFilter1);

        closeServiceBroadcastReceiver=new CloseServiceBroadcastReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(constant.SHOW_CLOSE_SERVICE_BROADCAST);
        registerReceiver(closeServiceBroadcastReceiver,filter);

        boolean showMenu = true;//??????false??????
        getUserList();
        getDeviceId();
        /*
        * 0-?????????????????????1???????????????
        * */
        init(showMenu, "0");
        //5 ????????????????????????????????????????????????????????????
        if (mFloatballManager.getMenuItemSize() == 0) {
            mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {

                }
            });
        }
        //6 ???????????????????????????????????????????????????????????????
        getApplication().registerActivityLifecycleCallbacks(mActivityLifeCycleListener);
      /*  IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constant.CLOSE_BROWSER_BROADCAST);
        myBrowserReceiver = new MyReceiver();
        registerReceiver(myBrowserReceiver, intentFilter2);*/

/*        //????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] message = new byte[1024];
                try {
                    // ??????Socket??????
                    try {
                        InetAddress address = InetAddress.getByName("255.255.255.255");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    DatagramSocket datagramSocket = new DatagramSocket(6789);
                    datagramSocket.setBroadcast(true);
                    DatagramPacket datagramPacket = new DatagramPacket(message,
                            message.length);

                    try {
                        while (true) {
                            int p = datagramPacket.getPort();
                            // ??????????????????
                            datagramSocket.receive(datagramPacket);
                            String strMsg = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), "UTF-8");
                            Log.d("ddfff", strMsg);
                            Message msg = new Message();
                            if (Hawk.contains("android_id")) {
                                String id = Hawk.get("android_id");
                                if (!strMsg.equals(id)) {
                                    msg.what = 1;
                                    mHander.sendEmptyMessage(1);
                                }
                            }
                            // msg.what = 1;
                          *//*  if (strMsg.length() > 9) {
                                if (".Cvi".equals(strMsg.substring(0, 4)) && ("End.".equals(strMsg.substring(strMsg.length() - 4, strMsg.length())))) {
                                    msg.obj = datagramPacket.getAddress().toString();
                                    mHander.sendMessage(msg);
                                }
                            }*//*
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


            }
        }).start();*/
        /*
        *
        * ??????????????????????????????Servive?????????????????????????????????????????????????????????????????????????????????????????????
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
                                // ??????????????????
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
                                    * ??????????????????
                                    * */
                                        Log.e("5555555","??????????????????");
                                        socketShareFileManager.SendFlag("1");
                                    }
                                    else if (strMsg.contains(constant.TEMP_MEETINGPUSH_FILE)) {
                                        /*
                                         * ??????????????????
                                         * */
                                        Log.e("5555555","??????????????????");
                                      //  socketShareFileManager.SendFlag("2");
                                    }

                                    else if (strMsg.contains(constant.FINISH_SHARE_SCEEN)) {
                                     /*
                                    * ???????????????????????????????????????????????????????????????
                                    * */
                                        String ip = datagramPacket.getAddress().getHostAddress();
                                        Message mes = new Message();
                                        mes.what = 911;
                                        mes.obj = ip;
                                        mHander.sendMessage(mes);
                                    } else if (strMsg.contains(constant.START_SHARE_SCEEN)) {
                                        String ip = datagramPacket.getAddress().getHostAddress();
                                        /*
                                        * ???????????????????????????????????????????????????,???????????????IP ????????????
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

    protected int dp2px(float dp) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * ????????????????????????
     */
    @AfterPermissionGranted(constant.WRITE_PERMISSION_CODE1)
    private void checkWritePermission1() {
        if (!PermissionManager.checkPermission(this, constant.PERMS_WRITE1)) {
            PermissionManager.requestPermission(this, constant.WRITE_PERMISSION_TIP, constant.WRITE_PERMISSION_CODE1, constant.PERMS_WRITE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //?????????????????????EasyPermission?????????
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_LONG).show();


    }

    /**
     * ??????????????????
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
        /**
         * ??????????????????????????????????????????'NEVER ASK AGAIN.'??????'????????????'?????????????????????
         * ?????????????????????????????????????????????????????????????????????
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        myBinder = ((WhiteBoardServies.MyBinder) iBinder).getService();
        Log.d(TAG, "??????11111=" + componentName.getShortClassName() + "" + componentName.getClass() + "  ");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG, "??????222");
    }

    @Override
    public void onBindingDied(ComponentName name) {
        Log.d(TAG, "??????33333");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (requestCode == 0) {
            startFloatingButtonService();
        } else*/
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            videoEncoder = new VideoEncoderUtil(mediaProjection, "");
            videoEncoder.start();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (null != mFloatingButton) {
            mWindowManager.removeView(mFloatingButton);
        }

        if (Hawk.contains("WhiteBoaord")) {
            hideWhite = Hawk.delete("WhiteBoaord");
        }

        if (Hawk.contains(constant.team_share)) {
            Hawk.delete(constant.team_share);
        }
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
//        if (isBind) {
//            unbindService(serviceConnection);
//
//        }
        if (closeServiceBroadcastReceiver!=null){
            unregisterReceiver(closeServiceBroadcastReceiver);
        }
        getApplication().unregisterActivityLifecycleCallbacks(mActivityLifeCycleListener);
    }

    @Override
    public void onBackPressed() {
        if (!UserUtil.ISCHAIRMAN) {
            return;
        } else {
            showFinishMeetingDialog();
        }
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "????????????????????????",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    private void startFloatingButtonService() {
        Log.d("gfjjfghjfg", "?????????????????????~");
        if (!Settings.canDrawOverlays(this)) {
            Log.d("gfjjfghjfg", "11111?????????????????????~");
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            Log.d("gfjjfghjfg", "2222?????????????????????~");
            bindService(new Intent(MainActivity.this, WhiteBoardServies.class), MainActivity.this, BIND_AUTO_CREATE);
            isWhiteStart = true;
          //startService(new Intent(MainActivity1.this, WhiteBoardServies.class));
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.InfoDialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_service, null);
        ImageView close_image = view.findViewById(R.id.close_image);
        LinearLayout root_ll = view.findViewById(R.id.root_ll);
        RelativeLayout water_rl = view.findViewById(R.id.water_rl);
        RelativeLayout pen_rl = view.findViewById(R.id.pen_rl);
        RelativeLayout other_rl = view.findViewById(R.id.other_rl);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceDialog.dismiss();
            }
        });
        water_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResualtDialog();
            }
        });
        pen_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResualtDialog();
            }
        });
        other_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResualtDialog();
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        serviceDialog = builder.create();
        serviceDialog.show();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = serviceDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //????????????
        serviceDialog.getWindow().setAttributes(lp);
    }
    //??????????????????
    private void showExtraordinarySameScreenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.InfoDialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_extraordinary_meeting_screnn, null);
        builder.setView(view);
        builder.setCancelable(true);
        sameScreenDialog = builder.create();
        sameScreenDialog.show();
        TextView confir_btn = (TextView) view.findViewById(R.id.confir_btn);
        TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        TextView number_tv = (TextView) view.findViewById(R.id.tip);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = sameScreenDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //????????????
        sameScreenDialog.getWindow().setAttributes(lp);

        confir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread sendThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatagramSocket socket = new DatagramSocket();
                            String str = constant.START_SHARE_SCEEN;
                            ;
                            byte[] sendStr = str.getBytes();
                            InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                            DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                            socket.send(packet);
                            socket.close();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sendThread.start();
                try {
                    Thread.sleep(2000);//??????2????????????????????????????????????????????????????????????
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);
                isSameScreen = true;
                sameScreenDialog.dismiss();
                delayBtn.setVisibility(View.VISIBLE);
            }

        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameScreenDialog.dismiss();
            }
        });

    }
    //??????????????????
    private void showSameScreenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.InfoDialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_same_screen, null);
        builder.setView(view);
        builder.setCancelable(true);
        sameScreenDialog = builder.create();
        sameScreenDialog.show();
        TextView confir_btn = (TextView) view.findViewById(R.id.confir_btn);
        TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        TextView number_tv = (TextView) view.findViewById(R.id.number_tv);
        TextView choose_num = (TextView) view.findViewById(R.id.choose_num);
        MyListView myListView = (MyListView) view.findViewById(R.id.same_screeen_listView);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.same_screnn_state);
        number_tv.setText("???????????????" + attendeBeanList.size() + ")");
        choose_num.setText("??????" + checkNum + "???");
        confir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNum == 0) {
                    Toast.makeText(MainActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
           /*     Intent intent = new Intent(MainActivity.this, ScreenReceiveActivity.class);
                startActivity(intent);*/
              /*
        * ????????????????????????????????????
        * */
                Thread sendThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DatagramSocket socket = new DatagramSocket();
                            String str = constant.START_SHARE_SCEEN;
                            byte[] sendStr = str.getBytes();
                            InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                            DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                            socket.send(packet);
                            socket.close();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sendThread.start();
                try {
                    Thread.sleep(2000);//??????2????????????????????????????????????????????????????????????
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);
                isSameScreen = true;
                sameScreenDialog.dismiss();
                delayBtn.setVisibility(View.VISIBLE);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameScreenDialog.dismiss();
            }
        });
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = sameScreenDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //????????????
        sameScreenDialog.getWindow().setAttributes(lp);

        sameScreenAdapter = new SameScreenAdapter(attendeBeanList, MainActivity.this);
        myListView.setAdapter(sameScreenAdapter);
        sameScreenAdapter.notifyDataSetChanged();
        checkNum = attendeBeanList.size();
        // ??????listView????????????
//        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                SameScreenAdapter.ViewHolder holder = (SameScreenAdapter.ViewHolder) arg1.getTag();
//                // ??????CheckBox?????????
//                holder.sameScrennState.toggle();
//                // ???CheckBox???????????????????????????
//                SameScreenAdapter.getIsSelected().put(arg2, holder.sameScrennState.isChecked());
//                // ??????????????????
//                if (holder.sameScrennState.isChecked() == true) {
//                    checkNum++;
//                } else {
//                    checkNum--;
//                }
//                choose_num.setText("??????" + checkNum + "???");
//                if (checkNum == attendeBeanList.size()) {
//                    checkBox.setChecked(true);
//                } else {
//                    checkBox.setChecked(false);
//                }
//            }
//        });
        checkBox.setChecked(true);
        for (int i = 0; i < attendeBeanList.size(); i++) {
            SameScreenAdapter.getIsSelected().put(i, true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // ??????list???????????????MyAdapter??????map???????????????true
                    for (int i = 0; i < attendeBeanList.size(); i++) {
                        SameScreenAdapter.getIsSelected().put(i, true);
                    }
                    // ????????????list?????????
                    checkNum = attendeBeanList.size();
                    // ??????listview???TextView?????????

                } else {
                    if (checkNum == attendeBeanList.size()) {
                        // ??????list??????????????????????????????????????????
                        checkNum = 0;
                        for (int i = 0; i < attendeBeanList.size(); i++) {
                            if (SameScreenAdapter.getIsSelected().get(i)) {
                                SameScreenAdapter.getIsSelected().put(i, false);

                            }
                        }
                    }
                }
                choose_num.setText("??????" + checkNum + "???");
                sameScreenAdapter.notifyDataSetChanged();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameScreenDialog.dismiss();
            }
        });

//????????????
     /*   confir_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             *//**//*   if (checkNum == 0) {
                Toast.makeText(MainActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }*//**//*
                sameScreenDialog.dismiss();
                Intent captureIntent = projectionManager.createScreenCaptureIntent();

                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);

                isSameScreen = true;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                            DatagramSocket socket = new DatagramSocket();
                            String sendData = "";
                            if (getDeviceId() != null) {
                                sendData = getDeviceId();
                            }
                            Log.d("fdsgsdgg", sendData);
                            DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, broadcastAddress, 6789);
                            socket.send(sendPacket);

                             *//**//*   byte[] sendStr = android_id.getBytes();
                        InetAddress inet = InetAddress.getByName("255.255.255.255");
                        DatagramPacket packet = new DatagramPacket(sendStr, 0, inet, 6789);
                        DatagramSocket datagramSocket = new DatagramSocket();
                        datagramSocket.setSoTimeout(10000);
                        datagramSocket.send(packet);*//**//*
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).

                        start();
            }
        });*/
    }


    private void showResualtDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.InfoDialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_results, null);
        builder.setView(view);
        builder.setCancelable(true);
        results = builder.create();
        results.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                results.dismiss();
            }
        }, 1500);
    }

    private void startFloatingBrowserService() {
        Log.d("gfjjfghjfg", "?????????????????????~");
        if (!Settings.canDrawOverlays(this)) {
            Log.d("gfjjfghjfg", "11111?????????????????????~");
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
        } else {
            Log.d("gfjjfghjfg", "2222?????????????????????~");
//            bindService(new Intent(MainActivity.this, BrowserServies.class), new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                   browserServies = ((BrowserServies.MyBroadBinder) iBinder).getService();
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName componentName) {
//
//                }
//            }, BIND_AUTO_CREATE);
//            isBroadStart = true;
//           startService(new Intent(MainActivity.this, BrowserServies.class));
        }
    }

    //??????????????????
    private void closeWhiteServie() {
        if (isWhiteStart) {
            unbindService(this);
            isWhiteStart = false;
        }
    }

    //?????????????????????
    private void closeBroadServie() {
        if (isBroadStart) {
            //  unbindService(this);
            isBroadStart = false;
        }
    }

    //????????????
    public interface WhiteBoardOnClickInterface {
        void onShowWhiteBoard();
    }


    //?????????????????????
    private void showDaleyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_daley, null);
        builder.setView(view);
        builder.setCancelable(true);
        Button confirm = view.findViewById(R.id.confir_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        Wheel3DView wheel3DView = view.findViewById(R.id.wheel3d);
        wheel3DView.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                CharSequence text = view.getItem(newIndex);
                Log.d("fdgg", text.toString());

                //  delayTime = text.toString();
            }
        });

        daleyTimeDialog = builder.create();
        daleyTimeDialog.show();
      /*  WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = daleyTimeDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //????????????
        daleyTimeDialog.getWindow().setAttributes(lp);
      */
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDaleyTime();
                daleyTimeDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daleyTimeDialog.dismiss();
            }
        });


    }

    //????????????
    private void showFinishMeetingDialog() {
        CVIPaperDialogUtils.showCustomDialog(MainActivity.this, "????????????????????????", "?????????/?????????????????????!!!", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {
                    // ??????????????????????????????????????????????????????
                    if(isSameScreen){
                        videoEncoder.stop();
                        isSameScreen = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DatagramSocket socket = new DatagramSocket();
                                    String str = constant.FINISH_SHARE_SCEEN;
                                    byte[] sendStr = str.getBytes();
                                    InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                                    DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                                    socket.send(packet);
                                    socket.close();
                                } catch (SocketException e) {
                                    e.printStackTrace();
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    if (UserUtil.isTempMeeting) {
                        if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                            // ????????????????????????????????????????????????
                            String code = getIntent().getStringExtra("code");
                            UDPBroadcastManager.getInstance().sendDestroyCode(code);
                            ServerManager.getInstance().StopMyWebsocketServer();
                            UDPBroadcastManager.getInstance().removeUDPBroastcast();
                        }
                        JWebSocketClientService.closeConnect();

                        finish();
                    } else {
                        String _id = UserUtil.meeting_record_id;
                        String c_id = Hawk.contains(constant.c_id) ? Hawk.get(constant.c_id) : "";
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", _id);
                        map.put("status", "FINISH");
                        map.put("c_id", c_id);
                        //??????????????????
                        NetWorkManager.getInstance().getNetWorkApiService().finishMeeting(map).compose(MainActivity.this.<BasicResponse>bindToLifecycle())
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
                                            finish();
                                        }
                                    }
                                });
                    }

                }
            }
        });
    }

    //????????????
    private void showVolumeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_user_volume, null);
        builder.setView(view);
        builder.setCancelable(false);
        volumeDialog = builder.create();
        volumeDialog.show();
        ImageView close = (ImageView) view.findViewById(R.id.dialog_close_icon);
        TextView volumeTxt = (TextView) view.findViewById(R.id.volume);//?????????
        CVIVolumeSeekBar volumeSeekbar = (CVIVolumeSeekBar) view.findViewById(R.id.volume_seekbar);//??????????????????
        TextView madamPresidentName = (TextView) view.findViewById(R.id.madam_president_name);//????????????
        CircleImageView madampPesidentPoto = (CircleImageView) view.findViewById(R.id.madam_president_poto);//????????????
        CheckBox startEndState = (CheckBox) view.findViewById(R.id.start_end_state);//?????????????????????
        busYIdleState = (ThreeStateCheckbox) view.findViewById(R.id.busy_idle_state);//??????????????????
        TextView volume_minus = (TextView) view.findViewById(R.id.volume_minus);
        TextView volume_plus = (TextView) view.findViewById(R.id.volume_plus);

        ListView attendVolumeListView = (ListView) view.findViewById(R.id.attend_volume_listView);      //??????????????????????????????
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = volumeDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.33); //????????????
        volumeDialog.getWindow().setAttributes(lp);

        // ??????????????????
        busYIdleState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (busYIdleState.getState() == ThreeStateCheckbox.SPEACH) {
                    MeetingAPP.getInstance().getHexadecimal().sendCancelSpeech(new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.CANCELSPEECH, msg)) {

                                busYIdleState.setState(ThreeStateCheckbox.NORMAL);
                                meetingMicrophone.setImageResource(R.drawable.volume_idle);
                                micIsChecked = false;
                                ToastUtil.makeText(MainActivity.this, "??????????????????!");
                            } else {
                                meetingMicrophone.setImageResource(R.drawable.volume_checked);
                                micIsChecked = true;
                                ToastUtil.makeText(MainActivity.this, "??????????????????");
                                Log.e(TAG, "??????????????????");
                            }
                        }
                    });

                } else {
                    MeetingAPP.getInstance().getHexadecimal().sendApplySpeech(new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.APPLYSPEECH, msg)) {
                                busYIdleState.setState(ThreeStateCheckbox.SPEACH);
                                micIsChecked = true;
                                meetingMicrophone.setImageResource(R.drawable.volume_checked);
                                ToastUtil.makeText(MainActivity.this, "??????????????????!");
                            } else {
                                meetingMicrophone.setImageResource(R.drawable.volume_idle);
                                micIsChecked = false;
                                ToastUtil.makeText(MainActivity.this, "??????????????????!");
                                Log.e(TAG, "??????????????????!");
                            }
                        }
                    });
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volumeDialog.dismiss();
                busYIdleState = null;
            }
        });

        Integer valueTen2 = StringUtils.isEmpty(mVolume) ? 50 : Integer.parseInt(mVolume, 16);
        volumeSeekbar.setCurrentCount(valueTen2);
        volumeTxt.setText(valueTen2 + "%");

        volumeSeekbar.setOnVolumeChangeListener(new CVIVolumeSeekBar.onVolumeChangeListener() {
            @Override
            public void volumechange(int volume) {
                volumeTxt.setText(volume + "%");
            }

            @Override
            public void volumeStopChange(int volume) {
                if (FastClickUtils.init().isClickFast()) {
                    return;
                }
                String haxVolume = FLUtil.TenToHexAndAppendZeroTo2(volume);
                MeetingAPP.getInstance().getHexadecimal().sendVolume(haxVolume, new SerialPortListener() {
                    @Override
                    public void onMessageResponse(String msg, boolean isComplete) {
                        if (isComplete && SerialConstants.isSetOk(SerialConstants.SetOkEnum.volume, msg)) {
                            mVolume = haxVolume;

                            // ????????????????????????
                            sbChandelier.setProgress(volume);

                        } else {
                            ToastUtil.makeText(MainActivity.this, "??????????????????!");
                        }
                    }
                });
            }
        });

        volume_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = volumeSeekbar.getCurrentProgress();
                if (progress <= 0) {
                    return;
                } else {
                    progress -= 1;
                    volumeSeekbar.setCurrentCount(progress);
                    volumeTxt.setText(progress + "%");
                }

            }
        });

        volume_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = volumeSeekbar.getCurrentProgress();
                if (progress >= 100) {
                    return;
                } else {
                    progress += 1;
                    volumeSeekbar.setCurrentCount(progress);
                    volumeTxt.setText(progress + "%");
                }

            }
        });

        List<AttendeBean> newAttendeList = new ArrayList<>();
        for (int i = 0; i < attendeBeanList.size(); i++) {
            AttendeBean a = attendeBeanList.get(i);
            if (a.getRole().equals("0")) {
                madamPresidentName.setText(a.getName());
                ImageLoader.getInstance().displayImage(a.getAvatar(), madampPesidentPoto);
            } else {
                newAttendeList.add(a);
            }
        }

        attendeesVolumeAdapter = new AttendeesVolumeAdapter(MainActivity.this, newAttendeList);
        attendVolumeListView.setAdapter(attendeesVolumeAdapter);
        attendeesVolumeAdapter.notifyDataSetChanged();

        /**
         * ListView item??????????????? ???????????????????????????
         * micMac newAttendeList?????????mac??????
         * */
        attendVolumeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (newAttendeList.get(i).getDevice() == null) {
                    ToastUtil.makeText(MainActivity.this, "??????????????????!");
                    return;
                }
                String micMac = newAttendeList.get(i).getDevice().getMac();
                if (attendeesVolumeAdapter.getIsSelected().get(i) == ThreeStateCheckbox.NORMAL) {
                    MeetingAPP.getInstance().getHexadecimal().sendMuteMac(micMac, new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.muteMic, msg)) {
                                //  ????????????
                                attendeesVolumeAdapter.getIsSelected().put(i, ThreeStateCheckbox.MUTE);
                            } else {
                                ToastUtil.makeText(MainActivity.this, "????????????!");
                            }
                        }
                    });
                } else {
                    MeetingAPP.getInstance().getHexadecimal().sendCancelMuteMac(micMac, new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.cancelMuteMic, msg)) {
                                //  ??????????????????
                                attendeesVolumeAdapter.getIsSelected().put(i, ThreeStateCheckbox.NORMAL);
                            } else {
                                ToastUtil.makeText(MainActivity.this, "??????????????????!");
                            }
                        }
                    });
                }
                attendeesVolumeAdapter.notifyDataSetChanged();
            }
        });

        //ListView item ?????????????????????????????????
        attendeesVolumeAdapter.setOnItemClickListener(new AttendeesVolumeAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int i) {
                if (newAttendeList.get(i).getDevice() == null) {
                    ToastUtil.makeText(MainActivity.this, "??????????????????!");
                    return;
                }
                String micMac = newAttendeList.get(i).getDevice().getMac();
                if (attendeesVolumeAdapter.getIsSelected().get(i) == ThreeStateCheckbox.NORMAL) {
                    MeetingAPP.getInstance().getHexadecimal().sendMuteMac(micMac, new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.muteMic, msg)) {
                                //  ????????????
                                attendeesVolumeAdapter.getIsSelected().put(i, ThreeStateCheckbox.MUTE);
                            } else {
                                ToastUtil.makeText(MainActivity.this, "????????????!");
                            }
                        }
                    });
                } else {
                    MeetingAPP.getInstance().getHexadecimal().sendCancelMuteMac(micMac, new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.cancelMuteMic, msg)) {
                                //  ??????????????????
                                attendeesVolumeAdapter.getIsSelected().put(i, ThreeStateCheckbox.NORMAL);
                            } else {
                                ToastUtil.makeText(MainActivity.this, "??????????????????!");
                            }
                        }
                    });
                }
                attendeesVolumeAdapter.notifyDataSetChanged();
            }
        });

        //  ???????????? checkbox???????????????????????????
        startEndState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startEndState.isChecked()) {
                    Log.e(TAG, "????????????");
                    MeetingAPP.getInstance().getHexadecimal().sendMuteAll(new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.MUTEALL, msg)) {
                                // ??????list???????????????MyAdapter??????map???????????????true
                                for (int i = 0; i < attendeBeanList.size(); i++) {
                                    attendeesVolumeAdapter.getIsSelected().put(i, ThreeStateCheckbox.MUTE);
                                }
                                attendeesVolumeAdapter.notifyDataSetChanged();
                                startEndState.setText("????????????");
                                Log.e(TAG, "????????????!!!");
                            } else {
                                startEndState.setText("????????????");
                                startEndState.setChecked(false);
                                ToastUtil.makeText(MainActivity.this, "????????????");
                                Log.e(TAG, "????????????!!!");
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "????????????");
                    MeetingAPP.getInstance().getHexadecimal().sendCancelMuteAll(new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            if (SerialConstants.isSetOk(SerialConstants.SetOkEnum.CANCELMUTEALL, msg)) {
                                // ??????list???????????????MyAdapter??????map???????????????true
                                for (int i = 0; i < attendeBeanList.size(); i++) {
                                    attendeesVolumeAdapter.getIsSelected().put(i, ThreeStateCheckbox.NORMAL);
                                }
                                // ????????????list?????????
                                checkNum = attendeBeanList.size();
                                // ??????listview???TextView?????????
                                attendeesVolumeAdapter.notifyDataSetChanged();

                                startEndState.setText("????????????");
                                Log.e(TAG, "??????????????????");
                            } else {
                                startEndState.setChecked(true);
                                startEndState.setText("????????????");
                                ToastUtil.makeText(MainActivity.this, "??????????????????");
                                Log.e(TAG, "??????????????????");
                            }
                        }
                    });
                }
            }
        });

      /*  WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = daleyTimeDialog.getWindow().getAttributes();
        lp.width = (int) (display.getHeight() * 0.8); //????????????
        finishMeetingDialog.getWindow().setAttributes(lp);*/

    }


    /**
     * ??????????????????
     **/
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");    //????????????????????????
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<>();
        list.add("15");
        list.add("30");
        list.add("45");
        list.add("60");
        return list;
    }


    class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("asdfaf", "??????");
            if (constant.CLOSE_WHITEBOARD_BROADCAST.equals(intent.getAction())) {
                closeWhiteServie();
                Log.i(TAG, "???????????????1111");
            }/* else if (constant.CLOSE_BROWSER_BROADCAST.equals(intent.getAction())) {
                closeBroadServie();

            }*/

        }
    }

    /* ?????????????????????????????????fragmet???????????????????????? ??????FragmentPagerAdapter
       */
    class FragViewAdapter extends FragmentPagerAdapter implements TabAdapter {
        @Override
        public q.rorbin.verticaltablayout.widget.TabView.TabBadge getBadge(int position) {
            return new q.rorbin.verticaltablayout.widget.TabView.TabBadge.Builder().setBadgeNumber(0).setBackgroundColor(0xff2faae5)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                            // targetView.setBackgroundColor(Color.parseColor("#fc6500"));
                        }
                    }).build();
        }

        @Override
        public q.rorbin.verticaltablayout.widget.TabView.TabIcon getIcon(int position) {
            MenuBean menu = menuBeanList.get(position);
            return new q.rorbin.verticaltablayout.widget.TabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.TOP)
                    .setIconMargin(dp2px(5))
                    .setIconSize(dp2px(15), dp2px(15))
                    .build();
        }

        @Override
        public q.rorbin.verticaltablayout.widget.TabView.TabTitle getTitle(int position) {
            MenuBean menu = menuBeanList.get(position);
            return new q.rorbin.verticaltablayout.widget.TabView.TabTitle.Builder()
                    .setContent(menu.mTitle)
                    .setTextColor(0xFFFF7400, 0xFF0071FF)
                    .setTextSize(22)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }

        List<Fragment> fragmentList_;
        List<MenuBean> titleList_;

        public List<Fragment> getFragmentList_() {
            return fragmentList_;
        }

        public void setFragmentList_(List<Fragment> fragmentList_) {
            this.fragmentList_ = fragmentList_;
        }

        public List<MenuBean> getTitleList_() {
            return titleList_;
        }

        public void setTitleList_(List<MenuBean> titleList_) {
            this.titleList_ = titleList_;
        }

        public FragViewAdapter(FragmentManager fm, List<Fragment> fragmentList, List<MenuBean> titleList) {
            super(fm);
            fragmentList_ = fragmentList;
            titleList_ = titleList;
        }

        @Override//fragment??????
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem  " + fragmentList_.get(position));
            return fragmentList_.get(position);
        }

        @Override//??????fragment?????????
        public int getCount() {
            return titleList_.size();
        }

        @Override//?????????????????????
        public CharSequence getPageTitle(int position) {
            Log.i(TAG, "getPageTitle  " + titleList_.get(position));
            return titleList_.get(position).mTitle;
        }

        @Override//?????? ???????????????????????????
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragmentList_.get(position).onDestroy();
            fragmentList_.get(position).onStop();
        }
    }

    /* ?????????????????????????????????fragmet???????????????????????? ??????FragmentPagerAdapter
    */
    class FitFragViewAdapter extends FragmentPagerAdapter implements TabAdapter {
        @Override
        public q.rorbin.verticaltablayout.widget.TabView.TabBadge getBadge(int position) {
            return new q.rorbin.verticaltablayout.widget.TabView.TabBadge.Builder().setBadgeNumber(0).setBackgroundColor(0xff2faae5)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                            // targetView.setBackgroundColor(Color.parseColor("#fc6500"));
                        }
                    }).build();
        }

        @Override
        public q.rorbin.verticaltablayout.widget.TabView.TabIcon getIcon(int position) {
            MenuBean menu = fitMenuBeanList.get(position);
            return new q.rorbin.verticaltablayout.widget.TabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.TOP)
                    .setIconMargin(dp2px(5))
                    .setIconSize(dp2px(25), dp2px(25))
                    .build();
        }

        @Override
        public q.rorbin.verticaltablayout.widget.TabView.TabTitle getTitle(int position) {
            MenuBean menu = fitMenuBeanList.get(position);
            return new q.rorbin.verticaltablayout.widget.TabView.TabTitle.Builder()
                    .setContent(menu.mTitle)
                    .setTextColor(0xFF0071FF, 0xFF0071FF)
                    .setTextSize(18)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }

        List<Fragment> fragmentList_;
        List<MenuBean> titleList_;

        public List<Fragment> getFragmentList_() {
            return fragmentList_;
        }

        public void setFragmentList_(List<Fragment> fragmentList_) {
            this.fragmentList_ = fragmentList_;
        }

        public List<MenuBean> getTitleList_() {
            return titleList_;
        }

        public void setTitleList_(List<MenuBean> titleList_) {
            this.titleList_ = titleList_;
        }

        public FitFragViewAdapter(FragmentManager fm, List<Fragment> fragmentList, List<MenuBean> titleList) {
            super(fm);
            fragmentList_ = fragmentList;
            titleList_ = titleList;
        }

        @Override//fragment??????
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem  " + fragmentList_.get(position));
            return fragmentList_.get(position);
        }

        @Override//??????fragment?????????
        public int getCount() {
            return titleList_.size();
        }

        @Override//?????????????????????
        public CharSequence getPageTitle(int position) {
            Log.i(TAG, "getPageTitle  " + titleList_.get(position));
            return titleList_.get(position).mTitle;
        }

        @Override//?????? ???????????????????????????
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragmentList_.get(position).onDestroy();
            fragmentList_.get(position).onStop();
        }
    }

    /*
    * ??????item??????--??????????????????-start
    * */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFloatballManager.show();
        mFloatballManager.onFloatBallClick();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFloatballManager.hide();
    }

    private boolean isfull = false;

    //???????????????????????????
    private void setFullScreen() {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isfull = true;
    }

    private void quitFullScreen() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        isfull = false;
    }

    public void setFullScreen(View view) {
        if (isfull == true) {
            quitFullScreen();
        } else {
            setFullScreen();
        }
    }

    private void init(boolean showMenu, String flag) {
        //1 ??????????????????????????????????????????????????????icon???drawable
        int ballSize = DensityUtil.dip2px(this, 50);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floating_button", this);
        //???????????????????????????????????????config???
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.LEFT_CENTER,false);
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.LEFT_BOTTOM, -100);
//        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_TOP, 100);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_BOTTOM, -25);
        //???????????????????????????
        ballCfg.setHideHalfLater(false);
        if (showMenu) {
            //2 ????????????????????????
            //2.1 ???????????????????????????????????????item??????????????????item?????????
            int menuSize = DensityUtil.dip2px(this, 180);
            int menuItemSize = DensityUtil.dip2px(this, 50);
            FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
            //3 ??????floatballManager
            mFloatballManager = new FloatBallManager(getApplicationContext(), ballCfg, menuCfg);
            addFloatMenuItem(flag);

        } else {
            mFloatballManager = new FloatBallManager(getApplicationContext(), ballCfg);
        }
        setFloatPermission();
    }

    private void setFloatPermission() {
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //???????????????permission???????????????????????????
        mFloatPermissionManager = new FloatPermissionManager();
        mFloatballManager.setPermission(new FloatBallManager.IFloatBallPermission() {
            @Override
            public boolean onRequestFloatBallPermission() {
                requestFloatBallPermission(MainActivity.this);
                return true;
            }

            @Override
            public boolean hasFloatBallPermission(Context context) {
                return mFloatPermissionManager.checkPermission(context);
            }

            @Override
            public void requestFloatBallPermission(Activity activity) {
                mFloatPermissionManager.applyPermission(activity);
            }

        });
    }

    public class ActivityLifeCycleListener implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            ++resumed;
            setFloatballVisible(true);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            --resumed;
            if (!isApplicationInForeground()) {
                setFloatballVisible(false);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
    }

    private void addFloatMenuItem(String flag) {
        if (mFloatballManager != null) {
            mFloatballManager.clearMenuItem();
        }
        MenuItem sameScreen = new MenuItem(BackGroudSeletor.getdrawble("ic_same_screen", this)) {
            @Override
            public void action() {
                if (!isSameScreen) {

                    if (UserUtil.isTempMeeting) {
                        showExtraordinarySameScreenDialog();

                    } else {
                        showSameScreenDialog();//????????????????????????

                    }
                } else {
                    delayBtn.setVisibility(View.GONE);
                    videoEncoder.stop();
                    isSameScreen = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                DatagramSocket socket = new DatagramSocket();
                                String str = constant.FINISH_SHARE_SCEEN;
                                byte[] sendStr = str.getBytes();
                                InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                                DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                                socket.send(packet);
                                socket.close();
                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            /*    InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                                DatagramSocket socket = new DatagramSocket();
                                String sendData = "";
                                if (getDeviceId() != null) {
                                    sendData = getDeviceId();
                                }
                                DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, broadcastAddress, 1234);
                                socket.send(sendPacket);*/

                             /*   byte[] sendStr = android_id.getBytes();
                                InetAddress inet = InetAddress.getByName("255.255.255.255");
                                DatagramPacket packet = new DatagramPacket(sendStr, 0, inet, 6789);
                                DatagramSocket datagramSocket = new DatagramSocket();
                                datagramSocket.setSoTimeout(10000);
                                datagramSocket.send(packet);*/
                        }
                    }).start();
                }
                mFloatballManager.closeMenu();
            }
        };

        MenuItem note = new MenuItem(BackGroudSeletor.getdrawble("note", this)) {
            @Override
            public void action() {
                {
                  /*  if (UserUtil.isFloating) {
                        return;
                    }
                  UserUtil.isFloating = true;*/
                    Intent intent = new Intent(MainActivity.this, AddContent.class);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        try {
                            intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                            intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                        } catch (SQLException e) {
                            e.getLocalizedMessage();
                        }
                    }
                    startActivity(intent);
                }
                mFloatballManager.closeMenu();
                // toast("????????????");
            }
        };
        MenuItem ic_secretary = new MenuItem(BackGroudSeletor.getdrawble("ic_secretary", this)) {
            @Override
            public void action() {
                {
               /*     if (UserUtil.isFloating) {
                        return;
                    }
                   UserUtil.isFloating = true;*/
                    Intent intent = new Intent(MainActivity.this, ActivityMinuteMeeting.class);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        try {
                            intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                            intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                        } catch (SQLException e) {
                            e.getLocalizedMessage();
                        }
                    }
                    startActivity(intent);
                }
                mFloatballManager.closeMenu();
                // toast("????????????");
            }
        };
      /*  MenuItem settingItem = new MenuItem(BackGroudSeletor.getdrawble("ic_email", this)) {
            @Override
            public void action() {
               // toast("????????????");
                mFloatballManager.closeMenu();
            }
        };*/
        Log.d("fdgsgs555", isSecretaryId + "");
        if ("0".equals(flag)) {
            if (isSecretaryId) {
                mFloatballManager.addMenuItem(sameScreen)
                        .addMenuItem(note)
                        .addMenuItem(ic_secretary)
            /*    .addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)*/
                        .buildMenu();
                Log.d("fdgsgs6666", isSecretaryId + "");
            } else {
                mFloatballManager
                        .addMenuItem(sameScreen)
                        .addMenuItem(note)
            /*    .addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)*/
                        .buildMenu();
                Log.d("fdgsgs777", isSecretaryId + "");

            }
        } else if ("1".equals(flag)) {
            if (isSecretaryId) {
                mFloatballManager
                        .addMenuItem(note)
                        .addMenuItem(ic_secretary)
            /*    .addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)*/
                        .buildMenu();
                Log.d("fdgsgs888", isSecretaryId + "");
            } else {
                mFloatballManager
                        .addMenuItem(note)
            /*    .addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)*/
                        .buildMenu();
                Log.d("fdgsgs999", isSecretaryId + "");
            }


        }


    }

    private void setFloatballVisible(boolean visible) {
        if (visible) {
            mFloatballManager.show();
        } else {
            mFloatballManager.hide();
        }
    }

    public String getDeviceId() {

        android_id = Settings.Secure.getString(MainActivity.this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Hawk.put("android_id", android_id);
        Log.d("dfasff22", android_id);
        return android_id;

    }

    /**
     * ??????????????????????????????
     */
    private void getUserList() {
        String _id = UserUtil.meeting_record_id;
       /* if (Hawk.contains("_id")) {
            _id = Hawk.get("_id");
        }*/
        if (attendeBeanList != null) {
            attendeBeanList.clear();
        }
        if(UserUtil.isTempMeeting){
            // ??????????????????
            TempWSBean bean = new TempWSBean();
            bean.setReqType(0);
            bean.setUserMac_id(FLUtil.getMacAddress());
            bean.setPackType(constant.QUERYATTEND);
            bean.setBody("");
            String strJson = new Gson().toJson(bean);
            JWebSocketClientService.sendMsg(strJson);
        }else {
            NetWorkManager.getInstance().getNetWorkApiService().getMeetingUserList(_id).compose(this.<BasicResponse<List<AttendeBean>>>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<List<AttendeBean>>>() {
                        @Override
                        protected void onSuccess(BasicResponse<List<AttendeBean>> response) {
                            Log.d("RigthContragment2222", response.getData().toString() + "======??????");
                            attendeBeanList = (List<AttendeBean>) response.getData();

                            Hawk.put(constant.attendeBeanList, attendeBeanList);

                            List<String> attendeIdList = new ArrayList<>();
                            for (int i = 0; i < attendeBeanList.size(); i++) {
                                AttendeBean bean = attendeBeanList.get(i);
                                attendeIdList.add(bean.getUser_id());
                                if ("1".equals(attendeBeanList.get(i).getRole())) {
                                    if (UserUtil.user_id.equals(attendeBeanList.get(i).getUser_id())) {
                                        /*
                                         * ?????????????????????????????????????????????????????????????????????????????????????????????
                                         * */
                                        isSecretaryId = true;
                                        meeting_summary.setVisibility(View.GONE);
                                        Message message = new Message();
                                        message.what = 201;
                                        mHander.sendMessage(message);
                                    }

                                }
                            }
                            Hawk.put(constant.attendeIDList, attendeIdList);

                            Log.d("fsdgsgs22", attendeBeanList.size() + "");
                        }
                    });
        }
    }

    public boolean isApplicationInForeground() {
        return resumed > 0;
    }

   /* *//**
     * ??????byte??????????????????
     *
     * @param bytes ?????????????????????byte??????
     *//*
    private void createFileWithByte(byte[] bytes) {
        // TODO Auto-generated method stub
        */

    /**
     * ??????File???????????????????????????????????????????????????????????????
     *//*
        File file = new File(Environment.getExternalStorageDirectory(),
                fileName);
        // ??????FileOutputStream??????
        FileOutputStream outputStream = null;
        // ??????BufferedOutputStream??????
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // ???????????????????????????
            if (file.exists()) {
                file.delete();
            }
            // ?????????????????????????????????????????????????????????
            file.createNewFile();
            // ??????FileOutputStream??????
            outputStream = new FileOutputStream(file);
            // ??????BufferedOutputStream??????
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // ???????????????????????????????????????byte??????
            bufferedOutputStream.write(bytes);
            // ?????????????????????????????????????????????????????????flush()??????????????????????????????????????????
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // ??????????????????
            e.printStackTrace();
        } finally {
            // ????????????????????????
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }*/

    /*
    * ??????item??????--??????????????????-end
    * */
      /*
* ??????-start
* */
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("tab");
            if ("file".equals(str)) {
//                if (UserUtil.ISCHAIRMAN) {
//                    tablayout.setTabSelected(2);
//                } else {
                    tablayout.setTabSelected(1);
//                }

            } else if ("vote".equals(str)) {
//                if (UserUtil.ISCHAIRMAN) {
//                    tablayout.setTabSelected(3);
//                } else {
                    tablayout.setTabSelected(2);
//                }

            }


        }
    }
    class CloseServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (serviceConnection!=null){
                unbindService(serviceConnection);

            }
        }
}}