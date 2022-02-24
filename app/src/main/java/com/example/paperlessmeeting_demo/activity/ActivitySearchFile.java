package com.example.paperlessmeeting_demo.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.adapter.FileListAdapter;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.NewFileBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.DownloadUtil;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.PermissionManager;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.tool.VideoEncoderUtil;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by 梅涛 on 2020/10/27.
 */

public class ActivitySearchFile extends BaseActivity implements EasyPermissions.PermissionCallbacks, FileListAdapter.PlayerClickInterface, FileListAdapter.PublicToPrivateInterface, FileListAdapter.DeleteInterface {

    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.search_edittext)
    EditText searchEdittext;
    @BindView(R.id.search)
    CardView search;
    @BindView(R.id.search_btn)
    TextView searchBtn;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.listView)
    MyListView listView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private FileListAdapter fileListAdapter;
    private String contenStr;
    private static final int ACTIVITY_RESULT_CODE = 110;
    private MediaProjectionManager projectionManager;
    private VideoEncoderUtil videoEncoder;
    private List<String> name = new ArrayList<>();
    private List<NewFileBean.MeetingFileListBean> fileOtherBeans = new ArrayList<>();//音视频文件
    private List<NewFileBean.MeetingFileListBean> fileBeans = new ArrayList<>();//doc，excle，pdf文件
    private String fileStrPath = Environment.getExternalStorageDirectory() + constant.DOWNLOAD_PATH;
    private String userName = "";
    private String uploadTime = "";
    private NewFileBean.MeetingFileListBean fileBean;
    private MediaPlayer mediaPlayer = new MediaPlayer();//实例化MediaPlayer类
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    NewFileBean.MeetingFileListBean fileListBean = (NewFileBean.MeetingFileListBean) msg.obj;
                    userName = fileListBean.getUser_id().getName();
                    uploadTime = fileListBean.getUpload_time();
                    Log.d("dsafaf", fileListBean.getFile_path() + "===" + fileListBean.getPath());
                    String fileName = fileListBean.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d("sdfsgsg", "文件类型=" + endStr + "文件名字" + fileName);
                    Log.d("FileDownloader000022", fileListBean.getFile_path() + "fileId=" + fileListBean.get_id() + "userIdBean=" + fileListBean.getUser_id().get_id());
                    fileBean = new NewFileBean.MeetingFileListBean(fileListBean.getName(), fileListBean.getPath(), userName, uploadTime);
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setUser_id(fileListBean.getUser_id());
                    fileBean.set_id(fileListBean.get_id());
                    fileBean.setC_id(fileListBean.getC_id());
                    fileBean.setUnclassified(fileListBean.getUnclassified());
                    fileBeans.add(fileBean);
                    fileListAdapter.setGridViewBeanList(fileBeans);
                    fileListAdapter.notifyDataSetChanged();
                    //完成主界面更新,拿到数据
                    //   getDownload(fileListBean.get_id(), fileListBean.getUser_id(), fileListBean.getUnclassified());
                    //    getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 请求权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Toast.makeText(ActivitySearchFile.this, "授权成功", Toast.LENGTH_LONG).show();


    }

    /**
     * 检查读写权限权限
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
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(ActivitySearchFile.this, "用户授权失败", Toast.LENGTH_LONG).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_file;
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initData() {

                /*
* 统计用户行为日志
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
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, ACTIVITY_RESULT_CODE);*/
                fileBeans.clear();
                contenStr = searchEdittext.getText().toString().trim();
                if (contenStr.isEmpty()) {
                    Toast.makeText(ActivitySearchFile.this, "搜索内容不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                getData(contenStr);


            }
        });

    }

    private void getData(String edtextStr) {
        String c_id = "";
        if (Hawk.contains("c_id")) {
            c_id = Hawk.get("c_id");
        }
        String meeting_id = UserUtil.meeting_record_id;
        NetWorkManager.getInstance().getNetWorkApiService().getSearchFileList(edtextStr, c_id, meeting_id).compose(this.<BasicResponse<List<NewFileBean.MeetingFileListBean>>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<List<NewFileBean.MeetingFileListBean>>>() {
                    @Override
                    protected void onSuccess(BasicResponse<List<NewFileBean.MeetingFileListBean>> response) {

                        if (response != null) {
                            // setData(response);
                            netFileName(response.getData());
                        }

                    }
                });

    }

    private void setData(BasicResponse<List<NewFileBean.MeetingFileListBean>> response) {
        fileBeans = response.getData();
        fileListAdapter = new FileListAdapter(ActivitySearchFile.this, fileBeans);
        listView.setAdapter(fileListAdapter);
        fileListAdapter.notifyDataSetChanged();

    }

    private void netFileName(List<NewFileBean.MeetingFileListBean> fileListBeanList) {
        name.clear();
        fileOtherBeans.clear();
        fileBeans.clear();
        for (int i = 0; i < fileListBeanList.size(); i++) {
            NewFileBean.MeetingFileListBean fileListBean = fileListBeanList.get(i);
            String c_id = fileListBeanList.get(i).getC_id();
            Log.d("vvvvvvvvvvvvvvvvvvvv", fileListBeanList.get(i).getC_id());
            String endStr = fileListBeanList.get(i).getFile_path().substring(fileListBeanList.get(i).getFile_path().lastIndexOf(".") + 1);
            Log.d("vrdvafd", "文件类型=" + fileListBeanList.get(i).getFile_name() + "文件名字" + fileListBeanList.get(i).getFile_path() + "p====" + fileListBeanList.get(i).getName());
            //下载文件--子线程
            if (constant.FILE_Public.equals(fileListBean.getUnclassified())) {
                if ("4".equals(getType(endStr))) {

                    String path = fileListBeanList.get(i).getFile_path();
                    String name = fileListBeanList.get(i).getFile_name();
                    String fileId = fileListBeanList.get(i).get_id();
                    NewFileBean.MeetingFileListBean.UserIdBean userIdBean = fileListBeanList.get(i).getUser_id();
                    String unclassified = fileListBeanList.get(i).getUnclassified();
                    Log.d("FileDownloader0000", fileListBean.getFile_path() + "fileId=" + fileListBeanList.get(i).get_id() + "userIdBean=" + userIdBean.get_id());
                    DownloadUtil.get().download(fileListBeanList.get(i).getFile_path(), fileStrPath, fileListBeanList.get(i).getFile_name(), new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            Log.v("dfsfff111", "下載成功,文件已存入手机内部存储设备根目录下Download文件夾中");
                      /*  Looper.prepare();//增加部分
                        Looper.loop();//增加部分*/
                            Message msg = new Message();
                            fileListBean.setName(name);
                            fileListBean.setPath(fileStrPath + name);
                            fileListBean.set_id(fileId);
                            fileListBean.setC_id(c_id);
                            fileListBean.setUser_id(userIdBean);
                            fileListBean.setUnclassified(unclassified);
                            Log.d("FileDownloader000011", fileListBean.getFile_path() + "fileId=" + fileId + "userIdBean222=" + c_id);
                            msg.obj = fileListBean;//可以是基本类型，可以是对象，可以是List、map等；
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                            Log.v("dfsfff222", "下載成功,文件已存入手机内部存储设备根目录下Download文件夾中");
                        }

                        @Override
                        public void onDownloading(int progress) {
//
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
//

                        }
                    });


                } else {
                    //除了文件外（doc,excle,pdf,ppt）,其他格式文件执行正常网络数据
                    fileBean = new NewFileBean.MeetingFileListBean(fileListBeanList.get(i).getFile_name(), fileListBeanList.get(i).getFile_path(), fileListBeanList.get(i).getUser_id().getName(), fileListBeanList.get(i).getUpload_time());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileListBean.setC_id(c_id);
                    fileBean.setUnclassified(fileListBeanList.get(i).getUnclassified());
                    fileListBean.set_id(fileListBeanList.get(i).get_id());
                    fileListBean.setUser_id(fileListBeanList.get(i).getUser_id());
                    fileOtherBeans.add(fileBean);
                }
            }
            if (fileOtherBeans != null) {
                Log.d("requestCodeUr666", fileOtherBeans.size() + "  ");
            }
            fileListAdapter = new FileListAdapter(ActivitySearchFile.this, fileOtherBeans);
            listView.setAdapter(fileListAdapter);
            fileListAdapter.notifyDataSetChanged();
            fileListAdapter.setOnPlayerClickInterface(this);
            fileListAdapter.setToPrivateInterface(this);
            fileListAdapter.setDeleteInterface(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    NewFileBean.MeetingFileListBean fileBean = (NewFileBean.MeetingFileListBean) adapterView.getAdapter().getItem(i);
                    Log.d("requestCodeUr333", fileBean.getPath());
                    Intent intent;
                    switch (fileBean.getFile_type()) {
                        case "1"://音乐
                            startPlayer(fileBean.getPath());
                            //getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                            // FileUtils.openFile("file://"+fileBean.getPath(), getActivity());
                            break;
                        case "2"://视频
                            intent = new Intent();
                            intent.setClass(ActivitySearchFile.this, ActivityVideoView.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", true);
                            startActivity(intent);
                            //  FileUtils.openFile(fileBean.getPath(), getActivity());
                            break;
                        case "3"://图片
                            intent = new Intent();
                            intent.setClass(ActivitySearchFile.this, ActivityImage.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", true);
                            startActivity(intent);
                            break;
                        case "4":
                            startActivity(FileUtils.openFile(fileBean.getPath(), ActivitySearchFile.this));
                            // FileUtils.openFile(fileBean.getPath(), getActivity());
                          /*  intent = new Intent();
                            intent.setClass(getActivity(), CommonWebViewActivity.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            startActivity(intent);*/
                            break;
                    }

                }
            });
            fileListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPlayerClick(String url) {
        Log.d("dfafaf333", "222223333" + url);
        Uri uri = Uri.parse(url);//网络中的音乐文件
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(ActivitySearchFile.this, uri);//音乐文件路径
            mediaPlayer.prepare();//资源文件准备
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();//播放
        }
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

    private void startPlayer(String url) {
        if (url.isEmpty()) {
            return;
        }
        Log.d("requestCodeUrppppp", url);
        {
            Uri uri = Uri.parse(url);//网络中的音乐文件
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(ActivitySearchFile.this, uri);//音乐文件路径
                mediaPlayer.prepare();//资源文件准备
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();//播放
                // mediaPlayer.setLooping(true);
            }
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
        } else if (end.equals("xls")) {
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
        } else if (end.equals("xls")) {
            return "4";
        } else if (end.equals("doc") || end.equals("docx")) {
            return "4";
        } else if (end.equals("pdf")) {
            return "4";
        } else {
            return "0";
        }

    }

    @Override
    public void onTransfor(String id, String userId) {
        Log.d("dfafaf222", id + "====" + userId);
        //文件公开变成私有
        FileTypeTransfor(id, userId);
    }

    //删除文件
    @Override
    public void onDelte(String id, String c_id) {
        deleteFile(id, c_id);
    }

    public void FileTypeTransfor(String id, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", userId);
        map.put("unclassified", constant.FILE_PRIVATE);
        //绑定生命周期
        NetWorkManager.getInstance().getNetWorkApiService().updateUnclassified(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onFail(BasicResponse response) {
                        Toast.makeText(ActivitySearchFile.this, response.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {
                        initData();

                    }
                });

    }

    public void deleteFile(String id, String c_id) {
        List<String> strings = new ArrayList<>();
        strings.add(id);
        Map<String, Object> map = new HashMap<>();
        map.put("ids", strings);
        map.put("c_id", c_id);
        //绑定生命周期
        NetWorkManager.getInstance().getNetWorkApiService().removeMeetingFile(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onFail(BasicResponse response) {
                        Toast.makeText(ActivitySearchFile.this, response.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {
                        initData();

                    }
                });

    }

    private void getFileName(File[] files, String fileId, NewFileBean.MeetingFileListBean.UserIdBean userbean, String unclassified) {

        String content = "";
        if (files != null) {// 先判断目录是否为空，否则会报空指针
            for (File file : files) {

                if (file.isDirectory()) {

                    getFileName(file.listFiles(), fileId, userbean, unclassified);

                } else {
                    String fileName = file.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    fileBean = new NewFileBean.MeetingFileListBean(file.getName(), file.getPath(), userName, uploadTime);
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUrppp", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.set_id(fileId);
                    fileBean.setUnclassified(unclassified);
                    fileBean.setUser_id(userbean);
                    if (constant.FILE_Public.equals(unclassified)) {
                        fileBeans.add(fileBean);
                    }

                }
            }
//
        }
        for (int i = 0; i < fileOtherBeans.size(); i++) {
            fileBeans.add(fileOtherBeans.get(i));
        }
        Log.d("requestCodeUr66622", fileBeans.size() + "");
        fileListAdapter.setGridViewBeanList(fileBeans);
        fileListAdapter.notifyDataSetChanged();
    }

    /**
     * Activity执行结果
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionManager.onActivityResult(requestCode, resultCode, data, this);
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
        if (videoEncoder != null) {
            videoEncoder.stop();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        checkWritePermission1();
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        PermissionManager.RequestOverlayPermission(this);
    }
}
