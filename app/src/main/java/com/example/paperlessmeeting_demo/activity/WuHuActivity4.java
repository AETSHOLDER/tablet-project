package com.example.paperlessmeeting_demo.activity;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/*
 * 去掉所有广播
 * */
public class WuHuActivity4 extends BaseActivity implements View.OnClickListener, WuHuNewTopicAdapter.saveSeparatelyInterface, WuHuNewTopicAdapter.deletSeparatelyInterface, WuHuNewTopicAdapter.addSeparatelyInterface, NettyListener {


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
    private List<String> stringsIp = new ArrayList<>();//临时会议存储各个设备Ip
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
    //切换目录广播
    private MyBroadcastReceiver mycatalogBroadcastReceiver;
    private InetAddress address;
    private boolean isBind = false;
    /*
     * 临时会议分享文件，service和InetAddress
     * */
    private BroadcastUDPFileService broadcastUDPFileService;
    private BroadcastUDPFileService.BroadcastUDPFileServiceBinder binder;
    private WuHuEditBean wuHuEditBean;
    //表示红线的flag
    private String lineFlag = "1";
    //标识主题的颜色
    private String themFlag = "3";
    private boolean isEditRl = false;
    private SocketShareFileManager socketShareFileManager;
    private ServiceConnection serviceConnection;
    private Handler handler;
    private Runnable runnable;
    private ArrayList<WuHuEditBean.EditListBean> editListBeans = new ArrayList<>();
    private List<WuHuEditBean.EditListBean.FileListBean> shareFileBeans = new ArrayList<>();//其他设备分享得到的文件集合
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//其他设备分享得到的文件夹路径
    private String netFilePath = Environment.getExternalStorageDirectory() + constant.WUHU_NET_FILE;//网络请求得到的文件夹路径
    private String VOTE_FILE = Environment.getExternalStorageDirectory() + constant.VOTE_FILE;
    private View foodView;
    private CompletedView completedView;
    private ImageView result_ima;
    private TextView tips;
    private String sharePushPos;
    private int allFileNum = 0;//总文件数量
    private int dowLoadNum = 0;//下载成功的文件数量
    private List<WuHuNetWorkBean> failList = new ArrayList<>();//下载失败的文件集合
    private ProgressBar progressBar;//显示文件传输进度
    private List<WuHuNetWorkBean> filesList = new ArrayList<>();//储存所有的文件地址

    // ----------------屏幕录制投屏模块
    private String TAG = "WuHuActivity";
    private Dialog shareScreenDialog;           // 投屏code弹框
    private VerificationCodeView shareScreen_codeview;
    private String my_code;
    private String mIp = "192.168.1.1";  // 获取到的服务端IP
    private MediaProjectionManager mMediaProjectionManage;
    private MediaProjection mediaProjection;
    private static final int ACTIVITY_RESULT_CODE_SCREEN = 110;
    private MyServiceConnect mConnect;

    private int upLoadNum = 0;//每个文件的切片数
    private int upLoadFileNum = 0;
    private int locaFileNum = 0;
    private int size = 1024 * 1024 * 100;
    private String upLoadFileType;
    private FileCutUtils fileCutUtils;  //文件切割工具类
    private int littlefilecount;  //切割文件个数
    private List<File> littlefilelist = new ArrayList<>();
    private String endStrAll;
    private int isAdd = 0;//议题集合是否添加索引为0的议题；
    private int littelFilePos = 0;
    private int issuePos = 0;//议题下标
    private int issueFileNum = 0;//每个议题下的文件
    private List<FileFragmentationBean> fileFragmentationBeans = new ArrayList<>();
    public FreshenFragmentInterface freshenFragment;

    public FreshenFragmentInterface getFreshenFragment() {
        return freshenFragment;
    }

    public void setFreshenFragment(FreshenFragmentInterface freshenFragment) {
        this.freshenFragment = freshenFragment;
    }

    /**
     * 点击返回时间
     */

    private long time = 2000;
    private long first_time = 0;
    private String meetingTime;//会议-月日时分
    private String week = "";
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
                    Toast.makeText(WuHuActivity4.this, "正在接受文件", Toast.LENGTH_SHORT).show();
                    break;
                case 33:
                    //分享 有文件
                    Toast.makeText(WuHuActivity4.this, "文件已成功接收，请在文件管理页面查看", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(WuHuActivity4.this, "文件接收失败", Toast.LENGTH_SHORT).show();
                    break;
                case 400:
                    //推送  有文件
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
                    fileBean400.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                  /*  bundle400.putString("flag", "2");
                    bundle400.putString("filePath", fileListBean1.getMd5Path());
                    bundle400.putString("topicPos", pos);
                    intent400.putExtras(bundle400);
                    intent400.setAction(constant.SHARE_FILE_BROADCAST);
                    sendBroadcast(intent400);*/
                    if (fileBean400.getFile_type().equals("3")) {
                        //防止普通参会人员重复打开页面
                    /*    if ( isActivityTop(ActivityImage.class,WuHuActivity.this)){
                            Intent  intent8=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                            Bundle bundle2=new Bundle();
                            bundle2.putString("url",fileBean.getPath());
                            intent8.putExtras(bundle2);
                           sendBroadcast(intent8);

                        }*/
                        Activity topActivity = ActivityUtils.getTopActivity();
                        if (topActivity != null) {
                            // 如果是在签批内，先关闭，再进入,否则未销毁tbs,会一直显示加载中(看情况添加用户提示)
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
                        intent99.setClass(WuHuActivity4.this, ActivityImage.class);
                        intent99.putExtra("url", fileBean400.getPath());
                        intent99.putExtra("isOpenFile", true);
                        intent99.putExtra("isNetFile", false);
                        startActivity(intent99);
                        Log.d("重复打开  ActivityImage activity", "11111111");
                    } else if (fileBean400.getFile_type().equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                // 如果是在签批内，先关闭，再进入,否则未销毁tbs,会一直显示加载中(看情况添加用户提示)
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

                            intent400.setClass(WuHuActivity4.this, SignActivity.class);
                            intent400.putExtra("url", fileBean400.getPath());
                            intent400.putExtra("isOpenFile", true);
                            intent400.putExtra("isNetFile", false);
                            intent400.putExtra("tempPath", false);
                            intent400.putExtra("fileName", fileBean400.getName());
                            startActivity(intent400);
                            Log.d("重复打开  SignActivity activity", "2222222");
                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(WuHuActivity4.this, "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(fileBean400.getPath(), WuHuActivity4.this));
                                }
                            });
                        }
                    }
                    break;
                case 500:
                    //推送 无文件
                    SharePushFileBean pushHaveFileBean = new SharePushFileBean();
                    WuHuEditBean.EditListBean.FileListBean fileListBean2 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    pushHaveFileBean.setHave(false);
                    pushHaveFileBean.setMac(fileListBean2.getMac());
                    pushHaveFileBean.setPos(fileListBean2.getPos());
                    wsUpdata(fileListBean2, constant.FILERESPONDPUSH);
                    break;
                case 600:
                    //分享  有文件
                    WuHuEditBean.EditListBean.FileListBean fileListBean3 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    SharePushFileBean shareNoFileBean = new SharePushFileBean();
                    shareNoFileBean.setPos(fileListBean3.getPos());
                    shareNoFileBean.setHave(true);
                    shareNoFileBean.setMac(fileListBean3.getMac());
                    wsUpdata(shareNoFileBean, constant.FILERESPONDSHARE);
                    break;
                case 700:
                    //分享 无文件
                    SharePushFileBean shareHaveFileBean = new SharePushFileBean();
                    WuHuEditBean.EditListBean.FileListBean fileListBean4 = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    shareHaveFileBean.setHave(false);
                    shareHaveFileBean.setMac(fileListBean4.getMac());
                    shareHaveFileBean.setPos(fileListBean4.getPos());
                    wsUpdata(shareHaveFileBean, constant.FILERESPONDSHARE);
                    break;
                case 88:
                    //推送 有文件
                    if (UserUtil.ISCHAIRMAN) {
                        return;
                    }
                    Toast.makeText(WuHuActivity4.this, "推送文件已成功接收，请在文件管理页面查看", Toast.LENGTH_SHORT).show();
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
                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    bundle.putString("flag", "2");
                    bundle.putString("filePath", filePath);
                    bundle.putString("topicPos", pos);
                    intent.putExtras(bundle);
                    intent.setAction(constant.SHARE_FILE_BROADCAST);
                    //  sendBroadcast(intent);
                    if (fileBean.getFile_type().equals("3")) {
                        //防止普通参会人员重复打开页面
                    /*    if ( isActivityTop(ActivityImage.class,WuHuActivity.this)){
                            Intent  intent8=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                            Bundle bundle2=new Bundle();
                            bundle2.putString("url",fileBean.getPath());
                            intent8.putExtras(bundle2);
                           sendBroadcast(intent8);

                        }*/
                        Activity topActivity = ActivityUtils.getTopActivity();
                        if (topActivity != null) {
                            // 如果是在签批内，先关闭，再进入,否则未销毁tbs,会一直显示加载中(看情况添加用户提示)
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
                        intent9.setClass(WuHuActivity4.this, ActivityImage.class);
                        intent9.putExtra("url", fileBean.getPath());
                        intent9.putExtra("isOpenFile", true);
                        intent9.putExtra("isNetFile", false);
                        startActivity(intent9);
                    } else if (fileBean.getFile_type().equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                // 如果是在签批内，先关闭，再进入,否则未销毁tbs,会一直显示加载中(看情况添加用户提示)
                                if (topActivity.getLocalClassName().contains("SignActivity")) {
                                    // 防止前一个打开签批的立即结束
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
                            intent.setClass(WuHuActivity4.this, SignActivity.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", false);
                            intent.putExtra("tempPath", false);
                            intent.putExtra("fileName", fileBean.getName());
                            startActivity(intent);

                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(WuHuActivity4.this, "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(fileBean.getPath(), WuHuActivity4.this));
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
                case 5:
                    Toast.makeText(WuHuActivity4.this, "文件发送成功哈哈哈哈！", Toast.LENGTH_SHORT).show();
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
                        //临时存放的议题集合
                        List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        if (wuHuEditBean != null && wuHuEditBean.getEditListBeanList() != null) {

                            copyEdList.addAll(wuHuEditBean.getEditListBeanList());

                        }

                        //遍历更新当前议题下的文件
                        List<WuHuEditBean.EditListBean.FileListBean> shareService = new ArrayList<>();
                        shareService.clear();
                        if (copyEdList != null && copyEdList.size() > 0) {

                            for (int i = 0; i < copyEdList.size(); i++) {
                                //每一个议题
                                WuHuEditBean.EditListBean editListBean = copyEdList.get(i);

                                if (editListBean.getPos().equals(sharePushPos)) {
                                    if (editListBean.getFileListBeanList() != null) {
                                        shareService.addAll(editListBean.getFileListBeanList());
                                    }
                                    //把当前本地议题文件集合放入对应的议题
                                    shareService.addAll(shareFiles);
                                    //去除重复分享和推送的文件
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
                        //防止普通参会人员重复打开页面
                    /*    if ( isActivityTop(ActivityImage.class,WuHuActivity.this)){
                            Intent  intent8=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                            Bundle bundle2=new Bundle();
                            bundle2.putString("url",fileBean.getPath());
                            intent8.putExtras(bundle2);
                           sendBroadcast(intent8);

                        }*/
                        Activity topActivity = ActivityUtils.getTopActivity();
                        if (topActivity != null) {
                            // 如果是在签批内，先关闭，再进入,否则未销毁tbs,会一直显示加载中(看情况添加用户提示)
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
                        intent9.setClass(WuHuActivity4.this, ActivityImage.class);
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
                                // 如果是在签批内，先关闭，再进入,否则未销毁tbs,会一直显示加载中(看情况添加用户提示)
                                if (topActivity.getLocalClassName().contains("SignActivity")) {
                                    // 防止前一个打开签批的立即结束
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
                            intent.setClass(WuHuActivity4.this, SignActivity.class);
                            if (path != null) {
                                intent.putExtra("url", path);
                                intent.putExtra("isOpenFile", true);
                                intent.putExtra("isNetFile", false);
                                intent.putExtra("tempPath", false);
                                intent.putExtra("fileName", name);
                                startActivity(intent);

                            }


                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(WuHuActivity4.this, "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    if (path != null) {

                                        startActivity(FileUtils.openFile(path, WuHuActivity4.this));
                                    }

                                }
                            });
                        }
                    }

                    break;

            }


        }
    };


    /*
     * 遍历得到分享和推送的文件并且提交到数据库
     * */
    private void getShareFileComit(String textNub) {
        //分享和推送时先复制到分享文件夹
        File path = new File(fileShare);
        File[] files = path.listFiles();// 读取
        if (files == null) {
            return;
        }
        shareFileBeans.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (UserUtil.object) {

                    //子线程开始
                    String content = "";
                    if (files != null) {
                        // 先判断目录是否为空，否则会报空指针
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
                                        fileBean.setFileMd5(Md5Util.getFileMD5(file));//MD5文件校验
                                        fileBean.setPos(pos);
                                        fileBean.setNet(false);
                                        fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                                        //  fileBean.setType(endStr);
                                        fileBean.setType(getFileType(endStr));
                                        shareFileBeans.add(fileBean);
                                    }

                                }

                            }
                        }
                        //去除重复分享和推送的文件
                        for (int i = 0; i < shareFileBeans.size() - 1; i++) {
                            for (int j = shareFileBeans.size() - 1; j > i; j--) {
                                if (shareFileBeans.get(j).getName().equals(shareFileBeans.get(i).getName()) && shareFileBeans.get(j).getPos().equals(shareFileBeans.get(i).getPos())) {
                                    shareFileBeans.remove(j);
                                }
                            }
                        }

                        //发送消息跟新文件列表
                        Message shareMsg = new Message();
                        shareMsg.what = 800;
                        shareMsg.obj = shareFileBeans;
                        mHander.sendMessage(shareMsg);

                    }


                }

                //子线程执行结束
            }
        }).start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
      /*  myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.ADD_FRAGMENT_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);
        Log.d("wrewarawrawer", "onCreate");

        mycatalogBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter4 = new IntentFilter();
        filter4.addAction(constant.CHANGE_CATALOG_BROADCAST);
        registerReceiver(mycatalogBroadcastReceiver, filter4);*/

        wuHuEditBeanList.clear();
        wuHuListAdapter = new WuHuNewTopicAdapter(WuHuActivity4.this.mContext, wuHuEditBeanList);
        wuHuListAdapter.setSaveSeparatelyInterface(this);
        wuHuListAdapter.setDeletSeparatelyInterface(this);
        wuHuListAdapter.setAddSeparatelyInterface(this);

        mPagerAdapter = new PagerAdapter2(getSupportFragmentManager(), mTestFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(mTestFragments.size()-1);
        UDPBroadcastManager.getInstance().stopReceiveUDP();
        // 如果是临时会议,判断是否是主席
        if (UserUtil.isTempMeeting) {
            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageClient)) {
                //  客户端默认非主席
                UserUtil.ISCHAIRMAN = false;
                String ipUrl = getIntent().getStringExtra("ip");
                String isReuse = getIntent().getStringExtra("isreuse");
                Hawk.put("isreuse", isReuse);//储存是否重复利用会议模板标识 //1:代表复用模板  2：代表不复用模板 3：代表没有模板
                UserUtil.serverIP = ipUrl;
                if (!StringUtils.isEmpty(ipUrl)) {
                    UrlConstant.TempWSIPString = "ws://" + ipUrl + ":" + UrlConstant.port;
//                    Log.e("临时会议", "临时会议的server IP ======" + UrlConstant.TempWSIPString);
                }
                //  连接websocket(server，client均要连)
                JWebSocketClientService.initSocketClient();

            } else if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer)) {
                UserUtil.ISCHAIRMAN = true;
                String code = getIntent().getStringExtra("code");
                String isReuse = getIntent().getStringExtra("isreuse");
                Hawk.put("isreuse", isReuse);//储存是否重复利用会议模板标识 //1:代表复用模板  2：代表不复用模板 3：代表没有模板
                //  alertCodeDialog(code);

                //  广播四位数code以及IP地址
                UDPBroadcastManager.getInstance().sendUDPWithCode(code + "/" + isReuse);
                // 开启socket服务器
                ServerManager.getInstance().startMyWebsocketServer(UrlConstant.port);
                UrlConstant.TempWSIPString = "ws://" + FLUtil.getIPAddress() + ":" + UrlConstant.port;
                // 主席的client在收到server创建成功后连接
            }
        }

        EventBus.getDefault().register(this);
        // loadData();
        if (UserUtil.ISCHAIRMAN) {
            cc1.setVisibility(View.VISIBLE);
            cc2.setVisibility(View.GONE);
            if (!UserUtil.isNetDATA) {
                finish_meeting.setText("保存会议");
                vote_ll.setVisibility(View.GONE);
            }

            //只有服务端才有的方法：初始化议题数据并提交到服务端
            //  initiaServerData();
        } else {
            cc1.setVisibility(View.GONE);
            cc2.setVisibility(View.VISIBLE);
        }
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
                            datagramSocket = new DatagramSocket(null);
                            datagramSocket.setReuseAddress(true);
                            datagramSocket.setBroadcast(true);
                            datagramSocket.bind(new InetSocketAddress(constant.EXTRAORDINARY_MEETING_PORT));


                            DatagramPacket datagramPacket = new DatagramPacket(bytes,
                                    bytes.length);
                            while (true) {
                                String strMsg = null;
                                int p = datagramPacket.getPort();
                                // 准备接收数据
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
                                         * 接收分享文件
                                         * */
                                        Log.e("5555555", "接收分享文件");
                                        socketShareFileManager.SendFlag("1");
                                    } else if (strMsg.contains(constant.TEMP_MEETINGPUSH_FILE)) {
                                        /*
                                         * 接收推送文件
                                         * */
                                        Log.d("SendFlag5555555", "接收推送文件");
                                        socketShareFileManager.SendFlag("2");
                                    } else if (strMsg.contains(constant.TEMP_VOTE_IMAGE_FILE)) {
                                        /*
                                         * 投票图片
                                         * */
                                        socketShareFileManager.SendFlag("3");

                                    } else if (strMsg.contains(constant.FINISH_SHARE_SCEEN)) {
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
                // 单播对接上，进行tcp 连接
                Log.e(TAG, "马上进行tcp 连接 ip===" + ip);

                mIp = ip;
                MeetingAPP.getInstance().reinitSocket(mIp);
                MeetingAPP.getInstance().getNettyClient().setListener(WuHuActivity4.this);

                if (!MeetingAPP.getInstance().getNettyClient().getConnectStatus()) {
                    MeetingAPP.getInstance().getNettyClient().connect();
                }
            }

            @Override
            public void udpDisConnec(String message) {
            }
        });
    }

    private void initiaServerData() {

        if (UserUtil.isNetDATA) {
            filesList.clear();
            dowLoadNum = 0;
            showFileTransferDialog();
            List<WuHuEditBean.EditListBean> wb = new ArrayList<>();
            //投票集合接受类
            List<WuHuMeetingListResponse.ContentDTO.VoteListBeanDTO.DataDTO> voteList = new ArrayList<>();

            WuHuMeetingListResponse wuHuMeetingListResponse;
            if (Hawk.contains("WuHuMeetingListResponse")) {
                failList.clear();//清空下载失败的url
                wuHuMeetingListResponse = Hawk.get("WuHuMeetingListResponse");
                WuHuMeetingListResponse.ContentDTO contentDTO = wuHuMeetingListResponse.getContent();
                if (wuHuMeetingListResponse.getName() != null) {
                    Hawk.put("company_name", wuHuMeetingListResponse.getName());
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
//gtgwrtwwrtwt大文件上传分片--
                }

                List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO> netEditListBeanList = new ArrayList<>();//议题集合
                netEditListBeanList.size();
                netEditListBeanList.addAll(contentDTO.getEditListBeanList());
                for (int i = 0; i < netEditListBeanList.size(); i++) {
                    WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanListDTO = netEditListBeanList.get(i);//获取单个议题
                    List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO.FileListBeanListDTO> netLocaFiles = new ArrayList<>();//存放议题对应的文件集合
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
                            allFileNum++;
                            filesList.add(wuHuNetWorkBean);
                        }
                    }
                }
                if (filesList.size() == 0) {
                    if (networkFileDialog != null) {
                        networkFileDialog.dismiss();
                    }

                } else {

                    //遍历议题下载文件
                    for (int i = 0; i < filesList.size(); i++) {
                        WuHuNetWorkBean wuHuNetWorkBean = filesList.get(i);
                        DownloadUtil.get().download(wuHuNetWorkBean.getUrl(), netFilePath, wuHuNetWorkBean.getName(), new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                dowLoadNum++;

                                Log.d("dffasfsdfafafdowLoadNum11", dowLoadNum + "    " + filesList.size());
                                Log.d("gtgwrtwwrtwt大文件上传分片----00", dowLoadNum + "    文件名： " + file.getName() + "   下载文件大小 " + Formatter.formatFileSize(WuHuActivity4.this, file.length()));
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


                if (isAdd == 0) {
                    //索引0充当目录页面
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
                    //会议名称，横线颜色  背景图，会议类型
                    wuHuEditBean.set_id(wuHuMeetingListResponse.get_id());
                    wuHuEditBean.setTopics(contentDTO.getTopics());
                    wuHuEditBean.setTopic_type(contentDTO.getTopic_type());
                    wuHuEditBean.setLine_color("1");
                    wuHuEditBean.setThem_color("2");
                    List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO> editListBeanListDTOS = new ArrayList<>();
                    editListBeanListDTOS.clear();
                    //议题集合赋值
                    editListBeanListDTOS.addAll(contentDTO.getEditListBeanList());
                    for (int i = 0; i < editListBeanListDTOS.size(); i++) {
                        WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanListDTO = editListBeanListDTOS.get(i);
                        //创建本地议题接受类
                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setLine_color("1");
                        editListBean.setPos(i + "");
                        editListBean.setThem_color("2");
                        editListBean.setParticipantUnits(editListBeanListDTO.getParticipantUnits());
                        editListBean.setReportingUnit(editListBeanListDTO.getReportingUnit());
                        editListBean.setSubTopics(editListBeanListDTO.getSubTopics());
                        //赋值主席的本地文件（包含分享和本地文件）
                        List<WuHuEditBean.EditListBean.FileListBean> editFiles = new ArrayList<>();
                        editFiles.clear();
                        if (editListBeanListDTO.getFileListBeanList() != null && editListBeanListDTO.getFileListBeanList().size() > 0) {
                            for (int k = 0; k < editListBeanListDTO.getFileListBeanList().size(); k++) {
                                WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO.FileListBeanListDTO localFilesDTO = editListBeanListDTO.getFileListBeanList().get(k);
                                Log.d("gfdfddhdh下载时的文件路径    ", localFilesDTO.getName() + "   " + localFilesDTO.getPath());
                                WuHuEditBean.EditListBean.FileListBean fileListBean = new WuHuEditBean.EditListBean.FileListBean(localFilesDTO.getName(), localFilesDTO.getPath(), "", "");
                                fileListBean.setPos((Integer.valueOf(localFilesDTO.getPos()) + 1) + "");
                                String endStr = localFilesDTO.getName().substring(localFilesDTO.getName().lastIndexOf(".") + 1);
                                fileListBean.setResImage(getIamge(endStr));
                                fileListBean.setFile_type(getType(endStr));
                                fileListBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                                fileListBean.setType(getFileType(endStr));
                                // fileListBean.setNet(localFilesDTO.getNet());
                                fileListBean.setNet(true);
                                fileListBean.setLocalPath(netFilePath + localFilesDTO.getName());
                                Log.d("gfdfddhdh下载后的文件路径    ", netFilePath + localFilesDTO.getName());
                                editFiles.add(fileListBean);
                            }
                        }
                        editListBean.setLocalFiles(editFiles);
                        wb.add(editListBean);
                    }
                    //提交到主席service端
                    wuHuEditBean.setEditListBeanList(wb);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                    Log.d("fsdgsgsgf", voteList.size() + "");
                    //整理投票类
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

                                    if (temporBeanListDTOS.get(k).getContent().contains("jpg") || temporBeanListDTOS.get(k).getContent().contains("gif") || temporBeanListDTOS.get(k).getContent().contains("png") || temporBeanListDTOS.get(k).getContent().contains("jpeg") ||
                                            temporBeanListDTOS.get(k).getContent().contains("bmp")) {
                                        String endStr = temporBeanListDTOS.get(k).getContent().substring(temporBeanListDTOS.get(k).getContent().lastIndexOf(".") + 1);
                                        if (endStr.equals("jpg") || endStr.equals("gif") || endStr.equals("png") ||
                                                endStr.equals("jpeg") || endStr.equals("bmp")) {
                                            String[] strConten = temporBeanListDTOS.get(k).getContent().split("/");
                                            String fileName = "";
                                            if (strConten.length > 0) {
                                                fileName = strConten[strConten.length - 1];
                                                //投票主席发起的  若是主席  直接显示主席投票路径
                                                if (UserUtil.ISCHAIRMAN) {
                                                    options_list.add(temporBeanListDTOS.get(k).getContent());
                                                    //若是参会端直接显示参会端路径
                                                } else {
                                                    options_list.add(VOTE_FILE + fileName);

                                                }
                                            }


                                        }

                                    } else {

                                        options_list.add(temporBeanListDTOS.get(k).getContent());
                                    }


                                }

                                VoteListBean.VoteBean.TemporBean temporBean = new VoteListBean.VoteBean.TemporBean();
                                if (temporBeanListDTOS.get(k).getChecked() != null) {
                                    temporBean.setChecked(temporBeanListDTOS.get(k).getChecked());

                                }

                                if (temporBeanListDTOS.get(k).getContent() != null) {
                                    if (temporBeanListDTOS.get(k).getContent().contains("jpg") || temporBeanListDTOS.get(k).getContent().contains("gif") || temporBeanListDTOS.get(k).getContent().contains("png") || temporBeanListDTOS.get(k).getContent().contains("jpeg") ||
                                            temporBeanListDTOS.get(k).getContent().contains("bmp")) {
                                        String endStr = temporBeanListDTOS.get(k).getContent().substring(temporBeanListDTOS.get(k).getContent().lastIndexOf(".") + 1);
                                        if (endStr.equals("jpg") || endStr.equals("gif") || endStr.equals("png") ||
                                                endStr.equals("jpeg") || endStr.equals("bmp")) {
                                            String[] strConten = temporBeanListDTOS.get(k).getContent().split("/");
                                            String fileName = "";
                                            if (strConten.length > 0) {
                                                fileName = strConten[strConten.length - 1];
                                                //投票主席发起的  若是主席  直接显示主席投票路径
                                                if (UserUtil.ISCHAIRMAN) {
                                                    temporBean.setContent(temporBeanListDTOS.get(k).getContent());
                                                    //若是参会端直接显示参会端路径
                                                } else {
                                                    temporBean.setContent(VOTE_FILE + fileName);
                                                }
                                            }
                                        }

                                    } else {
                                        temporBean.setContent(temporBeanListDTOS.get(k).getContent());
                                    }

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

                        // 下面两行数据不能颠倒
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
                //1:代表复用模板  2：代表不复用模板 3：代表没有模板
                if (isreuse.equals("2") || isreuse.equals("3")) {
                    wuHuEditBeanList.clear();
                    WuHuEditBean wuHuEditBean = new WuHuEditBean();
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    if (Hawk.contains("WuHuFragmentData")) {
                        wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics("区政府会议纪要");
                        wuHuEditBean.setTopic_type("会议纪要");

                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setSubTopics("aa");
                        editListBean.setReportingUnit("aa");
                        editListBean.setParticipantUnits("aa");
                        editListBean.setTopics("区政府会议纪要");
                        editListBean.setPos("0");
                        editListBean.setTopic_type("会议纪要");
                        wuHuEditBeanList.add(editListBean);
                        WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                        editListBean1.setSubTopics("总结2022年");
                        editListBean1.setReportingUnit("汇报单位名称");
                        editListBean1.setParticipantUnits("列席单位名称");
                        editListBean1.setTopics("区政府会议纪要");
                        editListBean1.setTopic_type("会议纪要");
                        editListBean1.setPos("1");
                        wuHuEditBeanList.add(editListBean1);

                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                    }
                } else if (isreuse.equals("1")) {
                    WuHuEditBean templetEuHuEditBean = null;
                    if (Hawk.contains("WuHuFragmentData")) {
                        //有模版直接提交服务器
                        templetEuHuEditBean = Hawk.get("WuHuFragmentData");
                        wsUpdata(templetEuHuEditBean, constant.SUBMITANISSUE);
                    }

                } else {
                    //参会端第一次做主席  选择了模版操作
                    WuHuEditBean wuHuEditBean = new WuHuEditBean();
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    if (Hawk.contains("WuHuFragmentData")) {
                        wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBean.setTopics("区政府会议纪要");
                        wuHuEditBean.setTopic_type("会议纪要");
                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setSubTopics("aa");
                        editListBean.setReportingUnit("aa");
                        editListBean.setParticipantUnits("aa");
                        editListBean.setTopics("区政府会议纪要");
                        editListBean.setPos("0");
                        editListBean.setTopic_type("会议纪要");
                        wuHuEditBeanList.add(editListBean);
                        WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                        editListBean1.setSubTopics("总结2022年");
                        editListBean1.setReportingUnit("汇报单位名称");
                        editListBean1.setParticipantUnits("列席单位名称");
                        editListBean1.setTopics("区政府会议纪要");
                        editListBean1.setTopic_type("会议纪要");
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
        //文件下载完成  通知主线程  弹框消失  数据开始赋值
        if (dowLoadNum == filesList.size()) {
            if (networkFileDialog != null) {
                networkFileDialog.dismiss();
            }
        }
    }

    private void creatVote(VoteListBean.VoteBean bean, String flag) {
        if (UserUtil.isTempMeeting) {
            wsUpdataVote(bean, constant.NEWVOTE, flag);
            //网络会议的投票不展示投票

        }
    }

    /**
     * websocket发送投票更新
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

    //会议结束上传进度弹框
    public void showUpLoadFileDialog() {
        //自定义dialog显示布局
        inflate = LayoutInflater.from(WuHuActivity4.this).inflate(R.layout.wuhu_netfile_progress_dialog, null);
        //自定义dialog显示风格
        dialog = new MyDialog(WuHuActivity4.this, R.style.dialogTransparent);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(false);
        tips = inflate.findViewById(R.id.tips);
        tips.setText("文件上传保存中，请勿操作！");
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
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
        //获取当前Activity所在的窗体
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//设置宽
        wlp.height = (int) (height * 0.25);
        window.setAttributes(wlp);
        dialog.setOnTouchOutside(new MyDialog.onTouchOutsideInterFace() {
            @Override
            public void outSide() {
                Log.d("sdfsdfdsff", "路过~~~~~");
                //  Toast.makeText(getActivity(),"弹框",Toast.LENGTH_SHORT).show();
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
        foodView = LayoutInflater.from(WuHuActivity4.this).inflate(R.layout.list_food, null);
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


        meetingTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA13);//会议-月日时分
        Calendar c1 = Calendar.getInstance();
        int day = c1.get(Calendar.DAY_OF_WEEK);
        switch (day) {
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
        timeTv.setText(meetingTime + " " + week);
        runnable = new Runnable() {
            @Override
            public void run() {
                meetingTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA13);//会议-月日时分
                timeTv.setText(meetingTime + " " + week);
                handler.postDelayed(this, 50);// 50是延时时长
            }
        };

        handler.postDelayed(runnable, 1000 * 60);// 打开定时器，执行操作


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
                intent = new Intent(WuHuActivity4.this, SignListActivity.class);
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
                    Toast.makeText(WuHuActivity4.this, "名字不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Hawk.put(constant.user_name, edit_name.getText().toString());
                UserUtil.user_name = edit_name.getText().toString();
                name.setText(edit_name.getText().toString());

                break;
            case R.id.vote_ll:
                intent = new Intent(WuHuActivity4.this, WuHuVoteActivity.class);
                startActivity(intent);
                break;
            case R.id.finish_ll:
                showFinishMeetingDialog();
                break;
            case R.id.vote_ll1:
                intent = new Intent(WuHuActivity4.this, WuHuVoteActivity.class);
                startActivity(intent);
                break;
            case R.id.consult_ll1:
                intent = new Intent(WuHuActivity4.this, SignListActivity.class);
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
                    CVIPaperDialogUtils.showCustomDialog(WuHuActivity4.this, "是否结束投屏?", "", "结束", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                stopRecording();
                                shareScreen_tv.setText("pc投屏");
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

    /******************************************投屏模块************************************************************/
    /**
     * 投屏弹框
     */
    private void alertShareScreen() {
        shareScreenDialog = new Dialog(WuHuActivity4.this, R.style.update_dialog);
        View view = LayoutInflater.from(WuHuActivity4.this).inflate(R.layout.diaog_sharescreen_code, null);//加载自己的布局
        shareScreen_codeview = view.findViewById(R.id.verificationcodeview);
        shareScreen_codeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {

            }

            @Override
            public void onComplete(View view, String content) {
                if (view == shareScreen_codeview) {
                    closeKeyboardHidden(WuHuActivity4.this);
                    shareScreenDialog.dismiss();
                    my_code = content;
                    Log.e("WuHuActivity", "content===" + content);

                    if (StringUtil.isNullOrEmpty(my_code)) {
                        ToastUtils.showShort("连接码为空!");
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
        if (requestCode == ACTIVITY_RESULT_CODE_SCREEN && resultCode == Activity.RESULT_OK) {
            if (mMediaProjectionManage == null) {
                mMediaProjectionManage = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            }
            mediaProjection = mMediaProjectionManage.getMediaProjection(resultCode, data);
            startRecord();
        }
    }

    private void startRecord() {
        UserUtil.isShareScreen = true;
        shareScreen_tv.setText("断开投屏");
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

    //TCP 连接回调
    @Override
    public void onMessageReaderTimeout() {
    }

    /**
     * 回调客户端接收的信息
     */
    @Override
    public void onMessageResponse(final ReceiveData data) {
        Log.e(TAG, "客户端收到消息===" + data.getHeader().getMainCmd());
        switch (data.getHeader().getMainCmd()) {
            case SocketCmd.SocketCmd_RepAccept:
//                MeetingAPP.mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
                // TODO 获取屏幕发数据 直接调用startActivityForResult在WuHuActivity可以，退出到login再进来没有回调，原因未知，用下面方法解决了。。。
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
                        ToastUtils.showShort("拒绝投屏!!!");
                    }
                });
                break;
        }
    }

    //连接状态变化
    @Override
    public void onServiceStatusConnectChanged(final int statusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (statusCode == NettyListener.STATUS_CONNECT_SUCCESS) {
                    Log.e(TAG, "STATUS_CONNECT_SUCCESS:");
                    if (MeetingAPP.getInstance().getNettyClient().getConnectStatus()) {
                        ToastUtils.showShort("投屏服务连接成功");
                        sendStartData();
                    }
                } else {
//                    ToastUtils.showShort("连接失败");
                    Log.e(TAG, "onServiceStatusConnectChanged:" + statusCode);
                    stopRecording();
                }
            }
        });
    }

    /**
     * 发送开始标识
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
     * 停止录屏
     */
    private void stopRecording() {
        try {
            unbindService(mConnect);
        } catch (Exception e) {

        }
        UserUtil.isShareScreen = false;
//        ToastUtils.showShort("已停止投屏");
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
     * 隐藏软键盘
     **/
    private static void closeKeyboardHidden(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            wuHuEditBean.setPosition(position + "");
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                wuHuEditBeanList.get(i).setTopics(company_name.getText().toString());
                wuHuEditBeanList.get(i).setTopic_type(tittle2.getText().toString());
                wuHuEditBeanList.get(i).setLine_color(lineFlag);
                wuHuEditBeanList.get(i).setThem_color(themFlag);
            }

            wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
            Hawk.put("WuHuFragmentData", wuHuEditBean);
            //更新单个数据
            wsUpdata(wuHuEditBean, constant.REFRASHWuHUSIGLEDATA);
        }
        Hawk.put("company_name", company_name.getText().toString());
        Hawk.put("tittle2", tittle2.getText().toString());
        Toast.makeText(WuHuActivity4.this, "保存成功", Toast.LENGTH_SHORT).show();

    }


    //全部保存数据及点击首页按钮刷新目录页面
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
            Hawk.put("WuHuFragmentData", wuHuEditBean);
            //更新单个数据
            wsUpdata(wuHuEditBean, constant.REFRASHWuHUSIGLEDATA);
        }


    }

    @Override
    public void deletData(int position) {

        CVIPaperDialogUtils.showCustomDialog(WuHuActivity4.this, "", "是否要删除当前议题", "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
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
                        wsUpdata(wuHuEditBean, constant.DELETE_WUHU_FRAGMENT);
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
            //通知其他设备增加fragment
            wsUpdata(wuHuEditBean, constant.WUHUADDFRAGMENT);
        }
    }

    //结束会议
    private void showFinishMeetingDialog() {
        Log.d("Broadcast111", "UrlConstant.baseUrl= " + UrlConstant.baseUrl);
        String str = "";
        if (UserUtil.ISCHAIRMAN) {
            str = "确定要保存会议？";

        } else {
            str = "确定要结束会议？";

        }
        CVIPaperDialogUtils.showCustomDialog(WuHuActivity4.this, str, "请保存/上传好会议文件!!!", "确定", true, new CVIPaperDialogUtils.ConfirmDialogListener() {
            @Override
            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                if (clickConfirm) {
                    stopRecording();
                    if (UserUtil.isTempMeeting) {
                        Log.d("Broadcast22", "UrlConstant.baseUrl= " + UrlConstant.baseUrl);
                        if (UrlConstant.baseUrl.equals("http://192.168.1.1:3006")) {
                            Toast.makeText(WuHuActivity4.this, "服务端不在线！", Toast.LENGTH_SHORT).show();
                            UserUtil.user_id = "";
                            UserUtil.meeting_record_id = "";
                            if (Hawk.contains(constant._id)) {
                                Hawk.delete(constant._id);
                            }
                            if (Hawk.contains(constant.user_id)) {
                                Hawk.delete(constant.user_id);
                            }
                            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                                // 如果是服务端，关掉服务，关停广播
                                String code = getIntent().getStringExtra("code");
                                UDPBroadcastManager.getInstance().sendDestroyCode(code);
                                ServerManager.getInstance().StopMyWebsocketServer();
                                UDPBroadcastManager.getInstance().removeUDPBroastcast();
                            }
                            JWebSocketClientService.closeConnect();
                            finish();
                        } else {
                            //1.显示上传文件进度弹框；2.会议数据上传服务器
                            wuHuFinishMeeting();

                        }



               /*         if (Hawk.contains(constant._id)) {
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
                        //绑定生命周期
                        NetWorkManager.getInstance().getNetWorkApiService().finishMeeting(map).compose(WuHuActivity4.this.bindToLifecycle())
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
            String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//会议-年
            wuHuEditBean.setStartTime(data);
        }
        WuHuEditBeanRequset wuHuEditBeanRequset = new WuHuEditBeanRequset();
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
                        Log.d("失败11111111", response.getMsg() + "   " + response.getData().toString());
                    }

                    @Override
                    protected void onSuccess(BasicResponse<MeetingIdBean> response) {
                        if (response != null) {
                            MeetingIdBean meetingIdBean = response.getData();
                            UserUtil.meeting_record_id = meetingIdBean.getId();
                            //开始上传文件
                            uploadFile();

                        }
                    }
                });

    }

    private void uploadFile() {
        if (Hawk.contains("WuHuFragmentData")) {

            wuHuEditBeanList.clear();
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBeanList.addAll(wuHuEditBean.getEditListBeanList());
            if (wuHuEditBeanList == null || wuHuEditBeanList.size() == 0) {
                return;
            }
            Log.d("fdsgdsgsdfgf1111", wuHuEditBeanList.size() + "  原始议题集合大小");
            List<WuHuEditBean.EditListBean> ets = new ArrayList<>();
            ets.clear();
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);

                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getLocalFiles() != null && editListBean.getLocalFiles().size() > 0) {
                    fileListBeanList.addAll(editListBean.getLocalFiles());
                    editListBean.setFileListBeanList(fileListBeanList);//每个议题下的本地文件复制到网络文件中去

                }
                ets.add(editListBean);//本地文件和网络文件都有的议题集合及只有单纯议题的集合
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
            Log.d("fdsgdsgsdfgf1111", wuHuEditBean.getEditListBeanList().size() + "  编辑完议题文件大小后的议题大小");


            for (int i = 0; i < ets.size(); i++) {
                WuHuEditBean.EditListBean editListBean = ets.get(i);
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
            //locaFileNum==0;代表没有本地文件要上传的，直接上传文本会议记录
            if (locaFileNum == 0) {
                //当文件为空时  ets集合被清空了一次
                wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                Hawk.put("WuHuFragmentData", wuHuEditBean);
                reSetdata();
            }
     /*     if (littelFilePos < fileListBeanList.size()) {
                        WuHuEditBean.EditListBean.FileListBean fileListBean = fileListBeanList.get(littelFilePos);
                        if (!fileListBean.isNet()) {
                            //只上传本地文件
                            cutfile(fileListBean.getPath(), fileListBean.getName(), fileListBean.getPos());
                        }

                    }*/
            fileFragmentationBeans.clear();
            //记录每个议题下的文件信息
            Hawk.put("FileFragmentationBean", fileFragmentationBeans);
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);
                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getLocalFiles() != null && editListBean.getLocalFiles().size() > 0) {
                    fileListBeanList.addAll(editListBean.getLocalFiles());
                    editListBean.setFileListBeanList(fileListBeanList);//每个议题下的本地文件复制到网络文件中去
                    for (int k = 0; k < fileListBeanList.size(); k++) {

                        WuHuEditBean.EditListBean.FileListBean fileListBean = fileListBeanList.get(k);
                        if (!fileListBean.isNet()) {
                            //只上传本地文件
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
                    mergeShards(fragmentationBean.getPos(), fragmentationBean.getPos());//分片数量和上传分片数量相等时执行合并
                    Log.d("gtgwrtwwrtwt大文件上传分片9999999","文件名： "+fragmentationBean.getFileName()+"   分片集合大小：  "+fragmentationBean.getAllFenPianNum()+"   上传的分片大小： "+fragmentationBean.getNumberUploads());
                }
            }
*/
        }

    }

    /**
     * 获取文件MimeType
     *
     * @param filename 文件名
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "multipart/form-data"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    private void cutfile(String filePath, String fileName, String pos) {

        try {
            long mBufferSize = size; //分片的大小，可自定义
            fileCutUtils = new FileCutUtils();
            littlefilecount = fileCutUtils.getSplitFile(new File(filePath), mBufferSize, pos);
            Log.d("gtgwrtwwrtwt大文件上传分片--11", filePath);
            littlefilelist = fileCutUtils.getLittlefilelist();
            upload(fileName, pos, littlefilelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload(String fileName, String pos, List<File> filelist) {
        //记录每个议题下的文件信息
        FileFragmentationBean fragmentationBean = new FileFragmentationBean();
        fragmentationBean.setPos(pos);
        fragmentationBean.setAllFenPianNum(filelist.size());
        fragmentationBean.setFileName(fileName);
        fragmentationBean.setNumberUploads(0);//未上传分片前都是0
        fileFragmentationBeans.add(fragmentationBean);
      /*  if(Hawk.contains("FileFragmentationBean")){
            List<FileFragmentationBean> fragmentationBeans=Hawk.get("FileFragmentationBean");
            fileFragmentationBeans.addAll(fragmentationBeans);
            if (fileFragmentationBeans.size()==0){


            }
        }*/
        Log.d("dfdssdgdsgs222222", fileName);
        Log.d("gtgwrtwwrtwt大文件上传分片33", "议题：  " + pos + "    " + fileName + "  切片数：" + filelist.size());
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
                            upLoadNum++;//每个文件的切片数
                            littelFilePos++;//议题下的文件分片索引
                            Log.d("gtgwrtwwrtwt大文件上传分片pppp1111", "上传路过   upLoadNum" + upLoadNum + "  fileName=   " + fileName);
                            Log.d("失败111222222", response.getMsg() + "   " + response.getData().toString());

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
                                    Log.d("gtgwrtwwrtwt大文件上传分片ppppp2222", "议题：  " + pos + "   缓存文件名：" + fileFragmentationBeans.get(n).getFileName() + "   传递的文件名字：" + fileName + "  总大小 " + fileFragmentationBeans.get(n).getAllFenPianNum() + "  fenpiandaxiao " + fileFragmentationBeans.get(n).getNumberUploads());

                                }

                            }
                         /*   if (upLoadNum == filelist.size()) {
                                upLoadNum = 0;
                                mergeShards(fileName, pos);
                            }*/

                        }

                        @Override
                        protected void onSuccess(BasicResponse response) {
                            upLoadNum++;//每个文件的切片数
                            littelFilePos++;//议题下的文件分片索引
                            try {
                                Log.d("gtgwrtwwrtwt大文件上传分片----90", "议题：  " + pos +
                                        "    分片集合大小：" + filelist.size() + "  fileName=   " + fileName + "   上传文件大小 " +
                                        Formatter.formatFileSize(WuHuActivity4.this, f.length()) + "   body体内大小： " +
                                        Formatter.formatFileSize(WuHuActivity4.this, requestBody.contentLength()) + "     缓存大小： " + fileFragmentationBeans.size());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                          /*  try {
                                Log.d("gtgwrtwwrtwt大文件上传分片222", "议题：  " + pos + "  上传路过   upLoadNum   分片数：" + upLoadNum +
                                        "    分片集合大小：" + filelist.size() + "  fileName=   " + fileName + "   上传文件大小 " +
                                        Formatter.formatFileSize(WuHuActivity.this, f.length()) + "   body体内大小： " +
                                        Formatter.formatFileSize(WuHuActivity.this, requestBody.contentLength()) + "     缓存大小： " + fileFragmentationBeans.size());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            for (int n = 0; n < fileFragmentationBeans.size(); n++) {
                                if (fileFragmentationBeans.get(n).getFileName().equals(fileName)) {
                                    Log.d("gtgwrtwwrtwt大文件上传分片ttttt", "议题：  " + pos + "   缓存文件名：" + fileFragmentationBeans.get(n).getFileName() + "   传递的文件名字：" + fileName);
                                    int a = fileFragmentationBeans.get(n).getNumberUploads();
                                    a++;
                                    fileFragmentationBeans.get(n).setNumberUploads(a);
                                    if (fileFragmentationBeans.get(n).getNumberUploads() == fileFragmentationBeans.get(n).getAllFenPianNum()) {
                                        upLoadNum = 0;
                                        mergeShards(fileFragmentationBeans.get(n).getFileName(), fileFragmentationBeans.get(n).getPos());
                                    }
                                    Log.d("gtgwrtwwrtwt大文件上传分片ppppp", "议题：  " + pos + "   缓存文件名：" + fileFragmentationBeans.get(n).getFileName() + "   传递的文件名字：" + fileName + "  总大小 " + fileFragmentationBeans.get(n).getAllFenPianNum() + "  fenpiandaxiao " + fileFragmentationBeans.get(n).getNumberUploads());

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
                        upLoadNum = 0;//合并完文件的分片总数量置为0
                        littelFilePos = 0;//议题下的文件分片索引
                        issueFileNum++;//每个议题下的文件
                        if (locaFileNum == upLoadFileNum) {
                            reSetdata();
                        }
                        Log.d("失败1113333", response.getMsg() + "   " + response.getData().toString());
                        Log.d("gtgwrtwwrtwt大文件上传分片ppppp333", "总文件： " + locaFileNum + "     执行过上传的数量：" + upLoadFileNum + "  fileName   " + fileName);
                    }

                    @Override
                    protected void onSuccess(BasicResponse<MergeChunkBean> response) {
                        if (response != null) {
                            upLoadFileNum++;
                            littelFilePos = 0;//议题下的文件分片索引
                            issueFileNum++;//每个议题下的文件
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
                                    //获取每一个议题
                                    WuHuEditBean.EditListBean editListBean = beanList.get(i);
                                    //当前议题和返回的议题索引作对比
                                    if (String.valueOf(mergeChunkBean.getIndex()).equals(editListBean.getPos())) {
                                        List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                                        fileListBeanList.clear();
                                        if (editListBean.getFileListBeanList() != null & editListBean.getFileListBeanList().size() > 0) {
                                            //获取当前议题下的文件集合
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
                            upLoadNum = 0;//合并完文件的分片总数量置为0
                            //  isFinish=true;
                            Log.d("gtgwrtwwrtwt大文件上传分片4444444", "总文件： " + locaFileNum + "     执行过上传的数量：" + upLoadFileNum + "  fileName   " + fileName + "   upLoadNum=" + upLoadNum);
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //第一个fragment只显示会议目录
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBeanList.clear();
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            if (wuHuEditBean != null && wuHuEditBean.getEditListBeanList() != null) {

                wuHuEditBeanList.addAll(wuHuEditBean.getEditListBeanList());

            }


        }
    }

    private void reSetdata() {

        WuHuEditBeanRequset wuHuEditBeanRequset = new WuHuEditBeanRequset();
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");


            //去掉占位目录的 议题item
            if (wuHuEditBean.getEditListBeanList().get(0).getParticipantUnits().equals("aa") && wuHuEditBean.getEditListBeanList().get(0).getReportingUnit().equals("aa")) {
                wuHuEditBean.getEditListBeanList().remove(0);

            }

            if (Hawk.contains("VoteListBean")) {
                VoteListBean voteListBean = Hawk.get("VoteListBean");
                wuHuEditBean.setVoteListBean(voteListBean);
            } else {
                VoteListBean voteListBean = new VoteListBean();
                ArrayList<VoteListBean.VoteBean> voteList = new ArrayList<>();
                voteListBean.setData(voteList);
                wuHuEditBean.setVoteListBean(voteListBean);
            }

            String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//会议-年
            wuHuEditBean.setStartTime(data);
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
                        Log.d("失败11144444", response.getMsg() + "   " + response.getData().toString());
                        upLoadNum = 0;//合并完文件的分片总数量置为0
                        UrlConstant.baseUrl = "http://192.168.1.1:3006";
                        FLUtil.BroadCastIP = "192.168.00.000";
                    }

                    @Override
                    protected void onSuccess(BasicResponse<MeetingIdBean> response) {
                        //上传文件成功
                        if (response != null) {
                            MeetingIdBean meetingIdBean = response.getData();
                            Log.d("gtgwrtwwrtwt大文件上传分片66666", "文件上传成功！");
                            allFileNum = 0;
                            upLoadFileNum = 0;
                            upLoadNum = 0;
                            upLoadNum = 0;//合并完文件的分片总数量置为0
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            //会议结束后删除模版
                            if (Hawk.contains("WuHuFragmentData")) {
                                Hawk.delete("WuHuFragmentData");
                            }
                            if (Hawk.contains("VoteListBean")) {
                                Hawk.delete("VoteListBean");
                            }
                            UserUtil.meeting_record_id = "";
                            locaFileNum = 0;
                            UrlConstant.baseUrl = "http://192.168.1.1:3006";
                            FLUtil.BroadCastIP = "192.168.00.000";

                            if (Hawk.get(constant.TEMPMEETING).equals(MessageReceiveType.MessageServer) || ServerManager.getInstance().isServerIsOpen()) {
                                // 如果是服务端，关掉服务，关停广播
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

    //网络文件初始化
    public void showFileTransferDialog() {
        //自定义dialog显示布局
        View inflate = LayoutInflater.from(WuHuActivity4.this).inflate(R.layout.wuhu_netfile_progress_dialog, null);
        //自定义dialog显示风格
        networkFileDialog = new MyDialog(WuHuActivity4.this, R.style.dialogTransparent);
        networkFileDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        networkFileDialog.setCanceledOnTouchOutside(false);
        //将布局设置给Dialog
        networkFileDialog.setContentView(inflate);
        networkFileDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        networkFileDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        networkFileDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
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
        tips.setText("请稍等，会议数据初始化中...");
        //获取当前Activity所在的窗体
        Window window = networkFileDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.alpha = 1.0f;
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        wlp.width = (int) (width * 0.45);//设置宽
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
     * 收到websocket 信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg0000: ", message.toString());

        if (UserUtil.isTempMeeting) {
            //  名称已经确定
            if (message.getMessage().contains(constant.SURENAME)) {
                UserUtil.user_name = Hawk.get(constant.myNumber);
                // ttendeesName.setText(Hawk.get(constant.myNumber) + "号   您好");
                if (Hawk.contains(constant.myNumber)) {
                    UserUtil.user_name = Hawk.get(constant.myNumber);
                }

                if (Hawk.contains(constant.user_name)) {
                    UserUtil.user_name = Hawk.get(constant.user_name);
                }
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
            if (message.getMessage().contains(constant.START_SHARE_SCEEN)) {
                if (UserUtil.ISCHAIRMAN) {
                    return;
                }
                Intent inte = new Intent(WuHuActivity4.this, ScreenReceiveActivity.class);
                startActivity(inte);
            } else if (message.getMessage().contains(constant.FINISH_SHARE_SCEEN)) {
                Intent in = new Intent();
                in.setAction(constant.FINISH_SHARE_SCREEN_BROADCAST);
                sendBroadcast(in);
            } else
                // 查询议题列表信息
                if (message.getMessage().contains(constant.WUHUADDFRAGMENT)) {
                    Log.e("onReceiveMsg11111:   wuwhuactivity  添加议题", message.toString());
                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        //  收到vote的websocket的信息
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean wuHuEditBean = wsebean.getBody();
                            Log.d("onReceiveMsg11111大小=  wuwhuactivity  添加议题", fragmentPos + "");
                            mTestFragments.put(key++, WuHuFragment2.newInstance(fragmentPos + ""));
                            fragmentPos++;
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
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean wuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.addAll(wuHuEditBean.getEditListBeanList());
                                wuHuEditBeanList.addAll(listBeans);
                                wuHuFragmentData.setTopics(listBeans.get(0).getTopics());
                                wuHuFragmentData.setTopic_type(listBeans.get(0).getTopic_type());
                                wuHuFragmentData.setLine_color(listBeans.get(0).getLine_color());
                                wuHuFragmentData.setThem_color(listBeans.get(0).getThem_color());

                                wuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                Hawk.put("WuHuFragmentData", wuHuFragmentData);
                          /*  //更新单个数据
                            wsUpdata(wuHuEditBean,constant.REFRASHWuHUALL);*/
                            }
                            if (wuHuListAdapter != null) {
                                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                wuHuListAdapter.notifyDataSetChanged();
                            }
                            mViewPager.setOffscreenPageLimit(mTestFragments.size()-1);
                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            //   sendBroadcast(intent);
                            Message message1 = new Message();
                            message1.obj = "1";
                            message1.what = 9112;
                            mHander.sendMessage(message1);

                            if (freshenFragment != null) {
                                freshenFragment.freshFragmentUi("新增");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().contains(constant.REFRASHWuHUSIGLEDATA)) {

                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        //  收到vote的websocket的信息
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean wuHuEditBean = wsebean.getBody();
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean refrashWuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.clear();
                                listBeans.addAll(wuHuEditBean.getEditListBeanList());
                                if (listBeans.size() == 0) {
                                    return;
                                }
                                Log.d("onReceiveMsg activity wuhuactivity  ", "REFRASHWuHUALL");
                         /*   WuHuEditBean.EditListBean editListBean = listBeans.get(Integer.valueOf(refrashWuHuFragmentData.getPosition()));
                            refrashWuHuFragmentData.setTopic_type(editListBean.getTopic_type());
                            refrashWuHuFragmentData.setTopics(editListBean.getTopics());
                            refrashWuHuFragmentData.setLine_color(editListBean.getLine_color());
                            refrashWuHuFragmentData.setThem_color(editListBean.getThem_color());*/
                                wuHuEditBeanList.addAll(listBeans);
                                refrashWuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                Log.d("列单位  activity  单个  ", wuHuEditBeanList.get(1).getParticipantUnits());
                                Hawk.put("WuHuFragmentData", refrashWuHuFragmentData);
                                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                wuHuListAdapter.notifyDataSetChanged();
                            }


                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            // sendBroadcast(intent);

                            EventBus.getDefault().post(1);
                            Message message1 = new Message();
                            message1.obj = "1";
                            message1.what = 9112;
                            mHander.sendMessage(message1);
                            if (freshenFragment != null) {
                                freshenFragment.freshFragmentUi("刷新单个碎片");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.REFRASHWuHUALL)) {
                    Log.d("dfaffaf333", "收到款福克斯的方式代理费");
                    try {
                        TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                        }.getType());
                        //  收到vote的websocket的信息
                        if (wsebean != null) {
                            wuHuEditBeanList.clear();
                            WuHuEditBean wuHuEditBean = wsebean.getBody();
                            Log.d("onReceiveMsg q全部保存 wuhuactivity  ", "REFRASHWuHUALL");
                            Hawk.put("company_name", wuHuEditBean.getTopics());
                            Hawk.put("tittle2", wuHuEditBean.getTopic_type());
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean refrashWuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.addAll(wuHuEditBean.getEditListBeanList());
                                if (listBeans.size() == 0) {
                                    return;
                                }

                                WuHuEditBean.EditListBean editListBean = listBeans.get(0);
                                refrashWuHuFragmentData.setTopic_type(editListBean.getTopic_type());
                                refrashWuHuFragmentData.setTopics(editListBean.getTopics());
                                refrashWuHuFragmentData.setLine_color(editListBean.getLine_color());
                                refrashWuHuFragmentData.setThem_color(editListBean.getThem_color());
                                wuHuEditBeanList.addAll(listBeans);
                                refrashWuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                Hawk.put("WuHuFragmentData", refrashWuHuFragmentData);
                                Log.d("列单位  fragment   全部保存 ", wuHuEditBeanList.get(Integer.valueOf(1)).getParticipantUnits());
                                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                wuHuListAdapter.notifyDataSetChanged();
                            }

                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            // sendBroadcast(intent);
                            Message message1 = new Message();
                            message1.obj = "1";
                            message1.what = 9112;
                            mHander.sendMessage(message1);
                            if (freshenFragment != null) {
                                freshenFragment.freshFragmentUi("刷新所有");
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

                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean wuHuFragmentData = Hawk.get("WuHuFragmentData");
                                List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                                listBeans.addAll(deletWuHuEditBean.getEditListBeanList());
                                if (listBeans.size() == 0) {
                                    return;
                                }
                                wuHuEditBeanList.addAll(listBeans);
                                wuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                                Hawk.put("WuHuFragmentData", wuHuFragmentData);
                                wuHuListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                                wuHuListAdapter.notifyDataSetChanged();
                            }
                            mViewPager.setOffscreenPageLimit(mTestFragments.size()-1);
                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            // sendBroadcast(intent);
                            Message message1 = new Message();
                            message1.obj = "1";
                            message1.what = 9112;
                            mHander.sendMessage(message1);
                            if (freshenFragment != null) {
                                freshenFragment.freshFragmentUi("删除碎片");
                            }

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
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                } else if (message.getMessage().contains(constant.QUERYVOTE_WUHU_FRAGMENT)) {
                    Log.d("wrewarawrawerwqeqwe   wuhuactivity", "查询");
                    Log.e("onReceiveMsg查询:   wuhuactivity", message.toString());

                    try {
                        TempWSBean<ArrayList> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<WuHuEditBean.EditListBean>>>() {
                        }.getType());
                        //  收到vote的websocket的信息
                        if (wsebean != null) {
                            editListBeans.clear();
                            editListBeans.addAll(wsebean.getBody());
                            Log.d("fddgdsgsdgsgdsg-actiivity", editListBeans.size() + "");
                            if (Hawk.contains("WuHuFragmentData")) {
                                WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                                wuHuEditBean.setEditListBeanList(editListBeans);
                                Hawk.put("WuHuFragmentData", wuHuEditBean);
                            } else {
                                WuHuEditBean wuHuEditBean = new WuHuEditBean();
                                wuHuEditBean.setEditListBeanList(editListBeans);
                                Hawk.put("WuHuFragmentData", wuHuEditBean);
                            }
                            querywuhufragment(editListBeans);
                            mViewPager.setOffscreenPageLimit(mTestFragments.size()-1);
                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            // sendBroadcast(intent);
                            Message message1 = new Message();
                            message1.obj = "1";
                            message1.what = 9112;
                            mHander.sendMessage(message1);
                            if (freshenFragment != null) {
                                freshenFragment.freshFragmentUi("查询");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().contains(constant.FILEMD5PUSH)) {
                    //查询推送文件是否存在
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
                    //查询分享文件是否存在
                    //查询推送文件是否存在
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


                        Log.d("md5验证信息", "attendeBeanList.size==" + size);
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
                            Log.d("fdsafadffadfazz1111  ", "提交成功!");
                            WuHuEditBean wuHuEditBean = wsebean.getBody();

                            Log.e("1111", "wuHuEditBean===" + wuHuEditBean + "   2222===wuHuEditBean.getEditListBeanList()==" + wuHuEditBean.getEditListBeanList());
                            if (wuHuEditBean != null && wuHuEditBean.getEditListBeanList() != null) {
                                comitEdit.addAll(wuHuEditBean.getEditListBeanList());
                            }

                            for (int i = 0; i < comitEdit.size(); i++) {

                                Log.d("fdsafadffadfazz22222   ", comitEdit.get(i).getFileListBeanList().size() + "");
                            }
                            Hawk.put("WuHuFragmentData", wuHuEditBean);
                            Intent intent = new Intent();
                            intent.setAction(constant.FRESH_CATalog_BROADCAST);
                            //   sendBroadcast(intent);
                            Message message1 = new Message();
                            message1.obj = "1";
                            message1.what = 9112;
                            mHander.sendMessage(message1);
                            if (freshenFragment != null) {
                                freshenFragment.freshFragmentUi("提交");
                            }

                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }


                } else if (message.getMessage().contains(constant.PUSH_FILE_WEBSOCK)) {
                    try {
                        //主席端自己不接收
                        if (UserUtil.ISCHAIRMAN) {
                            return;

                        }
                        TempWSBean<PushBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<PushBean>>() {
                        }.getType());
                        if (wsebean != null) {
                            PushBean pushBean = wsebean.getBody();
                            if (pushBean != null && pushBean.getFileName() != null) {
                                //分享和推送时先复制到分享文件夹
                                File path = new File(netFilePath);
                                File[] files = path.listFiles();// 读取
                                if (files == null) {
                                    return;
                                }
                                getallfiles(files, pushBean.getFileName());
                            }

                            Log.e("dfsdgfsdgfs", pushBean.getFileName());

                        }

                    } catch (Exception e) {

                    }


                }/* else if (message.getMessage().contains(constant.CHANGE_COLOR_BG)) {
                    try {

                        TempWSBean<String> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<String>>() {
                        }.getType());
                        if (wsebean != null) {
                            String strColor = wsebean.getBody();
                            Log.d("strColor1111",strColor);
                            if (strColor != null && strColor != "") {
                                Message message1 = new Message();
                                message1.obj = strColor;
                                message1.what = 912;
                                mHander.sendMessage(message1);
                            }

                        }

                    } catch (Exception e) {

                    }
                }*/
            if (message.getMessage().contains(constant.SURENAME)) {
                loadData();
            }
          /*  Intent intent = new Intent();
            intent.setAction(constant.FRESH_CATalog_BROADCAST);
            sendBroadcast(intent);*/
        }
    }

    //获取投票列表数据状态数据
    public void loadData() {
        if (UserUtil.ISCHAIRMAN) {
            //只有服务端才有的方法：初始化议题数据并提交到服务端
            initiaServerData();
        } else {
            //当是网络会议时  参会人员要下载文件的
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }

                    @Override
                    protected void onSuccess(BasicResponse<WuHuMeetingListResponse> response) {
                        if (response != null) {
                            WuHuMeetingListResponse wuHuMeetingListResponse = response.getData();
                            setData(wuHuMeetingListResponse);

                        }

                    }
                });

    }

    //下载文件
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
            WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO editListBeanListDTO = editListBeanListDTOS.get(i);//获取单个议题
            List<WuHuMeetingListResponse.ContentDTO.EditListBeanListDTO.FileListBeanListDTO> netLocaFiles = new ArrayList<>();//存放议题对应的文件集合
            netLocaFiles.clear();
            if (editListBeanListDTO.getFileListBeanList() != null && editListBeanListDTO.getFileListBeanList().size() > 0) {
                netLocaFiles.addAll(editListBeanListDTO.getFileListBeanList());
                for (int k = 0; k < netLocaFiles.size(); k++) {
                    WuHuNetWorkBean wuHuNetWorkBean = new WuHuNetWorkBean();
                    wuHuNetWorkBean.setUrl(UrlConstant.baseUrl + "/" + netLocaFiles.get(k).getPath());

                    wuHuNetWorkBean.setName(netLocaFiles.get(k).getName());
                    allFileNum++;
                    filesList.add(wuHuNetWorkBean);
                }
            }
        }
        if (filesList.size() == 0) {
            if (networkFileDialog != null) {
                networkFileDialog.dismiss();
            }
        }
        //遍历议题下载文件
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

    //action 1 分享 2  推送
    private void checkFileMd5(WuHuEditBean.EditListBean.FileListBean fileBean, String action) {
        File path = null;
        //当文件时本地文件时  从分享文件夹中遍历验证文件有无；当文件是网络文件时  从网络文件夹中验证文件有无
        if (fileBean.isNet()) {
            path = new File(fileShare);
        } else {
            path = new File(netFilePath);
        }
        File[] files = path.listFiles();// 读取
        //验证当前议题分享和推送文件有无
        getShareFile(files, action, fileBean);
    }

    /*
     * 遍历得到分享的文件
     * */
    private void getShareFile(File[] files, String action, final WuHuEditBean.EditListBean.FileListBean fileListBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("111111111111111111", " 遍历得到分享的文件 遍历得到分享的文件 遍历得到分享的文件 遍历得到分享的文件");
                //子线程开始
                String content = "";
                if (files != null) {
                    String fileMd5 = "-1";
                    String md5FilePath = null;
                    // 先判断目录是否为空，否则会报空指针
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
                            //分享
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
                                //议题号和MD5都相同则有这样的文件
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

                    //子线程执行结束
                    //分享
                    if (action.equals("1")) {
                        if (fileMd5.equals("-1")) {
                            //本地无分享的文件 直接发送33更新列表
                            Message shareMsg = new Message();
                            shareMsg.what = 700;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "700");
                        } else {
                            //本地有分享的文件 回消息让其执行分享操作
                            Message shareMsg = new Message();
                            shareMsg.what = 600;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "600");
                        }

                    } else if (action.equals("2")) {
                        if (fileMd5.equals("-1")) {
                            //本地无当前推送的文件，发送88直接打开
                            Message shareMsg = new Message();
                            shareMsg.what = 500;
                            shareMsg.obj = fileListBean;
                            mHander.sendMessage(shareMsg);
                            Log.d("vvcvsvsfgsf ", "500");
                        } else {
                            //本地有推送的文件回给发送端  让其推送
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

    //获取本地上传的文件
    private void getFileName(File[] files) {
        WuHuEditBean.EditListBean.FileListBean fileBean;
        String content = "";
        if (files != null) {
            // 先判断目录是否为空，否则会报空指针
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
                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    //  fileBean.setType(endStr);
                    fileBean.setNet(false);

                }
            }

        }
    }

    /**
     * 获取指定目录内所有文件路径
     *
     * @param dirpath 需要查询的文件目录
     * @param _type   查询类型，比如mp3什么的
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
       /* if (f.exists()) {//判断路径是否存在
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
     * 遍历得到分享的文件
     * */
    private void getShareFile(File[] files, String name) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (UserUtil.object) {

                    //子线程开始
                    String content = "";
                    if (files != null) {
                        // 先判断目录是否为空，否则会报空指针
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

                      /*  //发送消息跟新文件列表
                        Message shareMsg = new Message();
                        shareMsg.what = 1080;
                        shareMsg.obj = shareFileBeans;
                        handler.sendMessage(shareMsg);*/

                    }


                }

                //子线程执行结束
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
            //1:代表复用模板  2：代表不复用模板 3：代表没有模板
            if (isreuse.equals("2")) {
                WuHuEditBean wuHuEditBean = new WuHuEditBean();
                Hawk.put("WuHuFragmentData", wuHuEditBean);
                if (Hawk.contains("WuHuFragmentData")) {
                    wuHuEditBean = Hawk.get("WuHuFragmentData");
                    wuHuEditBean.setTopics("区政府会议纪要");
                    wuHuEditBean.setTopic_type("会议纪要");

                    WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                    editListBean.setSubTopics("总结2022年");
                    editListBean.setReportingUnit("某某，某某，某某，某");
                    editListBean.setParticipantUnits("某某2，某某2，某某2，某2");
                    editListBean.setTopics("区政府会议纪要");
                    editListBean.setPos("0");
                    editListBean.setTopic_type("会议纪要");
                    wuHuEditBeanList.add(editListBean);


                    WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                    editListBean1.setSubTopics("总结2022年");
                    editListBean1.setReportingUnit("某某，某某，某某，某");
                    editListBean1.setParticipantUnits("某某2，某某2，某某2，某2");
                    editListBean1.setTopics("区政府会议纪要");
                    editListBean1.setTopic_type("会议纪要");
                    editListBean1.setPos("1");
                    wuHuEditBeanList.add(editListBean1);

                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                }
                Log.d("GASDJKASDD宿主=", "rfresettt111");
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
                        wuHuEditBean.setTopics("区政府会议纪要");
                        wuHuEditBean.setTopic_type("会议纪要");

                        WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                        editListBean.setSubTopics("总结2022年");
                        editListBean.setReportingUnit("某某，某某，某某，某");
                        editListBean.setParticipantUnits("某某2，某某2，某某2，某2");
                        editListBean.setTopics("区政府会议纪要");
                        editListBean.setTopic_type("会议纪要");
                        editListBean.setPos("0");
                        wuHuEditBeanList.add(editListBean);


                        WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                        editListBean1.setSubTopics("总结2022年");
                        editListBean1.setReportingUnit("某某，某某，某某，某");
                        editListBean1.setParticipantUnits("某某2，某某2，某某2，某2");
                        editListBean1.setTopics("区政府会议纪要");
                        editListBean1.setPos("1");
                        editListBean1.setTopic_type("会议纪要");
                        wuHuEditBeanList.add(editListBean1);

                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                    }
                    Log.d("GASDJKASDD宿主=", "rfresettt111");
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
                            wuHuEditBean.setTopics("区政府会议纪要");
                            wuHuEditBean.setTopic_type("会议纪要");
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
                            wuHuEditBean.setTopics("区政府会议纪要");
                            wuHuEditBean.setTopic_type("会议纪要");

                            WuHuEditBean.EditListBean editListBean = new WuHuEditBean.EditListBean();
                            editListBean.setSubTopics("总结2022年");
                            editListBean.setReportingUnit("某某，某某，某某，某");
                            editListBean.setParticipantUnits("某某2，某某2，某某2，某2");
                            editListBean.setTopics("区政府会议纪要");
                            editListBean.setPos("0");
                            editListBean.setTopic_type("会议纪要");
                            wuHuEditBeanList.add(editListBean);

                            WuHuEditBean.EditListBean editListBean1 = new WuHuEditBean.EditListBean();
                            editListBean1.setSubTopics("总结2022年");
                            editListBean1.setReportingUnit("某某，某某，某某，某");
                            editListBean1.setParticipantUnits("某某2，某某2，某某2，某2");
                            editListBean1.setTopics("区政府会议纪要");
                            editListBean1.setPos("1");
                            editListBean1.setTopic_type("会议纪要");
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
        //自定义dialog显示布局
        inflate = LayoutInflater.from(WuHuActivity4.this).inflate(R.layout.dialog_edit_wuhu, null);
        //自定义dialog显示风格
        dialog = new MyDialog(WuHuActivity4.this, R.style.BottomSheetEdit);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(true);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
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
        //  纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WuHuActivity4.this);
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

                wsUpdata(lineFlag, constant.CHANGE_COLOR_BG);
              /*  intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);*/
                if (freshenFragment != null) {
                    freshenFragment.freshFragmentUi("选择了背景");
                }
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
                wsUpdata(themFlag, constant.CHANGE_COLOR_BG);
              /*  intent.putExtras(bundle);
                intent.setAction(constant.SAVE_SEPARATELY_BROADCAST);
                sendBroadcast(intent);*/
                if (freshenFragment != null) {
                    freshenFragment.freshFragmentUi("选择了颜色");
                }

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
                    //通知其他设备增加fragment
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
                    //更新单个数据
                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUALL);
                }

                Hawk.put("company_name", company_name.getText().toString());
                Hawk.put("tittle2", tittle2.getText().toString());
                Toast.makeText(WuHuActivity.this, "全部保存成功", Toast.LENGTH_SHORT).show();*/


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
                        Log.d("fsdfgsggsg", "议题   " + i + "  对应的列席单位：" + wuHuEditBeanList.get(i).getParticipantUnits());
                    }

                    wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                    //更新单个数据
                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUALL);
                }

                Hawk.put("company_name", company_name.getText().toString());
                Hawk.put("tittle2", tittle2.getText().toString());
                Toast.makeText(WuHuActivity4.this, "全部保存成功", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        //获取当前Activity所在的窗体
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽，高
        wlp.gravity = Gravity.RIGHT;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Point size = new Point();
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
        EventBus.getDefault().unregister(this);
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
        if (serviceConnection != null) {
            Log.d("gfdgfdhdfhdfhgdh11", "会结束销毁文件接收");
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

    public interface FreshenFragmentInterface {

        void freshFragmentUi(String flag);

    }

    /**
     * 绑定接口
     *
     * @param
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        try {
            freshenFragment = (FreshenFragmentInterface) fragment;

            Log.d("gfddhfddfhd11111", "set  interface");
        } catch (Exception e) {
            Log.d("gfddhfddfhd22222", e.getMessage().toString());
        }
        super.onAttachFragment(fragment);
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

// 获得当前得到焦点的View，一般情况下就是EditText(特殊情况就是轨迹求或者实体案件会移动焦点)

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
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
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
     * 判断某activity是否处于栈顶
     *
     * @return true在栈顶 false不在栈顶
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

    public void setHandler(Handler handler) {

        this.mHander = handler;

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
