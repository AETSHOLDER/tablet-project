package com.example.paperlessmeeting_demo.activity;


import static android.content.Context.WIFI_SERVICE;

import static com.example.paperlessmeeting_demo.tool.FLUtil.getNetworkType;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Sign.SignActivity;
import com.example.paperlessmeeting_demo.activity.Sign.SignListActivity;
import com.example.paperlessmeeting_demo.adapter.PagerAdapter2;
import com.example.paperlessmeeting_demo.adapter.WuHuNewTopicAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.FileFragmentationBean;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.MeetingIdBean;
import com.example.paperlessmeeting_demo.bean.MergeChunkBean;
import com.example.paperlessmeeting_demo.bean.PushBean;
import com.example.paperlessmeeting_demo.bean.SharePushFileBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBeanRequset;
import com.example.paperlessmeeting_demo.bean.WuHuMeetingListResponse;
import com.example.paperlessmeeting_demo.bean.WuHuNetWorkBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.fragment.WuHuFragment2;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.sharefile.BroadcastUDPFileService;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.DownloadUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.FileCutUtils;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.Md5Util;
import com.example.paperlessmeeting_demo.tool.PCScreen.Netty.NettyListener;
import com.example.paperlessmeeting_demo.tool.PCScreen.ScreenImageService;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.Manager.UDPClientManager;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.OnUdpConnectListener;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.EncodeV1;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.ServerManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.UDPBroadcastManager;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.CompletedView;
import com.example.paperlessmeeting_demo.widgets.MyDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jyn.vcview.VerificationCodeView;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.FileNameMap;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.StringUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.RestAdapter;

/*
 * ???????????????????????????
 * */
public class WuHuActivity3 extends BaseActivity implements View.OnClickListener, WuHuNewTopicAdapter.saveSeparatelyInterface, WuHuNewTopicAdapter.deletSeparatelyInterface, WuHuNewTopicAdapter.addSeparatelyInterface, NettyListener {


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

    @BindView(R.id.consult_ll1)
    RelativeLayout consult_ll1;
    @BindView(R.id.vote_ll1)
    RelativeLayout vote_ll1;
    @BindView(R.id.finish_ll)
    RelativeLayout finish_ll;
    @BindView(R.id.home_ll1)
    RelativeLayout home_ll1;
    @BindView(R.id.home_ll)
    RelativeLayout home_ll;

    @BindView(R.id.shareScreen_ll1)
    RelativeLayout shareScreen_rl;
    @BindView(R.id.shareScreen_tv)
    TextView shareScreen_tv;

    @BindView(R.id.cc1)
    RelativeLayout cc1;
    @BindView(R.id.cc2)
    RelativeLayout cc2;
    private MyDialog dialog;
    private MyDialog networkFileDialog;

    @BindView(R.id.comfirm)
    ImageView comfirm;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView timeTv;
    @BindView(R.id.finish_meeting)
    TextView finish_meeting;

    @BindView(R.id.rigth_rl)
    RelativeLayout rigth_rl;
    @BindView(R.id.left_rl)
    RelativeLayout left_rl;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    private List<String> stringsIp = new ArrayList<>();//??????????????????????????????Ip
    /*  @BindView(R.id.tittle2)*/
    private EditText tittle2;
    /* @BindView(R.id.company_name)*/
    private EditText company_name;
    private RecyclerView myListView;
    //private WuHuListAdapter wuHuListAdapter;
    private WuHuNewTopicAdapter wuHuListAdapter;
    private int k = 0;
    private View inflate;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList = new ArrayList<>();

    private RelativeLayout add_topic_rl;
    private RelativeLayout dialg_rl_root;
    private RelativeLayout sava_all;
    private Button mBtnDelete;
    private Button mBtnAdd;
    private SparseArray<WuHuFragment2> mTestFragments = new SparseArray<>();
    private PagerAdapter2 mPagerAdapter;
    private int key;
    private int mCurPos;
    private int fragmentPos = 0;
    private MyBroadcastReceiver myBroadcastReceiver;
    private MyBroadcastReceiver myRefreshBroadcastReceiver;
    //??????????????????
    private MyBroadcastReceiver mycatalogBroadcastReceiver;
    private InetAddress address;
    private boolean isBind = false;
    /*
     * ???????????????????????????service???InetAddress
     * */
    private BroadcastUDPFileService broadcastUDPFileService;
    private BroadcastUDPFileService.BroadcastUDPFileServiceBinder binder;
    private WuHuEditBean wuHuEditBean;
    //???????????????flag
    private String lineFlag = "1";
    //?????????????????????
    private String themFlag = "3";
    private boolean isEditRl = false;
    private SocketShareFileManager socketShareFileManager;
    private ServiceConnection serviceConnection;
    private Handler handler;
    private Runnable runnable;
    private ArrayList<WuHuEditBean.EditListBean> editListBeans = new ArrayList<>();
    private List<WuHuEditBean.EditListBean.FileListBean> shareFileBeans = new ArrayList<>();//???????????????????????????????????????
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//??????????????????????????????????????????
    private String netFilePath = Environment.getExternalStorageDirectory() + constant.WUHU_NET_FILE;//????????????????????????????????????
    private View foodView;
    private CompletedView completedView;
    private ImageView result_ima;
    private TextView tips;
    private String sharePushPos;
    private int allFileNum = 0;//???????????????
    private int dowLoadNum = 0;//???????????????????????????
    private List<WuHuNetWorkBean> failList = new ArrayList<>();//???????????????????????????
    private ProgressBar progressBar;//????????????????????????
    private List<WuHuNetWorkBean> filesList = new ArrayList<>();//???????????????????????????

    // ----------------????????????????????????
    private String TAG = "WuHuActivity";
    private Dialog shareScreenDialog;           // ??????code??????
    private VerificationCodeView shareScreen_codeview;
    private String my_code;
    private String mIp = "192.168.1.1";  // ?????????????????????IP
    private MediaProjectionManager mMediaProjectionManage;
    private MediaProjection mediaProjection;
    private static final int ACTIVITY_RESULT_CODE_SCREEN = 110;
    private MyServiceConnect mConnect;

    private int upLoadNum = 0;//????????????????????????
    private int upLoadFileNum = 0;
    private int locaFileNum = 0;
    private int size = 1024 * 1024 * 100;
    private String upLoadFileType;
    private FileCutUtils fileCutUtils;  //?????????????????????
    private int littlefilecount;  //??????????????????
    private List<File> littlefilelist = new ArrayList<>();
    private String endStrAll;
    private int isAdd = 0;//?????????????????????????????????0????????????
    private int littelFilePos = 0;
    private int issuePos = 0;//????????????
    private int issueFileNum = 0;//????????????????????????
    private List<FileFragmentationBean> fileFragmentationBeans = new ArrayList<>();
    private String VOTE_FILE = Environment.getExternalStorageDirectory() + constant.VOTE_FILE;
    /**
     * ??????????????????
     */

    private long time = 2000;
    private long first_time = 0;
    private String meetingTime;//??????-????????????
    private String week = "";
    private String selfIp = "";
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            String selfIp = "";
            String stIp = "";
            File file = null;
            String fileName = "-1";
            String endStr = "-1";
            String pos = "-1";
            Uri uri = null;
            switch (msg.what) {

                case 2:
                    Toast.makeText(WuHuActivity3.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 33:
                    //?????? ?????????
                    Toast.makeText(WuHuActivity3.this, "??????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    Log.d("vvcvsvsfgsf", "case 3");
                    int flag = msg.arg1;
                    int flag2 = msg.arg2;
                    String filePath1 = (String) msg.obj;
                    File fileShare = new File(filePath1);
                    String fileShareName = fileShare.getName();
                    String posShare;
                    if (fileShareName.contains(constant.WUHUSHARE)) {
                        String[] fileNameAll = fileShareName.split(constant.WUHUSHARE);
                        fileShareName = fileNameAll[1];
                        posShare = fileNameAll[0];
                    } else {
                        posShare = "-1";
                    }
                    sharePushPos = posShare;
                    getShareFileComit(posShare);
                    Intent intent1 = new Intent();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flag", "1");
                    bundle1.putString("topicPos", posShare);
                    bundle1.putString("filePath", filePath1);
                    intent1.putExtras(bundle1);
                    intent1.setAction(constant.SHARE_FILE_BROADCAST);
                    //  sendBroadcast(intent1);
                    break;
                case 4:
                    Toast.makeText(WuHuActivity3.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 400:
                    //??????  ?????????
                    WuHuEditBean.EditListBean.FileListBean fileListBean1 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    SharePushFileBean pushNoFileBean = new SharePushFileBean();
                    pushNoFileBean.setPos(fileListBean1.getPos());
                    pushNoFileBean.setHave(true);
                    pushNoFileBean.setMac(fileListBean1.getMac());
                    wsUpdata(pushNoFileBean, constant.FILERESPONDPUSH);


                    Intent intent400 = new Intent();
                    Bundle bundle400 = new Bundle();

                    FileListBean fileBean400;
                    file = new File(fileListBean1.getMd5Path());
                    fileName = file.getName();
                    endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (fileName.contains(constant.WUHUPUSH)) {
                        String[] fileNameAll = fileName.split(constant.WUHUPUSH);
                        fileName = fileNameAll[1];
                        pos = fileNameAll[0];
                        fileBean400 = new FileListBean(fileName, file.getPath(), "", "");
                    } else {
                        fileBean400 = new FileListBean(file.getName(), file.getPath(), "", "");
                        pos = "-1";
                    }

                    uri = Uri.fromFile(file);
                    Log.d("wuhuactivityrequestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean400.setFile_type(getType(endStr));
                    fileBean400.setNet(false);
                    fileBean400.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                  /*  bundle400.putString("flag", "2");
                    bundle400.putString("filePath", fileListBean1.getMd5Path());
                    bundle400.putString("topicPos", pos);
                    intent400.putExtras(bundle400);
                    intent400.setAction(constant.SHARE_FILE_BROADCAST);
                    sendBroadcast(intent400);*/
                    if (fileBean400.getFile_type().equals("3")) {
                        //??????????????????????????????????????????
                    /*    if ( isActivityTop(ActivityImage.class,WuHuActivity.this)){
                            Intent  intent8=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                            Bundle bundle2=new Bundle();
                            bundle2.putString("url",fileBean.getPath());
                            intent8.putExtras(bundle2);
                           sendBroadcast(intent8);

                        }*/
                        Activity topActivity = ActivityUtils.getTopActivity();
                        if (topActivity != null) {
                            // ?????????????????????????????????????????????,???????????????tbs,????????????????????????(???????????????????????????)
                            if (topActivity.getLocalClassName().contains("ActivityImage")) {
                                {
                                    ActivityImage activityImage = (ActivityImage) topActivity;
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (topActivity.getLocalClassName().contains("SignActivity")) {
                                SignActivity signActivity = (SignActivity) topActivity;
                                signActivity.clearData();
                                topActivity.finish();
                                try {
                                    Thread.sleep(200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        Intent intent99 = new Intent();
                        intent99.setClass(WuHuActivity3.this, ActivityImage.class);
                        intent99.putExtra("url", fileBean400.getPath());
                        intent99.putExtra("isOpenFile", true);
                        intent99.putExtra("isNetFile", false);
                        startActivity(intent99);
                        Log.d("????????????  ActivityImage activity", "11111111");
                    } else if (fileBean400.getFile_type().equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                // ?????????????????????????????????????????????,???????????????tbs,????????????????????????(???????????????????????????)
                                if (topActivity.getLocalClassName().contains("SignActivity")) {
                                    SignActivity signActivity = (SignActivity) topActivity;
                                    signActivity.clearData();
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (topActivity.getLocalClassName().contains("ActivityImage")) {
                                    ActivityImage activityImage = (ActivityImage) topActivity;
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            intent400.setClass(WuHuActivity3.this, SignActivity.class);
                            intent400.putExtra("url", fileBean400.getPath());
                            intent400.putExtra("isOpenFile", true);
                            intent400.putExtra("isNetFile", false);
                            intent400.putExtra("tempPath", false);
                            intent400.putExtra("fileName", fileBean400.getName());
                            startActivity(intent400);
                            Log.d("????????????  SignActivity activity", "2222222");
                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(WuHuActivity3.this, "???????????????????????????wps????????????", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(fileBean400.getPath(), WuHuActivity3.this));
                                }
                            });
                        }
                    }
                    break;
                case 500:
                    //?????? ?????????
                    SharePushFileBean pushHaveFileBean = new SharePushFileBean();
                    WuHuEditBean.EditListBean.FileListBean fileListBean2 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    pushHaveFileBean.setHave(false);
                    pushHaveFileBean.setMac(fileListBean2.getMac());
                    pushHaveFileBean.setPos(fileListBean2.getPos());
                    wsUpdata(fileListBean2, constant.FILERESPONDPUSH);
                    break;
                case 600:
                    //??????  ?????????
                    WuHuEditBean.EditListBean.FileListBean fileListBean3 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    SharePushFileBean shareNoFileBean = new SharePushFileBean();
                    shareNoFileBean.setPos(fileListBean3.getPos());
                    shareNoFileBean.setHave(true);
                    shareNoFileBean.setMac(fileListBean3.getMac());
                    wsUpdata(shareNoFileBean, constant.FILERESPONDSHARE);
                    break;
                case 700:
                    //?????? ?????????
                    SharePushFileBean shareHaveFileBean = new SharePushFileBean();
                    WuHuEditBean.EditListBean.FileListBean fileListBean4 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    shareHaveFileBean.setHave(false);
                    shareHaveFileBean.setMac(fileListBean4.getMac());
                    shareHaveFileBean.setPos(fileListBean4.getPos());
                    wsUpdata(shareHaveFileBean, constant.FILERESPONDSHARE);
                    break;
                case 88:
                    //?????? ?????????
                    if (UserUtil.ISCHAIRMAN) {
                        return;
                    }
                    Toast.makeText(WuHuActivity3.this, "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    Log.e("hahahahahahaa", "case 8");
                    String filePath = (String) msg.obj;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    FileListBean fileBean;
                    file = new File(filePath);
                    fileName = file.getName();
                    endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (fileName.contains(constant.WUHUPUSH)) {
                        String[] fileNameAll = fileName.split(constant.WUHUPUSH);
                        fileName = fileNameAll[1];
                        pos = fileNameAll[0];
                        fileBean = new FileListBean(fileName, file.getPath(), "", "");
                    } else {
                        fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                        pos = "-1";
                    }
                    sharePushPos = pos;
                    getShareFileComit(pos);
                    uri = Uri.fromFile(file);
                    Log.d("wuhuactivityrequestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setNet(false);
                    fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                    bundle.putString("flag", "2");
                    bundle.putString("filePath", filePath);
                    bundle.putString("topicPos", pos);
                    intent.putExtras(bundle);
                    intent.setAction(constant.SHARE_FILE_BROADCAST);
                    //  sendBroadcast(intent);
                    if (fileBean.getFile_type().equals("3")) {
                        //??????????????????????????????????????????
                    /*    if ( isActivityTop(ActivityImage.class,WuHuActivity.this)){
                            Intent  intent8=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                            Bundle bundle2=new Bundle();
                            bundle2.putString("url",fileBean.getPath());
                            intent8.putExtras(bundle2);
                           sendBroadcast(intent8);

                        }*/
                        Activity topActivity = ActivityUtils.getTopActivity();
                        if (topActivity != null) {
                            // ?????????????????????????????????????????????,???????????????tbs,????????????????????????(???????????????????????????)
                            if (topActivity.getLocalClassName().contains("ActivityImage")) {
                                {
                                    ActivityImage activityImage = (ActivityImage) topActivity;
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (topActivity.getLocalClassName().contains("SignActivity")) {
                                SignActivity signActivity = (SignActivity) topActivity;
                                signActivity.clearData();
                                topActivity.finish();
                                try {
                                    Thread.sleep(200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        Intent intent9 = new Intent();
                        intent9.setClass(WuHuActivity3.this, ActivityImage.class);
                        intent9.putExtra("url", fileBean.getPath());
                        intent9.putExtra("isOpenFile", true);
                        intent9.putExtra("isNetFile", false);
                        startActivity(intent9);
                    } else if (fileBean.getFile_type().equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                // ?????????????????????????????????????????????,???????????????tbs,????????????????????????(???????????????????????????)
                                if (topActivity.getLocalClassName().contains("SignActivity")) {
                                    // ??????????????????????????????????????????
                                    try {
                                        Thread.sleep(100);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    SignActivity signActivity = (SignActivity) topActivity;
                                    signActivity.clearData();
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (topActivity.getLocalClassName().contains("ActivityImage")) {
                                    ActivityImage activityImage = (ActivityImage) topActivity;
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            intent = new Intent();
                            intent.setClass(WuHuActivity3.this, SignActivity.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", false);
                            intent.putExtra("tempPath", false);
                            intent.putExtra("fileName", fileBean.getName());
                            startActivity(intent);

                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(WuHuActivity3.this, "???????????????????????????wps????????????", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(fileBean.getPath(), WuHuActivity3.this));
                                }
                            });
                        }
                    }

                    break;
                case 32:
                    Log.e("hahahahahahaa", "case 32");
                    String filePath2 = (String) msg.obj;
                    Intent intent2 = new Intent();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("flag", "2");
                    bundle2.putString("filePath", filePath2);
                    intent2.putExtras(bundle2);
                    intent2.setAction(constant.RUSH_VOTE_LIST_BROADCAST);
                    sendBroadcast(intent2);

                    break;
                case 101:
                    Log.e("hahahahahahaa", "case 100");
                    Intent intent3 = new Intent();
                    intent3.setAction(constant.RUSH_SIGN_LIST_BROADCAST);
                    sendBroadcast(intent3);
                    break;
                case 119:
                    String adressIp = (String) msg.obj;
                    String strIp = "";
                    Log.d("stringsIp????????????111=", adressIp);
                    if (Hawk.contains("SelfIpAddress")) {
                        strIp = Hawk.get("SelfIpAddress");
                    }
                    if (!adressIp.equals(strIp)) {
                        stringsIp.add(adressIp);

                        for (int i = 0; i < stringsIp.size() - 1; i++) {
//                        Log.d("gdhh222", stringsIp.get(i) + "");
                            for (int j = stringsIp.size() - 1; j > i; j--) {
                                if (stringsIp.get(i).equals(stringsIp.get(j)))
                                    stringsIp.remove(j);
                            }
                            Log.d("stringsIp????????????222=", stringsIp.get(i));
                        }
                        if (Hawk.contains("stringsIp")) {
                            List<String> ips = new ArrayList<>();
                            ips = Hawk.get("stringsIp");
                            if (!setContains(ips, adressIp)) {
                                Hawk.put("stringsIp", stringsIp);
                            }
                        } else {
                            Hawk.put("stringsIp", stringsIp);
                        }

                        for (int i = 0; i < stringsIp.size(); i++) {
                            Log.d("stringListIp=??????", stringsIp.get(i));

                        }
                    }

                    break;
                case 5:
                    Toast.makeText(WuHuActivity3.this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case 120:
//                    selfIp = (String) msg.obj;
//                    if (Hawk.contains("SelfIpAddress")) {
//                        stIp = Hawk.get("SelfIpAddress");
//                        if (selfIp.equals(stIp)) {
//                            //    delayBtn.setVisibility(View.VISIBLE);
//                            return;
//                        }
//                    }
//                    String  isScrenn="";
////                    if (Hawk.contains("isSameScreen")){
////                        isScrenn=Hawk.get("isSameScreen");
////                    }
////                    if ("1".equals(isScrenn)){
//                        Intent inte = new Intent(WuHuActivity.this, ScreenReceiveActivity.class);
//                        startActivity(inte);
////                    }

                    break;
                case 911:
//                    selfIp = (String) msg.obj;
//                    if (Hawk.contains("SelfIpAddress")) {
//                        stIp = Hawk.get("SelfIpAddress");
//                        if (selfIp.equals(stIp)) {
//                            //   delayBtn.setVisibility(View.GONE);
//                            return;
//                        }
//                    }

                    break;
                case 800:
                    List<WuHuEditBean.EditListBean.FileListBean> shareFiles = (List<WuHuEditBean.EditListBean.FileListBean>) msg.obj;
                    if (shareFiles == null) {
                        return;
                    }
                    if (Hawk.contains("WuHuFragmentData")) {
                        //???????????????????????????
                        List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        if (wuHuEditBean != null && wuHuEditBean.getEditListBeanList() != null) {

                            copyEdList.addAll(wuHuEditBean.getEditListBeanList());

                        }

                        //????????????????????????????????????
                        List<WuHuEditBean.EditListBean.FileListBean> shareService = new ArrayList<>();
                        shareService.clear();
                        if (copyEdList != null && copyEdList.size() > 0) {

                            for (int i = 0; i < copyEdList.size(); i++) {
                                //???????????????
                                WuHuEditBean.EditListBean editListBean = copyEdList.get(i);

                                if (editListBean.getPos().equals(sharePushPos)) {
                                    if (editListBean.getFileListBeanList() != null) {
                                        shareService.addAll(editListBean.getFileListBeanList());
                                    }
                                    //??????????????????????????????????????????????????????
                                    shareService.addAll(shareFiles);
                                    //????????????????????????????????????
                                    for (int k = 0; k < shareService.size() - 1; k++) {
                                        for (int j = shareService.size() - 1; j > k; j--) {
                                            if (shareService.get(j).getPath().equals(shareService.get(k).getPath()) && shareService.get(j).getPos().equals(shareService.get(k).getPos())) {
                                                shareService.remove(j);
                                            }
                                        }
                                    }
                                    editListBean.setFileListBeanList(shareService);
                                    copyEdList.set(Integer.valueOf(sharePushPos), editListBean);

                                }
                            }
                            wuHuEditBean.setEditListBeanList(copyEdList);
                        }
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        if (UserUtil.ISCHAIRMAN) {
                            wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                        }
                    }


                    break;
                case 1080:
                    String path = (String) msg.obj;
                    String type = "";
                    String endStr2 = "";
                    String name = "";
                    if (path == null) {
                        return;
                    }
                    if (path != null) {
                        File ff = new File(path);
                        //   String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);s
                        String str = ff.getName().substring(ff.getName().lastIndexOf(".") + 1);
                        endStr2 = getType(str);
                        name = ff.getName();
                    }

                    if (endStr2.equals("3")) {
                        //??????????????????????????????????????????
                    /*    if ( isActivityTop(ActivityImage.class,WuHuActivity.this)){
                            Intent  intent8=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                            Bundle bundle2=new Bundle();
                            bundle2.putString("url",fileBean.getPath());
                            intent8.putExtras(bundle2);
                           sendBroadcast(intent8);

                        }*/
                        Activity topActivity = ActivityUtils.getTopActivity();
                        if (topActivity != null) {
                            // ?????????????????????????????????????????????,???????????????tbs,????????????????????????(???????????????????????????)
                            if (topActivity.getLocalClassName().contains("ActivityImage")) {
                                {
                                    ActivityImage activityImage = (ActivityImage) topActivity;
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (topActivity.getLocalClassName().contains("SignActivity")) {
                                SignActivity signActivity = (SignActivity) topActivity;
                                signActivity.clearData();
                                topActivity.finish();
                                try {
                                    Thread.sleep(200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        Intent intent9 = new Intent();
                        intent9.setClass(WuHuActivity3.this, ActivityImage.class);
                        if (path != null) {
                            intent9.putExtra("url", path);
                            intent9.putExtra("isOpenFile", true);
                            intent9.putExtra("isNetFile", false);
                            startActivity(intent9);
                        }
                    } else if (endStr2.equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                // ?????????????????????????????????????????????,???????????????tbs,????????????????????????(???????????????????????????)
                                if (topActivity.getLocalClassName().contains("SignActivity")) {
                                    // ??????????????????????????????????????????
                                    try {
                                        Thread.sleep(100);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    SignActivity signActivity = (SignActivity) topActivity;
                                    signActivity.clearData();
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (topActivity.getLocalClassName().contains("ActivityImage")) {
                                    ActivityImage activityImage = (ActivityImage) topActivity;
                                    topActivity.finish();
                                    try {
                                        Thread.sleep(200);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            intent = new Intent();
                            intent.setClass(WuHuActivity3.this, SignActivity.class);
                            if (path != null) {
                                intent.putExtra("url", path);
                                intent.putExtra("isOpenFile", true);
                                intent.putExtra("isNetFile", false);
                                intent.putExtra("tempPath", false);
                                intent.putExtra("fileName", name);
                                startActivity(intent);

                            }


                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(WuHuActivity3.this, "???????????????????????????wps????????????", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if (path != null) {

                                        startActivity(FileUtils.openFile(path, WuHuActivity3.this));
                                    }

                                }
                            });
                        }
                    }

                    break;

            }


        }
    };

    private boolean setContains(List<String> list, String value) {
        Set<String> stringSet = new HashSet<String>(list);
        return stringSet.contains(value);
    }

    /*
     * ????????????????????????????????????????????????????????????
     * */
    private void getShareFileComit(String textNub) {
        //?????????????????????????????????????????????
        File path = new File(fileShare);
        File[] files = path.listFiles();// ??????
        if (files == null) {
            return;
        }
        shareFileBeans.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (UserUtil.object) {

                    //???????????????
                    String content = "";
                    if (files != null) {
                        // ???????????????????????????????????????????????????
                        for (File file : files) {
                            if (file.isDirectory()) {
                                getFileName(file.listFiles());

                            } else {
                                String fileName = file.getName();
                                String[] fileNameAll = null;
                                String pos = "-1";
                                if (fileName.contains(constant.WUHUPUSH)) {
                                    fileNameAll = fileName.split(constant.WUHUPUSH);
                                    fileName = fileNameAll[1];
                                    pos = fileNameAll[0];
                                } else if (fileName.contains(constant.WUHUSHARE)) {
                                    fileNameAll = fileName.split(constant.WUHUSHARE);
                                    pos = fileNameAll[0];
                                }

                                if (textNub != null && pos != null) {
                                    if (textNub.equals(pos)) {
                                        String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                                        WuHuEditBean.EditListBean.FileListBean fileBean = new WuHuEditBean.EditListBean.FileListBean(fileName, file.getPath(), "", "");
                                        Uri uri = Uri.fromFile(file);
                                        fileBean.setResImage(getIamge(endStr));
                                        fileBean.setFile_type(getType(endStr));
                                        fileBean.setFileMd5(Md5Util.getFileMD5(file));//MD5????????????
                                        fileBean.setPos(pos);
                                        fileBean.setNet(false);
                                        fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                                        //  fileBean.setType(endStr);
                                        fileBean.setType(getFileType(endStr));
                                        shareFileBeans.add(fileBean);
                                    }

                                }

                            }
                        }
                        //????????????????????????????????????
                        for (int i = 0; i < shareFileBeans.size() - 1; i++) {
                            for (int j = shareFileBeans.size() - 1; j > i; j--) {
                                if (shareFileBeans.get(j).getName().equals(shareFileBeans.get(i).getName()) && shareFileBeans.get(j).getPos().equals(shareFileBeans.get(i).getPos())) {
                                    shareFileBeans.remove(j);
                                }
                            }
                        }

                        //??????????????????????????????
                        Message shareMsg = new Message();
                        shareMsg.what = 800;
                        shareMsg.obj = shareFileBeans;
                        mHander.sendMessage(shareMsg);

                    }


                }

                //?????????????????????
            }
        }).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.ADD_FRAGMENT_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);
        Log.d("wrewarawrawer", "onCreate");

        mycatalogBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter4 = new IntentFilter();
        filter4.addAction(constant.CHANGE_CATALOG_BROADCAST);
        registerReceiver(mycatalogBroadcastReceiver, filter4);

        wuHuEditBeanList.clear();
        wuHuListAdapter = new WuHuNewTopicAdapter(WuHuActivity3.this.mContext, wuHuEditBeanList);
        wuHuListAdapter.setSaveSeparatelyInterface(this);
        wuHuListAdapter.setDeletSeparatelyInterface(this);
        wuHuListAdapter.setAddSeparatelyInterface(this);

        mPagerAdapter = new PagerAdapter2(getSupportFragmentManager(), mTestFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        //  mViewPager.setOffscreenPageLimit(2);
        if (mTestFragments.size() > 0) {
            //  mViewPager.setOffscreenPageLimit(mTestFragments.size() - 1);
        }

        UDPBroadcastManager.getInstance().stopReceiveUDP();
        // ?????????????????????,?????????????????????
        if (UserUtil.isTempMeeting) {
            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageClient)) {
                //  ????????????????????????
                UserUtil.ISCHAIRMAN = false;
                String ipUrl = getIntent().getStringExtra("ip");
                String isReuse = getIntent().getStringExtra("isreuse");
                Hawk.put("isreuse", isReuse);//?????????????????????????????????????????? //1:??????????????????  2???????????????????????? 3?????????????????????
                UserUtil.serverIP = ipUrl;
                if (!StringUtils.isEmpty(ipUrl)) {
                    UrlConstant.TempWSIPString = "ws://" + ipUrl + ":" + UrlConstant.port;
//                    Log.e("????????????", "???????????????server IP ======" + UrlConstant.TempWSIPString);
                }
                //  ??????websocket(server???client?????????)
                JWebSocketClientService.initSocketClient();

            } else if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                UserUtil.ISCHAIRMAN = true;
                String code = getIntent().getStringExtra("code");
                String isReuse = getIntent().getStringExtra("isreuse");
                Hawk.put("isreuse", isReuse);//?????????????????????????????????????????? //1:??????????????????  2???????????????????????? 3?????????????????????
                //  alertCodeDialog(code);

                //  ???????????????code??????IP??????
                UDPBroadcastManager.getInstance().sendUDPWithCode(code + "/" + isReuse);
                // ??????socket?????????
                ServerManager.getInstance().startMyWebsocketServer(UrlConstant.port);
                UrlConstant.TempWSIPString = "ws://" + FLUtil.getIPAddress() + ":" + UrlConstant.port;
                // ?????????client?????????server?????????????????????
            }
        }

        EventBus.getDefault().register(this);
        // loadData();
        if (UserUtil.ISCHAIRMAN) {
            cc1.setVisibility(View.VISIBLE);
            cc2.setVisibility(View.GONE);
            if (!UserUtil.isNetDATA) {
                finish_meeting.setText("????????????");
                vote_ll.setVisibility(View.GONE);
            }

            //???????????????????????????????????????????????????????????????????????????
            //  initiaServerData();
        } else {
            cc1.setVisibility(View.GONE);
            cc2.setVisibility(View.VISIBLE);
        }
        //????????????????????????????????????ip
        //  sendIp();
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
                            datagramSocket = new DatagramSocket(null);
                            datagramSocket.setReuseAddress(true);
                            datagramSocket.setBroadcast(true);
                            datagramSocket.bind(new InetSocketAddress(constant.EXTRAORDINARY_MEETING_PORT));


                            DatagramPacket datagramPacket = new DatagramPacket(bytes,
                                    bytes.length);
                            while (true) {
                                String strMsg = null;
                                int p = datagramPacket.getPort();
                                // ??????????????????
                                try {
                                    // datagramSocket.setSoTimeout(20000);
                                    datagramSocket.receive(datagramPacket);
                                    strMsg = new String(datagramPacket.getData(), 0, datagramPacket.getData().length, StandardCharsets.UTF_8);
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
                                        Log.e("5555555", "??????????????????");
                                        socketShareFileManager.SendFlag("1");
                                    } else if (strMsg.contains(constant.TEMP_MEETINGPUSH_FILE)) {
                                        /*
                                         * ??????????????????
                                         * */
                                        Log.d("SendFlag5555555", "??????????????????");
                                        socketShareFileManager.SendFlag("2");
                                    } else if (strMsg.contains(constant.TEMP_VOTE_IMAGE_FILE)) {
                                        /*
                                         * ????????????
                                         * */
                                        socketShareFileManager.SendFlag("3");

                                    } else if (strMsg.contains(constant.FINISH_SHARE_SCEEN)) {
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
                        Log.d("gsfgdgg", msg);
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
        isReuse();

        UDPClientManager.getInstance().receiveUDP(new OnUdpConnectListener() {
            @Override
            public void udpConnectSuccess(String ip) {
                // ????????????????????????tcp ??????
                Log.e(TAG, "????????????tcp ?????? ip===" + ip);

                mIp = ip;
                MeetingAPP.getInstance().reinitSocket(mIp);
                MeetingAPP.getInstance().getNettyClient().setListener(WuHuActivity3.this);

                if (!MeetingAPP.getInstance().getNettyClient().getConnectStatus()) {
                    MeetingAPP.getInstance().getNettyClient().connect();
                }
            }

            @Override
            public void udpDisConnec(String message) {
            }
        });
    }

    private void sendIp() {
        /*
         * ???????????????????????????????????????Ip?????????????????????
         * */
        selfIp = getNetworkType();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);//??????3
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        String str = constant.SHARE_FILE_IP + getIpAddressString();
                        Hawk.put("SelfIpAddress", selfIp);//??????Ip
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
            }
        }).start();
    }

    /*
     * ????????????????????????????????????IP??????
     * */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    /*
     * ????????????????????????????????????IP??????
     * */
    public String getIpAddress() {
        WifiManager wifiManager = (WifiManager) WuHuActivity3.this.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }

    private void initiaServerData() {

        if (UserUtil.isNetDATA) {
            filesList.clear();
            dowLoadNum = 0;
            showFileTransferDialog();
            List<WuHuEditBean.EditListBean> wb = new ArrayList<>();
            //?????????????????????
            List<WuHuMeetingListResponse.ContentDTO.VoteListBeanDTO.DataDTO> voteList = new ArrayList<>();

            WuHuMeetingListResponse wuHuMeetingListResponse;
            if (Hawk.contains("WuHuMeetingListResponse")) {
                failList.clear();//?????????????????????url
                wuHuMeetingListResponse = Hawk.get("WuHuMeetingListResponse");
                WuHuMeetingListResponse.ContentDTO contentDTO = wuHuMeetingListResponse.getContent();
                if (contentDTO == null || contentDTO.getEditListBeanList() == null || contentDTO.getEditListBeanList().size() < 1) {
                    Toast.makeText(WuHuActivity3.this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (wuHuMeetingListResponse.getName() != null) {
                    Hawk.put("company_name", wuHuMeetingListResponse.getName());
                }
                // ?????????????????? SignFilePath editean???????????????
                if (wuHuMeetingListResponse.getContent() != null && wuHuMeetingListResponse.getContent().getSignFilePath() != null) {
                    Hawk.put(constant.SignFilePath, wuHuMeetingListResponse.getContent().getSignFilePath());
                }

                if (contentDTO.getTopic_type() != null) {
                    String[] strings = contentDTO.getTopic_type().split(" ");
                    if (strings.length > 0 && strings != null) {
                        StringBuilder sb = new StringBuilder();
                        String str = "";
                        for (int i = 0; i < strings.length; i++) {
                            sb.append(strings[i]).append("\n");
                        }
                        str = sb.substring(0, sb.length() - 1);
                        Hawk.put("tittle2", str);

                    }
                    {
                        Hawk.put("tittle2", contentDTO.getTopic_type());

                    }

                }
                UserUtil.meeting_record_id = wuHuMeetingListResponse.get_id();
                Log.d("coming_id", UserUtil.meeting_record_id);
                if (wuHuMeetingListResponse.getContent().getVoteListBean() != null) {
                    if (wuHuMeetingListResponse.getContent().getVoteListBean().getData() != null && wuHuMeetingListResponse.getContent().getVoteListBean().getData().size() > 0) {
                        voteList.addAll(wuHuMeetingListResponse.getContent().getVoteListBean().getData());
                    }
//gtgwrtwwrtwt?????????????????????--
                }

                List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO> netEditListBeanList = new ArrayList<>();//????????????
                netEditListBeanList.clear();
                netEditListBeanList.addAll(contentDTO.getEditListBeanList());
                for (int i = 0; i < netEditListBeanList.size(); i++) {
                    WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanListDTO = netEditListBeanList.get(i);//??????????????????
                    List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO.FileListBeanListDTO> netLocaFiles = new ArrayList<>();//?????????????????????????????????
                    if (editListBeanListDTO.getParticipantUnits().equals("aa") && editListBeanListDTO.getReportingUnit().equals("aa")) {
                        isAdd++;
                    }
                    netLocaFiles.clear();
                    if (editListBeanListDTO.getFileListBeanList() != null && editListBeanListDTO.getFileListBeanList().size() > 0) {
                        netLocaFiles.addAll(editListBeanListDTO.getFileListBeanList());
                        for (int k = 0; k < netLocaFiles.size(); k++) {
                            WuHuNetWorkBean wuHuNetWorkBean = new WuHuNetWorkBean();
                            wuHuNetWorkBean.setUrl(UrlConstant.baseUrl + "/" + netLocaFiles.get(k).getPath());
                            Log.d("coming_id2222", UrlConstant.baseUrl + "/" + netLocaFiles.get(k).getPath());
                            wuHuNetWorkBean.setName(netLocaFiles.get(k).getName());
                            wuHuNetWorkBean.setPos(netLocaFiles.get(k).getPos());
                            wuHuNetWorkBean.setServicePath(netLocaFiles.get(k).getPath());
                            allFileNum++;
                            filesList.add(wuHuNetWorkBean);
                        }
                    }
                }
                if (filesList.size() > 0) {

                    for (int a = 0; a < filesList.size(); a++) {
                        filesList.get(a).setPos((Integer.valueOf(filesList.get(a).getPos()) + 1) + "");

                    }
                    //???????????????????????????????????????????????????
                    for (int k = 0; k < filesList.size() - 1; k++) {
                        for (int j = filesList.size() - 1; j > k; j--) {
                            if (filesList.get(k).getName().equals(filesList.get(j).getName())) {
                                filesList.get(k).setName(filesList.get(k).getPos() + filesList.get(k).getName());
                                filesList.get(j).setName(filesList.get(j).getPos() + filesList.get(j).getName());
                            }
                        }
                    }
                }
                if (filesList.size() < 1) {
                    if (networkFileDialog != null) {
                        networkFileDialog.dismiss();
                    }

                } else {


                    //????????????????????????
                    for (int i = 0; i < filesList.size(); i++) {
                        WuHuNetWorkBean wuHuNetWorkBean = filesList.get(i);
                        DownloadUtil.get().download(wuHuNetWorkBean.getUrl(), netFilePath, wuHuNetWorkBean.getName(), new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                dowLoadNum++;

                                Log.d("dffasfsdfafafdowLoadNum11", dowLoadNum + "    " + filesList.size());
                                Log.d("gtgwrtwwrtwt?????????????????????----00", dowLoadNum + "    ???????????? " + file.getName() + "   ?????????????????? " + Formatter.formatFileSize(WuHuActivity3.this, file.length()));
                                // completeDownload();
                                if (dowLoadNum == filesList.size()) {
                                    if (networkFileDialog != null) {
                                        networkFileDialog.dismiss();
                                    }
                                }

                            }

                            @Override
                            public void onDownloading(int progress) {
                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                dowLoadNum++;
                                Log.d("dffasfsdfafafdowLoadNum222", dowLoadNum + "    " + filesList.size());
                                failList.add(wuHuNetWorkBean);
                                if (dowLoadNum == filesList.size()) {
                                    if (networkFileDialog != null) {
                                        networkFileDialog.dismiss();
                                    }
                                }
                                //   completeDownload();
                            }
                        });

                    }
                }
                Log.d("paichayuanyin", "?????????????????????????????????");

                if (isAdd == 0) {
                    //??????0??????????????????
                    WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanDTO = new WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO();
                    editListBeanDTO.setParticipantUnits("aa");
                    editListBeanDTO.setReportingUnit("aa");
                    editListBeanDTO.setSubTopics("aa");
                    contentDTO.getEditListBeanList().add(0, editListBeanDTO);
                }
                isAdd = 0;
                wb.clear();
                WuHuEditBean wuHuEditBean = new WuHuEditBean();
                Hawk.put("WuHuFragmentData", wuHuEditBean);
                if (Hawk.contains("WuHuFragmentData")) {
                    //???????????????????????????  ????????????????????????
                    wuHuEditBean.set_id(wuHuMeetingListResponse.get_id());
                    if (wuHuMeetingListResponse.getName() != null) {
                        wuHuEditBean.setTopics(wuHuMeetingListResponse.getName());
                    }

                    wuHuEditBean.setTopic_type(contentDTO.getTopic_type());
                    wuHuEditBean.setSignFilePath(contentDTO.getSignFilePath());
                    wuHuEditBean.setLine_color("1");
                    wuHuEditBean.setThem_color("2");
                    List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO> editListBeanListDTOS = new ArrayList<>();
                    editListBeanListDTOS.clear();
                    if (contentDTO.getEditListBeanList().size() < 1 || contentDTO.getEditListBeanList() == null) {
                        Toast.makeText(WuHuActivity3.this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //??????????????????
                    editListBeanListDTOS.addAll(contentDTO.getEditListBeanList());
                    for (int i = 0; i < editListBeanListDTOS.size(); i++) {
                        WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanListDTO = editListBeanListDTOS.get(i);
                        //???????????????????????????
                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setLine_color("1");
                        editListBean.setPos(i + "");
                        editListBean.setThem_color("2");
                        editListBean.setParticipantUnits(editListBeanListDTO.getParticipantUnits());
                        editListBean.setReportingUnit(editListBeanListDTO.getReportingUnit());
                        editListBean.setSubTopics(editListBeanListDTO.getSubTopics());
                        //????????????????????????????????????????????????????????????
                        List<WuHuEditBean.EditListBean.FileListBean> editFiles = new ArrayList<>();
                        editFiles.clear();
                        if (editListBeanListDTO.getFileListBeanList() != null && editListBeanListDTO.getFileListBeanList().size() > 0) {

                            for (int k = 0; k < editListBeanListDTO.getFileListBeanList().size(); k++) {
                                WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO.FileListBeanListDTO localFilesDTO = editListBeanListDTO.getFileListBeanList().get(k);
                                Log.d("gfdfddhdh????????????????????????    ", localFilesDTO.getName() + "   " + localFilesDTO.getPath());
                                WuHuEditBean.EditListBean.FileListBean fileListBean = new WuHuEditBean.EditListBean.FileListBean(localFilesDTO.getName(), localFilesDTO.getPath(), "", "");
                                fileListBean.setPos((Integer.valueOf(localFilesDTO.getPos()) + 1) + "");
                                String endStr = localFilesDTO.getName().substring(localFilesDTO.getName().lastIndexOf(".") + 1);
                                fileListBean.setResImage(getIamge(endStr));
                                fileListBean.setFile_type(getType(endStr));
                                fileListBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                                fileListBean.setType(getFileType(endStr));
                                // fileListBean.setNet(localFilesDTO.getNet());
                                if (filesList.size() > 0 && filesList != null) {
                                    for (int m = 0; m < filesList.size(); m++) {
                                       // Log.d("srtetret=444", filesList.get(m).getPos() + "   " + fileListBean.getPos() + "     " + filesList.get(m).getServicePath() + "   " + localFilesDTO.getPath());
                                        if (filesList.get(m).getPos().equals(fileListBean.getPos()) && filesList.get(m).getServicePath().equals(localFilesDTO.getPath())) {
                                            fileListBean.setName(filesList.get(m).getName());
                                            fileListBean.setLocalPath(netFilePath + filesList.get(m).getName());
                                          //  Log.d("srtetret=333", filesList.get(m).getName());
                                        }

                                    }
                                }
                                fileListBean.setNet(true);
                                editFiles.add(fileListBean);
                            }
                        }


                        for (int k = 0; k < editFiles.size(); k++) {
                            Log.d("srtetret=2222", editFiles.get(k).getName());
                        }
                        editListBean.setLocalFiles(editFiles);
                        wb.add(editListBean);
                    }
                    //???????????????service???
                    wuHuEditBean.setEditListBeanList(wb);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() < 1) {
                        Toast.makeText(WuHuActivity3.this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                    }
                  /*  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                        }
                    }).start();*/

                    Log.d("paichayuanyin", "????????????????????????????????????");
                    //???????????????
                    for (int i = 0; i < voteList.size(); i++) {
                        WuHuMeetingListResponse.ContentDTO.VoteListBeanDTO.DataDTO dataDTO = voteList.get(i);
                        List<WuHuMeetingListResponse.ContentDTO.VoteListBeanDTO.DataDTO.TemporBeanListDTO> temporBeanListDTOS = new ArrayList<>();
                        temporBeanListDTOS.addAll(dataDTO.getTemporBeanList());
                        ArrayList<VoteListBean.VoteBean.TemporBean> temporBeanArrayList = new ArrayList();
                        ArrayList<String> options_list = new ArrayList();
                        temporBeanArrayList.clear();
                        options_list.clear();
                        if (temporBeanListDTOS != null && temporBeanListDTOS.size() > 0) {

                            for (int K = 0; K < temporBeanListDTOS.size(); K++) {
                                if (temporBeanListDTOS.get(k).getContent() != null) {
                              /*      if (temporBeanListDTOS.get(k).getContent().contains("jpg") || temporBeanListDTOS.get(k).getContent().contains("gif") || temporBeanListDTOS.get(k).getContent().contains("png") || temporBeanListDTOS.get(k).getContent().contains("jpeg") ||
                                            temporBeanListDTOS.get(k).getContent().contains("bmp")) {
                                        String endStr = temporBeanListDTOS.get(k).getContent().substring(temporBeanListDTOS.get(k).getContent().lastIndexOf(".") + 1);
                                        if (endStr.equals("jpg") || endStr.equals("gif") || endStr.equals("png") ||
                                                endStr.equals("jpeg") || endStr.equals("bmp")) {
                                            String[] strConten = temporBeanListDTOS.get(k).getContent().split("/");
                                            String fileName = "";
                                            if (strConten.length > 0) {
                                                fileName = strConten[strConten.length - 1];
                                                //?????????????????????  ????????????  ??????????????????????????????
                                                if (UserUtil.ISCHAIRMAN) {
                                                    options_list.add(temporBeanListDTOS.get(k).getContent());
                                                    //??????????????????????????????????????????
                                                } else {
                                                    options_list.add(VOTE_FILE + fileName);

                                                }
                                            }


                                        }

                                    } else {

                                        options_list.add(temporBeanListDTOS.get(k).getContent());
                                    }*/
                                    options_list.add(temporBeanListDTOS.get(k).getContent());

                                }

                                VoteListBean.VoteBean.TemporBean temporBean = new VoteListBean.VoteBean.TemporBean();
                                if (temporBeanListDTOS.get(k).getChecked() != null) {
                                    temporBean.setChecked(temporBeanListDTOS.get(k).getChecked());

                                }

                                if (temporBeanListDTOS.get(k).getContent() != null) {
                               /*     if (temporBeanListDTOS.get(k).getContent().contains("jpg") || temporBeanListDTOS.get(k).getContent().contains("gif") || temporBeanListDTOS.get(k).getContent().contains("png") || temporBeanListDTOS.get(k).getContent().contains("jpeg") ||
                                            temporBeanListDTOS.get(k).getContent().contains("bmp")) {
                                        String endStr = temporBeanListDTOS.get(k).getContent().substring(temporBeanListDTOS.get(k).getContent().lastIndexOf(".") + 1);
                                        if (endStr.equals("jpg") || endStr.equals("gif") || endStr.equals("png") ||
                                                endStr.equals("jpeg") || endStr.equals("bmp")) {
                                            String[] strConten = temporBeanListDTOS.get(k).getContent().split("/");
                                            String fileName = "";
                                            if (strConten.length > 0) {
                                                fileName = strConten[strConten.length - 1];
                                                //?????????????????????  ????????????  ??????????????????????????????
                                                if (UserUtil.ISCHAIRMAN) {
                                                    temporBean.setContent(temporBeanListDTOS.get(k).getContent());
                                                    //??????????????????????????????????????????
                                                } else {
                                                    temporBean.setContent(VOTE_FILE + fileName);
                                                }
                                            }
                                        }

                                    } else {
                                        temporBean.setContent(temporBeanListDTOS.get(k).getContent());
                                    }*/
                                    temporBean.setContent(temporBeanListDTOS.get(k).getContent());
                                }

                                if (temporBeanListDTOS.get(k).getOrderNumb() != null) {

                                    temporBean.setOrderNumb(temporBeanListDTOS.get(k).getOrderNumb());
                                }

                                temporBeanArrayList.add(temporBean);

                            }
                        }

                        VoteListBean.VoteBean voteBean = new VoteListBean.VoteBean();
                        VoteListBean.VoteBean.FromBean fromBean = new VoteListBean.VoteBean.FromBean();
                        fromBean.setName(UserUtil.user_name);
                        fromBean.set_id(FLUtil.getMacAddress());
                        voteBean.setTemporBeanList(temporBeanArrayList);
                        voteBean.setTopic(dataDTO.getTopic());
                        voteBean.setOptions(options_list);
                        voteBean.setTemporBeanList(temporBeanArrayList);
                        voteBean.setEnd_time("2222222");
                        voteBean.setType(dataDTO.getType());
                        voteBean.setAnonymity(dataDTO.getAnonymity());
                        voteBean.setFrom(fromBean);
                        if (StringUtils.isEmpty(dataDTO.getFlag() + "")) {
                            voteBean.setFlag("1");
                        } else {
                            voteBean.setFlag(dataDTO.getFlag() + "");
                        }

                        // ??????????????????????????????
                        voteBean.setUser_list(new ArrayList<>());
                        voteBean.setStatus("ENABLE");
                        voteBean.setStatus(dataDTO.getStatus());
                        if (StringUtils.isEmpty(dataDTO.getFlag() + "")) {
                            creatVote(voteBean, "1");
                        } else {
                            creatVote(voteBean, dataDTO.getFlag() + "");
                        }

                    }

                }
            }
            //====
        } else {
            if (Hawk.contains("isreuse")) {
                String isreuse = Hawk.get("isreuse");
                //1:??????????????????  2???????????????????????? 3?????????????????????
                if (isreuse.equals("2") || isreuse.equals("3")) {
                    wuHuEditBeanList.clear();
                    WuHuEditBean wuHuEditBean = new WuHuEditBean();
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    if (Hawk.contains("WuHuFragmentData")) {
                        wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics("?????????????????????");
                        wuHuEditBean.setTopic_type("????????????");

                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setSubTopics("aa");
                        editListBean.setReportingUnit("aa");
                        editListBean.setParticipantUnits("aa");
                        editListBean.setTopics("?????????????????????");
                        editListBean.setPos("0");
                        editListBean.setTopic_type("????????????");
                        wuHuEditBeanList.add(editListBean);
                        WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                        editListBean1.setSubTopics("??????2022???");
                        editListBean1.setReportingUnit("??????????????????");
                        editListBean1.setParticipantUnits("??????????????????");
                        editListBean1.setTopics("?????????????????????");
                        editListBean1.setTopic_type("????????????");
                        editListBean1.setPos("1");
                        wuHuEditBeanList.add(editListBean1);

                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                    }
                } else if (isreuse.equals("1")) {
                    WuHuEditBean templetEuHuEditBean = null;
                    if (Hawk.contains("WuHuFragmentData")) {
                        //??????????????????????????????
                        templetEuHuEditBean = Hawk.get("WuHuFragmentData");
                        wsUpdata(templetEuHuEditBean, constant.SUBMITANISSUE);
                    }

                } else {
                    //???????????????????????????  ?????????????????????
                    WuHuEditBean wuHuEditBean = new WuHuEditBean();
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    if (Hawk.contains("WuHuFragmentData")) {
                        wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics("?????????????????????");
                        wuHuEditBean.setTopic_type("????????????");
                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setSubTopics("aa");
                        editListBean.setReportingUnit("aa");
                        editListBean.setParticipantUnits("aa");
                        editListBean.setTopics("?????????????????????");
                        editListBean.setPos("0");
                        editListBean.setTopic_type("????????????");
                        wuHuEditBeanList.add(editListBean);
                        WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                        editListBean1.setSubTopics("??????2022???");
                        editListBean1.setReportingUnit("??????????????????");
                        editListBean1.setParticipantUnits("??????????????????");
                        editListBean1.setTopics("?????????????????????");
                        editListBean1.setTopic_type("????????????");
                        editListBean1.setPos("1");
                        wuHuEditBeanList.add(editListBean1);
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);

                    }

                }

            }
        }
    }

    private void completeDownload() {
        //??????????????????  ???????????????  ????????????  ??????????????????
        if (dowLoadNum == filesList.size()) {
            if (networkFileDialog != null) {
                networkFileDialog.dismiss();
            }
        }
    }

    private void creatVote(VoteListBean.VoteBean bean, String flag) {
        if (UserUtil.isTempMeeting) {
            wsUpdataVote(bean, constant.NEWVOTE, flag);
            //????????????????????????????????????

        }
    }

    /**
     * websocket??????????????????
     */
    private void wsUpdataVote(Object obj, String packType, String flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("gdgsdgsdgdgf444666", flag + "");
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setFlag(flag);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(packType);
                bean.setBody(obj);
                String strJson = new Gson().toJson(bean);
                JWebSocketClientService.sendMsg(strJson);
            }
        }).start();

    }

    //??????????????????????????????
    public void showUpLoadFileDialog() {
        //?????????dialog????????????
        inflate = LayoutInflater.from(WuHuActivity3.this).inflate(R.layout.wuhu_netfile_progress_dialog, null);
        //?????????dialog????????????
        dialog = new MyDialog(WuHuActivity3.this, R.style.dialogTransparent);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        dialog.setCanceledOnTouchOutside(false);
        tips = inflate.findViewById(R.id.tips);
        tips.setText("???????????????????????????????????????");
        //??????????????????Dialog
        dialog.setContentView(inflate);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //???????????????????????????
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //??????
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //???????????????
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

      /*  completedView = inflate.findViewById(R.id.tasks_view);
        result_ima = inflate.findViewById(R.id.result_ima);
        completedView.setVisibility(View.VISIBLE);*/
        // result_ima.setVisibility(View.GONE);
        //????????????Activity???????????????
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//?????????
        wlp.height = (int) (height * 0.25);
        window.setAttributes(wlp);
        dialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {
                Log.d("sdfsdfdsff", "??????~~~~~");
                //  Toast.makeText(getActivity(),"??????",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wuhu_main;
    }

    @Override
    protected void initView() {
        foodView = LayoutInflater.from(WuHuActivity3.this).inflate(R.layout.list_food, null);
        Log.d("wrewarawrawer", "initView");
        handler = new Handler();
        if (Hawk.contains(constant.myNumber)) {
            String n = Hawk.get(constant.myNumber);
            name.setText(n);
        }

        if (Hawk.contains(constant.user_name)) {
            String n = Hawk.get(constant.user_name);
            name.setText(n);
        }


        meetingTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA13);//??????-????????????
        Calendar c1 = Calendar.getInstance();
        int day = c1.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                week = "?????????";
                break;
            case 2:
                week = "?????????";
                break;
            case 3:
                week = "?????????";
                break;
            case 4:
                week = "?????????";
                break;
            case 5:
                week = "?????????";
                break;
            case 6:
                week = "?????????";
                break;
            case 7:
                week = "?????????";
                break;
        }
        timeTv.setText(meetingTime + " " + week);
        runnable = new Runnable() {
            @Override
            public void run() {
                meetingTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA13);//??????-????????????
                timeTv.setText(meetingTime + " " + week);
                handler.postDelayed(this, 50);// 50???????????????
            }
        };

        handler.postDelayed(runnable, 1000 * 60);// ??????????????????????????????


    }

    private void isReuse() {

        edit_rl.setOnClickListener(this);
        edit_ll.setOnClickListener(this);
        comfirm.setOnClickListener(this);
        vote_ll.setOnClickListener(this);
        consult_ll.setOnClickListener(this);
        finish_ll.setOnClickListener(this);
        left_rl.setOnClickListener(this);
        rigth_rl.setOnClickListener(this);
        consult_ll1.setOnClickListener(this);
        vote_ll1.setOnClickListener(this);
        home_ll1.setOnClickListener(this);
        home_ll.setOnClickListener(this);
        shareScreen_rl.setOnClickListener(this);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnAdd = findViewById(R.id.btn_add);
        edit_name_rl.setVisibility(View.GONE);

        left_rl.setVisibility(View.GONE);
        rigth_rl.setVisibility(View.GONE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos = position;
                if (mTestFragments.size() > 1) {
                    if (mCurPos == 0) {
                        left_rl.setVisibility(View.GONE);
                        rigth_rl.setVisibility(View.VISIBLE);

                    } else if (mCurPos == (mTestFragments.size() - 1)) {

                        left_rl.setVisibility(View.VISIBLE);
                        rigth_rl.setVisibility(View.GONE);
                    } else {
                        left_rl.setVisibility(View.VISIBLE);
                        rigth_rl.setVisibility(View.VISIBLE);

                    }
                }
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
                Bundle bundle = new Bundle();
                bundle.putString("aa", "aa");
                intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);

            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.consult_ll:
                intent = new Intent(WuHuActivity3.this, SignListActivity.class);
                startActivity(intent);

                break;
            case R.id.edit_rl:
                if (!isEditRl) {
                    edit_name_rl.setVisibility(View.VISIBLE);
                    isEditRl = true;
                } else {
                    edit_name_rl.setVisibility(View.GONE);
                    isEditRl = false;
                }

                break;
            case R.id.edit_ll:
                showRightDialog();
                break;
            case R.id.comfirm:
                edit_name_rl.setVisibility(View.GONE);
                if (edit_name.getText().toString().isEmpty()) {
                    Toast.makeText(WuHuActivity3.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                Hawk.put(constant.user_name, edit_name.getText().toString());
                UserUtil.user_name = edit_name.getText().toString();
                name.setText(edit_name.getText().toString());

                break;
            case R.id.vote_ll:
                intent = new Intent(WuHuActivity3.this, WuHuVoteActivity.class);
                startActivity(intent);
                break;
            case R.id.finish_ll:
                showFinishMeetingDialog();
                break;
            case R.id.vote_ll1:
                intent = new Intent(WuHuActivity3.this, WuHuVoteActivity.class);
                startActivity(intent);
                break;
            case R.id.consult_ll1:
                intent = new Intent(WuHuActivity3.this, SignListActivity.class);
                startActivity(intent);
                break;
            case R.id.rigth_rl:

                int totalcount = mTestFragments.size();//autoChangeViewPager.getChildCount();
                int currentItem1 = mViewPager.getCurrentItem();
                currentItem1 = currentItem1 + 1;
                mViewPager.setCurrentItem(currentItem1, true);


                break;
            case R.id.left_rl:
                int currentItem2 = mViewPager.getCurrentItem();
                currentItem2 = currentItem2 - 1;
                mViewPager.setCurrentItem(currentItem2, true);
                break;
            case R.id.shareScreen_ll1:
                if (UserUtil.isShareScreen) {
                    CVIPaperDialogUtils.showCustomDialog(WuHuActivity3.this, "???????????????????", "", "??????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                stopRecording();
                                shareScreen_tv.setText("pc??????");
                            }
                        }
                    });
                } else {
                    alertShareScreen();
                }
                break;
            case R.id.home_ll1:
                mViewPager.setCurrentItem(0);
                saveAllData();
                break;
            case R.id.home_ll:
                mViewPager.setCurrentItem(0);
                saveAllData();
                break;
        }

    }

    /******************************************????????????************************************************************/
    /**
     * ????????????
     */
    private void alertShareScreen() {
        shareScreenDialog = new Dialog(WuHuActivity3.this, R.style.update_dialog);
        View view = LayoutInflater.from(WuHuActivity3.this).inflate(R.layout.diaog_sharescreen_code, null);//?????????????????????
        shareScreen_codeview = view.findViewById(R.id.verificationcodeview);
        shareScreen_codeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {

            }

            @Override
            public void onComplete(View view, String content) {
                if (view == shareScreen_codeview) {
                    closeKeyboardHidden(WuHuActivity3.this);
                    shareScreenDialog.dismiss();
                    my_code = content;
                    Log.e("WuHuActivity", "content===" + content);

                    if (StringUtil.isNullOrEmpty(my_code)) {
                        ToastUtils.showShort("???????????????!");
                        return;
                    }
                    UDPClientManager.getInstance().removeUDPBroastcast();
                    UDPClientManager.getInstance().sendUDPWithCode(my_code);
                }
            }
        });
        ImageView imageView = view.findViewById(R.id.clear_ima);
        ImageView close_img = view.findViewById(R.id.ima_close);
        shareScreenDialog.setContentView(view);
        shareScreenDialog.setCancelable(false);
        shareScreenDialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScreen_codeview.setEmpty();
            }
        });
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareScreenDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_CODE_SCREEN ) {
            if (resultCode == Activity.RESULT_OK) {
                if (mMediaProjectionManage == null) {
                    mMediaProjectionManage = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                }
                mediaProjection = mMediaProjectionManage.getMediaProjection(resultCode, data);
                startRecord();
            }else {
                if(MeetingAPP.getInstance().getNettyClient() != null){
                    MeetingAPP.getInstance().getNettyClient().disconnect();
                }

                CVIPaperDialogUtils.showConfirmDialog(WuHuActivity3.this, "??????????????????????????????????????????!", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                    @Override
                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {

                    }
                });
            }


        }
    }

    private void startRecord() {
        UserUtil.isShareScreen = true;
        shareScreen_tv.setText("????????????");
        Intent intent = new Intent(this, ScreenImageService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        mConnect = new MyServiceConnect();
        bindService(intent, mConnect, Context.BIND_AUTO_CREATE);
//        checkIgnoreBattery();
    }

    //TCP ????????????
    @Override
    public void onMessageReaderTimeout() {
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onMessageResponse(final ReceiveData data) {
        Log.e(TAG, "?????????????????????===" + data.getHeader().getMainCmd());
        switch (data.getHeader().getMainCmd()) {
            case SocketCmd.SocketCmd_RepAccept:
//                MeetingAPP.mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
                // TODO ????????????????????? ????????????startActivityForResult???WuHuActivity??????????????????login????????????????????????????????????????????????????????????????????????
                mMediaProjectionManage = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                Intent captureIntent = mMediaProjectionManage.createScreenCaptureIntent();
                Activity top = ActivityUtils.getTopActivity();
                if (top != null && top.getLocalClassName().contains("WuHuActivity")) {
                    top.startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE_SCREEN);
                }
//                    }
//                });
                break;
            case SocketCmd.SocketCmd_RepReject_001:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("????????????!!!");
                    }
                });
                break;
        }
    }

    //??????????????????
    @Override
    public void onServiceStatusConnectChanged(final int statusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (statusCode == NettyListener.STATUS_CONNECT_SUCCESS) {
                    Log.e(TAG, "STATUS_CONNECT_SUCCESS:");
                    if (MeetingAPP.getInstance().getNettyClient().getConnectStatus()) {
                        ToastUtils.showShort("????????????????????????");
                        sendStartData();
                    }
                } else {
//                    ToastUtils.showShort("????????????");
                    Log.e(TAG, "onServiceStatusConnectChanged:" + statusCode);
                    stopRecording();

                }
            }
        });
    }

    /**
     * ??????????????????
     */
    private void sendStartData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EncodeV1 encodeV1 = new EncodeV1(SocketCmd.SocketCmd_ReqReceiveScreen, new byte[0]);
                MeetingAPP.getInstance().getNettyClient().sendMsgToServer(encodeV1.buildSendContent(), new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {                //4
                            Log.d(TAG, "sendStartData  successful");
                        } else {
                            Log.d(TAG, "Write auth error");
                        }
                    }
                });
            }
        }).start();


    }

    /**
     * ????????????
     */
    private void stopRecording() {
        if(shareScreen_tv!=null){
            shareScreen_tv.setText("pc??????");
        }

        try {
            unbindService(mConnect);
        } catch (Exception e) {

        }
        UserUtil.isShareScreen = false;
//        ToastUtils.showShort("???????????????");
    }

    class MyServiceConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ((ScreenImageService.ScreenImageBinder) binder).getService().startController(mediaProjection);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * ???????????????
     **/
    private static void closeKeyboardHidden(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //??????????????????
    @Override
    public void saveData(int position) {
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBean.setTopics(company_name.getText().toString());
            wuHuEditBean.setTopic_type(tittle2.getText().toString());
            wuHuEditBean.setLine_color(lineFlag);
            wuHuEditBean.setThem_color(themFlag);
            wuHuEditBean.setPosition(position + "");
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                wuHuEditBeanList.get(i).setTopics(company_name.getText().toString());
                wuHuEditBeanList.get(i).setTopic_type(tittle2.getText().toString());
                wuHuEditBeanList.get(i).setLine_color(lineFlag);
                wuHuEditBeanList.get(i).setThem_color(themFlag);
            }

            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
            Hawk.put("WuHuFragmentData", wuHuEditBean);

            //??????????????????
            new Thread(new Runnable() {
                @Override
                public void run() {
                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUSIGLEDATA);
                }
            }).start();

        }
        if (company_name != null && company_name.getText().toString() != null) {

            Hawk.put("company_name", company_name.getText().toString());
        }
        if (tittle2 != null && tittle2.getText().toString() != null) {

            Hawk.put("tittle2", tittle2.getText().toString());
        }
        Toast.makeText(WuHuActivity3.this, "????????????", Toast.LENGTH_SHORT).show();

    }


    //?????????????????????????????????????????????????????????
    private void saveAllData() {
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
       /*     wuHuEditBean.setTopics(company_name.getText().toString());
            wuHuEditBean.setTopic_type(tittle2.getText().toString());
            wuHuEditBean.setLine_color(lineFlag);
            wuHuEditBean.setThem_color(themFlag);
            wuHuEditBean.setPosition(position + "");
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                wuHuEditBeanList.get(i).setTopics(company_name.getText().toString());
                wuHuEditBeanList.get(i).setTopic_type(tittle2.getText().toString());
                wuHuEditBeanList.get(i).setLine_color(lineFlag);
                wuHuEditBeanList.get(i).setThem_color(themFlag);
            }

            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);*/
            if (company_name != null && company_name.getText().toString() != null) {

                Hawk.put("company_name", company_name.getText().toString());
            }
            if (tittle2 != null && tittle2.getText().toString() != null) {

                Hawk.put("tittle2", tittle2.getText().toString());
            }


            Hawk.put("WuHuFragmentData", wuHuEditBean);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????
                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUSIGLEDATA);
                }
            }).start();

        }


    }

    @Override
    public void deletData(int position) {

        CVIPaperDialogUtils.showCustomDialog(WuHuActivity3.this, "", "???????????????????????????", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {
                    wuHuEditBeanList.remove(position);
                    wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                    wuHuListAdapter.notifyDataSetChanged();

                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setPosition(position + "");
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //??????????????????
                                wsUpdata(wuHuEditBean, constant.DELETE_WUHU_FRAGMENT);
                            }
                        }).start();

                    }

                }
            }
        });


    }

    @Override
    public void addData(int position) {

        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
        editListBean.setPos(wuHuEditBeanList.size() + "");
        editListBean.setSubTopics(wuHuEditBeanList.get(wuHuEditBeanList.size() - 1).getSubTopics());
        editListBean.setReportingUnit(wuHuEditBeanList.get(wuHuEditBeanList.size() - 1).getReportingUnit());
        editListBean.setParticipantUnits(wuHuEditBeanList.get(wuHuEditBeanList.size() - 1).getParticipantUnits());
        if (company_name != null) {
            editListBean.setTopics(company_name.getText().toString());
        }
        if (tittle2 != null) {
            editListBean.setTopic_type(tittle2.getText().toString());
        }
        wuHuEditBeanList.add(editListBean);
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
            Hawk.put("WuHuFragmentData", wuHuEditBean);
            //????????????????????????fragment
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????
                    wsUpdata(wuHuEditBean, constant.WUHUADDFRAGMENT);
                }
            }).start();

        }
    }

    //????????????
    private void showFinishMeetingDialog() {
        Log.d("Broadcast111", "UrlConstant.baseUrl= " + UrlConstant.baseUrl);
        String str = "";
        if (UserUtil.ISCHAIRMAN) {
            str = "????????????????????????";

        } else {
            str = "????????????????????????";

        }
        CVIPaperDialogUtils.showCustomDialog(WuHuActivity3.this, str, "?????????/?????????????????????!!!", "??????", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {
                    stopRecording();
                    if (UserUtil.isTempMeeting) {
                        Log.d("Broadcast22", "UrlConstant.baseUrl= " + UrlConstant.baseUrl);
                        if (UrlConstant.baseUrl.equals("http://192.168.1.1:3006")) {
                            Toast.makeText(WuHuActivity3.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                            UserUtil.user_id = "";
                            UserUtil.meeting_record_id = "";
                            if (Hawk.contains(constant._id)) {
                                Hawk.delete(constant._id);
                            }
                            if (Hawk.contains(constant.user_id)) {
                                Hawk.delete(constant.user_id);
                            }
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
                            //1.?????????????????????????????????2.???????????????????????????
                            wuHuFinishMeeting();

                        }



               /*         if (Hawk.contains(constant._id)) {
                            String _id = Hawk.contains(constant._id) ? Hawk.get(constant._id) : "";
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", _id);
                            //??????????????????
                            NetWorkManager.getInstance().getNetWorkApiService().finishWUHUMeeting(map).compose(WuHuActivity.this.<BasicResponse>bindToLifecycle())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultObserver<BasicResponse>() {
                                        @Override
                                        protected void onSuccess(BasicResponse response) {
                                            if (response != null) {

                                                UserUtil.user_id = "";
                                                UserUtil.meeting_record_id = "";
                                                if (Hawk.contains(constant._id)) {
                                                    Hawk.delete(constant._id);
                                                }
                                                if (Hawk.contains(constant.user_id)) {
                                                    Hawk.delete(constant.user_id);
                                                }
                                                finish();
                                            }
                                        }
                                    });
                        }*/
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {

                        }


                    } else {
                        String _id = UserUtil.meeting_record_id;
                        String c_id = Hawk.contains(constant.c_id) ? Hawk.get(constant.c_id) : "";
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", _id);
                        map.put("status", "FINISH");
                        map.put("c_id", c_id);
                        //??????????????????
                        NetWorkManager.getInstance().getNetWorkApiService().finishMeeting(map).compose(WuHuActivity3.this.bindToLifecycle())
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

    private void wuHuFinishMeeting() {
        showUpLoadFileDialog();
        WuHuEditBean wuHuEditBean = null;
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBean = Hawk.get("WuHuFragmentData");
            String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//??????-???
            wuHuEditBean.setStartTime(data);
            WuHuEditBeanRequset wuHuEditBeanRequset = new WuHuEditBeanRequset();
            //?????????????????????????????????
            if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() < 0) {
                Log.d("Valueisempty4444", "wuHuFinishMeeting??????   ");
                if (dialog != null) {
                    dialog.dismiss();
                }
                //???????????????????????????
                if (Hawk.contains("WuHuFragmentData")) {
                    Hawk.delete("WuHuFragmentData");
                }
                if (Hawk.contains("VoteListBean")) {
                    Hawk.delete("VoteListBean");
                }
                UserUtil.meeting_record_id = "";
                locaFileNum = 0;
                // UrlConstant.baseUrl = "http://192.168.1.1:3006";
                // FLUtil.BroadCastIP = "192.168.00.000";

                if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                    // ????????????????????????????????????????????????
                    String code = getIntent().getStringExtra("code");
                    UDPBroadcastManager.getInstance().sendDestroyCode(code);
                    ServerManager.getInstance().StopMyWebsocketServer();
                    UDPBroadcastManager.getInstance().removeUDPBroastcast();
                }
                JWebSocketClientService.closeConnect();
                finish();
                Toast.makeText(WuHuActivity3.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Hawk.contains(constant.SignFilePath) && Hawk.get(constant.SignFilePath) != null) {
                List<String> SignFilePaths = Hawk.get(constant.SignFilePath);
                wuHuEditBean.setSignFilePath(SignFilePaths);
            }

            wuHuEditBeanRequset.setContent(wuHuEditBean);
            Log.d("coming_id_un", UserUtil.meeting_record_id);
            wuHuEditBeanRequset.setId(UserUtil.meeting_record_id);

            NetWorkManager.getInstance().getNetWorkApiService().meeting(wuHuEditBeanRequset).compose(this.bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<MeetingIdBean>>() {
                        @Override
                        protected void onFail(BasicResponse<MeetingIdBean> response) {
                            super.onFail(response);
                            Log.d("??????11111111", response.getMsg() + "   " + response.getData().toString());
                        }

                        @Override
                        protected void onSuccess(BasicResponse<MeetingIdBean> response) {
                            if (response != null) {
                                MeetingIdBean meetingIdBean = response.getData();
                                UserUtil.meeting_record_id = meetingIdBean.getId();
                                //??????????????????
                                uploadFile();

                            }
                        }
                    });
        }


    }

    private void uploadFile() {
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBeanList.clear();
            String meetingName = "";
            String meetingName3 = "";
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            //?????????????????????????????????
            if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() < 0) {
                Log.d("Valueisempty5555", "uploadFile??????   ");
                if (dialog != null) {
                    dialog.dismiss();
                }
                //???????????????????????????
                if (Hawk.contains("WuHuFragmentData")) {
                    Hawk.delete("WuHuFragmentData");
                }
                if (Hawk.contains("VoteListBean")) {
                    Hawk.delete("VoteListBean");
                }
                UserUtil.meeting_record_id = "";
                locaFileNum = 0;
                // UrlConstant.baseUrl = "http://192.168.1.1:3006";
                // FLUtil.BroadCastIP = "192.168.00.000";

                if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                    // ????????????????????????????????????????????????
                    String code = getIntent().getStringExtra("code");
                    UDPBroadcastManager.getInstance().sendDestroyCode(code);
                    ServerManager.getInstance().StopMyWebsocketServer();
                    UDPBroadcastManager.getInstance().removeUDPBroastcast();
                }
                JWebSocketClientService.closeConnect();
                finish();
                Toast.makeText(WuHuActivity3.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }

            wuHuEditBeanList.addAll(wuHuEditBean.getEditListBeanList());

            if (wuHuEditBeanList == null || wuHuEditBeanList.size() == 0) {
                return;
            }
            Log.d("fdsgdsgsdfgf1111", wuHuEditBeanList.size() + "  ????????????????????????");
            List<WuHuEditBean.EditListBean> ets = new ArrayList<>();
            ets.clear();
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);

                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getLocalFiles() != null && editListBean.getLocalFiles().size() > 0) {
                    fileListBeanList.addAll(editListBean.getLocalFiles());
                    editListBean.setFileListBeanList(fileListBeanList);//?????????????????????????????????????????????????????????

                }
                ets.add(editListBean);//??????????????????????????????????????????????????????????????????????????????
            }


            for (int y = 0; y < ets.size() - 1; y++) {
                for (int j = ets.size() - 1; j > y; j--) {

                    if (ets.get(j).getFileListBeanList() != null && ets.get(j).getFileListBeanList().size() > 0) {

                        for (int n = 0; n < ets.get(j).getFileListBeanList().size(); n++) {

                            if (ets.get(y).getFileListBeanList() != null && ets.get(y).getFileListBeanList().size() > 0) {

                                for (int m = 0; m < ets.get(y).getFileListBeanList().size(); m++) {

                                    if (ets.get(j).getFileListBeanList().get(n).getName().equals(ets.get(y).getFileListBeanList().get(m).getName())) {

                                        ets.get(j).getFileListBeanList().get(n).setName(ets.get(j).getPos() + ets.get(j).getFileListBeanList().get(n).getName());

                                        ets.get(y).getFileListBeanList().get(m).setName(ets.get(y).getPos() + ets.get(y).getFileListBeanList().get(m).getName());

                                    }


                                }


                            }


                        }


                    }
                  /*  ets.get(j).getFileListBeanList();
                    ets.get(y).getFileListBeanList();*/
                   /* if (ets.get(j).getName().equals(ets.get(y).getName())) {
                        locService.remove(j);
                    }*/
                }
            }


            wuHuEditBean.setEditListBeanList(ets);
            Hawk.put("WuHuFragmentData", wuHuEditBean);
            Log.d("fdsgdsgsdfgf1111", wuHuEditBean.getEditListBeanList().size() + "  ?????????????????????????????????????????????");

            if (Hawk.contains("company_name")) {
                meetingName = Hawk.get("company_name");

            }
            if (Hawk.contains("tittle2")) {
                meetingName3 = Hawk.get("tittle2");
            }

            for (int i = 0; i < ets.size(); i++) {
                WuHuEditBean.EditListBean editListBean = ets.get(i);
                if (meetingName != null || !meetingName.equals("")) {

                    editListBean.setTopics(meetingName);
                }
                if (meetingName3 != null || !meetingName3.equals("")) {

                    editListBean.setTopic_type(meetingName3);
                }


                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getFileListBeanList() != null && editListBean.getFileListBeanList().size() > 0) {

                    for (int k = 0; k < editListBean.getFileListBeanList().size(); k++) {
                        if (!editListBean.getFileListBeanList().get(k).isNet()) {
                            locaFileNum++;
                        }

                    }
                }

            }
            //locaFileNum==0;?????????????????????????????????????????????????????????????????????
            if (locaFileNum == 0) {
                //??????????????????  ets????????????????????????
                wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                Hawk.put("WuHuFragmentData", wuHuEditBean);
                reSetdata();
            }
     /*     if (littelFilePos < fileListBeanList.size()) {
                        WuHuEditBean.EditListBean.FileListBean fileListBean = fileListBeanList.get(littelFilePos);
                        if (!fileListBean.isNet()) {
                            //?????????????????????
                            cutfile(fileListBean.getPath(), fileListBean.getName(), fileListBean.getPos());
                        }

                    }*/
            fileFragmentationBeans.clear();
            //????????????????????????????????????
            Hawk.put("FileFragmentationBean", fileFragmentationBeans);
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);
                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getLocalFiles() != null && editListBean.getLocalFiles().size() > 0) {
                    fileListBeanList.addAll(editListBean.getLocalFiles());
                    editListBean.setFileListBeanList(fileListBeanList);//?????????????????????????????????????????????????????????
                    for (int k = 0; k < fileListBeanList.size(); k++) {

                        WuHuEditBean.EditListBean.FileListBean fileListBean = fileListBeanList.get(k);
                        if (!fileListBean.isNet()) {
                            //?????????????????????
                            Log.d("dfdssdgdsgs11111", fileListBean.getName());
                            cutfile(fileListBean.getPath(), fileListBean.getName(), fileListBean.getPos());
                        }
                    }
                }
            }

            //==================================
      /*      for (int i = 0; i < fileFragmentationBeans.size(); i++) {
                FileFragmentationBean fragmentationBean=fileFragmentationBeans.get(i);
                if (fragmentationBean.getNumberUploads() == fragmentationBean.getAllFenPianNum()) {
                    mergeShards(fragmentationBean.getPos(), fragmentationBean.getPos());//??????????????????????????????????????????????????????
                    Log.d("gtgwrtwwrtwt?????????????????????9999999","???????????? "+fragmentationBean.getFileName()+"   ?????????????????????  "+fragmentationBean.getAllFenPianNum()+"   ???????????????????????? "+fragmentationBean.getNumberUploads());
                }
            }
*/
        }

    }

    /**
     * ????????????MimeType
     *
     * @param filename ?????????
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "multipart/form-data"; //* exe,????????????????????????
        }
        return contentType;
    }

    private void cutfile(String filePath, String fileName, String pos) {

        try {
            long mBufferSize = size; //??????????????????????????????
            fileCutUtils = new FileCutUtils();
            littlefilecount = fileCutUtils.getSplitFile(new File(filePath), mBufferSize, pos);
            Log.d("gtgwrtwwrtwt?????????????????????--11", filePath);
            littlefilelist = fileCutUtils.getLittlefilelist();
            upload(fileName, pos, littlefilelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload(String fileName, String pos, List<File> filelist) {
        //????????????????????????????????????
        FileFragmentationBean fragmentationBean = new FileFragmentationBean();
        fragmentationBean.setPos(pos);
        fragmentationBean.setAllFenPianNum(filelist.size());
        fragmentationBean.setFileName(fileName);
        fragmentationBean.setNumberUploads(0);//????????????????????????0
        fileFragmentationBeans.add(fragmentationBean);
      /*  if(Hawk.contains("FileFragmentationBean")){
            List<FileFragmentationBean> fragmentationBeans=Hawk.get("FileFragmentationBean");
            fileFragmentationBeans.addAll(fragmentationBeans);
            if (fileFragmentationBeans.size()==0){


            }
        }*/
        Log.d("dfdssdgdsgs222222", fileName);
        Log.d("gtgwrtwwrtwt?????????????????????33", "?????????  " + pos + "    " + fileName + "  ????????????" + filelist.size());
        for (int i = 0; i < filelist.size(); i++) {
            File f = filelist.get(i);
            upLoadFileType = getMimeType(filelist.get(i).getName());
            endStrAll = filelist.get(i).getName().substring(filelist.get(i).getName().lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), f);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileName + "/" + i + "/" + pos + "/" + UserUtil.meeting_record_id, requestBody);

            NetWorkManager.getInstance().getNetWorkApiService().receiveChunk(part).compose(this.bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse>() {
                        @Override
                        protected void onFail(BasicResponse response) {
                            super.onFail(response);
                            upLoadNum++;//????????????????????????
                            littelFilePos++;//??????????????????????????????
                            Log.d("gtgwrtwwrtwt?????????????????????pppp1111", "????????????   upLoadNum" + upLoadNum + "  fileName=   " + fileName);
                            Log.d("??????111222222", response.getMsg() + "   " + response.getData().toString());

                            for (int n = 0; n < fileFragmentationBeans.size(); n++) {
                                if (fileFragmentationBeans.get(n).getFileName().equals(fileName)) {


                                    int a = fileFragmentationBeans.get(n).getNumberUploads();
                                    a++;
                                    fileFragmentationBeans.get(n).setNumberUploads(a);
                                    if (fileFragmentationBeans.get(n).getNumberUploads() == fileFragmentationBeans.get(n).getAllFenPianNum()) {
                                        upLoadNum = 0;
                                        Log.d("dfdssdgdsgs33333", fileFragmentationBeans.get(n).getFileName());
                                        mergeShards(fileFragmentationBeans.get(n).getFileName(), fileFragmentationBeans.get(n).getPos());
                                    }
                                    Log.d("gtgwrtwwrtwt?????????????????????ppppp2222", "?????????  " + pos + "   ??????????????????" + fileFragmentationBeans.get(n).getFileName() + "   ????????????????????????" + fileName + "  ????????? " + fileFragmentationBeans.get(n).getAllFenPianNum() + "  fenpiandaxiao " + fileFragmentationBeans.get(n).getNumberUploads());

                                }

                            }
                         /*   if (upLoadNum == filelist.size()) {
                                upLoadNum = 0;
                                mergeShards(fileName, pos);
                            }*/

                        }

                        @Override
                        protected void onSuccess(BasicResponse response) {
                            upLoadNum++;//????????????????????????
                            littelFilePos++;//??????????????????????????????
                            try {
                                Log.d("gtgwrtwwrtwt?????????????????????----90", "?????????  " + pos +
                                        "    ?????????????????????" + filelist.size() + "  fileName=   " + fileName + "   ?????????????????? " +
                                        Formatter.formatFileSize(WuHuActivity3.this, f.length()) + "   body??????????????? " +
                                        Formatter.formatFileSize(WuHuActivity3.this, requestBody.contentLength()) + "     ??????????????? " + fileFragmentationBeans.size());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                          /*  try {
                                Log.d("gtgwrtwwrtwt?????????????????????222", "?????????  " + pos + "  ????????????   upLoadNum   ????????????" + upLoadNum +
                                        "    ?????????????????????" + filelist.size() + "  fileName=   " + fileName + "   ?????????????????? " +
                                        Formatter.formatFileSize(WuHuActivity.this, f.length()) + "   body??????????????? " +
                                        Formatter.formatFileSize(WuHuActivity.this, requestBody.contentLength()) + "     ??????????????? " + fileFragmentationBeans.size());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            for (int n = 0; n < fileFragmentationBeans.size(); n++) {
                                if (fileFragmentationBeans.get(n).getFileName().equals(fileName)) {
                                    Log.d("gtgwrtwwrtwt?????????????????????ttttt", "?????????  " + pos + "   ??????????????????" + fileFragmentationBeans.get(n).getFileName() + "   ????????????????????????" + fileName);
                                    int a = fileFragmentationBeans.get(n).getNumberUploads();
                                    a++;
                                    fileFragmentationBeans.get(n).setNumberUploads(a);
                                    if (fileFragmentationBeans.get(n).getNumberUploads() == fileFragmentationBeans.get(n).getAllFenPianNum()) {
                                        upLoadNum = 0;
                                        mergeShards(fileFragmentationBeans.get(n).getFileName(), fileFragmentationBeans.get(n).getPos());
                                    }
                                    Log.d("gtgwrtwwrtwt?????????????????????ppppp", "?????????  " + pos + "   ??????????????????" + fileFragmentationBeans.get(n).getFileName() + "   ????????????????????????" + fileName + "  ????????? " + fileFragmentationBeans.get(n).getAllFenPianNum() + "  fenpiandaxiao " + fileFragmentationBeans.get(n).getNumberUploads());

                                }

                            }
                          /*  if (upLoadNum == filelist.size()) {
                                upLoadNum = 0;
                                mergeShards(fileName, pos);
                            }*/
                        }
                    });
        }

    }


    private void mergeShards(String fileName, String pos) {

        Map<String, Object> map = new HashMap<>();
        map.put("fileName", fileName);
        map.put("dirName", UserUtil.meeting_record_id);
        map.put("size", size);
        //  map.put("updateFileList", 1);
        map.put("index", Integer.valueOf(pos));
        NetWorkManager.getInstance().getNetWorkApiService().mergeChunk(map).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MergeChunkBean>>() {
                    @Override
                    protected void onFail(BasicResponse<MergeChunkBean> response) {
                        super.onFail(response);
                        upLoadFileNum++;
                        upLoadNum = 0;//???????????????????????????????????????0
                        littelFilePos = 0;//??????????????????????????????
                        issueFileNum++;//????????????????????????
                        if (locaFileNum == upLoadFileNum) {
                            reSetdata();
                        }
                        Log.d("??????1113333", response.getMsg() + "   " + response.getData().toString());
                        Log.d("gtgwrtwwrtwt?????????????????????ppppp333", "???????????? " + locaFileNum + "     ???????????????????????????" + upLoadFileNum + "  fileName   " + fileName);
                    }

                    @Override
                    protected void onSuccess(BasicResponse<MergeChunkBean> response) {
                        if (response != null) {
                            upLoadFileNum++;
                            littelFilePos = 0;//??????????????????????????????
                            issueFileNum++;//????????????????????????
                            MergeChunkBean mergeChunkBean = response.getData();
                            Log.d("sdfsfdfdf", mergeChunkBean.getUrl() + "     " + mergeChunkBean.getIndex());
                            if (Hawk.contains("WuHuFragmentData")) {
                                List<WuHuEditBean.EditListBean> beanList = new ArrayList<>();
                                beanList.clear();
                                WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                                beanList.addAll(wuHuEditBean.getEditListBeanList());
                                if (beanList == null || beanList.size() == 0) {
                                    return;
                                }
                                for (int i = 0; i < beanList.size(); i++) {
                                    //?????????????????????
                                    WuHuEditBean.EditListBean editListBean = beanList.get(i);
                                    //?????????????????????????????????????????????
                                    if (String.valueOf(mergeChunkBean.getIndex()).equals(editListBean.getPos())) {
                                        List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                                        fileListBeanList.clear();
                                        if (editListBean.getFileListBeanList() != null & editListBean.getFileListBeanList().size() > 0) {
                                            //????????????????????????????????????
                                            fileListBeanList.addAll(editListBean.getFileListBeanList());
                                            for (int k = 0; k < fileListBeanList.size(); k++) {
                                                if (fileName.equals(fileListBeanList.get(k).getName())) {

                                                    Log.d("fgdfhhhhfgdhdh   00", fileName.equals(fileListBeanList.get(k).getName()) + "  fileName=" + fileName + "   fileListBeanList.get(k).getName()=" + fileListBeanList.get(k).getName());
                                                    fileListBeanList.get(k).setNet(true);
                                                    fileListBeanList.get(k).setPath(mergeChunkBean.getUrl());
                                                }

                                            }
                                            editListBean.setFileListBeanList(fileListBeanList);
                                        }
                                    }
                                }
                                wuHuEditBean.setEditListBeanList(beanList);
                                Hawk.put("WuHuFragmentData", wuHuEditBean);
                            }
                            if (locaFileNum == upLoadFileNum) {
                                reSetdata();
                            }
                            upLoadNum = 0;//???????????????????????????????????????0
                            //  isFinish=true;
                            Log.d("gtgwrtwwrtwt?????????????????????4444444", "???????????? " + locaFileNum + "     ???????????????????????????" + upLoadFileNum + "  fileName   " + fileName + "   upLoadNum=" + upLoadNum);
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //?????????fragment?????????????????????
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBeanList.clear();
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            if (wuHuEditBean != null && wuHuEditBean.getEditListBeanList() != null) {

                if (Hawk.contains("company_name")) {
                    String name = Hawk.get("company_name");
                    if (name != null || !name.equals("")) {
                        wuHuEditBean.setTopics(name);

                    }

                }
                if (Hawk.contains("tittle2")) {
                    String tittle = Hawk.get("tittle2");
                    if (tittle != null || !tittle.equals("")) {
                        wuHuEditBean.setTopic_type(tittle);
                    }

                }

                wuHuEditBeanList.addAll(wuHuEditBean.getEditListBeanList());

            }


        }
/*
  if (!Hawk.contains("allVote")){
   */
/* ArrayList<HashMap<String,ArrayList<VoteListBean.VoteBean>> > hashMapArrayList=new ArrayList<>();
    ArrayList<VoteListBean.VoteBean>  voteList=new ArrayList<>();*//*

    HashMap<String,ArrayList<VoteListBean.VoteBean>> sites2 = new HashMap<String,ArrayList<VoteListBean.VoteBean>>();
    Hawk.put("allVote",sites2);
    }
*/

    }

    private void reSetdata() {

        WuHuEditBeanRequset wuHuEditBeanRequset = new WuHuEditBeanRequset();
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() < 0) {
                Log.d("Valueisempty6666", "reSetdata??????   ");
                Toast.makeText(WuHuActivity3.this, "???????????????????????????????????????", Toast.LENGTH_LONG).cancel();
                return;

            }

            //????????????????????? ??????item
            if (wuHuEditBean.getEditListBeanList().get(0).getParticipantUnits().equals("aa") && wuHuEditBean.getEditListBeanList().get(0).getReportingUnit().equals("aa")) {
                wuHuEditBean.getEditListBeanList().remove(0);
            }

            VoteListBean voteListBean = new VoteListBean();
            ArrayList<VoteListBean.VoteBean> voteList = new ArrayList<>();
            voteListBean.setData(voteList);
            wuHuEditBean.setVoteListBean(voteListBean);
            /*if (Hawk.contains("VoteListBean")) {
                VoteListBean voteListBean = Hawk.get("VoteListBean");
                wuHuEditBean.setVoteListBean(voteListBean);
            } else {
                VoteListBean voteListBean = new VoteListBean();
                ArrayList<VoteListBean.VoteBean> voteList = new ArrayList<>();
                voteListBean.setData(voteList);
                wuHuEditBean.setVoteListBean(voteListBean);
            }*/

            if (Hawk.contains("company_name")) {
                String name = Hawk.get("company_name");
                if (name != null || !name.equals("")) {
                    wuHuEditBean.setTopics(name);

                }

            }
            if (Hawk.contains("tittle2")) {
                String tittle = Hawk.get("tittle2");
                if (tittle != null || !tittle.equals("")) {
                    wuHuEditBean.setTopic_type(tittle);
                }

            }

            String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//??????-???
            wuHuEditBean.setStartTime(data);

            if (Hawk.contains(constant.SignFilePath) && Hawk.get(constant.SignFilePath) != null) {
                List<String> SignFilePaths = Hawk.get(constant.SignFilePath);
                wuHuEditBean.setSignFilePath(SignFilePaths);
            }
            wuHuEditBeanRequset.setContent(wuHuEditBean);
            wuHuEditBeanRequset.setId(UserUtil.meeting_record_id);
        }
        Log.d("dfggsdgsgs222", wuHuEditBeanRequset.toString());
        NetWorkManager.getInstance().getNetWorkApiService().meeting(wuHuEditBeanRequset).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MeetingIdBean>>() {
                    @Override
                    protected void onFail(BasicResponse<MeetingIdBean> response) {
                        super.onFail(response);
                        Log.d("??????11144444", response.getMsg() + "   " + response.getData().toString());
                        upLoadNum = 0;//???????????????????????????????????????0
                        //   UrlConstant.baseUrl = "http://192.168.1.1:3006";
                        //  FLUtil.BroadCastIP = "192.168.00.000";
                        if (Hawk.contains("VoteListBean")) {
                            Hawk.delete("VoteListBean");
                        }
                    }

                    @Override
                    protected void onSuccess(BasicResponse<MeetingIdBean> response) {
                        //??????????????????
                        if (response != null) {
                            MeetingIdBean meetingIdBean = response.getData();
                            Log.d("gtgwrtwwrtwt?????????????????????66666", "?????????????????????");
                            allFileNum = 0;
                            upLoadFileNum = 0;
                            upLoadNum = 0;
                            upLoadNum = 0;//???????????????????????????????????????0
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            //???????????????????????????
                            if (Hawk.contains("WuHuFragmentData")) {
                                Hawk.delete("WuHuFragmentData");
                            }
                            if (Hawk.contains("VoteListBean")) {
                                Hawk.delete("VoteListBean");
                            }
                            UserUtil.meeting_record_id = "";
                            locaFileNum = 0;
                            // UrlConstant.baseUrl = "http://192.168.1.1:3006";
                            //  FLUtil.BroadCastIP = "192.168.00.000";

                            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                                // ????????????????????????????????????????????????
                                String code = getIntent().getStringExtra("code");
                                UDPBroadcastManager.getInstance().sendDestroyCode(code);
                                ServerManager.getInstance().StopMyWebsocketServer();
                                UDPBroadcastManager.getInstance().removeUDPBroastcast();
                            }
                            JWebSocketClientService.closeConnect();
                            finish();
                        }
                    }
                });

    }

    //?????????????????????
    public void showFileTransferDialog() {
        //?????????dialog????????????
        View inflate = LayoutInflater.from(WuHuActivity3.this).inflate(R.layout.wuhu_netfile_progress_dialog, null);
        //?????????dialog????????????
        networkFileDialog = new MyDialog(WuHuActivity3.this, R.style.dialogTransparent);
        networkFileDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        networkFileDialog.setCanceledOnTouchOutside(false);
        //??????????????????Dialog
        networkFileDialog.setContentView(inflate);
        networkFileDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        networkFileDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        networkFileDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //???????????????????????????
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //??????
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //???????????????
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                networkFileDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        progressBar = inflate.findViewById(R.id.progressBar);
        tips = inflate.findViewById(R.id.tips);
        tips.setText("????????????????????????????????????...");
        //????????????Activity???????????????
        Window window = networkFileDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//?????????
        wlp.height = (int) (height * 0.25);
        window.setAttributes(wlp);
        networkFileDialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {

            }
        });
        networkFileDialog.show();

    }

    /**
     * ??????websocket ??????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg0000: ", message.toString());

        if (UserUtil.isTempMeeting) {
            //  ??????????????????
            if (message.getMessage().contains(constant.SURENAME)) {
                UserUtil.user_name = Hawk.get(constant.myNumber);
                // ttendeesName.setText(Hawk.get(constant.myNumber) + "???   ??????");
                if (Hawk.contains(constant.myNumber)) {
                    UserUtil.user_name = Hawk.get(constant.myNumber);
                }

                if (Hawk.contains(constant.user_name)) {
                    UserUtil.user_name = Hawk.get(constant.user_name);
                }
            }

            if (UserUtil.ISCHAIRMAN) {
                // ????????????????????? ???????????????client?????????????????????
                if (message.getMessage().equals(constant.SERVERSTART)) {
                    //  ??????websocket(server???client?????????)
                    JWebSocketClientService.initSocketClient();
                }
            }

        }
        //  client??????????????????
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
            if (message.getMessage().contains(constant.START_SHARE_SCEEN)) {
                if (UserUtil.ISCHAIRMAN) {
                    return;
                }
                Intent inte = new Intent(WuHuActivity3.this, ScreenReceiveActivity.class);
                startActivity(inte);
            } else if (message.getMessage().contains(constant.FINISH_SHARE_SCEEN)) {
                Intent in = new Intent();
                in.setAction(constant.FINISH_SHARE_SCREEN_BROADCAST);
                sendBroadcast(in);
            } else
                // ????????????????????????
                if (message.getMessage().contains(constant.WUHUADDFRAGMENT)) {
                    Log.e("onReceiveMsg11111:   wuwhuactivity  ????????????", message.toString());
                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        //  ??????vote???websocket?????????
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean wuHuEditBean = wsebean.getBody();
                            if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList().size() < 1 || wuHuEditBean.getEditListBeanList() == null) {

                                return;
                            }
                            Log.d("onReceiveMsg11111??????=  wuwhuactivity  ????????????", fragmentPos + "");
                            mTestFragments.put(key++, WuHuFragment2.newInstance(fragmentPos + ""));
                            fragmentPos++;
                            mPagerAdapter.setmTestFragments(mTestFragments);
                            //  mViewPager.setOffscreenPageLimit(mTestFragments.size() - 1);
                            mPagerAdapter.notifyDataSetChanged();

                            int totalcount = mTestFragments.size();
                            int currentItem = mViewPager.getCurrentItem();

                            if (totalcount > 1) {
                                if (currentItem == 0) {
                                    left_rl.setVisibility(View.GONE);
                                    rigth_rl.setVisibility(View.VISIBLE);
                                } else if (currentItem > 0) {
                                    left_rl.setVisibility(View.VISIBLE);
                                    rigth_rl.setVisibility(View.VISIBLE);
                                } else if (currentItem == (totalcount - 1)) {
                                    left_rl.setVisibility(View.VISIBLE);
                                    rigth_rl.setVisibility(View.GONE);
                                }

                            }
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean wuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.clear();
                                if (wuHuFragmentData == null || wuHuFragmentData.getEditListBeanList().size() < 1 || wuHuFragmentData.getEditListBeanList() == null) {

                                    return;
                                }
                                listBeans.addAll(wuHuEditBean.getEditListBeanList());
                                if (listBeans != null && listBeans.size() > 0) {
                                    wuHuEditBeanList.addAll(listBeans);
                                    wuHuFragmentData.setTopics(listBeans.get(0).getTopics());
                                    wuHuFragmentData.setTopic_type(listBeans.get(0).getTopic_type());
                                    wuHuFragmentData.setLine_color(listBeans.get(0).getLine_color());
                                    wuHuFragmentData.setThem_color(listBeans.get(0).getThem_color());
                                    if (wuHuEditBeanList != null && wuHuEditBeanList.size() > 0) {
                                        wuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                        Hawk.put("WuHuFragmentData", wuHuFragmentData);
                                        if (wuHuListAdapter != null) {
                                            wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                            wuHuListAdapter.notifyDataSetChanged();
                                        }

                                        Intent intent = new Intent();
                                        intent.setAction(constant.FRESH_CATalog_BROADCAST);
                                        sendBroadcast(intent);
                                    }


                                }

                          /*  //??????????????????
                            wsUpdata(wuHuEditBean,constant.REFRASHWuHUALL);*/
                            }


                            Log.d("paichayuanyin", "????????????");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().contains(constant.REFRASHWuHUSIGLEDATA)) {

                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        //  ??????vote???websocket?????????
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean wuHuEditBean = wsebean.getBody();
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean refrashWuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.clear();
                                if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() < 1) {

                                    return;
                                }
                                listBeans.addAll(wuHuEditBean.getEditListBeanList());
                                wuHuEditBeanList.addAll(listBeans);
                                if (wuHuEditBeanList != null && wuHuEditBeanList.size() > 0) {
                                    refrashWuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                    Log.d("?????????  activity  ??????  ", wuHuEditBeanList.get(1).getParticipantUnits());
                                    Hawk.put("WuHuFragmentData", refrashWuHuFragmentData);
                                    wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                    wuHuListAdapter.notifyDataSetChanged();
                                    Intent intent = new Intent();
                                    intent.setAction(constant.FRESH_CATalog_BROADCAST);
                                    sendBroadcast(intent);
                                    Log.d("paichayuanyin", "????????????");
                                }

                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.REFRASHWuHUALL)) {
                    Log.d("dfaffaf333", "????????????????????????????????????");
                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        //  ??????vote???websocket?????????
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean wuHuEditBean = wsebean.getBody();
                            Hawk.put("company_name", wuHuEditBean.getTopics());
                            Hawk.put("tittle2", wuHuEditBean.getTopic_type());
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean refrashWuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.clear();
                                if (wuHuEditBean == null || wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() < 1) {

                                    return;
                                }

                                listBeans.addAll(wuHuEditBean.getEditListBeanList());
                                WuHuEditBean.EditListBean editListBean = listBeans.get(0);
                                refrashWuHuFragmentData.setTopic_type(editListBean.getTopic_type());
                                refrashWuHuFragmentData.setTopics(editListBean.getTopics());
                                refrashWuHuFragmentData.setLine_color(editListBean.getLine_color());
                                refrashWuHuFragmentData.setThem_color(editListBean.getThem_color());
                                wuHuEditBeanList.addAll(listBeans);
                                if (wuHuEditBeanList != null && wuHuEditBeanList.size() > 0) {
                                    refrashWuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                    Hawk.put("WuHuFragmentData", refrashWuHuFragmentData);
                                    Log.d("?????????  fragment   ???????????? ", wuHuEditBeanList.get(Integer.valueOf(1)).getParticipantUnits());
                                    wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                    wuHuListAdapter.notifyDataSetChanged();
                                    Intent intent = new Intent();
                                    intent.setAction(constant.FRESH_CATalog_BROADCAST);
                                    sendBroadcast(intent);
                                    Log.d("paichayuanyin", "??????????????????");
                                }

                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.DELETE_WUHU_FRAGMENT)) {
                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean deletWuHuEditBean = wsebean.getBody();
                            fragmentPos--;
                            mTestFragments.removeAt(Integer.valueOf(deletWuHuEditBean.getPosition()));
                            mPagerAdapter.setmTestFragments(mTestFragments);
                            mPagerAdapter.notifyDataSetChanged();
                            if (deletWuHuEditBean == null || deletWuHuEditBean.getEditListBeanList() == null || deletWuHuEditBean.getEditListBeanList().size() < 1) {

                                return;
                            }
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean wuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.clear();
                                listBeans.addAll(deletWuHuEditBean.getEditListBeanList());
                                wuHuEditBeanList.addAll(listBeans);
                                if (wuHuEditBeanList != null && wuHuEditBeanList.size() > 0) {

                                    wuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                    Hawk.put("WuHuFragmentData", wuHuFragmentData);
                                    wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                    //  mViewPager.setOffscreenPageLimit(mTestFragments.size() - 1);
                                    wuHuListAdapter.notifyDataSetChanged();

                                    Intent intent = new Intent();
                                    intent.setAction(constant.FRESH_CATalog_BROADCAST);
                                    sendBroadcast(intent);
                                    Log.d("paichayuanyin", "????????????");
                                    int totalcount = mTestFragments.size();
                                    int currentItem = mViewPager.getCurrentItem();

                                    if (totalcount == 1) {
                                        left_rl.setVisibility(View.GONE);
                                        rigth_rl.setVisibility(View.GONE);
                                    } else if (totalcount > 1) {
                                        if (currentItem == 0) {
                                            left_rl.setVisibility(View.GONE);
                                            rigth_rl.setVisibility(View.VISIBLE);
                                        } else if (currentItem > 0 && currentItem < (totalcount - 1)) {
                                            left_rl.setVisibility(View.VISIBLE);
                                            rigth_rl.setVisibility(View.VISIBLE);
                                        } else if (currentItem == (totalcount - 1)) {
                                            left_rl.setVisibility(View.VISIBLE);
                                            rigth_rl.setVisibility(View.GONE);
                                        }

                                    }
                                }
                            }

                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.QUERYVOTE_WUHU_FRAGMENT)) {

                    Log.e("onReceiveMsg??????:   wuhuactivity", message.toString());

                    try {
                        TempWSBean<ArrayList> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<WuHuEditBean.EditListBean>>>() {
                        }.getType());
                        //  ??????vote???websocket?????????
                        if (wsebean != null) {
                            editListBeans.clear();

                            if (wsebean.getBody() == null || wsebean.getBody().size() < 1) {

                                Toast.makeText(WuHuActivity3.this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            editListBeans.addAll(wsebean.getBody());
                            Log.d("fddgdsgsdgsgdsg-actiivity", editListBeans.size() + "");
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                                if (wuHuEditBean == null) {
                                    WuHuEditBean we = new WuHuEditBean();
                                    we.setEditListBeanList(editListBeans);
                                    Hawk.put("WuHuFragmentData", we);
                                } else {
                                    wuHuEditBean.setEditListBeanList(editListBeans);
                                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                                }

                            } else {
                                WuHuEditBean wuHuEditBean = new WuHuEditBean();
                                wuHuEditBean.setEditListBeanList(editListBeans);
                                Hawk.put("WuHuFragmentData", wuHuEditBean);

                            }

                            if (wuHuEditBean != null && wuHuEditBean.getTopics() != null) {

                                Hawk.put("company_name", wuHuEditBean.getTopics());

                            }

                            if (wuHuEditBean != null && wuHuEditBean.getTopic_type() != null) {

                                Hawk.put("tittle2", wuHuEditBean.getTopics());
                            }

                            querywuhufragment(editListBeans);
                            //   mViewPager.setOffscreenPageLimit(mTestFragments.size() - 1);
                            Log.d("wrewarawrawerwqeqwe   wuhuactivity111", "??????" + editListBeans.size());
                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            sendBroadcast(intent);
                            Log.d("paichayuanyin", "????????????");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().contains(constant.FILEMD5PUSH)) {
                    //??????????????????????????????
                    try {
                        TempWSBean<WuHuEditBean.EditListBean.FileListBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean.EditListBean.FileListBean>>() {
                        }.getType());
                        if (wsebean != null) {
                            WuHuEditBean.EditListBean.FileListBean fileBean = wsebean.getBody();
                            if (fileBean != null) {
                                if (!fileBean.getMac().equals(FLUtil.getMacAddress())) {
                                    checkFileMd5(fileBean, "2");

                                }

                            }

                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.FILEMD5SHARE)) {
                    //??????????????????????????????
                    //??????????????????????????????
                    try {
                        TempWSBean<WuHuEditBean.EditListBean.FileListBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean.EditListBean.FileListBean>>() {
                        }.getType());
                        if (wsebean != null) {
                            WuHuEditBean.EditListBean.FileListBean fileBean = wsebean.getBody();
                            if (fileBean != null) {
                                if (!fileBean.getMac().equals(FLUtil.getMacAddress())) {
                                    checkFileMd5(fileBean, "1");

                                }

                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.QUERYATTENDSize)) {
                    try {
                        TempWSBean<Integer> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<Integer>>() {
                        }.getType());
                        int size = wsebean.getBody();


                        Log.d("md5????????????", "attendeBeanList.size==" + size);
//                        if (attendeBeanList != null) {
                        Hawk.put("TempMeetingAttende", size + "");
//                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().contains(constant.SUBMITANISSUE)) {


                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        if (wsebean != null) {
                            List<WuHuEditBean.EditListBean> comitEdit = new ArrayList<>();
                            Log.d("fdsafadffadfazz1111  ", "????????????!");
                            WuHuEditBean wuHuEditBean = wsebean.getBody();

                            Log.e("1111", "wuHuEditBean===" + wuHuEditBean + "   2222===wuHuEditBean.getEditListBeanList()==" + wuHuEditBean.getEditListBeanList());
                            if (wuHuEditBean != null && wuHuEditBean.getEditListBeanList() != null) {
                                comitEdit.addAll(wuHuEditBean.getEditListBeanList());
                            }

                            for (int i = 0; i < comitEdit.size(); i++) {

                                Log.d("fdsafadffadfazz22222   ", comitEdit.get(i).getFileListBeanList().size() + "");
                            }
                            Log.d("wrewarawrawerwqeqwe   wuhuactivity22", "SUBMITANISSUE" + comitEdit.size());
                            Hawk.put("WuHuFragmentData", wuHuEditBean);
                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            sendBroadcast(intent);
                            Log.d("paichayuanyin", "????????????");
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }


                } else if (message.getMessage().contains(constant.PUSH_FILE_WEBSOCK)) {
                    try {
                        //????????????????????????
                        if (UserUtil.ISCHAIRMAN) {
                            return;

                        }
                        TempWSBean<PushBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<PushBean>>() {
                        }.getType());
                        if (wsebean != null) {
                            PushBean pushBean = wsebean.getBody();
                            if (pushBean != null && pushBean.getFileName() != null) {
                                //?????????????????????????????????????????????
                                File path = new File(netFilePath);
                                File[] files = path.listFiles();// ??????
                                if (files == null) {
                                    return;
                                }
                                getallfiles(files, pushBean.getFileName());
                            }

                            Log.e("dfsdgfsdgfs", pushBean.getFileName());

                        }

                    } catch (Exception e) {

                    }


                }
            if (message.getMessage().contains(constant.SURENAME)) {
                loadData();
            }
          /*  Intent intent = new Intent();
            intent.setAction(constant.FRESH_CATalog_BROADCAST);
            sendBroadcast(intent);*/
        }
    }

    //????????????????????????????????????
    public void loadData() {
        // ????????????????????? ??????
        List<String> list = new ArrayList<>();
        Hawk.put(constant.SignFilePath, list);


        if (UserUtil.ISCHAIRMAN) {
            //???????????????????????????????????????????????????????????????????????????
            initiaServerData();
        } else {
            //?????????????????????  ??????????????????????????????
            if (UserUtil.isNetDATA) {
                getMeetingFiles();

            } else {
                if (Hawk.contains("isreuse")) {
                    String isreuse = Hawk.get("isreuse");
                    if (isreuse.equals("2") || isreuse.equals("3")) {
                        File share = new File(fileShare);
                        if (!share.exists()) {
                            share.mkdir();
                        }
                    }
                }
            }


        }
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(constant.QUERYVOTE_WUHU_FRAGMENT);
        bean.setBody("");
        String strJson = new Gson().toJson(bean);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JWebSocketClientService.sendMsg(strJson);
            }
        }).start();


    }

    //629d65c3c822e2cee0c41ff4
    private void getMeetingFiles() {
        showFileTransferDialog();
        String id = "";
        if (Hawk.contains("WuHuMeetingID")) {
            id = Hawk.get("WuHuMeetingID");
        }
        NetWorkManager.getInstance().getNetWorkApiService().getWuHuMeetingInfo(id).compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<WuHuMeetingListResponse>>() {
                    @Override
                    protected void onFail(BasicResponse<WuHuMeetingListResponse> response) {
                        super.onFail(response);
                        if (networkFileDialog != null) {
                            networkFileDialog.dismiss();
                        }
                        JWebSocketClientService.closeConnect();
                        finish();
                        Toast.makeText(WuHuActivity3.this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (networkFileDialog != null) {
                            networkFileDialog.dismiss();
                        }
                        Toast.makeText(WuHuActivity3.this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        JWebSocketClientService.closeConnect();
                        finish();
                    }

                    @Override
                    protected void onSuccess(BasicResponse<WuHuMeetingListResponse> response) {
                        if (response != null) {
                            WuHuMeetingListResponse wuHuMeetingListResponse = response.getData();

                            if (wuHuMeetingListResponse!=null&&wuHuMeetingListResponse.getContent() != null && wuHuMeetingListResponse.getContent().getSignFilePath() != null) {
                                Hawk.put(constant.SignFilePath, wuHuMeetingListResponse.getContent().getSignFilePath());
                                setData(wuHuMeetingListResponse);
                                UserUtil.meeting_record_id = wuHuMeetingListResponse.get_id();
                            }


                        }

                    }
                });

    }

    //????????????
    private void setData(WuHuMeetingListResponse wuHuMeetingListResponse) {
        filesList.clear();
        allFileNum = 0;
        if (wuHuMeetingListResponse.getContent().getTopics() != null) {
            Hawk.put("company_name", wuHuMeetingListResponse.getContent().getTopics());
        }
        if (wuHuMeetingListResponse.getContent().getTopic_type() != null) {
            Hawk.put("tittle2", wuHuMeetingListResponse.getContent().getTopic_type());
        }

        List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO> editListBeanListDTOS = new ArrayList<>();
        editListBeanListDTOS.clear();
        if (wuHuMeetingListResponse.getContent() != null && wuHuMeetingListResponse.getContent().getEditListBeanList() != null) {

            editListBeanListDTOS.addAll(wuHuMeetingListResponse.getContent().getEditListBeanList());

        }

        for (int i = 0; i < editListBeanListDTOS.size(); i++) {
            WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanListDTO = editListBeanListDTOS.get(i);//??????????????????
            List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO.FileListBeanListDTO> netLocaFiles = new ArrayList<>();//?????????????????????????????????
            netLocaFiles.clear();
            if (editListBeanListDTO.getFileListBeanList() != null && editListBeanListDTO.getFileListBeanList().size() > 0) {
                netLocaFiles.addAll(editListBeanListDTO.getFileListBeanList());
                for (int k = 0; k < netLocaFiles.size(); k++) {
                    WuHuNetWorkBean wuHuNetWorkBean = new WuHuNetWorkBean();
                    wuHuNetWorkBean.setUrl(UrlConstant.baseUrl + "/" + netLocaFiles.get(k).getPath());
                    wuHuNetWorkBean.setPos(netLocaFiles.get(k).getPos());
                    wuHuNetWorkBean.setName(netLocaFiles.get(k).getName());
                    allFileNum++;
                    filesList.add(wuHuNetWorkBean);
                }
            }
        }

        if (filesList.size() > 0) {

            for (int a = 0; a < filesList.size(); a++) {
                filesList.get(a).setPos((Integer.valueOf(filesList.get(a).getPos()) + 1) + "");

            }
            //???????????????????????????????????????????????????
            for (int k = 0; k < filesList.size() - 1; k++) {
                for (int j = filesList.size() - 1; j > k; j--) {
                    if (filesList.get(k).getName().equals(filesList.get(j).getName())) {
                        filesList.get(k).setName(filesList.get(k).getPos() + filesList.get(k).getName());
                        filesList.get(j).setName(filesList.get(j).getPos() + filesList.get(j).getName());
                    }
                }
            }
        }

        if (filesList.size() < 1) {
            if (networkFileDialog != null) {
                networkFileDialog.dismiss();
            }
        } else {
            //????????????????????????
            for (int i = 0; i < filesList.size(); i++) {
                WuHuNetWorkBean wuHuNetWorkBean = filesList.get(i);

                DownloadUtil.get().download(wuHuNetWorkBean.getUrl(), netFilePath, wuHuNetWorkBean.getName(), new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        dowLoadNum++;
                        Log.d("dffasfsdfafafdowLoadNum11", dowLoadNum + "    " + filesList.size());
                        Log.d("dffasfsdfafafdowLoadNum333", "wuHuNetWorkBean.getUrl()=    " + wuHuNetWorkBean.getUrl());
                        if (dowLoadNum == filesList.size()) {
                            if (networkFileDialog != null) {
                                networkFileDialog.dismiss();
                            }
                        }
                        //completeDownload();
                    }

                    @Override
                    public void onDownloading(int progress) {
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        dowLoadNum++;

                        Log.d("dffasfsdfafafdowLoadNum222", dowLoadNum + "    " + filesList.size());
                        failList.add(wuHuNetWorkBean);
                        if (dowLoadNum == filesList.size()) {
                            if (networkFileDialog != null) {
                                networkFileDialog.dismiss();
                            }
                        }
                        //completeDownload();
                    }
                });

            }
        }

    }

    //action 1 ?????? 2  ??????
    private void checkFileMd5(WuHuEditBean.EditListBean.FileListBean fileBean, String action) {
        File path = null;
        //???????????????????????????  ???????????????????????????????????????????????????????????????????????????  ???????????????????????????????????????
        if (fileBean.isNet()) {
            path = new File(fileShare);
        } else {
            path = new File(netFilePath);
        }
        File[] files = path.listFiles();// ??????
        //?????????????????????????????????????????????
        getShareFile(files, action, fileBean);
    }

    /*
     * ???????????????????????????
     * */
    private void getShareFile(File[] files, String action, final WuHuEditBean.EditListBean.FileListBean fileListBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("111111111111111111", " ??????????????????????????? ??????????????????????????? ??????????????????????????? ???????????????????????????");
                //???????????????
                String content = "";
                if (files != null) {
                    String fileMd5 = "-1";
                    String md5FilePath = null;
                    // ???????????????????????????????????????????????????
                    for (File file : files) {
                        if (file.isDirectory()) {
                            Log.d("vvcvsvsfgsf1111  ", "000000  " + file.getName());
                            getFileName(file.listFiles());
                        } else {
                            Log.d("vvcvsvsfgsf1111  ", "1111111  " + file.getName());
                            String fileName = file.getName();
                            String[] fileNameAll = null;
                            String pos = null;
                            Log.d("vvcvsvsfgsf22222 ", fileMd5 + "   action=" + action);
                            //??????
                            if (fileName.contains(constant.WUHUPUSH)) {
                                fileNameAll = fileName.split(constant.WUHUPUSH);
                                fileName = fileNameAll[1];
                                pos = fileNameAll[0];
                            } else if (fileName.contains(constant.WUHUSHARE)) {
                                fileNameAll = fileName.split(constant.WUHUSHARE);
                                fileName = fileNameAll[1];
                                pos = fileNameAll[0];
                            }

                            if (Md5Util.getFileMD5(file) != null && fileListBean.getFileMd5() != null) {
                                //????????????MD5??????????????????????????????
                                if (fileListBean.getFileMd5().equals(Md5Util.getFileMD5(file))) {
                                    // fileMd5 = Md5Util.getFileMD5(file);
                                    fileMd5 = "-1";
                                    fileListBean.setMd5Path(file.getPath());
                                    Log.d("vvcvsvsfgsf33333 ", fileMd5);
                                    md5FilePath = file.getPath();
                                }
                            }
                        }

                    }

                    //?????????????????????
                    //??????
                    if (action.equals("1")) {
                        if (fileMd5.equals("-1")) {
                            //???????????????????????? ????????????33????????????
                            Message shareMsg = new Message();
                            shareMsg.what = 700;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "700");
                        } else {
                            //???????????????????????? ?????????????????????????????????
                            Message shareMsg = new Message();
                            shareMsg.what = 600;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "600");
                        }

                    } else if (action.equals("2")) {
                        if (fileMd5.equals("-1")) {
                            //???????????????????????????????????????88????????????
                            Message shareMsg = new Message();
                            shareMsg.what = 500;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "500");
                        } else {
                            //???????????????????????????????????????  ????????????
                            Message shareMsg = new Message();
                            shareMsg.what = 400;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "400");
                        }


                    }
                }


            }


        }).start();
    }

    //???????????????????????????
    private void getFileName(File[] files) {
        WuHuEditBean.EditListBean.FileListBean fileBean;
        String content = "";
        if (files != null) {
            // ???????????????????????????????????????????????????
            for (File file : files) {


                if (file.isDirectory()) {

                    getFileName(file.listFiles());

                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);

                    fileBean = new WuHuEditBean.EditListBean.FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setFileMd5(Md5Util.getFileMD5(file));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                    //  fileBean.setType(endStr);
                    fileBean.setNet(false);

                }
            }

        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param dirpath ???????????????????????????
     * @param _type   ?????????????????????mp3?????????
     */
    private void getallfiles(File[] files, String name) {
        String path = "";
        // File f = new File(dirpath);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (files != null) {
                    for (int i = 0; i < files.length; i++) {

                        File f1 = files[i];
                        if (name.equals(f1.getName())) {

                            Message shareMsg = new Message();
                            shareMsg.what = 1080;
                            shareMsg.obj = f1.getPath();
                            mHander.sendMessage(shareMsg);


                        }

                    }

                }
       /* if (f.exists()) {//????????????????????????
            File[] files = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {

                    File f1 = files[i];
                    if (name.equals(f1.getName())) {

                        Message shareMsg = new Message();
                        shareMsg.what = 1080;
                        shareMsg.obj = f1.getPath();
                        mHander.sendMessage(shareMsg);


                    }

                }

            }

        }*/

            }
        }).start();

    }


    /*
     * ???????????????????????????
     * */
    private void getShareFile(File[] files, String name) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (UserUtil.object) {

                    //???????????????
                    String content = "";
                    if (files != null) {
                        // ???????????????????????????????????????????????????
                        for (File file : files) {
                            if (file.isDirectory()) {
                                getFileName(file.listFiles());

                            } else {
                                String fileName = file.getName();
                                String[] fileNameAll = null;
                                String pos = null;
                                if (fileName.contains(constant.WUHUPUSH)) {
                                    fileNameAll = fileName.split(constant.WUHUPUSH);
                                    fileName = fileNameAll[1];
                                    pos = fileNameAll[0];
                                } else if (fileName.contains(constant.WUHUSHARE)) {
                                    fileNameAll = fileName.split(constant.WUHUSHARE);
                                    fileName = fileNameAll[1];
                                    pos = fileNameAll[0];
                                }
                                Log.d("wuhuwuwhuwuhuwwhu   cvi", fileNameAll.length + "   " + fileNameAll[1] + "    " + fileNameAll[0]);


                            }
                        }

                      /*  //??????????????????????????????
                        Message shareMsg = new Message();
                        shareMsg.what = 1080;
                        shareMsg.obj = shareFileBeans;
                        handler.sendMessage(shareMsg);*/

                    }


                }

                //?????????????????????
            }
        }).start();
    }

    private void querywuhufragment(List<WuHuEditBean.EditListBean> editListBeanList) {
        if (editListBeanList.size() >= 1) {
            if (editListBeanList.get(1).getTopics() != null) {
                Hawk.put("company_name", editListBeanList.get(1).getTopics());
            }
            if (editListBeanList.get(1).getTopic_type() != null) {
                Hawk.put("tittle2", editListBeanList.get(1).getTopic_type());
            }
        }


        wuHuEditBeanList.clear();
        wuHuEditBeanList.addAll(editListBeanList);
        for (int i = 0; i < wuHuEditBeanList.size(); i++) {
            mTestFragments.put(key++, WuHuFragment2.newInstance(fragmentPos + ""));
            fragmentPos++;
        }
      /*for (int i=0;i<mTestFragments.size();i++){
          mTestFragments.get(i).UiaSsign(wuHuEditBeanList);
      }*/
 /*       Intent intent = new Intent();
        intent.setAction(constant.FRESH_CATalog_BROADCAST);
        sendBroadcast(intent);*/

        wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
        wuHuListAdapter.notifyDataSetChanged();

        mPagerAdapter.setmTestFragments(mTestFragments);
        mPagerAdapter.notifyDataSetChanged();

        int totalcount = mTestFragments.size();
        int currentItem = mViewPager.getCurrentItem();

        if (totalcount > 1) {
            if (currentItem == 0) {
                left_rl.setVisibility(View.GONE);
                rigth_rl.setVisibility(View.VISIBLE);
            } else if (currentItem > 0) {
                left_rl.setVisibility(View.VISIBLE);
                rigth_rl.setVisibility(View.VISIBLE);
            } else if (currentItem == (totalcount - 1)) {
                left_rl.setVisibility(View.VISIBLE);
                rigth_rl.setVisibility(View.GONE);
            }

        }

  /*      if (Hawk.contains("isreuse")) {
            String isreuse = Hawk.get("isreuse");
            //1:??????????????????  2???????????????????????? 3?????????????????????
            if (isreuse.equals("2")) {
                WuHuEditBean wuHuEditBean = new WuHuEditBean();
                Hawk.put("WuHuFragmentData", wuHuEditBean);
                if (Hawk.contains("WuHuFragmentData")) {
                    wuHuEditBean = Hawk.get("WuHuFragmentData");
                    wuHuEditBean.setTopics("?????????????????????");
                    wuHuEditBean.setTopic_type("????????????");

                    WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                    editListBean.setSubTopics("??????2022???");
                    editListBean.setReportingUnit("??????????????????????????????");
                    editListBean.setParticipantUnits("??????2?????????2?????????2??????2");
                    editListBean.setTopics("?????????????????????");
                    editListBean.setPos("0");
                    editListBean.setTopic_type("????????????");
                    wuHuEditBeanList.add(editListBean);


                    WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                    editListBean1.setSubTopics("??????2022???");
                    editListBean1.setReportingUnit("??????????????????????????????");
                    editListBean1.setParticipantUnits("??????2?????????2?????????2??????2");
                    editListBean1.setTopics("?????????????????????");
                    editListBean1.setTopic_type("????????????");
                    editListBean1.setPos("1");
                    wuHuEditBeanList.add(editListBean1);

                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                }
                Log.d("GASDJKASDD??????=", "rfresettt111");
                mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                fragmentPos++;
                mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                fragmentPos++;

                mPagerAdapter.setmTestFragments(mTestFragments);
                mPagerAdapter.notifyDataSetChanged();
                Log.d("wrewarawrawer", "isreuse");

            } else if (isreuse.equals("3")) {

                if (editListBeanList.size() >=2) {
                    wuHuEditBeanList.addAll(editListBeanList);
                    for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                        fragmentPos++;
                    }
                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics(wuHuEditBeanList.get(0).getTopics());
                        wuHuEditBean.setTopic_type(wuHuEditBeanList.get(0).getTopic_type());
                        wuHuEditBean.setLine_color(wuHuEditBeanList.get(0).getLine_color());
                        wuHuEditBean.setThem_color(wuHuEditBeanList.get(0).getThem_color());
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);

                    } else {
                        WuHuEditBean wuHuEditBean = new WuHuEditBean();
                        wuHuEditBean.setEditListBeanList(editListBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);

                    }


                } else {
                    WuHuEditBean wuHuEditBean = new WuHuEditBean();
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    if (Hawk.contains("WuHuFragmentData")) {
                        wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics("?????????????????????");
                        wuHuEditBean.setTopic_type("????????????");

                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setSubTopics("??????2022???");
                        editListBean.setReportingUnit("??????????????????????????????");
                        editListBean.setParticipantUnits("??????2?????????2?????????2??????2");
                        editListBean.setTopics("?????????????????????");
                        editListBean.setTopic_type("????????????");
                        editListBean.setPos("0");
                        wuHuEditBeanList.add(editListBean);


                        WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                        editListBean1.setSubTopics("??????2022???");
                        editListBean1.setReportingUnit("??????????????????????????????");
                        editListBean1.setParticipantUnits("??????2?????????2?????????2??????2");
                        editListBean1.setTopics("?????????????????????");
                        editListBean1.setPos("1");
                        editListBean1.setTopic_type("????????????");
                        wuHuEditBeanList.add(editListBean1);

                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                    }
                    Log.d("GASDJKASDD??????=", "rfresettt111");
                    mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                    fragmentPos++;
                    mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                    fragmentPos++;

                    mPagerAdapter.setmTestFragments(mTestFragments);
                    mPagerAdapter.notifyDataSetChanged();
                    Log.d("wrewarawrawer", "isreuse");

                }

            } else if (isreuse.equals("1")) {
                if (editListBeanList.size() >= 2) {
                    wuHuEditBeanList.addAll(editListBeanList);
                    for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                        fragmentPos++;
                    }

                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics(wuHuEditBeanList.get(0).getTopics());
                        wuHuEditBean.setTopic_type(wuHuEditBeanList.get(0).getTopic_type());
                        wuHuEditBean.setLine_color(wuHuEditBeanList.get(0).getLine_color());
                        wuHuEditBean.setThem_color(wuHuEditBeanList.get(0).getThem_color());
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);

                    } else {

                        WuHuEditBean wuHuEditBean = new WuHuEditBean();
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        if (Hawk.contains("WuHuFragmentData")) {
                            wuHuEditBean.setTopics("?????????????????????");
                            wuHuEditBean.setTopic_type("????????????");
                            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                            Hawk.put("WuHuFragmentData", wuHuEditBean);

                        }

                    }


                } else {
                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBeanList = wuHuEditBean.getEditListBeanList();
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                    } else {

                        WuHuEditBean wuHuEditBean = new WuHuEditBean();
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        if (Hawk.contains("WuHuFragmentData")) {
                            wuHuEditBean = Hawk.get("WuHuFragmentData");
                            wuHuEditBean.setTopics("?????????????????????");
                            wuHuEditBean.setTopic_type("????????????");

                            WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                            editListBean.setSubTopics("??????2022???");
                            editListBean.setReportingUnit("??????????????????????????????");
                            editListBean.setParticipantUnits("??????2?????????2?????????2??????2");
                            editListBean.setTopics("?????????????????????");
                            editListBean.setPos("0");
                            editListBean.setTopic_type("????????????");
                            wuHuEditBeanList.add(editListBean);

                            WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                            editListBean1.setSubTopics("??????2022???");
                            editListBean1.setReportingUnit("??????????????????????????????");
                            editListBean1.setParticipantUnits("??????2?????????2?????????2??????2");
                            editListBean1.setTopics("?????????????????????");
                            editListBean1.setPos("1");
                            editListBean1.setTopic_type("????????????");
                            wuHuEditBeanList.add(editListBean1);

                            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                            Hawk.put("WuHuFragmentData", wuHuEditBean);
                        }


                    }
                    if (wuHuEditBeanList == null || wuHuEditBeanList.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                        mTestFragments.put(key++, WuHuFragment.newInstance(fragmentPos + ""));
                        fragmentPos++;
                    }


                    //===
                }


            }
        }*/

    }

    public void showRightDialog() {
        //?????????dialog????????????
        inflate = LayoutInflater.from(WuHuActivity3.this).inflate(R.layout.dialog_edit_wuhu, null);
        //?????????dialog????????????
        dialog = new MyDialog(WuHuActivity3.this, R.style.BottomSheetEdit);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //????????????????????????????????????????????????????????????(false???????????????????????????????????????????????????)
        dialog.setCanceledOnTouchOutside(true);
        //??????????????????Dialog
        dialog.setContentView(inflate);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //???????????????????????????
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //??????
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //???????????????
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });


        View line = inflate.findViewById(R.id.line);
        RadioGroup line_colors = inflate.findViewById(R.id.line_colors);
        RadioGroup theme_colors = inflate.findViewById(R.id.theme_colors);
        myListView = inflate.findViewById(R.id.myList_view);
        company_name = inflate.findViewById(R.id.company_name);
        tittle2 = inflate.findViewById(R.id.tittle2);

        add_topic_rl = inflate.findViewById(R.id.add_topic_rl);
        dialg_rl_root = inflate.findViewById(R.id.dialg_rl_root);
        sava_all = inflate.findViewById(R.id.sava_all);
        Log.d("reyeyrty222", wuHuEditBeanList.size() + "");
        //myListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //myListView.addFooterView(foodView, null, false);
        //   myListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        //  ????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WuHuActivity3.this);
        myListView.setLayoutManager(linearLayoutManager);
        wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
        myListView.setAdapter(wuHuListAdapter);
        wuHuListAdapter.notifyDataSetChanged();
        if (Hawk.contains("company_name")) {
            String str = Hawk.get("company_name");
            company_name.setText(str);
        }
        if (Hawk.contains("tittle2")) {
            String str = Hawk.get("tittle2");
            tittle2.setText(str);
        }

        line_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                switch (checkedId) {

                    case R.id.color_rb1:
                        bundle.putString("refreshType", "1");
                        line.setBackgroundColor(Color.parseColor("#EA4318"));
                        lineFlag = "1";
                        break;
                    case R.id.color_rb2:
                        bundle.putString("refreshType", "2");
                        line.setBackgroundColor(Color.parseColor("#1D1D1D"));
                        lineFlag = "2";
                        break;

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wsUpdata(lineFlag, constant.CHANGE_COLOR_BG);
                    }
                }).start();
             /*   intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);*/
            }
        });

        theme_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                switch (checkId) {
                    case R.id.color_rb3:
                        bundle.putString("refreshType", "3");
                        themFlag = "3";

                        break;
                    case R.id.color_rb4:
                        bundle.putString("refreshType", "4");
                        themFlag = "4";

                        break;
                    case R.id.color_rb5:
                        bundle.putString("refreshType", "5");
                        themFlag = "5";

                        break;
                    case R.id.color_rb6:
                        bundle.putString("refreshType", "6");
                        themFlag = "6";

                        break;
                    case R.id.color_rb7:
                        bundle.putString("refreshType", "7");
                        themFlag = "7";
                        break;

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        wsUpdata(themFlag, constant.CHANGE_COLOR_BG);
                    }
                }).start();

              /*  intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);*/
            }
        });

        add_topic_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                editListBean.setPos(wuHuEditBeanList.size() + "");
                editListBean.setSubTopics(wuHuEditBeanList.get(wuHuEditBeanList.size() - 1).getSubTopics());
                editListBean.setReportingUnit(wuHuEditBeanList.get(wuHuEditBeanList.size() - 1).getReportingUnit());
                wuHuEditBeanList.add(editListBean);
                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                wuHuListAdapter.notifyDataSetChanged();

                if (Hawk.contains("WuHuFragmentData")) {
                    WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    //????????????????????????fragment
                    wsUpdata(wuHuEditBean, constant.WUHUADDFRAGMENT);
                }

            }
        });
        dialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {
/*
                List<WuHuEditBean.EditListBean>  saveAll=new ArrayList<>();
                saveAll.clear();

                if (Hawk.contains("WuHuFragmentData")) {
                    WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                    saveAll.addAll(wuHuEditBean.getEditListBeanList());
                    wuHuEditBean.setTopics(company_name.getText().toString());
                    wuHuEditBean.setTopic_type(tittle2.getText().toString());
                    wuHuEditBean.setLine_color(lineFlag);
                    wuHuEditBean.setThem_color(themFlag);
                    for (int i = 0; i < saveAll.size(); i++) {
                        saveAll.get(i).setTopics(company_name.getText().toString());
                        saveAll.get(i).setTopic_type(tittle2.getText().toString());
                        saveAll.get(i).setLine_color(lineFlag);
                        saveAll.get(i).setThem_color(themFlag);

                        saveAll.get(i).setPos(wuHuEditBeanList.get(i).getPos());
                        saveAll.get(i).setReportingUnit(wuHuEditBeanList.get(i).getReportingUnit());
                        saveAll.get(i).setSubTopics(wuHuEditBeanList.get(i).getSubTopics());
                        saveAll.get(i).setParticipantUnits(wuHuEditBeanList.get(i).getParticipantUnits());
                    }
                    wuHuEditBean.setEditListBeanList(saveAll);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    //??????????????????
                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUALL);
                }

                Hawk.put("company_name", company_name.getText().toString());
                Hawk.put("tittle2", tittle2.getText().toString());
                Toast.makeText(WuHuActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();*/


            }
        });
        sava_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Hawk.contains("WuHuFragmentData")) {
                    WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                    wuHuEditBean.setTopics(company_name.getText().toString());
                    wuHuEditBean.setTopic_type(tittle2.getText().toString());
                    wuHuEditBean.setLine_color(lineFlag);
                    wuHuEditBean.setThem_color(themFlag);

                    for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                        wuHuEditBeanList.get(i).setTopics(company_name.getText().toString());
                        wuHuEditBeanList.get(i).setTopic_type(tittle2.getText().toString());
                        wuHuEditBeanList.get(i).setLine_color(lineFlag);
                        wuHuEditBeanList.get(i).setThem_color(themFlag);
                        Log.d("fsdfgsggsg", "??????   " + i + "  ????????????????????????" + wuHuEditBeanList.get(i).getParticipantUnits());
                    }

                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    //??????????????????
                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUALL);
                }

                Hawk.put("company_name", company_name.getText().toString());
                Hawk.put("tittle2", tittle2.getText().toString());
                Toast.makeText(WuHuActivity3.this, "??????????????????", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        //????????????Activity???????????????
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay(); // ?????????????????????
        wlp.gravity = Gravity.RIGHT;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.4);//?????????
        wlp.height = 1600;//?????????
        window.setAttributes(wlp);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
        if (serviceConnection != null) {
            Log.d("gfdgfdhdfhdfhgdh11", "???????????????????????????");
            unbindService(serviceConnection);
        }

        if (mycatalogBroadcastReceiver != null) {
            unregisterReceiver(mycatalogBroadcastReceiver);
        }
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * websocket???????????????????????????
     */
    private void wsUpdata(Object obj, String packType) {

        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(packType);
        bean.setBody(obj);
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);

 /*       new Thread(new Runnable() {
            @Override
            public void run() {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(0);
                bean.setUserMac_id(FLUtil.getMacAddress());
                bean.setPackType(packType);
                bean.setBody(obj);
                String strJson = new Gson().toJson(bean);
                JWebSocketClientService.sendMsg(strJson);
            }
        }).start();*/

    }

    @Override
    protected void initData() {
        Log.d("wrewarawrawer", "initData");
      /*  if (!UserUtil.ISCHAIRMAN) {
            edit_ll.setVisibility(View.GONE);
        }*/
        Log.d("reyeyrty333", UserUtil.ISCHAIRMAN + "");
        //  loadData();
    }

    @Override
    public void onBackPressed() {
        if (!UserUtil.ISCHAIRMAN) {
            return;
        } else {
            showFinishMeetingDialog();
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        }
    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

// ???????????????????????????View????????????????????????EditText(????????????????????????????????????????????????????????????)

            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {

                hideSoftInput(v.getWindowToken());

            }

        }

        return super.dispatchTouchEvent(ev);

    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //????????????????????????location??????
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // ??????????????????????????????????????????EditText?????????
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    private String getType(String end) {
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return "1";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return "2";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return "3";
        } else if ((end.equals("ppt") || end.equals("pptx"))) {
            return "4";
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return "4";
        } else if (end.equals("doc") || end.equals("docx")) {
            return "4";
        } else if (end.equals("pdf") || end.equals("txt")) {
            return "4";
        } else {
            return "0";
        }

    }

    /**
     * ?????????activity??????????????????
     *
     * @return true????????? false????????????
     */
    private boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    private void hideSoftInput(IBinder token) {

        if (token != null) {

            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            im.hideSoftInputFromWindow(token,

                    InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }

    private int getIamge(String end) {
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return R.mipmap.ic_mp3;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return R.mipmap.ic_mp4;
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return R.mipmap.ic_image;
        } else if (end.equals("ppt") || end.equals("pptx")) {
            return R.mipmap.ic_ppt;
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return R.mipmap.ic_excle;
        } else if (end.equals("doc") || end.equals("docx")) {
            return R.mipmap.ic_word;
        } else if (end.equals("pdf")) {
            return R.mipmap.ic_pdf;
        } else if (end.equals("txt")) {
            return R.mipmap.ic_txt;
        } else {
            return R.mipmap.ic_file;
        }

    }


    private String getFileType(String end) {
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return constant.OTHER;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return constant.VIDEO;
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return constant.IMAGE;
        } else if ((end.equals("ppt") || end.equals("pptx"))) {
            return constant.DOCUMENT;
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return constant.DOCUMENT;
        } else if (end.equals("doc") || end.equals("docx")) {
            return constant.DOCUMENT;
        } else if (end.equals("pdf")) {
            return constant.DOCUMENT;
        } else if (end.equals("txt")) {
            return constant.DOCUMENT;
        } else {
            return constant.OTHER;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            if (in.getAction().equals(constant.ADD_FRAGMENT_BROADCAST)) {
                mTestFragments.put(key++, WuHuFragment2.newInstance(fragmentPos + ""));
                fragmentPos++;
                mPagerAdapter.notifyDataSetChanged();

            } else if (in.getAction().equals(constant.REFRESH_BROADCAST)) {


            } else if (in.getAction().equals(constant.CHANGE_CATALOG_BROADCAST)) {
                String pos = in.getStringExtra("catalog");
                mViewPager.setCurrentItem(Integer.valueOf(pos));
                saveAllData();
            }


        }
    }
}
