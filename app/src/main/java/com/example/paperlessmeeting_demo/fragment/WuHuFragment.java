package com.example.paperlessmeeting_demo.fragment;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.provider.FontRequest;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivityImage;
import com.example.paperlessmeeting_demo.activity.ActivityVideoView;
import com.example.paperlessmeeting_demo.activity.EditWuHuActivity;
import com.example.paperlessmeeting_demo.activity.PdfActivity;
import com.example.paperlessmeeting_demo.activity.Sign.SignActivity;
import com.example.paperlessmeeting_demo.adapter.FileListAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuFileListAdapter;
import com.example.paperlessmeeting_demo.adapter.WuHuListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.NewFileBean;
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
import com.example.paperlessmeeting_demo.tool.Define;
import com.example.paperlessmeeting_demo.tool.DownloadUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.MediaReceiver;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.ToastUtil;
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

public class WuHuFragment extends BaseFragment  implements MediaReceiver.sendfilePthInterface, WuHuFileListAdapter.upLoadFileInterface, WuHuFileListAdapter.PlayerClickInterface, WuHuFileListAdapter.ShareFileInterface, WuHuFileListAdapter.PushFileInterface , View.OnClickListener,WuHuListAdapter.saveSeparatelyInterface {
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
    @BindView(R.id.progressBar_ll)
    RelativeLayout progressBarLl;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.edit_ll)
    RelativeLayout edit_ll;
    @BindView(R.id.edit_rl)
    RelativeLayout edit_rl;
    @BindView(R.id.edit_name_rl)
    RelativeLayout edit_name_rl;
    @BindView(R.id.vote_ll)
    RelativeLayout vote_ll;


    @BindView(R.id.comfirm)
    ImageView comfirm;
    @BindView(R.id.aa)
    View fragmentLine;
    @BindView(R.id.topic)
    TextView topic;
    @BindView(R.id.tittle1)
    TextView tittle1;
    @BindView(R.id.tittle2)
    TextView tittle2;

    @BindView(R.id.attend)
    TextView attend;
    @BindView(R.id.tittle_num)
    TextView tittle_num;

    private RelativeLayout  add_topic_rl;
    private RelativeLayout  dialg_rl_root;
    private RelativeLayout sava_all;
    private Dialog dialog;
    private View inflate;
    private String selfIp = "";
    private WuHuFileListAdapter fileListAdapter;
    private List<FileListBean> fileBeans = new ArrayList<>();
    private List<FileListBean> shareFileBeans = new ArrayList<>();//其他设备分享得到的文件集合
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
    private String fileStrPath = Environment.getExternalStorageDirectory() + constant.COPY_PATH;
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//其他设备分享得到的文件夹路径
    private String path = "/storage/emulated/0/";//展厅临时文件路径
    private String videoPath = "";
    private String endStrAll;
    private SocketShareFileManager socketShareFileManager;
    private MyBroadcastReceiver myBroadcastReceiver;//分享文件成功后，刷新文件列表
    private MyBroadcastReceiver mySaveBroadcastReceiver;//更新单个会议议题
    private List<String> stringListIp = new ArrayList<>();
    private MyListView myListView;
    private WuHuListAdapter wuHuListAdapter;
    private  int  k=0;
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList=new ArrayList<>();
    private   List<WuHuNetFileBean.DataBean> fileListBeanList = new ArrayList<>();
    private List<FileListBean> fileOtherBeans = new ArrayList<>();//音视频文件
    private FileListBean fileBean;
    private boolean mReceiverTag = false;   //广播接受者标识
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 99:
                    FileListBean fileBean=(FileListBean)msg.obj;
                    fileBean.setNet(true);
                    fileBeans.add(fileBean);
                    fileListAdapter.setGridViewBeanList(fileBeans);
                    fileListAdapter.notifyDataSetChanged();
                    //完成主界面更新,拿到数据
                    //   getDownload(fileListBean.get_id(), fileListBean.getUser_id(), fileListBean.getUnclassified());
                    //    getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                    break;
                case 1:
                    //    fileBeans.clear();
                    getCopyFile();
                    break;
                /*
                 * 分享文件发送中，显示加载动画
                 * */
                case 4:
                    progressBarLl.setVisibility(View.VISIBLE);
                    String fileName = msg.obj.toString();
                    tips.setText(fileName);
                    break;
                /*
                 * 分享文件发送完毕，隐藏加载动画
                 * */
                case 5:
                    progressBarLl.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "文件分享成功", Toast.LENGTH_SHORT).show();
                    break;
                /*
                 * 分享文件发送失败，隐藏加载动画
                 * */
                case 6:
                    progressBarLl.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "文件分享失败", Toast.LENGTH_SHORT).show();
                    String df = msg.obj.toString();
                    Log.d("dfsfsdf", df);
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
        getNetFile();
        for (int i = 0; i < fileBeans.size(); i++) {
            String endStr = fileBeans.get(i).getName().substring(fileBeans.get(i).getName().lastIndexOf(".") + 1);
            fileBeans.get(i).setResImage(getIamge(endStr));
            fileBeans.get(i).setFile_type(getType(endStr));
        }
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

        fileListAdapter = new WuHuFileListAdapter(getActivity(), fileBeans);
        listView.setAdapter(fileListAdapter);
        fileListAdapter.setUpLoadFileInterface(this);
        fileListAdapter.setShareFileInterface(this);
        fileListAdapter.setPushFileInterface(this);
        fileListAdapter.notifyDataSetChanged();
        Log.d("fwerwr", "页面初始化1111");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FileListBean fileBean = (FileListBean) adapterView.getAdapter().getItem(i);

                Log.d("requestCodeUr333", fileBean.getPath());
                Intent intent;
                if (fileBean.getFile_type().equals("3")){
                    intent = new Intent();
                    intent.setClass(getActivity(), ActivityImage.class);
                    intent.putExtra("url", fileBean.getPath());
                    intent.putExtra("isOpenFile", true);
                    intent.putExtra("isNetFile", false);
                    startActivity(intent);
                }else if (fileBean.getFile_type().equals("4")){

                    if(UserUtil.isNetworkOnline){
                        intent = new Intent();
                        intent.setClass(getActivity(), SignActivity.class);
                        intent.putExtra("url", fileBean.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        intent.putExtra("tempPath", false);
                        intent.putExtra("fileName",fileBean.getName());
                        startActivity(intent);
                    }else {
                        CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                            @Override
                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                            }
                        });
                    }

                 /*   intent=new Intent(getActivity(), PdfActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("pdfPath", fileBean.getPath());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);*/
                }

              /*
                switch (fileBean.getFile_type()) {
                    case "1"://音乐
                        startPlayer("file://" + fileBean.getPath());
                        //  getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                        // FileUtils.openFile("file://"+fileBean.getPath(), getActivity());
                        break;
                    case "2"://视频
                        intent = new Intent();
                        intent.setClass(getActivity(), ActivityVideoView.class);
                        intent.putExtra("url", fileBean.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        startActivity(intent);
                        //  FileUtils.openFile(fileBean.getPath(), getActivity());
                        break;
                    case "3"://图片
                        intent = new Intent();
                        intent.setClass(getActivity(), ActivityImage.class);
                        intent.putExtra("url", fileBean.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        startActivity(intent);
                        break;
                    case "4":
                        String wpsPackageName;
                        if (FLUtil.checkPackage(context, Define.PACKAGENAME_KING_PRO)) {
                            wpsPackageName = Define.PACKAGENAME_KING_PRO;
                        } else if (FLUtil.checkPackage(context, Define.PACKAGENAME_PRO_DEBUG)) {
                            wpsPackageName = Define.PACKAGENAME_PRO_DEBUG;
                        } else if (FLUtil.checkPackage(context, Define.PACKAGENAME_ENG)) {
                            wpsPackageName = Define.PACKAGENAME_ENG;
                        } else if (FLUtil.checkPackage(context, Define.PACKAGENAME_KING_PRO_HW)) {
                            wpsPackageName = Define.PACKAGENAME_KING_PRO_HW;
                        } else if (FLUtil.checkPackage(context, Define.PACKAGENAME_K_ENG)) {
                            wpsPackageName = Define.PACKAGENAME_K_ENG;
                        } else {
                            ToastUtils.showToast(context, "文件打开失败，请安装WPS Office专业版");
                            return;
                        }
                        startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
//                       intent = new Intent();
//                        intent.setClass(getActivity(), CommonWebViewActivity.class);
//                        intent.putExtra("url", fileBean.getPath());
//                        intent.putExtra("isOpenFile", true);
//                        intent.putExtra("isNetFile", false);
//                        intent.putExtra("tempPath", false);
//                        startActivity(intent);
                        break;
                }*/

            }
        });


    }
    private  void getNetFile(){


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
                        if(response.getData()==null){
                            return;
                        }
                        fileListBeanList = (List<WuHuNetFileBean.DataBean>) response.getData();

                        if (fileListBeanList==null|fileListBeanList.size()<1){

                            return;
                        }

                        netFileName(fileListBeanList);

                    }
                }
            });

}
    }


    private void netFileName(List<WuHuNetFileBean.DataBean> fileListBeanList) {
        name.clear();
        fileOtherBeans.clear();
        fileBeans.clear();
        for (int i = 0; i < fileListBeanList.size(); i++) {
            WuHuNetFileBean.DataBean fileListBean = fileListBeanList.get(i);

            String c_id = fileListBeanList.get(i).get_id();
            String endStr = fileListBeanList.get(i).getFile_path().substring(fileListBeanList.get(i).getFile_path().lastIndexOf(".") + 1);
            Log.d("vrdvafd", "文件类型=" + fileListBeanList.get(i).getName() + "文件名字" + fileListBeanList.get(i).getFile_path() + "p====" + fileListBeanList.get(i).getName());
            //下载文件--子线程
                if ("4".equals(getType(endStr))) {

                    String path = UrlConstant.baseUrl+"/" + fileListBeanList.get(i).getFile_path();
                    String name = fileListBeanList.get(i).getName();
                    String fileId = fileListBeanList.get(i).get_id();
                    Log.d("网络文件地址","path====="+path);


                    //   String fileData = httpDownloader.downloadFiles(fileListBeanList.get(i).getFile_path());
                    DownloadUtil.get().download(path, fileStrPath, fileListBeanList.get(i).getName(), new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            Log.v("dfsfff111", "下載成功,文件已存入手机内部存储设备根目录下Download文件夾中");
                      /*  Looper.prepare();//增加部分
                        Looper.loop();//增加部分*/
                            Message msg = new Message();
                            fileListBean.setName(name);
                            fileListBean.setPath(fileStrPath + name);
                            fileListBean.set_id(fileId);
                            fileListBean.set_id(fileId);

                            fileListBean.setNet(true);
                            fileBean =new FileListBean(name,fileStrPath + name,"","");
                            fileBean.setResImage(getIamge(endStr));
                            fileBean.setFile_type(getType(endStr));

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
                    fileBean =new FileListBean(fileListBeanList.get(i).getName(),fileListBeanList.get(i).getFile_path() + fileListBeanList.get(i).getName(),"","");
                   // fileBean = new NewFileBean.MeetingFileListBean(fileListBeanList.get(i).getFile_name(), fileListBeanList.get(i).getFile_path(), fileListBeanList.get(i).getUser_id().getName(), fileListBeanList.get(i).getUpload_time());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileListBean.setNet(true);
                  /*  fileListBean.setC_id(c_id);
                    fileListBean.setC_id(c_id);*/
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
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
//
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    NewFileBean.MeetingFileListBean fileBean = (NewFileBean.MeetingFileListBean) adapterView.getAdapter().getItem(i);
//                    Log.d("requestCodeUr333", fileBean.getPath());
//                    Intent intent;
//                    switch (fileBean.getFile_type()) {
//                        case "1"://音乐
//                            startPlayer(fileBean.getPath());
//                            //getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
//                            // FileUtils.openFile("file://"+fileBean.getPath(), getActivity());
//                            break;
//                        case "2"://视频
//                            intent = new Intent();
//                            intent.setClass(getActivity(), ActivityVideoView.class);
//                            intent.putExtra("url", fileBean.getPath());
//                            intent.putExtra("isOpenFile", true);
//                            intent.putExtra("isNetFile", true);
//                            startActivity(intent);
//                            //  FileUtils.openFile(fileBean.getPath(), getActivity());
//                            break;
//                        case "3"://图片
//                            intent = new Intent();
//                            intent.setClass(getActivity(), ActivityImage.class);
//                            intent.putExtra("url", fileBean.getPath());
//                            intent.putExtra("isOpenFile", true);
//                            intent.putExtra("isNetFile", true);
//                            startActivity(intent);
//                            break;
//                        case "4":
//                            String wpsPackageName;
//                            if (FLUtil.checkPackage(context, Define.PACKAGENAME_KING_PRO))
//                            {
//                                wpsPackageName = Define.PACKAGENAME_KING_PRO;
//                            }
//                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_PRO_DEBUG))
//                            {
//                                wpsPackageName = Define.PACKAGENAME_PRO_DEBUG;
//                            }
//                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_ENG))
//                            {
//                                wpsPackageName = Define.PACKAGENAME_ENG;
//                            }
//                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_KING_PRO_HW))
//                            {
//                                wpsPackageName = Define.PACKAGENAME_KING_PRO_HW;
//                            }
//                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_K_ENG))
//                            {
//                                wpsPackageName = Define.PACKAGENAME_K_ENG;
//                            }
//                            else {
//                                ToastUtils.showToast(context,"文件打开失败，请安装WPS Office专业版");
//                                return;
//                            }
//
//
//                            getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
//                            // FileUtils.openFile(fileBean.getPath(), getActivity());
//                          /*  intent = new Intent();
//                            intent.setClass(getActivity(), CommonWebViewActivity.class);
//                            intent.putExtra("url", fileBean.getPath());
//                            intent.putExtra("isOpenFile", true);
//                            startActivity(intent);*/
//                            break;
//                    }
//
//                }
//            });
            fileListAdapter.notifyDataSetChanged();
        }

    }



    private void getCopyFile() {
        if (fileStrPath.isEmpty()) {
            return;
        }
        File path = new File(fileStrPath);
        Log.d("sjshgha", "路过~~~~~" + fileStrPath);
        File[] files = path.listFiles();// 读取
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            Log.d("sjshgha", files[i].toString() + "78687");
        }
        getFileName(files);
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
    //保存单个数据
    @Override
    public void saveData(int position) {
      if (textNub.equals(position+"")){
          topic.setText("会议议题："+wuHuEditBeanList.get(Integer.valueOf(textNub)).getSubTopics());
          attend.setText("参会人员："+wuHuEditBeanList.get(Integer.valueOf(textNub)).getAttendeBean());

      }

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

        final ArrayList<String> fileNames = new ArrayList<>();
        final ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        fileNames.add(name);
        Message.obtain(mHandler, 4, name).sendToTarget();
        /*
         * 通知其他设备Service，这是分享文件
         * */
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (Hawk.contains("stringsIp")) {
                    stringListIp = Hawk.get("stringsIp");
                }
                //根据Ip将文件发送到不同的设备
                for (int i = 0; i < stringListIp.size(); i++) {
                    socketShareFileManager.SendFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT,"1");
                }
            }

        });
        sendThread.start();
        // Message.obtain(handler, 0, response).sendToTarget();
    }
    @Override
    public void pushFileInfo(String path, String type, String flag, String name, String author, String time) {
        Log.d("FileFragment--推送", path + "===name==" + name);

        final ArrayList<String> fileNames = new ArrayList<>();
        final ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        fileNames.add(name);
        Message.obtain(mHandler, 4, name).sendToTarget();
        /*
         * 通知其他设备Service，这是分享文件
         * */
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (Hawk.contains("stringsIp")) {
                    stringListIp = Hawk.get("stringsIp");
                }
                //根据Ip将文件发送到不同的设备
                for (int i = 0; i < stringListIp.size(); i++) {
                    socketShareFileManager.SendFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT,"2");
                }


            }
        });
        sendThread.start();
        Intent intent;
        if (fileBean.getFile_type().equals("3")){
            intent = new Intent();
            intent.setClass(getActivity(), ActivityImage.class);
            intent.putExtra("url", path);
            intent.putExtra("isOpenFile", true);
            intent.putExtra("isNetFile", false);
            startActivity(intent);
        }else if (fileBean.getFile_type().equals("4")){
            if(UserUtil.isNetworkOnline){
                intent = new Intent();
                intent.setClass(getActivity(), SignActivity.class);
                intent.putExtra("url", path);
                intent.putExtra("isOpenFile", true);
                intent.putExtra("isNetFile", false);
                intent.putExtra("tempPath", false);
                intent.putExtra("fileName",name);
                startActivity(intent);
            }else {
                CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                    @Override
                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                        startActivity(FileUtils.openFile(path ,getActivity()));
                    }
                });
            }
        }
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

    private void getFileName(File[] files) {
        Log.d(TAG, "路过~~~~~11");
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
                    fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    //  fileBean.setType(endStr);
                    fileBean.setType(getFileType(endStr));
                    fileBean.setNet(false);
                    fileBeans.add(fileBean);

                }
            }
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void initView() {
        edit_ll.setOnClickListener(this);
        edit_rl.setOnClickListener(this);
        comfirm.setOnClickListener(this);
        vote_ll.setOnClickListener(this);
       edit_name_rl.setVisibility(View.GONE);
       // mTextView.setText(text);
        Log.d(TAG+toString(), "onCreateView: ");

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

        if (!UserUtil.ISCHAIRMAN) {
            edit_ll.setVisibility(View.GONE);
        }

    }
    /**
     * 收到websocket 信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg000011: " , message.toString());
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
                        Log.d("gdgsdgetEditListB大小=", wuHuEditBean.getEditListBeanList().size()+ "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(message.getMessage().contains(constant.REFRASHWuHUSIGLEDATA)){
                Log.d("onReceiveMsg",  "REFRASHWuHUSIGLEDATA");
                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        WuHuEditBean     wuHuEditBean = wsebean.getBody();
                        refreshUi(wuHuEditBean);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if(message.getMessage().contains(constant.REFRASHWuHUALL)){

                try {
                    TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuEditBean>>() {
                    }.getType());
                    //  收到vote的websocket的信息
                    if (wsebean != null) {
                        WuHuEditBean     wuHuEditBean = wsebean.getBody();
                        refreshUi(wuHuEditBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (message.getMessage().contains(constant.DELETE_WUHU_FRAGMENT)){
                try {
                    TempWSBean<WuHuDeleteFragmentBean> wsebean = new Gson().fromJson(message.getMessage(), new TypeToken<TempWSBean<WuHuDeleteFragmentBean>>() {
                    }.getType());
                    if (wsebean != null){
                        WuHuDeleteFragmentBean wuHuDeleteFragmentBean=wsebean.getBody();
                        int size=Integer.valueOf(wuHuDeleteFragmentBean.getListSize());
                        size=size+1;
                        if (Integer.valueOf(textNub)>size){
                            textNub=String.valueOf(Integer.valueOf(textNub)-1);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


            }

        }
    }
    @Override
    public void sendfilePth(String filePth) {

    }
    private void  refreshUi( WuHuEditBean wuHuEditBean){

        wuHuEditBeanList=wuHuEditBean.getEditListBeanList();
        if (wuHuEditBeanList==null||wuHuEditBeanList.size()==0){
            return;
        }
        tittle1.setText(wuHuEditBean.getTopics());
        tittle2.setText(wuHuEditBean.getTopic_type());
        int s=Integer.valueOf(textNub)+1;
        tittle_num.setText("议题"+s);
        topic.setText(wuHuEditBeanList.get(Integer.valueOf(textNub)).getSubTopics());
        attend.setText(wuHuEditBeanList.get(Integer.valueOf(textNub)).getAttendeBean());
        switch (wuHuEditBean.getLine_color()){

            case "1":
                fragmentLine.setBackgroundColor(Color.parseColor("#EA4318"));
                break;
            case "2":
                fragmentLine.setBackgroundColor(Color.parseColor("#1D1D1D"));
                break;
        }
        switch (wuHuEditBean.getThem_color()){
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

    public static WuHuFragment newInstance(String text){
        WuHuFragment wuHuFragment = new WuHuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        wuHuFragment.setArguments(bundle);
        return wuHuFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        textNub=getArguments().getString("text");

     if (isVisibleToUser) {
         if (Hawk.contains("WuHuFragmentData")) {
           /*  int s=Integer.valueOf(textNub)+1;
             tittle_num.setText("议题"+s);*/
             WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
             List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
             if (wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() == 0) {
                 return;
             }
             editListBeanList = wuHuEditBean.getEditListBeanList();

             Log.d("fdafafaff",textNub+"editListBeanList=   "+editListBeanList.size());
             topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
             if(attend!=null){
                 attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getAttendeBean());
             }

         }
     }


      /*      for (int i=0;i<editListBeanList.size();i++){
                Log.d("dsafaf",editListBeanList.get(i).getSubTopics()+"    "+editListBeanList.get(i).getAttendeBean()  +"text="+text+"  i"+i);
                if (text.equals(i+"")){
                    topic.setText(editListBeanList.get(i).getSubTopics());
                    attend.setText(editListBeanList.get(i).getAttendeBean());

                }

            }

        }*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG+toString(), "onCreate: "+textNub);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        // 监听socket信息
        EventBus.getDefault().register(this);
        IntentFilter iFilter = new IntentFilter();
        mBroadcastReceiver = new MediaReceiver();
        mBroadcastReceiver.setSendfilePthInterface(this);
        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addDataScheme("file");
        getActivity().registerReceiver(mBroadcastReceiver, iFilter);
        socketShareFileManager = new SocketShareFileManager(mHandler);
        if (!mReceiverTag){     //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
            myBroadcastReceiver = new MyBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(constant.SHARE_FILE_BROADCAST);
            getActivity().registerReceiver(myBroadcastReceiver, filter);
        }



      mySaveBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(constant.SAVE_SEPARATELY_BROADCAST);
        getActivity().registerReceiver(mySaveBroadcastReceiver, filter3);

        selfIp = getNetworkType();
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
                try {
                    File file = new File(getFilePath(getActivity(), uri));
                    Log.d("requestCodeUr00000", Environment.getExternalStorageDirectory().getAbsolutePath() + "===" + Environment.getExternalStorageDirectory().toString() + "====" + Environment.getStorageState(file));
                    Log.d("requestCodeUrl111", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                    String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    Log.d("requestCodeUr7788", "文件类型=" + endStr + "文件名字" + file.getName() + getFilePath(getActivity(), uri));
                    if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr)||endStr.equals("m4a") || endStr.equals("mp3") || endStr.equals("mid") ||
                            endStr.equals("xmf") || endStr.equals("ogg") || endStr.equals("wav")||endStr.equals("3gp") || endStr.equals("mp4")||endStr.equals("ppt") || endStr.equals("pptx")||
                            endStr.equals("xls") || endStr.equals("xlsx")||endStr.equals("doc") || endStr.equals("docx")||endStr.equals("pdf") || endStr.equals("txt")) {

                        fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                        fileBean.setResImage(getIamge(endStr));
                        fileBean.setFile_type(getType(endStr));
                        fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                        fileBean.setType(getFileType(endStr));
                        fileBean.setNet(false);
                        fileBeans.add(fileBean);

                    }else {
                        Toast.makeText(getActivity(), "请选择正确的文件格式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // fileBean = new FileListBean(file.getName(), "/storage/84BE-981E/声学在图书馆智慧空间建设中的应用.docx", "", "");

                  /*  //复制文件到指定目录
                    new Thread() {
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
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        try {
                            File file = new File(getFilePath(getActivity(), uri));
                            ToastUtil.makeText(getActivity(), "uri.getPath()=====" + uri.getPath());
                            Log.d("requestCodeUr2222", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName() + "++++++++++" + getFilePath(getActivity(), uri) + "===" + uri.getAuthority());
                            String endStr = file.getName().substring(file.getName().lastIndexOf(".") + 1);

                            if ("jpg".equals(endStr) || "gif".equals(endStr) || "png".equals(endStr) || "jpeg".equals(endStr) || "bmp".equals(endStr)||endStr.equals("m4a") || endStr.equals("mp3") || endStr.equals("mid") ||
                                    endStr.equals("xmf") || endStr.equals("ogg") || endStr.equals("wav")||endStr.equals("3gp") || endStr.equals("mp4")||endStr.equals("ppt") || endStr.equals("pptx")||
                                    endStr.equals("xls") || endStr.equals("xlsx")||endStr.equals("doc") || endStr.equals("docx")||endStr.equals("pdf") || endStr.equals("txt")) {
                                Log.d(TAG, "文件类型=" + endStr + "文件名字" + file.getName());
                                fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                                //复制文件到指定目录
                                new Thread() {
                                    @Override
                                    public void run() {
                                        copyFile(file.getPath(), fileStrPath + file.getName());
                                    }
                                }.start();
                                fileBean.setResImage(getIamge(endStr));
                                fileBean.setFile_type(getType(endStr));
                                fileBean.setNet(false);
                                fileBeans.add(fileBean);
                            }else {
                                Toast.makeText(getActivity(), "请选择正确的文件格式", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
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
                } else {
                    return "/storage/" + split[0] + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
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
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
            msg.what = 1;
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
        if (myBroadcastReceiver != null&&myBroadcastReceiver.isOrderedBroadcast()) {
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
        Log.d(TAG, "路过~~~~~11");
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
                    fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setNet(false);
                    fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
                    //  fileBean.setType(endStr);
                    fileBean.setType(getFileType(endStr));
                    shareFileBeans.add(fileBean);

                }
            }
            fileBeans.addAll(shareFileBeans);
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG+toString(), "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        int s=Integer.valueOf(textNub);
         s= s+1;
        tittle_num.setText("议题"+s);
   /*     Log.d(TAG+toString(), "onResume: "+"text="+text);
        if (Hawk.contains("WuHuFragmentData")) {
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
            if (wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() == 0) {
                return;
            }
            editListBeanList = wuHuEditBean.getEditListBeanList();
            topic.setText(editListBeanList.get(Integer.valueOf(text)).getSubTopics());
            attend.setText(editListBeanList.get(Integer.valueOf(text)).getAttendeBean());
        }
        Log.d(TAG+toString(), "onResume: ");*/

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG+toString(), "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG+toString(), "onStop: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG+toString(), "onDestroy: ");
        if (mySaveBroadcastReceiver!=null){
            getActivity().unregisterReceiver(mySaveBroadcastReceiver);
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
        Intent intent;
        switch (v.getId()){
            case  R.id.edit_ll:
                intent=new Intent(getActivity(), EditWuHuActivity.class);
                getActivity().startActivity(intent);

                break;
            case R.id.edit_rl:
                edit_name_rl.setVisibility(View.VISIBLE);
                break;
            case R.id.comfirm:
                edit_name_rl.setVisibility(View.GONE);
                break;
            case R.id.vote_ll:
                showRightDialog();
                break;

        }

    }
    public void showRightDialog( ) {
        //自定义dialog显示布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_wuhu, null);
        //自定义dialog显示风格
        dialog = new Dialog(getActivity(), R.style.DialogRight);
        //弹窗点击周围空白处弹出层自动消失弹窗消失(false时为点击周围空白处弹出层不自动消失)
        dialog.setCanceledOnTouchOutside(true);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        View line=inflate.findViewById(R.id.line);
        RadioGroup line_colors=inflate.findViewById(R.id.line_colors);
        RadioGroup theme_colors=inflate.findViewById(R.id.theme_colors);
        myListView=inflate.findViewById(R.id.myList_view);
        add_topic_rl=inflate.findViewById(R.id.add_topic_rl);
        dialg_rl_root=inflate.findViewById(R.id.dialg_rl_root);
        sava_all=inflate.findViewById(R.id.sava_all);
        Log.d("reyeyrty",UserUtil.ISCHAIRMAN+"");
        if (UserUtil.ISCHAIRMAN) {
            if (Hawk.contains("WuHuFragmentData")){
                WuHuEditBean wuHuEditBean= Hawk.get("WuHuFragmentData");
                wuHuEditBean.setTopics("2022年临时会议");
                wuHuEditBean.setTopic_type("会议记录");
                WuHuEditBean.EditListBean editListBean=new WuHuEditBean.EditListBean();
                editListBean.setSubTopics("总结2022年");
                editListBean.setAttendeBean("王二狗，李狐狸，张太郎，刘毛毛");
                wuHuEditBeanList.add(editListBean);
                Hawk.put("WuHuFragmentData",wuHuEditBean);
            }
        }

        wuHuListAdapter=new WuHuListAdapter(getActivity(),wuHuEditBeanList);
        wuHuListAdapter.setSaveSeparatelyInterface(this);
        myListView.setAdapter(wuHuListAdapter);
        wuHuListAdapter.notifyDataSetChanged();

        line_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.color_rb1:
                        Log.d("wwwwwww","dsffafaf111");
                        fragmentLine.setBackgroundColor(Color.parseColor("#EA4318"));
                        line.setBackgroundColor(Color.parseColor("#EA4318"));
                        break;
                    case R.id.color_rb2:
                        Log.d("wwwwwww","dsffafaf2222");
                        fragmentLine.setBackgroundColor(Color.parseColor("#1D1D1D"));
                       line.setBackgroundColor(Color.parseColor("#1D1D1D"));
                        break;

                }
            }
        });

        theme_colors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId) {
                switch (checkId){
                    case R.id.color_rb3:
                        root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home);
                      //  dialg_rl_root.setBackgroundResource(R.mipmap.bg_wuhu_home);
                        break;
                    case R.id.color_rb4:
                        root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home2);
                       // dialg_rl_root.setBackgroundResource(R.mipmap.bg_radiobutton4);
                        break;
                    case R.id.color_rb5:
                        root_lin.setBackgroundColor(Color.parseColor("#EBE9EA"));
                        //dialg_rl_root.setBackgroundColor(Color.parseColor("#EBE9EA"));
                        break;
                    case R.id.color_rb6:
                        root_lin.setBackgroundColor(Color.parseColor("#EFF4F8"));
                       // dialg_rl_root.setBackgroundColor(Color.parseColor("#EFF4F8"));
                        break;
                    case R.id.color_rb7:
                        root_lin.setBackgroundColor(Color.parseColor("#F2F8F5"));
                       // dialg_rl_root.setBackgroundColor(Color.parseColor("#F2F8F5"));
                        break;

                }
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
                        wsUpdata(wuHuEditBean,constant.WUHUADDFRAGMENT);
                    }

                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("aa","filePath");
                intent.putExtras(bundle);
                intent.setAction(constant.ADD_FRAGMENT_BROADCAST);
                getActivity().sendBroadcast(intent);

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

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
       if (in.getAction().equals(constant.SHARE_FILE_BROADCAST)){
            initData();
            File path = new File(fileShare);
            File[] files = path.listFiles();// 读取
            if (files == null) {
                return;
            }
            getShareFile(files);
            String filePath=in.getStringExtra("filePath");
            String flag=in.getStringExtra("flag");
            File file=new File(filePath);
            String fileName = file.getName();
            String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
            Log.d(TAG, "文件类型=" + endStr + "文件名字" + fileName);
            fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
            Uri uri = Uri.fromFile(file);
            Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
            fileBean.setResImage(getIamge(endStr));
            fileBean.setFile_type(getType(endStr));
           fileBean.setNet(false);
            fileBean.setSuffix(endStr);//上传文件后缀名和文件类型；setSuffix和setType所赋值内容一样。
            //  fileBean.setType(endStr);
            fileBean.setType(getFileType(endStr));
            //flag为1时是文件分享功能，只需更新文件列表，不需打开文件
           if (flag .equals("1")) {
               return;
           }

            Intent intent;
            if (fileBean.getFile_type().equals("3")){
                intent = new Intent();
                intent.setClass(getActivity(), ActivityImage.class);
                intent.putExtra("url", fileBean.getPath());
                intent.putExtra("isOpenFile", true);
                intent.putExtra("isNetFile", false);
                startActivity(intent);
            }else if (fileBean.getFile_type().equals("4")){
                if(UserUtil.isNetworkOnline){
                    intent = new Intent();
                    intent.setClass(getActivity(), SignActivity.class);
                    intent.putExtra("url", fileBean.getPath());
                    intent.putExtra("isOpenFile", true);
                    intent.putExtra("isNetFile", false);
                    intent.putExtra("tempPath", false);
                    intent.putExtra("fileName",fileBean.getName());
                    startActivity(intent);
                }else {
                    CVIPaperDialogUtils.showConfirmDialog(getActivity(), "当前无外网，会使用wps打开文件", "知道了", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                        }
                    });
                }
            }

            /*else if (fileBean.getFile_type().equals("4")){
                intent=new Intent(getActivity(), PdfActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("pdfPath", fileBean.getPath());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }*/

        }/*else if (in.getAction().equals(constant.SAVE_SEPARATELY_BROADCAST)){
           String type=in.getStringExtra("refreshType");

           if (type.equals("1")){
               fragmentLine.setBackgroundColor(Color.parseColor("#EA4318"));

           }else if (type.equals("2")){
               fragmentLine.setBackgroundColor(Color.parseColor("#1D1D1D"));
           }else if (type.equals("3")){
               root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home);
           }
           else if (type.equals("4")){
               root_lin.setBackgroundResource(R.mipmap.bg_wuhu_home2);
           }
           else if (type.equals("5")){
               root_lin.setBackgroundColor(Color.parseColor("#EBE9EA"));
           }
           else if (type.equals("6")){
               root_lin.setBackgroundColor(Color.parseColor("#EFF4F8"));
           }
           else if (type.equals("7")){
               root_lin.setBackgroundColor(Color.parseColor("#F2F8F5"));

           }else if (type.equals("8")) {

               if (Hawk.contains("WuHuFragmentData")) {
                   WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                   List<WuHuEditBean.EditListBean> editListBeanList = new ArrayList<>();
                   if (wuHuEditBean.getEditListBeanList() == null || wuHuEditBean.getEditListBeanList().size() == 0) {
                       return;
                   }
                   editListBeanList = wuHuEditBean.getEditListBeanList();
                   topic.setText(editListBeanList.get(Integer.valueOf(textNub)).getSubTopics());
                   attend.setText(editListBeanList.get(Integer.valueOf(textNub)).getAttendeBean());

                   tittle1.setText(wuHuEditBean.getTopics());
                   tittle2.setText(wuHuEditBean.getTopic_type());
               }

           }

       }*/
        }
    }
}
