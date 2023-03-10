package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.ActivityImage;
import com.example.paperlessmeeting_demo.activity.ActivityVideoView;
import com.example.paperlessmeeting_demo.adapter.PrivateFileListAdapter;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.NewFileBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.bean.WSBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.Define;
import com.example.paperlessmeeting_demo.tool.DownloadUtil;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.FileUtils;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.widgets.MyTabLayout;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ?????? on 2020/9/21.
 * ????????????
 */
@SuppressLint("ValidFragment")
public class PrivateFileFragment extends BaseFragment implements PrivateFileListAdapter.PlayerClickInterface, PrivateFileListAdapter.PrivateToPublicInterface, PrivateFileListAdapter.DeleteInterface {

    @BindView(R.id.tabLayout)
    MyTabLayout tabLayout;
    private int num = 0;//??????????????????
    @BindView(R.id.smartRefreshLayout_home)
    SmartRefreshLayout smartRefreshLayout;
    private String fileType = "Document";//??????????????????????????????????????????
    @Override
    protected void initView() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                fileListAdapter.setCompleted(true);
                fileListAdapter.notifyDataSetChanged();
                Log.d("ValidFragment", "????????????");

            }
        });
        List<String> stringList = new ArrayList<>();
        stringList.add("??????");
        stringList.add("??????");
        stringList.add("??????");
        stringList.add("??????");
    /*    TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("??????");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("??????");
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("??????");
        TabLayout.Tab tab4 = tabLayout.newTab();
        tab4.setText("??????");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tab4);*/
        tabLayout.setTitle(stringList);
        tabLayout.getTabAt(0).select(); //?????????????????????tab
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView txt_title = (TextView) view.findViewById(R.id.tab_text);
                Log.d("gfddhh", txt_title.getText().toString().trim());
                if ("??????".equals(txt_title.getText().toString().trim())) {
                    getData(constant.DOCUMENT);
                    fileType = constant.DOCUMENT;

                } else if ("??????".equals(txt_title.getText().toString().trim())) {
                    getData(constant.IMAGE);
                    fileType = constant.IMAGE;

                } else if ("??????".equals(txt_title.getText().toString().trim())) {
                    getData(constant.VIDEO);
                    fileType = constant.VIDEO;

                } else if ("??????".equals(txt_title.getText().toString().trim())) {
                    getData(constant.OTHER);
                    fileType = constant.OTHER;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    Unbinder unbinder;
    @BindView(R.id.listView)
    ListView listView;
    private String titles;
    private Context context;
    private List<String> name = new ArrayList<>();
    private String fileStrPath = Environment.getExternalStorageDirectory() + constant.DOWNLOAD_PATH;
    private FragmentManager fragmentManager;
    private PrivateFileListAdapter fileListAdapter;
    private List<NewFileBean.MeetingFileListBean> fileOtherBeans = new ArrayList<>();//???????????????
    private List<NewFileBean.MeetingFileListBean> fileBeans = new ArrayList<>();//doc???excle???pdf??????
    private NewFileBean.MeetingFileListBean fileBean;
    private String userName = "";
    private String uploadTime = "";
    private MediaPlayer mediaPlayer = new MediaPlayer();//?????????MediaPlayer???
    private BroadcastReceiver mBroadcastReceiver;
    @BindView(R.id.no_data)
    TextView no_data;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //  fileBeans.clear();
                    NewFileBean.MeetingFileListBean fileListBean = (NewFileBean.MeetingFileListBean) msg.obj;
                    userName = fileListBean.getUser_id().getName();
                    uploadTime = fileListBean.getUpload_time();
                    Log.d("dsafaf", fileListBean.getFile_path() + "===" + fileListBean.getPath());
                    String fileName = fileListBean.getName();
                    String endStr = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d("sdfsgsg", "????????????=" + endStr + "????????????" + fileName);
                    fileBean = new NewFileBean.MeetingFileListBean(fileListBean.getName(), fileListBean.getPath(), userName, uploadTime);
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setUser_id(fileListBean.getUser_id());
                    fileBean.set_id(fileListBean.get_id());
                    fileBean.setUnclassified(fileListBean.getUnclassified());
                    fileBeans.add(fileBean);
                    fileListAdapter.setGridViewBeanList(fileBeans);
                    fileListAdapter.notifyDataSetChanged();
                    //?????????????????????,????????????
                    // getDownload();
                    //    getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                    break;
                default:
                    break;
            }
        }
    };

    public PrivateFileFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public PrivateFileFragment() {
    }

    public PrivateFileFragment(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {
        /**
         * ????????????????????????
         */
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }
        getData(fileType);

        initSmartRefreshLayout();
    }
    //???????????????????????????
    private void initSmartRefreshLayout() {
        smartRefreshLayout.setEnableLoadmore(false);
      /*  smartRefreshLayout.setRefreshHeader(new CustomRefreshHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new CustomRefreshFooter(getContext(), "????????????"));*/
        smartRefreshLayout.setEnableScrollContentWhenLoaded(true);//??????????????????????????????????????????????????????
        smartRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //  presenter.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(fileType);
            }
        });

    }
    private void getData(String type) {
        smartRefreshLayout.setEnableRefresh(false);
        String _id = UserUtil.meeting_record_id;
        int publice = 0;
        num = 0;
        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().getFileList(_id, type).compose(this.<BasicResponse<NewFileBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<NewFileBean>>() {
                    @Override
                    protected void onSuccess(BasicResponse<NewFileBean> response) {
                        if (response != null) {
                            smartRefreshLayout.finishRefresh(true);
                            smartRefreshLayout.setEnableRefresh(true);
                            NewFileBean newFileBean = (NewFileBean) response.getData();
                            List<NewFileBean.MeetingFileListBean> fileListBeanList = new ArrayList<>();
                            fileListBeanList = newFileBean.getMeetingFileList();
                            if (newFileBean.getMeetingFileList().size() < 1) {

                                no_data.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < fileListBeanList.size(); i++) {
                                    if (constant.FILE_PRIVATE.equals(fileListBeanList.get(i).getUnclassified())) {
                                        ++num;
                                    }

                                }
                                Log.d("gfdh", num + "");
                                if (num > 0) {
                                    no_data.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);

                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                    Log.d("gfdh", num + "~~~~~");
                                }

                            }
                            if (fileListBeanList != null) {
                                Log.d("NetFileFragment", fileListBeanList.size() + "");
                            }
                            netFileName(fileListBeanList);
                            for (int i = 0; i < fileListBeanList.size(); i++) {


                            }
                        }
                    }
                });
    }

    private void netFileName(List<NewFileBean.MeetingFileListBean> fileListBeanList) {
        name.clear();
        fileOtherBeans.clear();
        fileBeans.clear();
        for (int i = 0; i < fileListBeanList.size(); i++) {
            NewFileBean.MeetingFileListBean fileListBean = fileListBeanList.get(i);
            String c_id = fileListBeanList.get(i).getC_id();
            String endStr = fileListBeanList.get(i).getFile_path().substring(fileListBeanList.get(i).getFile_path().lastIndexOf(".") + 1);
            Log.d("vrdvafd", "????????????=" + fileListBeanList.get(i).getFile_name() + "????????????" + fileListBeanList.get(i).getFile_path() + "p====" + fileListBeanList.get(i).getName());
            if (constant.FILE_PRIVATE.equals(fileListBean.getUnclassified())) {
                //????????????--?????????
                if ("4".equals(getType(endStr))) {
                    Log.d("FileDownloader0000", fileListBean.getFile_path() + "");

                    String path = fileListBeanList.get(i).getFile_path();
                    String name = fileListBeanList.get(i).getFile_name();
                    String fileId = fileListBeanList.get(i).get_id();
                    String unclassified = fileListBeanList.get(i).getUnclassified();
                    NewFileBean.MeetingFileListBean.UserIdBean userIdBean = fileListBeanList.get(i).getUser_id();
                    //       HttpDownloader httpDownloader = new HttpDownloader();
               /* if (Hawk.contains("fileName")) {
                    String fileName = Hawk.get("fileName");
                    if (fileListBeanList.get(i).getFile_name().equals(fileName)) {
                        return;
                    }
                }

                Hawk.put("fileName", fileListBeanList.get(i).getFile_name());*/
                    //   String fileData = httpDownloader.downloadFiles(fileListBeanList.get(i).getFile_path());
                    DownloadUtil.get().download(fileListBeanList.get(i).getFile_path(), fileStrPath, fileListBeanList.get(i).getFile_name(), new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            Log.v("dfsfff111", "????????????,???????????????????????????????????????????????????Download????????????");
                      /*  Looper.prepare();//????????????
                        Looper.loop();//????????????*/
                            Message msg = new Message();
                            fileListBean.setName(name);
                            fileListBean.setPath(fileStrPath + name);
                            fileListBean.set_id(fileId);
                            fileListBean.setC_id(c_id);
                            fileListBean.setUnclassified(unclassified);
                            fileListBean.setUser_id(userIdBean);
                            msg.obj = fileListBean;//???????????????????????????????????????????????????List???map??????
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                            Log.v("dfsfff222", "????????????,???????????????????????????????????????????????????Download????????????");
                        }

                        @Override
                        public void onDownloading(int progress) {
                            Log.v(TAG, "????????????" + progress);
//                progressDialog.setProgress(progress);
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }

                        }
                    });

            /*    DownloadUtil.get().download(fileListBeanList.get(i).getFile_path(), fileStrPath, fileListBeanList.get(i).getName() + ".doc",
                        new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                Message msg = new Message();
                                msg.obj = fileListBean;//???????????????????????????????????????????????????List???map??????
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                            }

                            @Override
                            public void onDownloading(int progress) {

                            }

                            @Override
                            public void onDownloadFailed(Exception e) {

                            }
                        });*/

            /*    FileDownloader.getImpl().create(fileListBean.getFile_path())
                        .setPath(fileStrPath + fileListBean.getFile_name(), false)
                        .setCallbackProgressTimes(300)
                        .setMinIntervalUpdateSpeed(400)
                        .setListener(new FileDownloadSampleListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                super.pending(task, soFarBytes, totalBytes);
                                Log.d("FileDownloader11", totalBytes + "");
                            }

                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                super.progress(task, soFarBytes, totalBytes);
                                Log.d("FileDownloader222", totalBytes + "-soFarBytes=" + soFarBytes);
                            }

                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {
                                super.error(task, e);
                                Log.d("FileDownloader3333", e.getMessage() + "");
                            }

                            @Override
                            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                                Log.d("FileDownloader444", totalBytes + "");
                            }

                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                super.paused(task, soFarBytes, totalBytes);
                                Log.d("FileDownloader555", totalBytes + "");
                            }

                            @Override
                            protected void completed(BaseDownloadTask task) {
                                super.completed(task);
                                Log.d("FileDownloader6666", task.getUrl() + "");


                                //  mHandler.sendEmptyMessage(0);
                                //???????????????????????????????????????
                                Message msg = new Message();
                                msg.obj = fileListBean;//???????????????????????????????????????????????????List???map??????
                                msg.what = 0;
                                mHandler.sendMessage(msg);

                            }

                            @Override
                            protected void warn(BaseDownloadTask task) {
                                super.warn(task);
                                Log.d("FileDownloader77777", task.getUrl() + "");
                            }
                        }).start();*/


                } else {
                    //??????????????????doc,excle,pdf,ppt???,??????????????????????????????????????????
                    fileBean = new NewFileBean.MeetingFileListBean(fileListBeanList.get(i).getFile_name(), fileListBeanList.get(i).getFile_path(), fileListBeanList.get(i).getUser_id().getName(), fileListBeanList.get(i).getUpload_time());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBean.setC_id(c_id);
                    fileBean.setUser_id(fileListBeanList.get(i).getUser_id());
                    fileBean.set_id(fileListBeanList.get(i).get_id());
                /*    fileListBean.setC_id(c_id);
                    fileListBean.set_id(fileListBeanList.get(i).get_id());*/
                    fileBean.setUnclassified(fileListBeanList.get(i).getUnclassified());
                    //  fileListBean.setUser_id(fileListBeanList.get(i).getUser_id());
                    fileOtherBeans.add(fileBean);
                }
            }
            if (fileOtherBeans != null) {
                Log.d("requestCodeUr666", fileOtherBeans.size() + "  ");
            }
            fileListAdapter = new PrivateFileListAdapter(getActivity(), fileOtherBeans);
            listView.setAdapter(fileListAdapter);
            fileListAdapter.notifyDataSetChanged();
            fileListAdapter.setOnPlayerClickInterface(this);
            fileListAdapter.setPrivateToPublicInterface(this);
            fileListAdapter.setDeleteInterface(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    NewFileBean.MeetingFileListBean fileBean = (NewFileBean.MeetingFileListBean) adapterView.getAdapter().getItem(i);
                    Log.d("requestCodeUr333", fileBean.getPath());
                    Intent intent;
                    switch (fileBean.getFile_type()) {
                        case "1"://??????
                            startPlayer(fileBean.getPath());
                            //getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
                            // FileUtils.openFile("file://"+fileBean.getPath(), getActivity());
                            break;
                        case "2"://??????
                            intent = new Intent();
                            intent.setClass(getActivity(), ActivityVideoView.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", true);
                            startActivity(intent);
                            //  FileUtils.openFile(fileBean.getPath(), getActivity());
                            break;
                        case "3"://??????
                            intent = new Intent();
                            intent.setClass(getActivity(), ActivityImage.class);
                            intent.putExtra("url", fileBean.getPath());
                            intent.putExtra("isOpenFile", true);
                            intent.putExtra("isNetFile", true);
                            startActivity(intent);
                            break;
                        case "4":
                            String wpsPackageName;
                            if (FLUtil.checkPackage(context, Define.PACKAGENAME_KING_PRO))
                            {
                                wpsPackageName = Define.PACKAGENAME_KING_PRO;
                            }
                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_PRO_DEBUG))
                            {
                                wpsPackageName = Define.PACKAGENAME_PRO_DEBUG;
                            }
                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_ENG))
                            {
                                wpsPackageName = Define.PACKAGENAME_ENG;
                            }
                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_KING_PRO_HW))
                            {
                                wpsPackageName = Define.PACKAGENAME_KING_PRO_HW;
                            }
                            else if (FLUtil.checkPackage(context, Define.PACKAGENAME_K_ENG))
                            {
                                wpsPackageName = Define.PACKAGENAME_K_ENG;
                            }
                            else {
                                ToastUtils.showToast(context,"??????????????????????????????WPS Office?????????");
                                return;
                            }
                            getActivity().startActivity(FileUtils.openFile(fileBean.getPath(), getActivity()));
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

    @Override
    public void onTransfor(String id, String userId) {
//????????????????????????
        FileTypeTransfor(id, userId);
    }

    //????????????
    @Override
    public void onDelte(String id, String c_id) {
        deleteFile(id, c_id);
    }

    public void FileTypeTransfor(String id, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", userId);
        map.put("unclassified", constant.FILE_Public);
        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().updateUnclassified(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onFail(BasicResponse response) {
                        Toast.makeText(getActivity(), response.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", fileType);
                        intent.putExtras(bundle);
                        intent.setAction(constant.FRESH_FILE);
                        getActivity().sendBroadcast(intent);
                        getData(fileType);

                    }
                });

    }

    public void deleteFile(String id, String c_id) {
        List<String> strings = new ArrayList<>();
        strings.add(id);
        Map<String, Object> map = new HashMap<>();
        map.put("ids", strings);
        map.put("c_id", c_id);
        //??????????????????
        NetWorkManager.getInstance().getNetWorkApiService().removeMeetingFile(map).compose(this.<BasicResponse>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse>() {
                    @Override
                    protected void onFail(BasicResponse response) {
                        Toast.makeText(getActivity(), response.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onSuccess(BasicResponse response) {
                        getData(fileType);

                    }
                });

    }

    private void startPlayer(String url) {
        if (url.isEmpty()) {
            return;
        }
        Log.d("requestCodeUrppppp", url);
        {
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
                // mediaPlayer.setLooping(true);
            }
        }
    }

    private void getDownload() {
        Log.d(TAG, "??????~~~~~8888");
        if (fileStrPath.isEmpty()) {
            return;
        }
        File path = new File(fileStrPath);
        File[] files = path.listFiles();// ??????
        if (files == null) {
            return;
        }
        getFileName(files);
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
                    fileBean = new NewFileBean.MeetingFileListBean(file.getName(), file.getPath(), userName, uploadTime);
                    Uri uri = Uri.fromFile(file);
                    Log.d("requestCodeUrppp", uri.getScheme() + "===" + uri.getPath() + "==" + file.getName());
                    fileBean.setResImage(getIamge(endStr));
                    fileBean.setFile_type(getType(endStr));
                    fileBeans.add(fileBean);
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
        } else if (end.equals("pdf")) {
            return "4";
        }
    else if (end.equals("txt")) {
        return "4";
    }else {
            return "0";
        }
    }

    /**
     * @url: ?????????????????????
     * @dest: ????????????????????????
     * @fileName: ?????????????????????????????????
     */
    //????????????
    private void downloadFile(String url, String dest, String fileName) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constant.FRESH_FILE);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
        // ??????socket??????
        EventBus.getDefault().register(this);
        return rootView;
    }
    /**
     *  ??????websocket ??????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
        Log.e("onReceiveMsg", TAG+"onReceiveMsg: " + message.toString());
        if(message.getMessage().contains(constant.RUSHFILELIST)){
            try {
                WSBean wsebean = new Gson().fromJson(message.getMessage(), WSBean.class);
                //  ??????vote???websocket?????????
                if(wsebean!=null){
                    if( wsebean.getPackType().equals(constant.RUSHFILELIST)){
                        getData(fileType);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("fileType");
            getData(type);

        }
    }
}
