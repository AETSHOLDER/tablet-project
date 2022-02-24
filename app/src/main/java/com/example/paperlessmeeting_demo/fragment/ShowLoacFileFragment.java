package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivityImage;
import com.example.paperlessmeeting_demo.activity.ActivityVideoView;
import com.example.paperlessmeeting_demo.activity.CommonWebViewActivity;
import com.example.paperlessmeeting_demo.adapter.LocationFileListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.FileListBean;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.MediaReceiver;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.MyListView;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 梅涛 on 2020/9/21.
 */
@SuppressLint("ValidFragment")
public class ShowLoacFileFragment extends BaseFragment implements MediaReceiver.sendfilePthInterface, LocationFileListAdapter.upLoadFileInterface, LocationFileListAdapter.PlayerClickInterface {
    @Override
    public void sendFileInfo(String path, String type, String flag) {

    }

    Unbinder unbinder;
    @BindView(R.id.image_add)
    ImageView imageAdd;
    @BindView(R.id.listView)
    MyListView listView;
    private LocationFileListAdapter fileListAdapter;
    private List<FileListBean> fileBeans = new ArrayList<>();
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
    private MediaPlayer mediaPlayer = new MediaPlayer();//实例化MediaPlayer类
    private String fileStrPath = Environment.getExternalStorageDirectory() + constant.DOWNLOAD_PATH;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    fileBeans.clear();
                    getCopyFile();
                    break;
                default:
                    break;
            }
        }
    };

    public ShowLoacFileFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ShowLoacFileFragment() {
    }

    public ShowLoacFileFragment(String titles, Context context) {
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

        fileListAdapter = new LocationFileListAdapter(getActivity(), fileBeans);
        listView.setAdapter(fileListAdapter);
        fileListAdapter.setUpLoadFileInterface(this);
        fileListAdapter.notifyDataSetChanged();
        Log.d("fwerwr", "页面初始化1111");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FileListBean fileBean = (FileListBean) adapterView.getAdapter().getItem(i);
                Log.d("requestCodeUr333", fileBean.getPath());
                Intent intent;
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
                        // startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                        intent = new Intent();
                        intent.setClass(getActivity(), CommonWebViewActivity.class);
                        intent.putExtra("url", fileBean.getPath());
                        intent.putExtra("isOpenFile", true);
                        intent.putExtra("isNetFile", false);
                        intent.putExtra("tempPath", false);
                        startActivity(intent);
                        break;
                }

            }
        });
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

    private void startPlayer(String url)

    {
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
                    fileBeans.add(fileBean);

                }
            }
            fileListAdapter.setGridViewBeanList(fileBeans);
            fileListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initView() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                fileListAdapter.setCompleted(true);
                fileListAdapter.notifyDataSetChanged();
                Log.d("ValidFragment", "播放完了");

            }
        });
    }

    @Override
    public void sendfilePth(String filePth) {

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
        Log.d("fwerwr", "页面初始化3333");
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}
