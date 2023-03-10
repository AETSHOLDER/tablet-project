package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivityImage;
import com.example.paperlessmeeting_demo.activity.PdfActivity;
import com.example.paperlessmeeting_demo.activity.Sign.SignActivity;
import com.example.paperlessmeeting_demo.adapter.LocationFileListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.sharefile.SocketShareFileManager;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.MediaReceiver;
import com.example.paperlessmeeting_demo.tool.NetWorkUtils;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.NetworkUtil;
import com.example.paperlessmeeting_demo.util.ToastUtil;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.orhanobut.hawk.Hawk;
import com.snow.common.tool.utils.FastClickUtils;

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

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ?????? on 2020/9/21.
 * ????????????
 */
@SuppressLint("ValidFragment")
public class FileFragment extends BaseFragment implements MediaReceiver.sendfilePthInterface, LocationFileListAdapter.upLoadFileInterface, LocationFileListAdapter.PlayerClickInterface, LocationFileListAdapter.ShareFileInterface, LocationFileListAdapter.PushFileInterface {

    Unbinder unbinder;
    @BindView(R.id.image_add)
    ImageView imageAdd;
    @BindView(R.id.listView)
    MyListView listView;
    @BindView(R.id.progressBar_ll)
    RelativeLayout progressBarLl;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.main_title_tv)
    TextView mainTitleTv;
    @BindView(R.id.sub_title_tv)
    TextView subTitleTv;

    private String selfIp = "";
    private LocationFileListAdapter fileListAdapter;
    private List<FileListBean> fileBeans = new ArrayList<>();
    private List<FileListBean> shareFileBeans = new ArrayList<>();//???????????????????????????????????????
    private FileListBean fileBean;
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
    private MediaPlayer mediaPlayer = new MediaPlayer();//?????????MediaPlayer???
    private String fileStrPath = Environment.getExternalStorageDirectory() + constant.COPY_PATH;
    private String fileShare = Environment.getExternalStorageDirectory() + constant.SHARE_FILE;//??????????????????????????????????????????
    private String path = "/storage/emulated/0/";//????????????????????????
    private String videoPath = "";
    private String endStrAll;
    private SocketShareFileManager socketShareFileManager;
    private MyBroadcastReceiver myBroadcastReceiver;//??????????????????????????????????????????
    private List<String> stringListIp = new ArrayList<>();
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //    fileBeans.clear();
                    getCopyFile();
                    break;
                /*
                 * ??????????????????????????????????????????
                 * */
                case 4:
                    progressBarLl.setVisibility(View.VISIBLE);
                    String fileName = msg.obj.toString();
                    tips.setText(fileName);
                    break;
                /*
                 * ?????????????????????????????????????????????
                 * */
                case 5:
                    progressBarLl.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
                /*
                 * ?????????????????????????????????????????????
                 * */
                case 6:
                    progressBarLl.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "??????????????????", Toast.LENGTH_SHORT).show();
                    String df = msg.obj.toString();
                    Log.d("dfsfsdf", df);
                    break;
                default:
                    break;
            }
        }
    };

    public FileFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FileFragment() {
    }

    public FileFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    protected int getLayoutId() {

        return R.layout.fragment_file_usb;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {

        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        fileBeans.clear();
//        FileListBean fileListBean1 = new FileListBean("??????????????????????????????????????????0622(2).docx", path + "??????????????????????????????????????????0622(2).docx", "", "");
//        fileListBean1.setType("Document");
//        fileListBean1.setSuffix(".docx");
//        FileListBean fileListBean2 = new FileListBean("?????????????????????_20210225154828.pdf", path + "?????????????????????_20210225154828.pdf", "", "");
//        fileListBean2.setType("Document");
//        fileListBean2.setSuffix(".pdf");
//        FileListBean fileListBean3 = new FileListBean("0????????????-????????????-2.pptx", path + "0????????????-????????????-2.pptx", "", "");
//        fileListBean3.setType("Document");
//        fileListBean3.setSuffix(".pptx");
//
//        fileBeans.add(fileListBean1);
//        fileBeans.add(fileListBean2);
//        fileBeans.add(fileListBean3);
        for (int i = 0; i < fileBeans.size(); i++) {
            String endStr = fileBeans.get(i).getName().substring(fileBeans.get(i).getName().lastIndexOf(".") + 1);
            fileBeans.get(i).setResImage(getIamge(endStr));
            fileBeans.get(i).setFile_type(getType(endStr));
        }
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//???????????????
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        });

        fileListAdapter = new LocationFileListAdapter(getActivity(), fileBeans);
        listView.setAdapter(fileListAdapter);
        fileListAdapter.setUpLoadFileInterface(this);
        fileListAdapter.setShareFileInterface(this);
        fileListAdapter.setPushFileInterface(this);
        fileListAdapter.notifyDataSetChanged();
        Log.d("fwerwr", "???????????????1111");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(FastClickUtils.init().isClickFast()){
                    return;
                }
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
                        CVIPaperDialogUtils.showConfirmDialog(getActivity(), "???????????????????????????wps????????????", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                            @Override
                            public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                                startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                            }
                        });
                    }
                }

              /*
                switch (fileBean.getFile_type()) {
                    case "1"://??????
                        startPlayer("file://" + fileBean.getPath());
                        //  getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                        // FileUtils.openFile("file://"+fileBean.getPath(), getActivity());
                        break;
                    case "2"://??????
                        intent = new Intent();
                        intent.setClass(getActivity(), ActivityVideoView.class);
                        intent.putExtra("url", fileBean.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        startActivity(intent);
                        //  FileUtils.openFile(fileBean.getPath(), getActivity());
                        break;
                    case "3"://??????
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
                            ToastUtils.showToast(context, "??????????????????????????????WPS Office?????????");
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

    private void getCopyFile() {
        if (fileStrPath.isEmpty()) {
            return;
        }
        File path = new File(fileStrPath);
        Log.d("sjshgha", "??????~~~~~" + fileStrPath);
        File[] files = path.listFiles();// ??????
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            Log.d("sjshgha", files[i].toString() + "78687");
        }
        getFileName(files);
    }

    private void startPlayer(String url) {
        Uri uri = Uri.parse(url);//????????????????????????
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            //  mediaPlayer.setDataSource(getActivity(), uri);//??????????????????
            mediaPlayer.prepare();//??????????????????
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();//??????
            //    mediaPlayer.setLooping(true);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("mediaPlayer", "????????????");
            }
        });
    }

    @Override
    public void onPlayerClick(String url) {
        Log.d("dfafaf333", "222223333" + url);
        Uri uri = Uri.parse(url);//????????????????????????
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getActivity(), uri);//??????????????????
            mediaPlayer.prepare();//??????????????????
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();//??????
        }
    }

    @Override
    public void onPauseClick(String url) {
        Log.d("dsffff", "11111122222" + url);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();//??????
        }
    }

    @Override
    public void onResetMusic(String url) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();//??????
            } else {
                onPlayerClick(url);
            }

        }

    }
//String actionType:?????????????????????????????????  1?????????  2?????????
    @Override
    public void shareFileInfo(String path, String type, String flag, String name, String author, String time) {
        Log.d("FileFragment--??????", path + "===name==" + name);

        final ArrayList<String> fileNames = new ArrayList<>();
        final ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        fileNames.add(name);
        Message.obtain(mHandler, 4, name).sendToTarget();
        /*
         * ??????????????????Service?????????????????????
         * */
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (Hawk.contains("stringsIp")) {
                    stringListIp = Hawk.get("stringsIp");
                }
                //??????Ip?????????????????????????????????
                for (int i = 0; i < stringListIp.size(); i++) {
                    socketShareFileManager.SendFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT,"1","1");
                }
            }
        });
        sendThread.start();
        // Message.obtain(handler, 0, response).sendToTarget();
    }
    @Override
    public void pushFileInfo(String path, String type, String flag, String name, String author, String time) {
        Log.d("FileFragment--??????", path + "===name==" + name);

        final ArrayList<String> fileNames = new ArrayList<>();
        final ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        fileNames.add(name);
        Message.obtain(mHandler, 4, name).sendToTarget();
        /*
         * ??????????????????Service?????????????????????
         * */
        Thread sendThread = new Thread(new Runnable() {
            @Override
                public void run() {

                if (Hawk.contains("stringsIp")) {
                    stringListIp = Hawk.get("stringsIp");
                }
                //??????Ip?????????????????????????????????
                for (int i = 0; i < stringListIp.size(); i++) {
                    socketShareFileManager.SendFile(fileNames, paths, stringListIp.get(i), constant.SHARE_PORT,"2","1");
                }
            }
        });
        sendThread.start();
    }

    @Override
    public void sendFileInfo(String path, String type, String flag) {
        //??????????????????RequestBody???

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
                            //????????????????????????
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
                            fileListBean.setType(getFileType(endStrAll));//??????????????????????????????????????????????????????????????????
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
                            Toast.makeText(getActivity(), "???????????????", Toast.LENGTH_SHORT).show();
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

    private void getFileName(File[] files) {
        Log.d(TAG, "??????~~~~~11");
        String content = "";
        if (files != null) {// ???????????????????????????????????????????????????
            for (File file : files) {
                Log.d(TAG, "??????~~~~~11" + file.getPath());

                if (file.isDirectory()) {
                    Log.d(TAG, "??????????????????????????????1" + file.getName().toString()
                            + file.getPath().toString());

                    getFileName(file.listFiles());
                    Log.d(TAG, "??????????????????????????????2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d(TAG, "????????????=" + endStr + "????????????" + fileName);
                    fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                    //  fileBean.setType(endStr);
                    fileBean.setType(getFileType(endStr));

                    fileBeans.add(fileBean);

                }
            }
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initView() {
        /*
         * ???????????????????????????????????????Ip?????????????????????
         * */
        if (UserUtil.isTempMeeting) {
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                fileListAdapter.setCompleted(true);
                fileListAdapter.notifyDataSetChanged();
                Log.d("ValidFragment", "????????????");

            }
        });
        if (Hawk.contains("meetingInfoBean")) {
            MeetingInfoBean meetingInfoBean = Hawk.get("meetingInfoBean");
            mainTitleTv.setText("??????:"+meetingInfoBean.getName());
        }
        if (UserUtil.isTempMeeting) {
            mainTitleTv.setText("??????1?????????????????????????????????????????????");
        }
    }

    @Override
    public void sendfilePth(String filePth) {

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

    //??????????????????????????????
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        IntentFilter iFilter = new IntentFilter();
        mBroadcastReceiver = new MediaReceiver();
        mBroadcastReceiver.setSendfilePthInterface(this);
        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addDataScheme("file");
        getActivity().registerReceiver(mBroadcastReceiver, iFilter);
        socketShareFileManager = new SocketShareFileManager(mHandler);
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHARE_FILE_BROADCAST);
        getActivity().registerReceiver(myBroadcastReceiver, filter);
        selfIp = getNetworkType();
        return rootView;
    }

    /*
     * ??????????????????????????????????????????????????????
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
                    Log.d("requestCodeUr7788", "????????????=" + endStr + "????????????" + file.getName() + getFilePath(getActivity(), uri));
                    // fileBean = new FileListBean(file.getName(), "/storage/84BE-981E/????????????????????????????????????????????????.docx", "", "");
                    fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
                    fileBean.setType(getFileType(endStr));
                    fileBeans.add(fileBean);
                  /*  //???????????????????????????
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
                //???????????????????????????
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
                            Log.d(TAG, "????????????=" + endStr + "????????????" + file.getName());
                            fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                            //???????????????????????????
                            new Thread() {
                                @Override
                                public void run() {
                                    copyFile(file.getPath(), fileStrPath + file.getName());
                                }
                            }.start();
                            fileBean.setResImage(getIamge(endStr));
                            fileBean.setFile_type(getType(endStr));
                            fileBeans.add(fileBean);
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
     * ??????????????????
     *
     * @param oldPath$Name String ???????????????+????????? ??????data/user/0/com.test/files/abc.txt
     * @param newPath$Name String ???????????????+????????? ??????data/user/0/com.test/cache/abc.txt
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

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);    //???????????????
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
            //  msg.obj = fileListBean;//???????????????????????????????????????????????????List???map??????
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
        super.onDestroyView();
        if (mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
        if (myBroadcastReceiver != null) {
            getActivity().unregisterReceiver(myBroadcastReceiver);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /*
     * ???????????????????????????
     * */
    private void getShareFile(File[] files) {
        shareFileBeans.clear();
        Log.d(TAG, "??????~~~~~11");
        String content = "";
        if (files != null) {// ???????????????????????????????????????????????????
            for (File file : files) {
                Log.d(TAG, "??????~~~~~11" + file.getPath());

                if (file.isDirectory()) {
                    Log.d(TAG, "??????????????????????????????1" + file.getName().toString()
                            + file.getPath().toString());

                    getFileName(file.listFiles());
                    Log.d(TAG, "??????????????????????????????2" + file.getName().toString()
                            + file.getPath().toString());
                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d(TAG, "????????????=" + endStr + "????????????" + fileName);
                    fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
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

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            initData();
            File path = new File(fileShare);
            File[] files = path.listFiles();// ??????
            if (files == null) {
                return;
            }
            getShareFile(files);
            String filePath=in.getStringExtra("filePath");
            File file=new File(filePath);
            String fileName = file.getName();
            String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
            Log.d(TAG, "????????????=" + endStr + "????????????" + fileName);
            fileBean = new FileListBean(file.getName(), file.getPath(), "", "");
            Uri uri = Uri.fromFile(file);
            Log.d("requestCodeUr555", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
            fileBean.setResImage(getIamge(endStr));
            fileBean.setFile_type(getType(endStr));
            fileBean.setSuffix(endStr);//???????????????????????????????????????setSuffix???setType????????????????????????
            //  fileBean.setType(endStr);
            fileBean.setType(getFileType(endStr));


            Intent intent;
//            if (fileBean.getFile_type().equals("3")){
//                intent = new Intent();
//                intent.setClass(getActivity(), ActivityImage.class);
//                intent.putExtra("url", fileBean.getPath());
//                intent.putExtra("isOpenFile", true);
//                intent.putExtra("isNetFile", false);
//                startActivity(intent);
//            }else if (fileBean.getFile_type().equals("4")){
//
//                intent=new Intent(getActivity(), PdfActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("pdfPath", fileBean.getPath());
//                intent.putExtras(bundle);
//                getActivity().startActivity(intent);
//            }

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
                    CVIPaperDialogUtils.showConfirmDialog(getActivity(), "???????????????????????????wps????????????", "?????????", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                        }
                    });
                }
            }


        }
    }


}
