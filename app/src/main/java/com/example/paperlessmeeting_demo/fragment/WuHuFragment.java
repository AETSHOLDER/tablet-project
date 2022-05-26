package com.example.paperlessmeeting_demo.fragment;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.WuHuLocalFileBean;
import com.example.paperlessmeeting_demo.activity.ActivityImage;
import com.example.paperlessmeeting_demo.activity.Sign.SignActivity;
import com.example.paperlessmeeting_demo.adapter.WuHuCalalogListAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuFileListAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.SharePushFileBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.bean.WuHuDeleteFragmentBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuNetFileBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.DownloadUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.Md5Util;
import com.example.paperlessmeeting_demo.tool.MediaReceiver;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.CompletedView;
import com.example.paperlessmeeting_demo.widgets.MyDialog;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.FileNameMap;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-05-10 10:37
 */

public class WuHuFragment extends BaseFragment implements MediaReceiver.sendfilePthInterface, WuHuFileListAdapter.upLoadFileInterface, WuHuFileListAdapter.PlayerClickInterface, WuHuFileListAdapter.ShareFileInterface, WuHuFileListAdapter.PushFileInterface, View.OnClickListener {
    Unbinder unbinder;
    private static final String TAG = "TestFragment";
    private String textNub;
 /*   @BindView(R.id.tv_test)
    TextView mTextView;*/

    @BindView(R.id.root_lin)
    RelativeLayout root_lin;
    @BindView(R.id.image_add)
    RelativeLayout imageAdd;
    @BindView(R.id.listView)
    MyListView listView;
    @BindView(R.id.listView_catalog)
    MyListView listView_catalog;
    @BindView(R.id.progressBar_ll)
    RelativeLayout progressBarLl;
    @BindView(R.id.aa)
    View fragmentLine;
    @BindView(R.id.nn)
    View nn;
    @BindView(R.id.topic)
    TextView topic;
    @BindView(R.id.tittle1)
    TextView tittle1;
    @BindView(R.id.tittle2)
    TextView tittle2;
    @BindView(R.id.attend)
    TextView attend;
    @BindView(R.id.attend2)
    TextView attend2;
    @BindView(R.id.tittle_num)
    TextView tittle_num;
    @BindView(R.id.file_ly)
    LinearLayout file_ly;
    @BindView(R.id.topic_ll)
    LinearLayout topic_ll;
    @BindView(R.id.fragment_ttile_rl)
    RelativeLayout fragment_ttile_rl;
    @BindView(R.id.listView_catalog_ll)
    LinearLayout listView_catalog_ll;
    @BindView(R.id.scrollView_file)
    ScrollView scrollView_file;
    @BindView(R.id.scrollView_cata)
    ScrollView scrollView_cata;


    private RelativeLayout add_topic_rl;
    private RelativeLayout dialg_rl_root;
    private RelativeLayout sava_all;
    private MyDialog dialog;
    private View inflate;
    private String selfIp = "";
    private WuHuFileListAdapter fileListAdapter;
    private WuHuCalalogListAdapter wuHuCalalogListAdapter;
    private List<WuHuEditBean.EditListBean.FileListBean> fileBeans = new ArrayList<>();//总的文件集合：其他设备分享得到的文件集合、本地上传得到的文件集合、网络服务器得到的文件集合
    private List<WuHuEditBean.EditListBean.FileListBean> shareFileBeans = new ArrayList<>();//其他设备分享得到的文件集合
    private List<WuHuEditBean.EditListBean.FileListBean> copyFileBeans = new ArrayList<>();//本地上传得到的文件集合
    private List<WuHuEditBean.EditListBean.FileListBean> netFileBeans = new ArrayList<>();//网络服务器得到的文件集合
    private FileListBean meetingFileListBeanfileBean;
    private String titles;
    private Context context;
    private List<String> name = new ArrayList<>();
    //   private String fileStr = Environment.getExternalStorageDirectory() + "/aa";
    // private String fileStr;
    private MediaReceiver mBroadcastReceiver;
    private FragmentManager fragmentManager;
    private StorageManager mStorageManager;
    private String upLoadFileType;
    private UploadBean uploadBean;
    private CreateFileBeanRequest createFileBeanRequest;
    private MediaPlayer mediaPlayer = new MediaPlayer();//实例化MediaPlayer类
    private String fileStrPath = Environment.getExternalStorageDirectory() + constant.COPY_PATH;//本地上传文件保存路径
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//其他设备分享得到的文件夹路径
    private String netFilePath = Environment.getExternalStorageDirectory() + constant.WUHU_NET_FILE;//网络请求得到的文件夹路径
    private String path = "/storage/emulated/0/";//展厅临时文件路径
    private String videoPath = "";
    private String endStrAll;
    private SocketShareFileManager socketShareFileManager;
    private MyBroadcastReceiver myBroadcastReceiver;//分享文件成功后，刷新文件列表
    private MyBroadcastReceiver mySaveBroadcastReceiver;//更新单个会议议题
    private MyBroadcastReceiver myCatalogBroadcastReceiver;//更新会议目录列表
    private List<String> stringListIp = new ArrayList<>();
    private MyListView myListView;
    private WuHuListAdapter wuHuListAdapter;
    private int k = 0;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList = new ArrayList<>();
    private List<WuHuNetFileBean.DataBean> fileListBeanList = new ArrayList<>();
    private List<WuHuEditBean.EditListBean.FileListBean> fileOtherBeans = new ArrayList<>();//音视频文件
    private WuHuEditBean.EditListBean.FileListBean fileBean;
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();
    private boolean mReceiverTag = false;   //广播接受者标识
    private boolean isDeletOption = false;//是否做了删除操作
    private ProgressBar progressBar;//显示文件传输进度
    private CompletedView completedView;
    private ImageView result_ima;
    private TextView tips;
    private String pushPath;
    private String pushType;
    private String pushName;

    private String sharePath;
    private String shareType;
    private String shareName;
    private boolean isPush = false;
    private boolean opened = false;
    private String pos = "-1";
    private int pushResponNum = 0;
    private int shareResponNum = 0;
    private int pushResponNo = 0;
    private int shareResponNo = 0;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 99:
                    WuHuEditBean.EditListBean.FileListBean fileBean = (WuHuEditBean.EditListBean.FileListBean) msg.obj;
                    fileBean.setNet(true);
                    netFileBeans.add(fileBean);
                    if (netFileBeans == null || netFileBeans.size() == 0) {
                        return;
                    }
                    fileListAdapter.setGridViewBeanList(netFileBeans);
                    fileListAdapter.notifyDataSetChanged();
                    //完成主界面更新,拿到数据
                    //   getDownload(fileListBean.get_id(), fileListBean.getUser_id(), fileListBean.getUnclassified());
                    //    getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                    break;

                case 150:
                    //分享和推送时先复制到分享文件夹
                    File path = new File(fileShare);
                    File[] files = path.listFiles();// 读取
                    if (files == null) {
                        return;
                    }
                    getShareFile(files);
                    break;
                //遍历分享文件
                case 100:
                    List<WuHuEditBean.EditListBean.FileListBean> shareFiles = (List<WuHuEditBean.EditListBean.FileListBean>) msg.obj;
                    if (shareFiles==null){
                        return;
                    }
                    fileBeans.clear();
                    fileBeans.addAll(shareFileBeans);
                    fileBeans.addAll(copyFileBeans);
                    fileBeans.addAll(netFileBeans);

                   //把分享文件得到的集合计入缓存
                    if (Hawk.contains("WuHuFragmentData")) {
                        //临时存放的议题集合
                        List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        copyEdList = wuHuEditBean.getEditListBeanList();
                        //遍历更新当前议题下的文件
                        if (copyEdList != null && copyEdList.size() > 0) {
                            for (int i = 0; i < copyEdList.size(); i++) {
                                //每一个议题
                                WuHuEditBean.EditListBean editListBean = copyEdList.get(i);

                                if (editListBean.getPos().equals(textNub)) {
                                    //把当前分享的议题文件集合放入对应的议题
                                    if (editListBean.getFileListBeanList()!=null){
                                        shareFileBeans.addAll(editListBean.getFileListBeanList());
                                    }



                                    editListBean.setFileListBeanList(shareFileBeans);
                                    editListBean.setLocalFiles(copyFileBeans);
                                    copyEdList.set(Integer.valueOf(textNub), editListBean);
                                }
                            }
                            wuHuEditBean.setEditListBeanList(copyEdList);
                        }
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        if (!UserUtil.ISCHAIRMAN) {
                           wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                        }
                    }

                    for (int k = 0; k < fileBeans.size() - 1; k++) {
                        for (int j = fileBeans.size() - 1; j > k; j--) {
                            if (fileBeans.get(j).getPath().equals(fileBeans.get(k).getPath()) && fileBeans.get(j).getPos().equals(fileBeans.get(k).getPos())) {
                                fileBeans.remove(j);
                            }
                        }
                    }


                    fileListAdapter.setGridViewBeanList(fileBeans);
                    fileListAdapter.notifyDataSetChanged();

                    break;
                //刷新议题对应的文件
                case 200:
                    List<WuHuEditBean.EditListBean.FileListBean> copyFiles = new ArrayList<>();
                    copyFiles.clear();
                    //取出缓存把当前议题文件关联到当前议题
                    if (Hawk.contains("WuHuFragmentData")) {
                        //临时存放的议题集合
                        List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                        copyEdList.clear();
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        copyEdList = wuHuEditBean.getEditListBeanList();
                        if (copyEdList == null || copyEdList.size() == 0) {
                            return;
                        }
                        copyFiles = copyEdList.get(Integer.valueOf(textNub)).getFileListBeanList();
                    }

                    /*   List<WuHuEditBean.EditListBean.FileListBean> copyFiles = (List<WuHuEditBean.EditListBean.FileListBean>) msg.obj;*/
                    if (copyFiles == null || copyFiles.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < copyFiles.size(); i++) {
                        Log.d("当前议题对应的文件 ", "     当前议题：" + textNub + "    文件所属议题：" + copyFiles.get(i).getPos() + "    文件名：" + copyFiles.get(i).getName());
                    }
                    copyFileBeans.clear();
                    copyFileBeans.addAll(copyFiles);
                    fileBeans.addAll(copyFileBeans);
                    fileBeans.addAll(netFileBeans);
                    fileBeans.addAll(shareFileBeans);
                    if (fileBeans == null || fileBeans.size() == 0) {
                        return;
                    }
                    fileListAdapter.setGridViewBeanList(fileBeans);
                    fileListAdapter.notifyDataSetChanged();

                    break;
               /* case 88:
                    if (Hawk.contains("wuhulocal")) {
                        List<WuHuEditBean.EditListBean.FileListBean> fileListBeans=new ArrayList<>();
                        fileListBeans = Hawk.get("wuhulocal");
                        copyFileBeans.clear();
                        copyFileBeans.addAll(fileListBeans);
                        fileBeans.addAll(copyFileBeans);
                        fileBeans.addAll(netFileBeans);
                        fileBeans.addAll(shareFileBeans);
                    }
                    fileListAdapter.setGridViewBeanList(fileBeans);
                    fileListAdapter.notifyDataSetChanged();
                    break;*/
                /*
                 * 分享文件发送中，显示加载动画
                 * */
                case 4:
                    // progressBarLl.setVisibility(View.VISIBLE);
                    showFileTransferDialog();
                    String fileName = msg.obj.toString();
                    tips.setText("正在发送");
                    break;
                /*
                 * 分享文件发送完毕，隐藏加载动画
                 * */
                case 55:
                    //   progressBarLl.setVisibility(View.GONE);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    completedView.setVisibility(View.GONE);
                    result_ima.setVisibility(View.VISIBLE);
                    result_ima.setImageResource(R.mipmap.ic_push_succes);
                    tips.setText("发送成功");
                    Toast.makeText(getActivity(), "文件发送成功", Toast.LENGTH_SHORT).show();

                    if (isPush) {
                        if (StringUtils.isEmpty(pushType) || StringUtils.isEmpty(pushPath) || StringUtils.isEmpty(pushName)) {
                            return;
                        }
                        Log.d("sADdDdD222", "pushType=" + pushType + "    pushPath=" + pushPath + "    pushName=" + pushName);
                        Intent intent;
                        if (pushType.equals("3")) {
                            if (pushPath == null) {
                                Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //防止普通参会人员重复打开页面
                            intent = new Intent();
                            intent.setClass(getActivity(), ActivityImage.class);
                            intent.putExtra("url", pushPath);
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", false);
                            startActivity(intent);
                            Log.d("重复打开  wuhuFment ActivityImage 55", "333333");
                         /*   if (!opened) {
                                startActivity(intent);
                            }
                            opened = true;*/

                        } else if (pushType.equals("4")) {
                            if (UserUtil.isNetworkOnline) {
                                if (pushPath == null) {
                                    Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                intent = new Intent();
                                intent.setClass(getActivity(), SignActivity.class);
                                intent.putExtra("url", pushPath);
                                intent.putExtra("isOpenFile", true);
                                intent.putExtra("isNetFile", false);
                                intent.putExtra("tempPath", false);
                                intent.putExtra("fileName", pushName);
                                startActivity(intent);
                                Log.d("重复打开  wuhuFment SignActivity 55", "333333");
                              /*  if (!opened) {
                                    startActivity(intent);
                                }
                                opened = true;*/
                            } else {
                                CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                    @Override
                                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                        startActivity(FileUtils.openFile(pushPath, getActivity()));
                                    }
                                });
                            }
                        }
                        isPush = false;
                    }
                    break;
                /*
                 * 文件传输进度
                 * */
                case 10:

                    String proress = (String) msg.obj;
                    completedView.setProgress(Integer.parseInt(proress));
                    break;
                /*
                 * 分享文件发送失败，隐藏加载动画
                 * */
                case 6:
                    //  progressBarLl.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    }, 1500);

                    completedView.setVisibility(View.GONE);
                    result_ima.setVisibility(View.VISIBLE);
                    // result_ima.setImageResource(R.mipmap.ic_push_fail);
                    tips.setText("努力发送中");
                    Toast.makeText(getActivity(), "网络有点差哦", Toast.LENGTH_SHORT).show();
                    String df = msg.obj.toString();
                    Log.d("dfsfsdf", df);
                    break;
                case  210:
                    File file = (File) msg.obj;
                    String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    WuHuEditBean.EditListBean.FileListBean    wb = new WuHuEditBean.EditListBean.FileListBean(file.getName(), file.getPath(), "", "");
                    wb.setResImage(getIamge(endStr));
                    wb.setFile_type(getType(endStr));
                    wb.setNet(true);
                    Intent intent;
                    if (wb.getPath() == null) {
                        Toast.makeText(getActivity(), "文件不存在,请等待主控端分享文件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (wb.getFile_type().equals("3")) {
                        intent = new Intent();
                        intent.setClass(getActivity(), ActivityImage.class);
                        intent.putExtra("url", wb.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        startActivity(intent);
                    } else if (wb.getFile_type().equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            intent = new Intent();
                            if (wb.getPath() == null) {
                                Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            intent.setClass(getActivity(), SignActivity.class);
                            intent.putExtra("url", wb.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", false);
                            intent.putExtra("tempPath", false);
                            intent.putExtra("fileName", wb.getName());
                            startActivity(intent);
                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(wb.getPath(), getActivity()));
                                }
                            });
                        }

                    }



                    break;
                case 211:
                    Toast.makeText(getActivity(), "文件不存在,请等待主控端分享文件", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.wuhu_fragment_layout;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {

        fileBeans.clear();
        copyFileBeans.clear();
        //  getNetFile();

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });
   /*     //第一个fragment只显示会议目录
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBeanList.clear();
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBeanList = wuHuEditBean.getEditListBeanList();
            if (textNub != null) {
                List<WuHuEditBean.EditListBean.FileListBean> tempFiles = wuHuEditBeanList.get(Integer.valueOf(textNub)).getFileListBeanList();
                if (tempFiles != null && tempFiles.size() > 0) {
                    fileBeans.addAll(tempFiles);
                }
            }

        }
        for (int i = 0; i < fileBeans.size(); i++) {
            String endStr = fileBeans.get(i).getName().substring(fileBeans.get(i).getName().lastIndexOf(".") + 1);
            fileBeans.get(i).setResImage(getIamge(endStr));
            fileBeans.get(i).setFile_type(getType(endStr));
        }*/

        Log.d("GASDJKASDD碎片=", "rfresettt222");
        Log.d("fdgsgsgsgsg2222", Hawk.contains("WuHuFragmentData") + "    议题号:" + textNub + "        议题数量：" + wuHuEditBeanList.size() + "       文件大小：" + fileBeans.size());

        wuHuCalalogListAdapter = new WuHuCalalogListAdapter(getActivity(), wuHuEditBeanList);
        listView_catalog.setAdapter(wuHuCalalogListAdapter);
        wuHuCalalogListAdapter.notifyDataSetChanged();

        //除第一个fragment只显示会议目录外，其他碎片都显示文件列表界面
        fileListAdapter = new WuHuFileListAdapter(getActivity(), fileBeans);
        Log.d("wvdfsdfgsdg22222", "fileListAdapter");
        listView.setAdapter(fileListAdapter);
        fileListAdapter.setUpLoadFileInterface(this);
        fileListAdapter.setShareFileInterface(this);
        fileListAdapter.setPushFileInterface(this);
        fileListAdapter.notifyDataSetChanged();
        listView_catalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("catalog", position + "");
                intent3.putExtras(bundle);
                intent3.setAction(constant.CHANGE_CATALOG_BROADCAST);
                getActivity().sendBroadcast(intent3);


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WuHuEditBean.EditListBean.FileListBean fileBean = (WuHuEditBean.EditListBean.FileListBean) adapterView.getAdapter().getItem(i);
                if (fileBean.getPath() == null) {
                    Toast.makeText(getActivity(), "文件不存在,请等待主控端分享文件", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("主席分享推送的路径  ","  名字 "+fileBean.getName()+"    路径"+fileBean.getPath());
                if (UserUtil.ISCHAIRMAN){
                    Intent intent;
                    if (fileBean.getPath() == null) {
                        Toast.makeText(getActivity(), "文件不存在,请等待主控端分享文件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (fileBean.getFile_type().equals("3")) {
                        intent = new Intent();
                        intent.setClass(getActivity(), ActivityImage.class);
                        intent.putExtra("url", fileBean.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        startActivity(intent);
                    } else if (fileBean.getFile_type().equals("4")) {

                        if (UserUtil.isNetworkOnline) {
                            intent = new Intent();
                            if (fileBean.getPath() == null) {
                                Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            intent.setClass(getActivity(), SignActivity.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", false);
                            intent.putExtra("tempPath", false);
                            intent.putExtra("fileName", fileBean.getName());
                            startActivity(intent);
                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                                }
                            });
                        }

                    }


                }else {

                    File path = new File(fileShare);
                    File[] files = path.listFiles();// 读取
                    if (files == null) {
                        Toast.makeText(getActivity(), "文件不存在,请等待主控端分享文件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getFile(files,fileBean);


                }

            }
        });

        // 监听socket信息
        EventBus.getDefault().register(this);
        Log.d("wvdfsdfgsdg", "EventBus注册事件");
    }

  /*  private void getNetFile() {


        if (Hawk.contains(constant._id)) {
            UserUtil.meeting_record_id = Hawk.get(constant._id);
            String _id = UserUtil.meeting_record_id;
            int publice = 0;
            //绑定生命周期
            NetWorkManager.getInstance().getNetWorkApiService().getWuHuFileList(_id).compose(this.<BasicResponse<List<WuHuNetFileBean.DataBean>>>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<List<WuHuNetFileBean.DataBean>>>() {
                        @Override
                        protected void onSuccess(BasicResponse<List<WuHuNetFileBean.DataBean>> response) {
                            if (response != null) {
                                if (response.getData() == null) {
                                    return;
                                }
                                fileListBeanList = (List<WuHuNetFileBean.DataBean>) response.getData();

                                if (fileListBeanList == null | fileListBeanList.size() < 1) {

                                    return;
                                }

                                netFileName(fileListBeanList);

                            }
                        }
                    });

        }
    }*/

/*
    private void netFileName(List<WuHuNetFileBean.DataBean> fileListBeanList) {
        name.clear();
        fileOtherBeans.clear();
        fileBeans.clear();
        netFileBeans.clear();
        for (int i = 0; i < fileListBeanList.size(); i++) {
            WuHuNetFileBean.DataBean fileListBean = fileListBeanList.get(i);

            String c_id = fileListBeanList.get(i).get_id();
            String endStr = fileListBeanList.get(i).getFile_path().substring(fileListBeanList.get(i).getFile_path().lastIndexOf(".") + 1);
            Log.d("vrdvafd", "文件类型=" + fileListBeanList.get(i).getName() + "文件名字" + fileListBeanList.get(i).getFile_path() + "p====" + fileListBeanList.get(i).getName());
            //下载文件--子线程
            if ("4".equals(getType(endStr))) {

                String path = UrlConstant.baseUrl + "/" + fileListBeanList.get(i).getFile_path();
                String name = fileListBeanList.get(i).getName();
                String fileId = fileListBeanList.get(i).get_id();
                Log.d("网络文件地址", "path=====" + path);


                //   String fileData = httpDownloader.downloadFiles(fileListBeanList.get(i).getFile_path());
                DownloadUtil.get().download(path, netFilePath, fileListBeanList.get(i).getName(), new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        Log.v("dfsfff111", "下載成功,文件已存入手机内部存储设备根目录下Download文件夾中");
                      *//*  Looper.prepare();//增加部分
                        Looper.loop();//增加部分*//*
                        Message msg = new Message();
                        fileListBean.setName(name);
                        fileListBean.setPath(netFilePath + name);
                        fileListBean.set_id(fileId);
                        fileListBean.set_id(fileId);
                        fileListBean.setNet(true);

                        fileBean = new WuHuEditBean.EditListBean.FileListBean(name, netFilePath + name, "", "");
                        fileBean.setResImage(getIamge(endStr));
                        fileBean.setFile_type(getType(endStr));
                        fileBean.setNet(true);
                        msg.obj = fileBean;//可以是基本类型，可以是对象，可以是List、map等；
                        msg.what = 99;
                        mHandler.sendMessage(msg);
                        Log.v("dfsfff222", "下載成功,文件已存入手机内部存储设备根目录下Download文件夾中");
                    }

                    @Override
                    public void onDownloading(int progress) {
//                            Log.v(TAG, "下載進度" + progress);
//                progressDialog.setProgress(progress);
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }

                    }
                });


            } else {
                //除了文件外（doc,excle,pdf,ppt）,其他格式文件执行正常网络数据
                fileBean = new WuHuEditBean.EditListBean.FileListBean(fileListBeanList.get(i).getName(), fileListBeanList.get(i).getFile_path() + fileListBeanList.get(i).getName(), "", "");
                // fileBean = new NewFileBean.MeetingFileListBean(fileListBeanList.get(i).getFile_name(), fileListBeanList.get(i).getFile_path(), fileListBeanList.get(i).getUser_id().getName(), fileListBeanList.get(i).getUpload_time());
                fileBean.setResImage(getIamge(endStr));
                fileBean.setFile_type(getType(endStr));
                fileListBean.setNet(true);
                  *//*  fileListBean.setC_id(c_id);
                    fileListBean.setC_id(c_id);*//*
                //  fileListBean.setUser_id(userIdBean);
                fileBean.setC_id(c_id);
                fileBean.set_id(fileListBeanList.get(i).get_id());
                //fileListBean.setUser_id(fileListBeanList.get(i).getUser_id());
                fileBeans.add(fileBean);
            }

            if (fileBeans != null) {
                Log.d("requestCodeUr666", fileOtherBeans.size() + "  ");
            }
            // fileListAdapter = new FileListAdapter(getActivity(), fileOtherBeans);
            if (fileBeans == null || fileBeans.size() == 0) {
                return;
            }
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }

    }*/


    //从分享文件夹中剥离每个议题对应的文件
    private void getCopyFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (UserUtil.object) {
                    Log.d("rfrfeewtwtt ", "     textNub=" + textNub);
                    WuHuLocalFileBean wuHuLocalFileBean;
                    List<WuHuLocalFileBean.FileBean> fbList = new ArrayList<>();
                    if (Hawk.contains("WuHuLocalFileBean")) {
                        wuHuLocalFileBean = Hawk.get("WuHuLocalFileBean");
                        fbList = wuHuLocalFileBean.getFileBeanList();
                    }
                    copyFileBeans.clear();
                    fileBeans.clear();

                    for (int i = 0; i < fbList.size() - 1; i++) {
                        for (int j = fbList.size() - 1; j > i; j--) {
                            if (fbList.get(j).getFileName().equals(fbList.get(i).getFileName()) && fbList.get(j).getPos().equals(fbList.get(i).getPos())) {
                                fbList.remove(j);
                            }
                        }
                    }

                    if (Hawk.contains("wuhulocal")) {
                        copyFileBeans = Hawk.get("wuhulocal");
                        for (int i = 0; i < copyFileBeans.size() - 1; i++) {
                            for (int j = copyFileBeans.size() - 1; j > i; j--) {
                                if (copyFileBeans.get(j).getPath().equals(copyFileBeans.get(i).getPath())) {
                                    copyFileBeans.remove(j);
                                }
                            }
                        }
                    }

                    Log.d("rfrfeewtwtt1111  ", copyFileBeans.size() + "     copyFileBeans大小" + "   " + fbList.size() + "   fileBeans大小");
                    List<WuHuEditBean.EditListBean.FileListBean> tempFileBeans = new ArrayList<>();
                    tempFileBeans.clear();
                    for (int i = 0; i < fbList.size(); i++) {
                        Log.d("rfrfeewtwtt0000 从分享文件夹中剥离每个议题对应的文件 ", "     textNub=" + textNub + "   " + fbList.get(i).getPos() + "");
                        if (fbList.get(i).getPos().equals(textNub)) {
                            for (int n = 0; n < copyFileBeans.size(); n++) {
                                if (fbList.get(i).getFileName().equals(copyFileBeans.get(n).getName())) {
                                    tempFileBeans.add(copyFileBeans.get(n));
                                }

                            }
                        }
                    }

                    for (int i = 0; i < tempFileBeans.size(); i++) {

                        Log.d("当前议题对应的文件 ", "     当前议题：" + textNub + "    文件所属议题：" + fbList.get(i).getPos() + "    文件名：" + tempFileBeans.get(i).getName());
                    }
                    //取出缓存把当前议题文件关联到当前议题
                    if (Hawk.contains("WuHuFragmentData")) {
                        wuHuEditBeanList.clear();
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        wuHuEditBeanList = wuHuEditBean.getEditListBeanList();
                        if (wuHuEditBeanList == null || wuHuEditBeanList.size() == 0) {
                            return;
                        }
                        wuHuEditBeanList.get(Integer.valueOf(textNub)).setFileListBeanList(tempFileBeans);
                        wuHuEditBean.setEditListBeanList(wuHuEditBeanList);
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                    }
                    //发送消息跟新文件列表
                    Message shareMsg = new Message();
                    shareMsg.what = 200;
                    shareMsg.obj = tempFileBeans;
                    mHandler.sendMessage(shareMsg);


                    //由copy路径文件改为Hawk储存
                    //  getFileName(files);share

                }
            }
        }).start();


    }

    private void startPlayer(String url) {
        Uri uri = Uri.parse(url);//网络中的音乐文件
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            //  mediaPlayer.setDataSource(getActivity(), uri);//音乐文件路径
            mediaPlayer.prepare();//资源文件准备
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();//播放
            //    mediaPlayer.setLooping(true);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("mediaPlayer", "播放完成");
            }
        });
    }

    @Override
    public void onPlayerClick(String url) {
        Log.d("dfafaf333", "222223333" + url);
        Uri uri = Uri.parse(url);//网络中的音乐文件
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getActivity(), uri);//音乐文件路径
            mediaPlayer.prepare();//资源文件准备
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();//播放
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Log.d("ffdgsdgsg",text+" 重新路过~~~~");
    }

    @Override
    public void onPauseClick(String url) {
        Log.d("dsffff", "11111122222" + url);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();//暂停
        }
    }

    @Override
    public void onResetMusic(String url) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();//播放
            } else {
                onPlayerClick(url);
            }

        }

    }

    //String actionType:标识事件是分享还是推送  1：分享  2：推送
    @Override
    public void shareFileInfo(String path, String type, String flag, String name, String author, String time) {
        Log.d("FileFragment--分享", path + "===name==" + name);
        //查询参会人员
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(constant.QUERYATTENDSize);
        bean.setBody("");
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
        shareResponNum = 0;
        shareResponNo = 0;
        WuHuEditBean.EditListBean.FileListBean fileListBean = new WuHuEditBean.EditListBean.FileListBean(name, path, "", "");
        fileListBean.setFileMd5(Md5Util.getFileMD5(new File(path)));
        fileListBean.setMac(FLUtil.getMacAddress());
        fileListBean.setPos(textNub);
        wsUpdata(fileListBean, constant.FILEMD5SHARE);


        isPush = false;
        sharePath = path;
        shareType = type;
        shareName = name;
        // Message.obtain(handler, 0, response).sendToTarget();
    }

    @Override
    public void pushFileInfo(String path, String type, String flag, String name, String author, String time) {
        Log.d("FileFragment--推送", path + "===name==" + name);
        //通知所有fragment 都更新对应的议题文件列表
        //推送前查询其他客户端有无当前推送的文件
        //查询参会人员
        TempWSBean bean = new TempWSBean();
        bean.setReqType(0);
        bean.setUserMac_id(FLUtil.getMacAddress());
        bean.setPackType(constant.QUERYATTENDSize);
        bean.setBody("");
        String strJson = new Gson().toJson(bean);
        JWebSocketClientService.sendMsg(strJson);
        pushResponNum = 0;
        pushResponNo = 0;

        WuHuEditBean.EditListBean.FileListBean fileListBean = new WuHuEditBean.EditListBean.FileListBean(name, path, "", "");
        fileListBean.setFileMd5(Md5Util.getFileMD5(new File(path)));
        fileListBean.setMac(FLUtil.getMacAddress());
        fileListBean.setPos(textNub);
        wsUpdata(fileListBean, constant.FILEMD5PUSH);
        opened = false;
        isPush = true;
        pushName = name;
        pushPath = path;
        pushType = type;

    }

    //发送每个fragment标识及文件数据名称
    private void sendFragmenFlag() {
        //通知所有fragment 都更新对应的议题文件列表
        WuHuLocalFileBean wuHuLocalFileBean = new WuHuLocalFileBean();
        List<WuHuLocalFileBean.FileBean> fileBeanList = new ArrayList<>();

        if (Hawk.contains("wuhulocal")) {
            List<WuHuEditBean.EditListBean.FileListBean> flagFileList = Hawk.get("wuhulocal");
            if (flagFileList == null || flagFileList.size() == 0) {
                return;
            }
            for (int i = 0; i < flagFileList.size(); i++) {
                WuHuLocalFileBean.FileBean fb = new WuHuLocalFileBean.FileBean();
                if (!StringUtils.isEmpty(textNub)) {
                    fb.setPos(flagFileList.get(i).getPos());
                }
                fb.setFileName(flagFileList.get(i).getName());
                fileBeanList.add(fb);
            }

        }

        wuHuLocalFileBean.setFileBeanList(fileBeanList);
        //1：添加本地
        wuHuLocalFileBean.setFlag("1");
        wsUpdata(wuHuLocalFileBean, constant.REFRESH_WUHU_FILE_FRAGMENT);

    }

    @Override
    public void sendFileInfo(String path, String type, String flag) {
        //把文件封装在RequestBody里

        File file = new File(path);
        if (file == null) {
            return;
        }
        videoPath = path;
        upLoadFileType = getMimeType(file.getName());
        endStrAll = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), file);
        progressBarLl.setVisibility(View.VISIBLE);
       /* Map<String, Object> map = new HashMap<>();
        map.put("dirName", creatDirectory(type));
        map.put("uid", "");
        map.put("file", "");*/
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        NetWorkManager.getInstance().getNetWorkApiService().upLoadFile(creatDirectory(type), "", "", part).compose(this.<BasicResponse<UploadBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<UploadBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<UploadBean> response) {
                        if (response != null) {
                            String c_id = "";
                            String user_id = "";
                            String _id = "";
                            if (Hawk.contains("_id")) {
                                _id = Hawk.get("_id");
                            }
                            if (Hawk.contains("c_id")) {
                                c_id = Hawk.get("c_id");
                            }
                            if (Hawk.contains("user_id")) {
                                user_id = Hawk.get("user_id");
                            }
                            uploadBean = (UploadBean) response.getData();
                            //创建文件请求体类
                            createFileBeanRequest = new CreateFileBeanRequest();
                            createFileBeanRequest.setC_id(c_id);
                            createFileBeanRequest.setUser_id(user_id);
                            // createFileBeanRequest.setType(endStr);
                            createFileBeanRequest.setMeeting_record_id(_id);
                            List<CreateFileBeanRequest.FileListBean> fileListBeans = new ArrayList<>();
                            CreateFileBeanRequest.FileListBean fileListBean = new CreateFileBeanRequest.FileListBean();
                            fileListBean.setName(file.getName());
                            fileListBean.setPath(uploadBean.getUrl());
                            fileListBean.setSize("100");
                            fileListBean.setSuffix(endStrAll);
                            fileListBean.setUnclassified(flag);
                            fileListBean.setType(getFileType(endStrAll));//约定的文件四种类型：文档、图片、视频、其他。
                            fileListBeans.add(fileListBean);
                            createFileBeanRequest.setFile_list(fileListBeans);
                            creatFile(createFileBeanRequest);


                        }

                    }
                });
    }

    private void creatFile(CreateFileBeanRequest createFileBeanRequest) {
        NetWorkManager.getInstance().getNetWorkApiService().createMeetingFile(createFileBeanRequest).compose(this.<BasicResponse<CreateFileBeanResponse>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<CreateFileBeanResponse>>() {
                    @Override
                    protected void onFail(BasicResponse<CreateFileBeanResponse> response) {
                        super.onFail(response);
                        progressBarLl.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        progressBarLl.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(BasicResponse<CreateFileBeanResponse> response) {
                        if (response != null) {
                            Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            progressBarLl.setVisibility(View.GONE);
//                            intent.setAction(constant.FRESH_FILE);
                            intent.setAction(constant.FRESH_FILE);
                            getActivity().sendBroadcast(intent);
                            Log.d("fhjfj", endStrAll + "------" + videoPath);
                            if ("mp4".equals(endStrAll)) {
                                Hawk.put("videoPath", videoPath);
                            }
                        }

                    }
                });
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

    //获取本地上传的文件
    private void getFileName(File[] files) {
        Log.d(TAG, "路过~~~~~11");
        copyFileBeans.clear();
        fileBeans.clear();
        String content = "";
        if (files != null) {
            // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                Log.d(TAG, "路过~~~~~11" + file.getPath());

                if (file.isDirectory()) {
                    Log.d(TAG, "若是文件目录。继续读1" + file.getName().toString()
                            + file.getPath().toString());

                    getFileName(file.listFiles());
                    Log.d(TAG, "若是文件目录。继续读2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d(TAG, "文件类型=" + endStr + "文件名字" + fileName);
                    fileBean = new WuHuEditBean.EditListBean.FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    //  fileBean.setType(endStr);
                    fileBean.setType(getFileType(endStr));
                    fileBean.setNet(false);
                    copyFileBeans.add(fileBean);

                }
            }
            fileBeans.addAll(copyFileBeans);
            fileBeans.addAll(netFileBeans);
            fileBeans.addAll(shareFileBeans);
           /* if (fileBeans == null || fileBeans.size() == 0) {
                return;
            }*/
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }
    }

/*    //获取网络下载到的文件
    private void getNetFileName(File[] files) {
        Log.d(TAG, "路过~~~~~11");
        netFileBeans.clear();
        fileBeans.clear();
        String content = "";
        if (files != null) {// 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                Log.d(TAG, "路过~~~~~11" + file.getPath());

                if (file.isDirectory()) {
                    Log.d(TAG, "若是文件目录。继续读1" + file.getName().toString()
                            + file.getPath().toString());

                    getFileName(file.listFiles());
                    Log.d(TAG, "若是文件目录。继续读2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d(TAG, "文件类型=" + endStr + "文件名字" + fileName);
                    fileBean = new WuHuEditBean.EditListBean.FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    //  fileBean.setType(endStr);
                    fileBean.setType(getFileType(endStr));
                    fileBean.setNet(true);
                    netFileBeans.add(fileBean);

                }
            }
            fileBeans.addAll(copyFileBeans);
            fileBeans.addAll(netFileBeans);
            fileBeans.addAll(shareFileBeans);
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }
    }*/

    @Override
    protected void initView() {

        // mTextView.setText(text);
        Log.d(TAG + toString(), "onCreateView: ");

        /*
         * 临时会议时，全局发送本设备Ip地址到其他设备
         * */

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);//休眠3
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        String str = constant.SHARE_FILE_IP + getIpAddressString();
                        Hawk.put("SelfIpAddress", selfIp);//自身Ip
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


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                fileListAdapter.setCompleted(true);
                fileListAdapter.notifyDataSetChanged();
                Log.d("ValidFragment", "播放完了");

            }
        });


    }

    /**
     * 收到websocket 信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(EventMessage message) {
        Log.e("onReceiveMsg000011: ", message.toString());
        Log.d("wvdfsdfgsdg222", "onReceiveMsg事件");
        //  client端接收到信息
        if (message.getType().equals(MessageReceiveType.MessageClient)) {
            Log.d("wvdfsdfgsdg3333", "MessageClient");
            // 查询投票信息
            if (message.getMessage().contains(constant.WUHUADDFRAGMENT)) {
                Log.e("onReceiveMsg11111   fragment: ", message.toString());
                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                     /*   WuHuEditBean wuHuEditBean = wsebean.getBody();
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        UiaSsign(wuHuEditBean.getEditListBeanList());
                        Log.d("gdgsdgetEditListB大小=  fragment", wuHuEditBean.getEditListBeanList().size() + "");
                        refreshUi(wuHuEditBean);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message.getMessage().contains(constant.REFRASHWuHUSIGLEDATA)) {
                Log.d("onReceiveMsg  fragment", "REFRASHWuHUSIGLEDATA");
                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        WuHuEditBean wuHuEditBean = wsebean.getBody();
                        // UiaSsign(wuHuEditBean.getEditListBeanList());
                    //    refreshUi(wuHuEditBean);
                        Log.d("onReceiveMsg 单个保存保存 fragment", "REFRASHWuHUSIGLEDATA");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (message.getMessage().contains(constant.REFRASHWuHUALL)) {

                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        WuHuEditBean wuHuEditBean = wsebean.getBody();
                        //UiaSsign(wuHuEditBean.getEditListBeanList());
                      //  refreshUi(wuHuEditBean);
                        Log.d("onReceiveMsg q全部保存 fragment", "REFRASHWuHUALL" + "     textNum=" + textNub);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (message.getMessage().contains(constant.DELETE_WUHU_FRAGMENT)) {
                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    if (wsebean != null) {
                     /*   wuHuEditBeanList.clear();
                        WuHuEditBean deletWuHuEditBean = wsebean.getBody();

                        if (Hawk.contains("WuHuFragmentData")) {
                            WuHuEditBean wuHuFragmentData = Hawk.get("WuHuFragmentData");
                            List<WuHuEditBean.EditListBean> listBeans = new ArrayList<>();
                            listBeans = deletWuHuEditBean.getEditListBeanList();
                            if (listBeans.size() == 0) {
                                return;
                            }
                            wuHuEditBeanList.addAll(listBeans);
                            wuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
                            Hawk.put("WuHuFragmentData", wuHuFragmentData);

                            wuHuEditBeanList.addAll(wuHuEditBeanList);
                            wuHuCalalogListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                            wuHuCalalogListAdapter.notifyDataSetChanged();
                        }*/
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


            } else if (message.getMessage().contains(constant.QUERYVOTE_WUHU_FRAGMENT)) {
                Log.d("wrewarawrawerwqeqwe   fragment", "查询");
                Log.e("onReceiveMsg查询:   fragment  ", message.toString());

                try {
                    TempWSBean<ArrayList> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<ArrayList<WuHuEditBean.EditListBean>>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        List<WuHuEditBean.EditListBean> editListBeans = new ArrayList<>();
                        editListBeans=wsebean.getBody();
                        UiaSsign(editListBeans);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message.getMessage().contains(constant.REFRESH_WUHU_FILE_FRAGMENT)) {

                try {
                    TempWSBean<WuHuLocalFileBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuLocalFileBean>>() {
                    }.getType());
                    if (wsebean != null) {
                        WuHuLocalFileBean wuHuLocalFileBean = wsebean.getBody();
                   /*     if (wuHuLocalFileBean != null) {
                            Hawk.put("WuHuLocalFileBean", wuHuLocalFileBean);
                        }
                        if (wuHuLocalFileBean != null) {
                            //  getCopyFile();
                        }*/

                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


            }
            //分享回应
            else if (message.getMessage().contains(constant.FILERESPONDSHARE)) {

                try {
                    TempWSBean<SharePushFileBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<SharePushFileBean>>() {
                    }.getType());
                    if (wsebean != null) {
                        SharePushFileBean sharePushFileBean = wsebean.getBody();
                        //其他fragment不处理
                        if (!sharePushFileBean.getPos().equals(textNub)) {
                            return;
                        }

                        shareResponNum++;

                        if (sharePushFileBean != null) {
                            //谁发出的分享谁处理
                            if (sharePushFileBean.getMac().equals(FLUtil.getMacAddress())) {
                                pushShareRespon(sharePushFileBean, "1");
                            }

                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

                //推送回应
            } else if (message.getMessage().contains(constant.FILERESPONDPUSH)) {

                try {
                    TempWSBean<SharePushFileBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<SharePushFileBean>>() {
                    }.getType());
                    if (wsebean != null) {
                        SharePushFileBean sharePushFileBean = wsebean.getBody();
                        //其他fragment不处理
                        if (!sharePushFileBean.getPos().equals(textNub)) {
                            return;
                        }
                        pushResponNum++;
                        //谁发出的推送谁处理
                        if (sharePushFileBean.getMac().equals(FLUtil.getMacAddress())) {
                            pushShareRespon(sharePushFileBean, "2");
                        }


                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    //fragment赋值
    private void UiaSsign(List<WuHuEditBean.EditListBean> editListBeanList) {

        Log.d("UiaSsign", "议题：" + textNub + "   收到更新消息");

        if (Integer.valueOf(textNub) == 0) {
            if (listView_catalog != null && file_ly != null && fragment_ttile_rl != null && topic_ll != null && fragmentLine != null && nn != null && listView_catalog_ll != null && scrollView_file != null && scrollView_cata != null) {
                listView_catalog.setVisibility(View.VISIBLE);
                listView_catalog_ll.setVisibility(View.VISIBLE);
                file_ly.setVisibility(View.GONE);
                fragment_ttile_rl.setVisibility(View.VISIBLE);
                topic_ll.setVisibility(View.GONE);
                fragmentLine.setVisibility(View.VISIBLE);
                nn.setVisibility(View.GONE);
                scrollView_file.setVisibility(View.GONE);
                scrollView_cata.setVisibility(View.VISIBLE);
                if (wuHuCalalogListAdapter != null) {
                    wuHuCalalogListAdapter.setWuHuEditBeanList(editListBeanList);
                    listView_catalog.setAdapter(wuHuCalalogListAdapter);
                    wuHuCalalogListAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (listView_catalog != null && file_ly != null && fragment_ttile_rl != null && topic_ll != null && fragmentLine != null && nn != null && listView_catalog_ll != null && scrollView_file != null && scrollView_cata != null) {
                listView_catalog.setVisibility(View.GONE);
                file_ly.setVisibility(View.VISIBLE);
                listView_catalog_ll.setVisibility(View.GONE);
                fragment_ttile_rl.setVisibility(View.GONE);
                topic_ll.setVisibility(View.VISIBLE);
                fragmentLine.setVisibility(View.GONE);
                nn.setVisibility(View.VISIBLE);
                scrollView_file.setVisibility(View.VISIBLE);
                scrollView_cata.setVisibility(View.GONE);
                if (fileListAdapter != null) {
                    for (int i = 0; i < editListBeanList.size(); i++) {
                        Log.d("ggggertretwetwt1111", "议题：  " + editListBeanList.get(i).getPos() + "    textNub=" + textNub + "   ");
                        if (editListBeanList.get(i).getPos().equals(textNub)) {

                            List<WuHuEditBean.EditListBean.FileListBean> allFiles = new ArrayList<>();
                            List<WuHuEditBean.EditListBean.FileListBean> shareFiles = new ArrayList<>();
                            List<WuHuEditBean.EditListBean.FileListBean> localFiles = new ArrayList<>();

                            if (editListBeanList.get(i).getFileListBeanList() != null) {
                                shareFiles.addAll(editListBeanList.get(i).getFileListBeanList());
                            }
                            if (editListBeanList.get(i).getLocalFiles() != null) {
                                localFiles.addAll(editListBeanList.get(i).getLocalFiles());
                            }

                            //当是主席时  显示本地文件+分享文件
                            if (UserUtil.ISCHAIRMAN) {
                                fileListAdapter.setGridViewBeanList(localFiles);
                                fileListAdapter.notifyDataSetChanged();
                            }else {
                                //当是非主席时  只显示分享文件
                                fileListAdapter.setGridViewBeanList(localFiles);
                                fileListAdapter.notifyDataSetChanged();

                            }



                        }
                    }
                }
                }



        }
        if (tittle1 != null) {
            tittle1.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopics());
            if (Hawk.contains("company_name")) {
                String str = Hawk.get("company_name");
                tittle1.setText(str);
            }
        }
        if (tittle2 != null) {

            tittle2.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
            if (Hawk.contains("tittle2")) {
                String str = Hawk.get("tittle2");
                tittle2.setText(str);
            }
        }
        Log.d("fdafafaff=fragment", textNub + "editListBeanList=   " + editListBeanList.get(Integer.valueOf(textNub)).getTopics() + "   " + editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
        if (topic != null) {
            topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
        }

        if (attend != null) {
            attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getReportingUnit());
        }

        if (attend2 != null) {
            attend2.setText(editListBeanList.get(Integer.valueOf(textNub)).getParticipantUnits());
        }

    }


    @Override
    public void sendfilePth(String filePth) {

    }

    private void pushShareRespon(SharePushFileBean sharePushFileBean, String action) {

        int peopleNum = 0;
        if (Hawk.contains("TempMeetingAttende")) {
            String str = Hawk.get("TempMeetingAttende");
            peopleNum = Integer.valueOf(str);
            //除去自身
            peopleNum = peopleNum - 1;
        }

        //分享回应
        if (action.equals("1")) {
            //统计没有文件的人数
            if (!sharePushFileBean.isHave()) {
                shareResponNo++;
            }
            //等人都回复完毕在判断是重新推送还是直接打开
            if (peopleNum == shareResponNum) {
                if (shareResponNo == 0) {
                    Toast.makeText(getActivity(), "对方文件已存在", Toast.LENGTH_SHORT).show();
                } else {
                    paths.clear();
                    fileNames.clear();
                    paths.add(sharePath);
                    fileNames.add(shareName);
                    Message.obtain(mHandler, 4, name).sendToTarget();
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (Hawk.contains("stringsIp")) {
                                stringListIp.clear();
                                stringListIp = Hawk.get("stringsIp");
                            }
                            //根据Ip将文件发送到不同的设备
                            for (int i = 0; i < stringListIp.size(); i++) {
                                socketShareFileManager.SendFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT, "1", textNub);
                            }
                        }

                    });
                    sendThread.start();
           /*  new Thread() {
                        @Override
                        public void run() {
                            copyFile(sharePath, fileShare +textNub+ constant.WUHUSHARE+shareName);
                        }
                    }.start();*/

                    String path=fileShare +textNub+ constant.WUHUSHARE+shareName;
                    String endStr = path.substring(path.lastIndexOf(".") + 1);
                    Log.d("fdgfsdgsgsg111",fileShare +textNub+ constant.WUHUSHARE+shareName);//textNub+ constant.WUHUSHARE+shareName
                    WuHuEditBean.EditListBean.FileListBean fileList=new WuHuEditBean.EditListBean.FileListBean(shareName,
                            fileShare +textNub+ constant.WUHUSHARE+shareName,"","");
                    fileList.setFile_type(getType(endStr));
                    fileList.setResImage(getIamge(endStr));
                    fileList.setFile_type(getType(endStr));
                    fileList.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    fileList.setType(getFileType(endStr));
                    fileList.setNet(false);
                    if (textNub != null) {
                        fileList.setPos(textNub);
                    }
                   if (Hawk.contains("WuHuFragmentData")) {
                        //临时存放的议题集合
                        List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        copyEdList = wuHuEditBean.getEditListBeanList();
                        //遍历更新当前议题下的文件
                        List<WuHuEditBean.EditListBean.FileListBean> shareService=new ArrayList<>();
                        shareService.clear();
                        if (copyEdList != null && copyEdList.size() > 0) {

                            for (int i = 0; i < copyEdList.size(); i++) {
                                //每一个议题
                                WuHuEditBean.EditListBean editListBean = copyEdList.get(i);

                                if (editListBean.getPos().equals(textNub)) {
                                    if (editListBean.getFileListBeanList()!=null){
                                        shareService.addAll(editListBean.getFileListBeanList());
                                    }

                                    //把当前本地议题文件集合放入对应的议题
                                    shareService.add(fileList);


                               /*     //去除重复分享和推送的文件
                                    for (int  k= 0; k < shareService.size() - 1; k++) {
                                        for (int j = shareService.size() - 1; j > k; j--) {
                                            if (shareService.get(j).getPath().equals(shareService.get(k).getPath()) && shareService.get(j).getPos().equals(shareService.get(k).getPos())) {
                                                shareService.remove(j);
                                            }
                                        }
                                    }*/

                                    editListBean.setFileListBeanList(shareService);
                                    copyEdList.set(Integer.valueOf(textNub), editListBean);

                                }
                            }
                            wuHuEditBean.setEditListBeanList(copyEdList);
                        }
                        Hawk.put("WuHuFragmentData", wuHuEditBean);
                        if (UserUtil.ISCHAIRMAN) {
                            wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                        }
                    }



                }

            }


            //推送回应
        } else if (action.equals("2")) {
            //统计没有文件的人数
            if (!sharePushFileBean.isHave()) {
                pushResponNo++;
            }
            //等人都回复完毕在判断是重新推送还是直接打开
            if (peopleNum == pushResponNum) {

                if (pushResponNo == 0) {
                    if (StringUtils.isEmpty(pushType) || StringUtils.isEmpty(pushPath) || StringUtils.isEmpty(pushName)) {
                        return;
                    }
                    Log.d("sADdDdD222", "pushType=" + pushType + "    pushPath=" + pushPath + "    pushName=" + pushName);
                    Intent intent;
                    if (pushType.equals("3")) {
                        Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                        if (topActivity != null) {
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
                        if (pushPath == null) {
                            Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent = new Intent();
                        intent.setClass(getActivity(), ActivityImage.class);
                        intent.putExtra("url", pushPath);
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        startActivity(intent);
                        Log.d("重复打开  wuhuFment ActivityImage md5", "333333");
//                        if (!opened) {
////                            startActivity(intent);
////                        }
////                        opened = true;
                             /*     if ( isActivityTop(ActivityImage.class,context)){
                                      intent=new Intent(constant.WUHU_IMAGE_FILE_BROADCAST);
                                      Bundle bundle=new Bundle();
                                      bundle.putString("url",pushPath);
                                      intent.putExtras(bundle);
                                      getActivity().sendBroadcast(intent);

                                  }else {
                                      intent = new Intent();
                                      intent.setClass(getActivity(), ActivityImage.class);
                                      intent.putExtra("url", pushPath);
                                      intent.putExtra("isOpenFile", true);
                                      intent.putExtra("isNetFile", false);
                                      startActivity(intent);
                                  }*/


                    } else if (pushType.equals("4")) {
                        if (UserUtil.isNetworkOnline) {
                            Activity topActivity = (Activity) ActivityUtils.getTopActivity();
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
                            if (pushPath == null) {
                                Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (pushPath == null) {
                                Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            intent = new Intent();
                            intent.setClass(getActivity(), SignActivity.class);
                            intent.putExtra("url", pushPath);
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", false);
                            intent.putExtra("tempPath", false);
                            intent.putExtra("fileName", pushName);
                            startActivity(intent);
                            Log.d("重复打开  wuhuFment SignActivity md5", "44444");
                          /*  if (!opened) {
                                startActivity(intent);
                            }
                            opened = true;*/
                        } else {
                            CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                                @Override
                                public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                    startActivity(FileUtils.openFile(path, getActivity()));
                                }
                            });
                        }
                    }
                    isPush = false;

                } else {
                    //有一个人未收到  全部重新发送
                    if (pushResponNo > 0) {
                        fileNames.clear();
                        paths.clear();
                        paths.add(pushPath);
                        fileNames.add(pushName);
                        Message.obtain(mHandler, 4, name).sendToTarget();

                        Thread sendThread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                if (Hawk.contains("stringsIp")) {
                                    stringListIp.clear();
                                    stringListIp = Hawk.get("stringsIp");
                                }
                                //根据Ip将文件发送到不同的设备
                                for (int i = 0; i < stringListIp.size(); i++) {
                                    socketShareFileManager.SendFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT, "2", textNub);
                                }


                            }
                        });
                        sendThread.start();
                      /*  new Thread() {
                            @Override
                            public void run() {
                                copyFile(pushPath, fileShare +textNub+ constant.WUHUPUSH+pushName);
                            }
                        }.start();*/

                        File  file=new File(fileShare +textNub+ constant.WUHUPUSH+pushName);
                        String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);//textNub+ constant.WUHUPUSH+pushName,
                        WuHuEditBean.EditListBean.FileListBean fileListBean=new WuHuEditBean.EditListBean.FileListBean(pushName,
                                fileShare +textNub+ constant.WUHUPUSH+pushName,"","");
                        fileListBean.setFile_type(getType(endStr));
                        fileListBean.setResImage(getIamge(endStr));
                        fileListBean.setFile_type(getType(endStr));
                        fileListBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                        fileListBean.setType(getFileType(endStr));
                        fileListBean.setNet(false);
                        if (textNub != null) {
                            fileListBean.setPos(textNub);
                        }
                     if (Hawk.contains("WuHuFragmentData")) {
                            //临时存放的议题集合
                            List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                            copyEdList = wuHuEditBean.getEditListBeanList();
                            //遍历更新当前议题下的文件
                            List<WuHuEditBean.EditListBean.FileListBean> shareService=new ArrayList<>();
                            shareService.clear();
                            if (copyEdList != null && copyEdList.size() > 0) {

                                for (int i = 0; i < copyEdList.size(); i++) {
                                    //每一个议题
                                    WuHuEditBean.EditListBean editListBean = copyEdList.get(i);

                                    if (editListBean.getPos().equals(textNub)) {
                                        if (editListBean.getFileListBeanList()!=null){
                                            shareService.addAll(editListBean.getFileListBeanList());
                                        }

                                        //把当前本地议题文件集合放入对应的议题
                                        shareService.add(fileListBean);



/*                                       //去除重复分享和推送的文件
                                        for (int  k= 0; k < shareService.size() - 1; k++) {
                                            for (int j = shareService.size() - 1; j > k; j--) {
                                                if (shareService.get(j).getPath().equals(shareService.get(k).getPath()) && shareService.get(j).getPos().equals(shareService.get(k).getPos())) {
                                                    shareService.remove(j);
                                                }
                                            }
                                        }*/

                                        editListBean.setFileListBeanList(shareService);
                                        copyEdList.set(Integer.valueOf(textNub), editListBean);

                                    }
                                }
                                wuHuEditBean.setEditListBeanList(copyEdList);
                            }
                            Hawk.put("WuHuFragmentData", wuHuEditBean);
                            if (UserUtil.ISCHAIRMAN) {
                                wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                            }
                        }











                    }


                }

            }

        }

        Log.d("md5验证信息", "除自身外一共人数：" + peopleNum + "    分享回复人数：" + shareResponNum + "    分享没有文件人数：" + shareResponNo + "    推送回复人数：" + pushResponNum + "    推送没有文件人数：" + pushResponNo);

    }

    private void deletRefreUi(WuHuDeleteFragmentBean wuHuDeleteFragmentBean) {

        int size = Integer.valueOf(wuHuDeleteFragmentBean.getListSize());
        size = size + 1;
        if (Integer.valueOf(textNub) == 0) {
            listView_catalog.setVisibility(View.VISIBLE);
            file_ly.setVisibility(View.GONE);

        } else {
            listView_catalog.setVisibility(View.GONE);
            file_ly.setVisibility(View.VISIBLE);
            if (Integer.valueOf(textNub) > size) {
                textNub = String.valueOf(Integer.valueOf(textNub) - 1);
                setTopicText(textNub);
            }
        }


    }

    private void refreshUi(WuHuEditBean wuHuEditBean) {
        Log.d("新增的textNub=", textNub + "");
        wuHuEditBeanList.clear();
        if (Integer.valueOf(textNub) == 0) {
            listView_catalog.setVisibility(View.VISIBLE);
            file_ly.setVisibility(View.GONE);

        } else {
            listView_catalog.setVisibility(View.GONE);
            file_ly.setVisibility(View.VISIBLE);

        }
        List<WuHuEditBean.EditListBean> allList = wuHuEditBean.getEditListBeanList();

        if (allList == null || allList.size() == 0) {
            return;
        }
        wuHuEditBeanList.addAll(allList);
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuFragmentData = Hawk.get("WuHuFragmentData");
            wuHuFragmentData.setEditListBeanList(wuHuEditBeanList);
            Hawk.put("WuHuFragmentData", wuHuFragmentData);
        }
        tittle1.setText(wuHuEditBean.getTopics());
        tittle2.setText(wuHuEditBean.getTopic_type());
        int s = Integer.valueOf(textNub);
        setTopicText(s + "");
        topic.setText(wuHuEditBeanList.get(Integer.valueOf(textNub)).getSubTopics());
        attend.setText(wuHuEditBeanList.get(Integer.valueOf(textNub)).getReportingUnit());
        attend2.setText(wuHuEditBeanList.get(Integer.valueOf(textNub)).getParticipantUnits());
        Log.d("列单位  fragment  单个 全部保存 ", wuHuEditBeanList.get(Integer.valueOf(textNub)).getParticipantUnits());
        wuHuCalalogListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
        wuHuCalalogListAdapter.notifyDataSetChanged();
        switch (wuHuEditBean.getLine_color()) {

            case "1":
                fragmentLine.setBackgroundColor(Color.parseColor("#EA4318"));
                nn.setBackgroundColor(Color.parseColor("#EA4318"));
                break;
            case "2":
                fragmentLine.setBackgroundColor(Color.parseColor("#1D1D1D"));
                nn.setBackgroundColor(Color.parseColor("#1D1D1D"));
                break;
        }
        switch (wuHuEditBean.getThem_color()) {
            case "3":
                root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home);
                break;
            case "4":
                root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home2);
                break;
            case "5":
                root_lin.setBackgroundColor(Color.parseColor("#EBE9EA"));
                break;
            case "6":
                root_lin.setBackgroundColor(Color.parseColor("#EFF4F8"));
                break;
            case "7":
                root_lin.setBackgroundColor(Color.parseColor("#F2F8F5"));
                break;
        }


    }

    private void setTopicText(String numb) {


        switch (numb) {
            case "1":
                tittle_num.setText("议题一");
                break;
            case "2":
                tittle_num.setText("议题二");
                break;
            case "3":
                tittle_num.setText("议题三");
                break;
            case "4":
                tittle_num.setText("议题四");
                break;
            case "5":
                tittle_num.setText("议题五");
                break;
            case "6":
                tittle_num.setText("议题六");
                break;
            case "7":
                tittle_num.setText("议题七");
                break;
            case "8":
                tittle_num.setText("议题八");
                break;
            case "9":
                tittle_num.setText("议题九");
                break;
            case "10":
                tittle_num.setText("议题十");
                break;
            default:
                tittle_num.setText("议题" + textNub);
                break;
        }

    }

    /*
     * 临时会议时获取设备有线网IP地址
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
     * 临时会议时获取设备无线网IP地址
     * */
    public String getIpAddress() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
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

    //传给后台创建文件目录
    private String creatDirectory(String end) {
        if ("1".equals(end)) {
            return "music";
        } else if ("2".equals(end)) {
            return "video";
        } else if ("3".equals(end)) {
            return "image";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else if ("4".equals(end)) {
            return "directory";
        } else {
            return "directory";
        }

    }

    public static WuHuFragment newInstance(String text) {
        WuHuFragment wuHuFragment = new WuHuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        wuHuFragment.setArguments(bundle);
        return wuHuFragment;
    }

    //删除操作更新tag值
    public void updateArguments(String newText) {

        this.textNub = newText;

        Bundle bundle = getArguments();

        if (bundle != null) {
            bundle.putString("text", newText);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        textNub = getArguments().getString("text");
        String strFilePath = fileShare;
        File path = new File(strFilePath);
        File[] files = path.listFiles();// 读取
        Log.d("缓存里面的的textNub=", textNub + "  isDeletOption=" + isDeletOption);
        if (isVisibleToUser) {
            if (Hawk.contains("WuHuFragmentData")) {
                       /*  int s=Integer.valueOf(textNub)+1;
             tittle_num.setText("议题"+s);*/
                WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
                editListBeanList = wuHuEditBean.getEditListBeanList();

                if (Integer.valueOf(textNub) == 0) {
                    if (listView_catalog != null && file_ly != null && fragment_ttile_rl != null && topic_ll != null && fragmentLine != null && nn != null && listView_catalog_ll != null && scrollView_file != null && scrollView_cata != null) {
                        listView_catalog.setVisibility(View.VISIBLE);
                        listView_catalog_ll.setVisibility(View.VISIBLE);
                        file_ly.setVisibility(View.GONE);
                        fragment_ttile_rl.setVisibility(View.VISIBLE);
                        topic_ll.setVisibility(View.GONE);
                        fragmentLine.setVisibility(View.VISIBLE);
                        nn.setVisibility(View.GONE);
                        scrollView_file.setVisibility(View.GONE);
                        scrollView_cata.setVisibility(View.VISIBLE);
                        if (wuHuCalalogListAdapter != null) {
                            wuHuCalalogListAdapter.setWuHuEditBeanList(editListBeanList);
                            listView_catalog.setAdapter(wuHuCalalogListAdapter);
                            wuHuCalalogListAdapter.notifyDataSetChanged();
                        }

                    }

                } else {
                    if (listView_catalog != null && file_ly != null && fragment_ttile_rl != null && topic_ll != null && fragmentLine != null && nn != null && listView_catalog_ll != null && scrollView_file != null && scrollView_cata != null) {
                        listView_catalog.setVisibility(View.GONE);
                        file_ly.setVisibility(View.VISIBLE);
                        listView_catalog_ll.setVisibility(View.GONE);
                        fragment_ttile_rl.setVisibility(View.GONE);
                        topic_ll.setVisibility(View.VISIBLE);
                        fragmentLine.setVisibility(View.GONE);
                        nn.setVisibility(View.VISIBLE);
                        scrollView_file.setVisibility(View.VISIBLE);
                        scrollView_cata.setVisibility(View.GONE);
                        if (fileListAdapter != null) {
                            for (int i = 0; i < editListBeanList.size(); i++) {
                                if (editListBeanList.get(i).getPos().equals(textNub)) {
                                    List<WuHuEditBean.EditListBean.FileListBean> allFiles = new ArrayList<>();

                                    List<WuHuEditBean.EditListBean.FileListBean> shareFiles = new ArrayList<>();

                                    List<WuHuEditBean.EditListBean.FileListBean> localFiles = new ArrayList<>();

                                    if (editListBeanList.get(i).getFileListBeanList() != null) {
                                        shareFiles.addAll(editListBeanList.get(i).getFileListBeanList());
                                    }
                                    if (editListBeanList.get(i).getLocalFiles() != null) {
                                        localFiles.addAll(editListBeanList.get(i).getLocalFiles());
                                    }
                                    //当是主席时  显示本地文件+分享文件
                                    if (UserUtil.ISCHAIRMAN) {

                                            fileListAdapter.setGridViewBeanList(localFiles);
                                            fileListAdapter.notifyDataSetChanged();

                                    } else {
                                        //当是非主席时  只显示分享文件
                                            fileListAdapter.setGridViewBeanList(localFiles);
                                            fileListAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        }
                    }

                }
                if (tittle1 != null) {
                    tittle1.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopics());
                    if (Hawk.contains("company_name")) {
                        String str = Hawk.get("company_name");
                        tittle1.setText(str);
                    }
                }
                if (tittle2 != null) {

                    tittle2.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
                    if (Hawk.contains("tittle2")) {
                        String str = Hawk.get("tittle2");
                        tittle2.setText(str);
                    }
                }
                Log.d("fdafafaff=fragment", textNub + "editListBeanList=   " + editListBeanList.get(Integer.valueOf(textNub)).getTopics() + "   " + editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
                if (topic != null) {
                    topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
                }

                if (attend != null) {
                    attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getReportingUnit());
                }

                if (attend2 != null) {
                    attend2.setText(editListBeanList.get(Integer.valueOf(textNub)).getParticipantUnits());
                }
     /*    if (fileListAdapter!=null){
             fileListAdapter.notifyDataSetChanged();
         }*/
           /*  int s=Integer.valueOf(textNub)+1;
             tittle_num.setText("议题"+s);*/
     /*        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
             List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
             if (wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() == 0) {
                 return;
             }
             editListBeanList = wuHuEditBean.getEditListBeanList();


             if (Integer.valueOf(textNub)==0){
                 if (listView_catalog!=null&&file_ly!=null){
                     listView_catalog.setVisibility(View.VISIBLE);
                     file_ly.setVisibility(View.GONE);
                 }

             }else {
                 if (listView_catalog!=null&&file_ly!=null){
                     listView_catalog.setVisibility(View.GONE);
                     file_ly.setVisibility(View.VISIBLE);
                 }

             }
               if (tittle1!=null){
                   tittle1.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopics());
                   if (Hawk.contains("company_name")){
                       String str= Hawk.get("company_name");
                       tittle1.setText(str);
                   }
               }
             if (tittle2!=null){

                 tittle2.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
                 if (Hawk.contains("tittle2")){
                     String str= Hawk.get("tittle2");
                     tittle2.setText(str);
                 }
             }
             Log.d("fdafafaff=fragment",textNub+"editListBeanList=   "+editListBeanList.get(Integer.valueOf(textNub)).getTopics()+"   "+editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
             if(topic!=null){
                 topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
             }

             if(attend!=null){
                 attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getAttendeBean());
             }
     *//*    if (fileListAdapter!=null){
             fileListAdapter.notifyDataSetChanged();
         }*/
            }
            if (files == null) {
                return;
            }
            // getShareFile(files);
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG + toString(), "onCreate: " + textNub);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        IntentFilter iFilter = new IntentFilter();
        mBroadcastReceiver = new MediaReceiver();
        mBroadcastReceiver.setSendfilePthInterface(this);
        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addDataScheme("file");
        getActivity().registerReceiver(mBroadcastReceiver, iFilter);
        socketShareFileManager = new SocketShareFileManager(mHandler);
        if (!mReceiverTag) {     //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
            myBroadcastReceiver = new MyBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(constant.SHARE_FILE_BROADCAST);
            getActivity().registerReceiver(myBroadcastReceiver, filter);
        }


        mySaveBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(constant.SAVE_SEPARATELY_BROADCAST);
        getActivity().registerReceiver(mySaveBroadcastReceiver, filter3);

        myCatalogBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter4 = new IntentFilter();
        filter4.addAction(constant.FRESH_CATalog_BROADCAST);
        getActivity().registerReceiver(myCatalogBroadcastReceiver, filter4);

        selfIp = getNetworkType();
        if (!UserUtil.ISCHAIRMAN){
            imageAdd.setVisibility(View.GONE);

        }
        return view;
    }

    /*
     * 临时会议时获取设备是有线网还是无线网
     * */
    private String getNetworkType() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return getIpAddressString();
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return getIpAddress();
            }

        }
        return "0.0.0.0";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                boolean repetition = false;//文件是否有重复
                try {
                    File file = new File(getFilePath(getActivity(), uri));
                    Log.d("requestCodeUr00000", Environment.getExternalStorageDirectory().getAbsolutePath() + "===" + Environment.getExternalStorageDirectory().toString() + "====" + Environment.getStorageState(file));
                    Log.d("requestCodeUrl111", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                    String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    Log.d("requestCodeUr7788", " 文件类型=" + endStr + "    文件名字=" + file.getName() + "     " + getFilePath(getActivity(), uri));
                    if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr) || endStr.equals("m4a") || endStr.equals("mp3") || endStr.equals("mid") ||
                            endStr.equals("xmf") || endStr.equals("ogg") || endStr.equals("wav") || endStr.equals("3gp") || endStr.equals("mp4") || endStr.equals("ppt") || endStr.equals("pptx") ||
                            endStr.equals("xls") || endStr.equals("xlsx") || endStr.equals("doc") || endStr.equals("docx") || endStr.equals("pdf") || endStr.equals("txt")) {
                        fileBean = new WuHuEditBean.EditListBean.FileListBean(file.getName(), file.getPath(), "", "");
                        fileBean.setResImage(getIamge(endStr));
                        fileBean.setFile_type(getType(endStr));
                        fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                        fileBean.setType(getFileType(endStr));
                        fileBean.setNet(false);
                        if (textNub != null) {
                            fileBean.setPos(textNub);
                        }
                        if (copyFileBeans.size() == 0) {
                            copyFileBeans.add(fileBean);
                        } else {
                            for (int p = 0; p < copyFileBeans.size(); p++) {
                                if (copyFileBeans.get(p).getName().equals(fileBean.getName()) && copyFileBeans.get(p).getPath().equals(fileBean.getPath())) {
                                    repetition = true;
                                }
                            }
                            if (!repetition) {
                                repetition = false;
                                copyFileBeans.add(fileBean);
                            } else {
                                Toast.makeText(getActivity(), "文件" + fileBean.getName() + "重复添加", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }



             /*           if (Hawk.contains("wuhulocal")){
                            copyFileBeans=Hawk.get("wuhulocal");
                            if (copyFileBeans.size() == 0) {
                                copyFileBeans.add(fileBean);
                            }else {
                            for (int k=0;k<copyFileBeans.size();k++){
                                if (file.getPath().equals(copyFileBeans.get(k).getPath())){
                                    continue;

                                }else {
                                    copyFileBeans.add(fileBean);
                                }

                            }
                            }
                        }*/
                        //非主席时保存自己导入的本地文件
                 /*       if (!UserUtil.ISCHAIRMAN){
                        if (Hawk.contains("wuhulocal")) {
                            List<WuHuEditBean.EditListBean.FileListBean> fileListBeans = Hawk.get("wuhulocal");
                            fileListBeans.add(fileBean);
                            Hawk.put("wuhulocal", fileListBeans);
                        } else {
                            Hawk.put("wuhulocal", copyFileBeans);

                        }
                        }*/

                        //本地文件
                        fileBeans.clear();
                        fileBeans.addAll(copyFileBeans);
                        fileBeans.addAll(netFileBeans);
                        fileBeans.addAll(shareFileBeans);

                        if (Hawk.contains("WuHuFragmentData")) {
                            //临时存放的议题集合
                            List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                            copyEdList = wuHuEditBean.getEditListBeanList();
                            //遍历更新当前议题下的文件
                            List<WuHuEditBean.EditListBean.FileListBean> locService=new ArrayList<>();
                            locService.clear();
                            if (copyEdList != null && copyEdList.size() > 0) {

                                for (int i = 0; i < copyEdList.size(); i++) {
                                    //每一个议题
                                    WuHuEditBean.EditListBean editListBean = copyEdList.get(i);

                                    if (editListBean.getPos().equals(textNub)) {
                                  if (editListBean.getLocalFiles()!=null){
                                      locService.addAll(editListBean.getLocalFiles());
                                  }

                                        //把当前本地议题文件集合放入对应的议题
                                        locService.add(fileBean);

                                        editListBean.setLocalFiles(locService);
                                        copyEdList.set(Integer.valueOf(textNub), editListBean);

                                    }
                                }
                                wuHuEditBean.setEditListBeanList(copyEdList);
                            }
                            fileListAdapter.setGridViewBeanList(locService);
                            fileListAdapter.notifyDataSetChanged();
                            Hawk.put("WuHuFragmentData", wuHuEditBean);

                            if (UserUtil.ISCHAIRMAN) {
                                wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                            }
                                try {
                                    Thread.sleep(200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                    //更新
                                    wsUpdata(wuHuEditBean, constant.REFRASHWuHUALL);
                        }
                        //通知所有fragment 都更新对应的议题文件列表
                        //   sendFragmenFlag();
                        //  fileBeans.add(fileBean);
                        Message msg = new Message();
                        //  msg.obj = fileListBean;//可以是基本类型，可以是对象，可以是List、map等；
                        mHandler.sendEmptyMessage(88);
                    } else {
                        Toast.makeText(getActivity(), "请选择正确的文件格式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // fileBean = new FileListBean(file.getName(), "/storage/84BE-981E/声学在图书馆智慧空间建设中的应用.docx", "", "");

                    //复制文件到指定目录
                 /*   new Thread() {
                        @Override
                        public void run() {
                            copyFile(file.getPath(), fileStrPath + file.getName());
                        }
                    }.start();*/

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            } else {
                //长按使用多选的情况
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    List<String> pathList = new ArrayList<>();
                    List<WuHuEditBean.EditListBean.FileListBean> locService=new ArrayList<>();
                    locService.clear();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        boolean repetition2 = false;
                        try {
                            File file = new File(getFilePath(getActivity(), uri));
                            //ToastUtil.makeText(getActivity(), "uri.getPath()=====" + uri.getPath());
                            Log.d("requestCodeUr2222", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                            String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);

                            if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp ".equals(endStr) || endStr.equals("m4a") || endStr.equals("mp3") || endStr.equals("mid") ||
                                    endStr.equals("xmf") || endStr.equals("ogg") || endStr.equals("wav") || endStr.equals("3gp") || endStr.equals("mp4") || endStr.equals("ppt") || endStr.equals("pptx") ||
                                    endStr.equals("xls") || endStr.equals("xlsx") || endStr.equals("doc") || endStr.equals("docx") || endStr.equals("pdf") || endStr.equals("txt")) {
                                Log.d(TAG, "文件类型=" + endStr + "文件名字" + file.getName());
                                fileBean = new WuHuEditBean.EditListBean.FileListBean(file.getName(), file.getPath(), "", "");
                                //复制文件到指定目录
                             /*   new Thread() {
                                    @Override
                                    public void run() {
                                        copyFile(file.getPath(), fileStrPath + file.getName());
                                    }
                                }.start();*/
                                fileBean.setResImage(getIamge(endStr));
                                fileBean.setFile_type(getType(endStr));
                                fileBean.setNet(false);
                                if (textNub != null) {
                                    fileBean.setPos(textNub);
                                }
                                if (copyFileBeans.size() == 0) {
                                    copyFileBeans.add(fileBean);
                                } else {
                                    for (int p = 0; p < copyFileBeans.size(); p++) {
                                        if (copyFileBeans.get(p).getName().equals(fileBean.getName()) && copyFileBeans.get(p).getPath().equals(fileBean.getPath())) {
                                            repetition2 = true;
                                        }
                                    }
                                    if (!repetition2) {
                                        repetition2 = false;
                                        copyFileBeans.add(fileBean);
                                    } else {
                                        Toast.makeText(getActivity(), "文件" + fileBean.getName() + "重复添加", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                        /*        if (Hawk.contains("wuhulocal")){
                                    copyFileBeans=Hawk.get("wuhulocal");
                                    if (copyFileBeans.size() == 0) {
                                        copyFileBeans.add(fileBean);
                                    }else {

                                    for (int k=0;k<copyFileBeans.size();k++){
                                        if (file.getPath().equals(copyFileBeans.get(k).getPath())){
                                            continue;

                                        }else {

                                        }


                                    }}
                                }*/
                                if (Hawk.contains("wuhulocal")) {
                                    List<WuHuEditBean.EditListBean.FileListBean> fileListBeans = Hawk.get("wuhulocal");
                                    fileListBeans.add(fileBean);
                                    Hawk.put("wuhulocal", fileListBeans);

                                } else {
                                    Hawk.put("wuhulocal", copyFileBeans);

                                }
                                //本地文件
                                fileBeans.clear();
                                fileBeans.addAll(copyFileBeans);
                                fileBeans.addAll(netFileBeans);
                                fileBeans.addAll(shareFileBeans);


                                if (Hawk.contains("WuHuFragmentData")) {
                                    //临时存放的议题集合
                                    List<WuHuEditBean.EditListBean> copyEdList = new ArrayList<>();
                                    WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                                    copyEdList = wuHuEditBean.getEditListBeanList();
                                    //遍历更新当前议题下的文件
                                    if (copyEdList != null && copyEdList.size() > 0) {
                                        for (int k = 0; k < copyEdList.size(); k++) {
                                            if (copyEdList.get(k).getPos().equals(textNub)) {
                                                WuHuEditBean.EditListBean editListBean = copyEdList.get(k);

                                                if (editListBean.getLocalFiles()!=null){
                                                    locService.addAll(editListBean.getLocalFiles());
                                                }
                                                //把当前本地议题文件集合放入对应的议题
                                                locService.add(fileBean);
                                                editListBean.setLocalFiles(locService);
                                                copyEdList.set(Integer.valueOf(textNub), editListBean);

                                            }
                                        }
                                        wuHuEditBean.setEditListBeanList(copyEdList);
                                    }
                                    Hawk.put("WuHuFragmentData", wuHuEditBean);

                                    if (UserUtil.ISCHAIRMAN) {
                                        wsUpdata(wuHuEditBean, constant.SUBMITANISSUE);
                                    }
                                }
                                // fileBeans.add(fileBean);
                                Message msg = new Message();
                                //  msg.obj = fileListBean;//可以是基本类型，可以是对象，可以是List、map等；
                                mHandler.sendEmptyMessage(88);
                            } else {
                                Toast.makeText(getActivity(), "请选择正确的文件格式", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }


                    fileListAdapter.setGridViewBeanList(locService);
                    fileListAdapter.notifyDataSetChanged();
                    //通知所有fragment 都更新对应的议题文件列表
                    //  sendFragmenFlag();
                }
            }

         /*   fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();*/
        }

    }

    @SuppressLint("NewApi")
    public String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                for (int i = 0; i < split.length; i++) {
                    Log.d("requestCodeUr4444", split[i] + "i==" + i);
                }
                Log.d("requestCodeUr55555", Environment.getExternalStorageDirectory() + "/" + split[1]);
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                } else {
                    return "/storage/" + split[0] + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);


                //=====================================================================================

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, uri, null, null);


/*
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",.
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    String path=contentUri.getPath();
                    if (path!=null){
                        return  path;
                    }
                }*/

          /*      // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;*/


            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                for (int i = 0; i < split.length; i++) {
                    Log.d("requestCodeUr6666", split[i]);
                }
                Log.d("requestCodeUr7777", split[0]);
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            String[] projection = {
                    MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            //cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (column_index < 0 || column_index > cursor.getColumnCount()) {


                    return null;
                }
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        Log.d("isDownloadsDocument", uri.getAuthority());
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;

    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);    //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            Message msg = new Message();
            //  msg.obj = fileListBean;//可以是基本类型，可以是对象，可以是List、map等；
            msg.what = 150;
            mHandler.sendMessage(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        if (mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
        if (myBroadcastReceiver != null && myBroadcastReceiver.isOrderedBroadcast()) {
            getActivity().unregisterReceiver(myBroadcastReceiver);
        }
        if (mediaPlayer != null) {
            //mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /*
     * 遍历得到分享的文件
     * */
    private void getShareFile(File[] files) {
        shareFileBeans.clear();
        fileBeans.clear();
        Log.d(TAG, "路过~~~~~11");
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
                                String pos=null;
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

                                if (textNub != null && pos != null) {
                                    if (textNub.equals(pos)) {
                                        String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                                        fileBean = new WuHuEditBean.EditListBean.FileListBean(fileName, file.getPath(), "", "");
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
                        shareMsg.what = 100;
                        shareMsg.obj = shareFileBeans;
                        mHandler.sendMessage(shareMsg);

                    }


                }

                //子线程执行结束
            }
        }).start();
    }
    /*
     * 列表点击打开文件
     * */
    private void getFile(File[] files,WuHuEditBean.EditListBean.FileListBean fileBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (UserUtil.object) {

                    //子线程开始
                    String content = "";
                    if (files != null) {
                        // 先判断目录是否为空，否则会报空指针
                        boolean isHaveFile = false;
                        for (File file : files) {
                            if (file.isDirectory()) {
                                getFileName(file.listFiles());
                            } else {
                                String fileName = file.getName();

                                if (fileBean.getName().equals(fileName)){
                                    isHaveFile = true;
                                    Message shareMsg = new Message();
                                    shareMsg.what = 210;
                                    shareMsg.obj = file;
                                    mHandler.sendMessage(shareMsg);
                                }
                                //发送消息跟新文件列表

                                }

                            }
                         if (!isHaveFile){
                             Message sm = new Message();
                             mHandler.sendEmptyMessage(211);
                             isHaveFile=false;

                         }

                    }


                }

                //子线程执行结束
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG + toString(), "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("onResume的的textNub=", textNub + "  isDeletOption=" + isDeletOption);
        if (textNub == null) {
            return;
        }
        if (Integer.valueOf(textNub) == 0) {
            listView_catalog.setVisibility(View.VISIBLE);
            file_ly.setVisibility(View.GONE);

        } else {
            listView_catalog.setVisibility(View.GONE);
            file_ly.setVisibility(View.VISIBLE);

        }
        int s = Integer.valueOf(textNub);
        // s= s+1;
        // tittle_num.setText("议题"+s);
        setTopicText(s + "");
        String strFilePath = fileShare;
        File path = new File(strFilePath);
        File[] files = path.listFiles();// 读取
/*
        if (Hawk.contains("WuHuFragmentData")) {
           *//*  int s=Integer.valueOf(textNub)+1;
             tittle_num.setText("议题"+s);*//*
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
            if (wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() == 0) {
                return;
            }
            editListBeanList = wuHuEditBean.getEditListBeanList();


            if (Integer.valueOf(textNub)==0){
                if (listView_catalog!=null&&file_ly!=null&&fragment_ttile_rl!=null&&topic_ll!=null&&fragmentLine!=null&&nn!=null&&listView_catalog_ll!=null&&scrollView_file!=null&&scrollView_cata!=null){
                    listView_catalog.setVisibility(View.VISIBLE);
                    listView_catalog_ll.setVisibility(View.VISIBLE);
                    file_ly.setVisibility(View.GONE);
                    fragment_ttile_rl.setVisibility(View.VISIBLE);
                    topic_ll.setVisibility(View.GONE);
                    fragmentLine.setVisibility(View.VISIBLE);
                    nn.setVisibility(View.GONE);
                    scrollView_file.setVisibility(View.GONE);
                    scrollView_cata.setVisibility(View.VISIBLE);
                }

            }else {
                if (listView_catalog!=null&&file_ly!=null&&fragment_ttile_rl!=null&&topic_ll!=null&&fragmentLine!=null&&nn!=null&&listView_catalog_ll!=null&&scrollView_file!=null&&scrollView_cata!=null){
                    listView_catalog.setVisibility(View.GONE);
                    file_ly.setVisibility(View.VISIBLE);
                    listView_catalog_ll.setVisibility(View.GONE);
                    fragment_ttile_rl.setVisibility(View.GONE);
                    topic_ll.setVisibility(View.VISIBLE);
                    fragmentLine.setVisibility(View.GONE);
                    nn.setVisibility(View.VISIBLE);
                    scrollView_file.setVisibility(View.VISIBLE);
                    scrollView_cata.setVisibility(View.GONE);
                }

            }
            if (tittle1!=null){
                tittle1.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopics());
                if (Hawk.contains("company_name")){
                    String str= Hawk.get("company_name");
                    tittle1.setText(str);
                }
            }
            if (tittle2!=null){

                tittle2.setText(editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
                if (Hawk.contains("tittle2")){
                    String str= Hawk.get("tittle2");
                    tittle2.setText(str);
                }
            }
            Log.d("fdafafaff=fragment",textNub+"editListBeanList=   "+editListBeanList.get(Integer.valueOf(textNub)).getTopics()+"   "+editListBeanList.get(Integer.valueOf(textNub)).getTopic_type());
            if(topic!=null){
                topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
            }

            if(attend!=null){
                attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getReportingUnit());
            }

            if(attend2!=null){
                attend2.setText(editListBeanList.get(Integer.valueOf(textNub)).getParticipantUnits());
            }
     *//*    if (fileListAdapter!=null){
             fileListAdapter.notifyDataSetChanged();
         }*//*
        }
        if (files == null) {
            return;
        }
        getShareFile(files);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG + toString(), "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG + toString(), "onStop: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG + toString(), "onDestroy: ");
        if (mySaveBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mySaveBroadcastReceiver);
        }
        if (myCatalogBroadcastReceiver != null) {
            getActivity().unregisterReceiver(myCatalogBroadcastReceiver);
        }
        if (mReceiverTag) {   //判断广播是否注册
            mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
            getActivity().unregisterReceiver(myBroadcastReceiver);  //注销广播
        }

    }

    @Override
    public String toString() {
        return textNub;
    }

    @Override
    public void onClick(View v) {
/*        Intent intent;
        switch (v.getId()){
            case  R.id.edit_ll:

                break;
            case R.id.edit_rl:
                edit_name_rl.setVisibility(View.VISIBLE);
                break;
            case R.id.comfirm:
                edit_name_rl.setVisibility(View.GONE);
                break;
            case R.id.vote_ll:
              //  showRightDialog();
                break;

        }*/

    }

    public void showFileTransferDialog() {
        //自定义dialog显示布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.wuhu_file_progress_dialog, null);
        //自定义dialog显示风格
        dialog = new MyDialog(getActivity(), R.style.dialogTransparent);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(false);
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
        progressBar = inflate.findViewById(R.id.progressBar);
        completedView = inflate.findViewById(R.id.tasks_view);
        result_ima = inflate.findViewById(R.id.result_ima);
        completedView.setVisibility(View.VISIBLE);
        tips = inflate.findViewById(R.id.tips);
        result_ima.setVisibility(View.GONE);
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
        wlp.height = (int) (width * 0.3);//设置宽
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

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            if (in.getAction().equals(constant.SHARE_FILE_BROADCAST)) {

                String topicPos = in.getStringExtra("topicPos");
                String filePath = in.getStringExtra("filePath");
                File path = new File(filePath);

                //只更新和当前分享的议题号相同的fragment的分享文件列表
                if (!StringUtils.isEmpty(textNub) && !StringUtils.isEmpty(topicPos)) {
                    if (textNub.equals(topicPos)) {
                        if (path != null) {
                            // 先判断目录是否为空，否则会报空指针
                            String fileName = path.getName();
                            String[] fileNameAll = null;
                            String pos=null;
                            if (fileName.contains(constant.WUHUPUSH)) {
                                fileNameAll = fileName.split(constant.WUHUPUSH);
                                fileName = fileNameAll[1];
                                pos = fileNameAll[0];
                            } else if (fileName.contains(constant.WUHUSHARE)) {
                                fileNameAll = fileName.split(constant.WUHUSHARE);
                                fileName = fileNameAll[1];
                                pos = fileNameAll[0];
                            }

                            if (textNub != null && pos != null) {
                                if (textNub.equals(pos)) {
                                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    fileBean = new WuHuEditBean.EditListBean.FileListBean(fileName, path.getPath(), "", "");
                                    Uri uri = Uri.fromFile(path);
                                    fileBean.setResImage(getIamge(endStr));
                                    fileBean.setFile_type(getType(endStr));
                                    fileBean.setFileMd5(Md5Util.getFileMD5(path));//MD5文件校验
                                    fileBean.setPos(pos);
                                    fileBean.setNet(false);
                                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                                    //  fileBean.setType(endStr);
                                    fileBean.setType(getFileType(endStr));
                                    shareFileBeans.add(fileBean);
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
                            shareMsg.what = 100;
                            shareMsg.obj = shareFileBeans;
                            mHandler.sendMessage(shareMsg);

                        }
                    }
                }

            } else if (in.getAction().equals(constant.SAVE_SEPARATELY_BROADCAST)) {
                String type = in.getStringExtra("refreshType");

                if (type.equals("1")) {
                    fragmentLine.setBackgroundColor(Color.parseColor("#EA4318"));
                    nn.setBackgroundColor(Color.parseColor("#EA4318"));
                } else if (type.equals("2")) {
                    fragmentLine.setBackgroundColor(Color.parseColor("#1D1D1D"));
                    nn.setBackgroundColor(Color.parseColor("#1D1D1D"));
                } else if (type.equals("3")) {
                    root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home);
                } else if (type.equals("4")) {
                    root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home2);
                } else if (type.equals("5")) {
                    root_lin.setBackgroundColor(Color.parseColor("#EBE9EA"));
                } else if (type.equals("6")) {
                    root_lin.setBackgroundColor(Color.parseColor("#EFF4F8"));
                } else if (type.equals("7")) {
                    root_lin.setBackgroundColor(Color.parseColor("#F2F8F5"));

                } else if (type.equals("8")) {

                    if (Hawk.contains("WuHuFragmentData")) {
                        WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                        List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
                        if (wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() == 0) {
                            return;
                        }
                        editListBeanList = wuHuEditBean.getEditListBeanList();
                        topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
                        attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getReportingUnit());
                        attend2.setText(editListBeanList.get(Integer.valueOf(textNub)).getParticipantUnits());
                        tittle1.setText(wuHuEditBean.getTopics());
                        tittle2.setText(wuHuEditBean.getTopic_type());
                    }

                }

            } else if (in.getAction().equals(constant.FRESH_CATalog_BROADCAST)) {
                if (StringUtils.isEmpty(textNub)) {
                    return;
                }
                if (Integer.valueOf(textNub) == 0) {
                    listView_catalog.setVisibility(View.VISIBLE);
                    file_ly.setVisibility(View.GONE);
                } else {
                    listView_catalog.setVisibility(View.GONE);
                    file_ly.setVisibility(View.VISIBLE);
                }
                Log.d("fdgsgsgsgsg2222", Hawk.contains("WuHuFragmentData") + "");
                if (Hawk.contains("WuHuFragmentData")) {
                    wuHuEditBeanList.clear();
                    WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                    wuHuEditBeanList = wuHuEditBean.getEditListBeanList();
                    if (wuHuEditBeanList!=null&&wuHuCalalogListAdapter!=null ) {
                        wuHuCalalogListAdapter.setWuHuEditBeanList(wuHuEditBeanList);
                        wuHuCalalogListAdapter.notifyDataSetChanged();
                    }
                    if (fileListAdapter!=null&&wuHuEditBeanList!=null){

                            for (int i=0;i<wuHuEditBeanList.size();i++){

                                if (wuHuEditBeanList.get(i).getPos()!=null&&textNub!=null){
                                    if (wuHuEditBeanList.get(i).getPos().equals(textNub)){
                                        if (UserUtil.ISCHAIRMAN){
                                            if (wuHuEditBeanList.get(i).getLocalFiles()!=null&&wuHuEditBeanList.get(i).getLocalFiles().size()>0){
                                                fileListAdapter.setGridViewBeanList(wuHuEditBeanList.get(i).getLocalFiles());
                                                fileListAdapter.notifyDataSetChanged();
                                            }

                                    }else {
                                            if (wuHuEditBeanList.get(i).getLocalFiles()!=null&&wuHuEditBeanList.get(i).getLocalFiles().size()>0){
                                                fileListAdapter.setGridViewBeanList(wuHuEditBeanList.get(i).getLocalFiles());
                                                fileListAdapter.notifyDataSetChanged();
                                            }

                                        }

                                }

                            }

                        }


                    }

                }

            }
        }
    }
}
