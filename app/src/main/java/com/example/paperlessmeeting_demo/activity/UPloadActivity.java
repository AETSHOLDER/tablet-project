/*
package com.example.paperlessmeeting_demo.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import com.example.paperlessmeeting_demo.bean.MeetingIdBean;
import com.example.paperlessmeeting_demo.bean.MergeChunkBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBeanRequset;
import com.example.paperlessmeeting_demo.bean.WuHuMeetingListResponse;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.FileCutUtils;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UPloadActivity extends BaseActivity {
    @BindView(R.id.upload_tv)
    TextView upload_tv;
    SVProgressHUD mSvp;
    private List<Integer> haveuploadfilenum;   //校验的返回的已上传的分片序号，已存在的分片无需再上传，可跳过循环

    private int littlefilecount;  //切割文件个数
    private List<File> littlefilelist = new ArrayList<>();
    private String endStrAll;
    private String path = "/storage/emulated/0/DCIM/Camera/VID_20220426_154506.mp4";
    private String videoname = "VID_20220426_154506.mp4";
    private List<Integer> integers = new ArrayList<>();
    private List<WuHuEditBean.EditListBean> wuHuEditBeanList = new ArrayList<>();
    private List<WuHuEditBean.EditListBean.FileListBean> fileListBeans = new ArrayList<>();
    private int upLoadNum = 0;//每个文件的切片数
    private int allFileNum = 0;//总文件
    private int upLoadFileNum = 0;
    private int size = 1024 * 1024 * 10;
    private String upLoadFileType;
    private FileCutUtils fileCutUtils;  //文件切割工具类
    private boolean isFinish = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 133:
                    littlefilelist.clear();
                    List<File> filelist = (List<File>) msg.obj;
                    littlefilelist.addAll(filelist);
                    Log.d("gtgwrtwwrtwt大文件分片大小", "一共： " + littlefilelist.size());
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.upload_file_activity;
    }

    @Override
    protected void initView() {

        Map<String, Object> map = new HashMap<>();
        map.put("hash", path);
        map.put("fileName", videoname);
        upload_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wuHuFinishMeeting();//创建会议拿到会议ID;

            }
        });
    }

    private void uploadFile() {
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBeanList.clear();
            WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
            wuHuEditBeanList = wuHuEditBean.getEditListBeanList();
            if (wuHuEditBeanList == null || wuHuEditBeanList.size() == 0) {
                return;
            }
            List<WuHuEditBean.EditListBean> ets = new ArrayList<>();
            ets.clear();
            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);
                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getLocalFiles() != null && editListBean.getLocalFiles().size() > 0) {
                    fileListBeanList.addAll(editListBean.getLocalFiles());
                    editListBean.setFileListBeanList(fileListBeanList);//每个议题下的本地文件复制到网络文件中去
                    ets.add(editListBean);//本地文件和网络文件都有的议题集合
                }
            }
            wuHuEditBean.setEditListBeanList(ets);
            Hawk.put("WuHuFragmentData", wuHuEditBean);

            for (int i = 0; i < wuHuEditBeanList.size(); i++) {
                WuHuEditBean.EditListBean editListBean = wuHuEditBeanList.get(i);
                List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                fileListBeanList.clear();
                if (editListBean.getLocalFiles() != null && editListBean.getLocalFiles().size() > 0) {
                    fileListBeanList.addAll(editListBean.getLocalFiles());
                    editListBean.setFileListBeanList(fileListBeanList);//每个议题下的本地文件复制到网络文件中去
                    for (int k = 0; k < fileListBeanList.size(); k++) {

                        allFileNum++;
                        WuHuEditBean.EditListBean.FileListBean fileListBean = fileListBeanList.get(k);
                        if (!fileListBean.isNet()) {
                            //只上传本地文件
                            */
/* synchronized (UserUtil.object2) {*//*

                            cutfile(fileListBean.getPath(), fileListBean.getName());
                            upload(fileListBean.getName(), fileListBean.getPos());
*/
/*
                            }*//*

                        }

                    }

                }

            }

        }

    }

    private void upload(String fileName, String pos) {
        // isFinish=false;
        upLoadNum = 0;
        Log.d("gtgwrtwwrtwt大文件上传分片33", "议题：  " + pos + "切片数：" + littlefilelist.size());
        for (int i = 0; i < littlefilelist.size(); i++) {
            upLoadFileType = getMimeType(littlefilelist.get(i).getName());
            endStrAll = littlefilelist.get(i).getName().substring(littlefilelist.get(i).getName().lastIndexOf(".") + 1);
            RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), fileName);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileName + "/" + i, requestBody);
            NetWorkManager.getInstance().getNetWorkApiService().receiveChunk(part).compose(this.<BasicResponse>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse>() {
                        @Override
                        protected void onFail(BasicResponse response) {
                            super.onFail(response);
                            upLoadNum++;
                            Log.d("gtgwrtwwrtwt大文件上传分片111", "上传路过   upLoadNum" + upLoadNum);
                            mergeShards(fileName, pos);
                        }

                        @Override
                        protected void onSuccess(BasicResponse response) {
                            upLoadNum++;
                            Log.d("gtgwrtwwrtwt大文件上传分片222", "上传路过   upLoadNum" + upLoadNum);
                            mergeShards(fileName, pos);
                        }
                    });
        }

    }

    private void mergeShards(String fileName, String pos) {
  */
/*      try {
            Thread.sleep(1000*60);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*


        if (upLoadNum == littlefilelist.size()) {
            upLoadNum = 0;
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("dirName", UserUtil.meeting_record_id);
            map.put("size", size);
            //  map.put("updateFileList", 1);
            map.put("index", Integer.valueOf(pos));
            NetWorkManager.getInstance().getNetWorkApiService().mergeChunk(map).compose(this.<BasicResponse<MergeChunkBean>>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<MergeChunkBean>>() {
                        @Override
                        protected void onFail(BasicResponse<MergeChunkBean> response) {
                            super.onFail(response);
                            upLoadFileNum++;
                            if (allFileNum == upLoadFileNum) {
                                reSetdata();
                            }
                            Log.d("fgfhgdherete", "总文件： " + allFileNum + "     执行过上传的数量：" + upLoadFileNum);
                        }

                        @Override
                        protected void onSuccess(BasicResponse<MergeChunkBean> response) {
                            if (response != null) {
                                upLoadFileNum++;
                                MergeChunkBean mergeChunkBean = response.getData();
                                if (Hawk.contains("WuHuFragmentData")) {
                                    List<WuHuEditBean.EditListBean> beanList = new ArrayList<>();
                                    beanList.clear();
                                    WuHuEditBean wuHuEditBean = Hawk.get("WuHuFragmentData");
                                    beanList.addAll(wuHuEditBean.getEditListBeanList());
                                    if (beanList == null || beanList.size() == 0) {
                                        return;
                                    }
                                    for (int i = 0; i < beanList.size(); i++) {
                                        WuHuEditBean.EditListBean editListBean = beanList.get(i);
                                        if (String.valueOf(mergeChunkBean.getIndex()).equals(editListBean.getPos())) {
                                            List<WuHuEditBean.EditListBean.FileListBean> fileListBeanList = new ArrayList<>();
                                            fileListBeanList.clear();
                                            if (editListBean.getFileListBeanList() != null & editListBean.getFileListBeanList().size() > 0) {
                                                fileListBeanList.addAll(editListBean.getFileListBeanList());
                                                for (int k = 0; k < fileListBeanList.size(); k++) {
                                                    fileListBeanList.get(k).setNet(true);
                                                    fileListBeanList.get(k).setPath(mergeChunkBean.getUrl());
                                                }
                                                editListBean.setFileListBeanList(fileListBeanList);
                                            }
                                        }
                                    }
                                    wuHuEditBean.setEditListBeanList(beanList);
                                    Hawk.put("WuHuFragmentData", wuHuEditBean);
                                }
                                if (allFileNum == upLoadFileNum) {
                                    reSetdata();
                                }
                                //  isFinish=true;
                                Log.d("fgfhgdherete", "总文件： " + allFileNum + "     执行过上传的数量：" + upLoadFileNum);
                            }
                        }
                    });
        }
        upLoadNum = 0;

    }


    private void reSetdata() {

        WuHuEditBean wuHuEditBean = null;
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBean = Hawk.get("WuHuFragmentData");
            String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//会议-年
            wuHuEditBean.setStartTime(data);
        }
        WuHuEditBeanRequset wuHuEditBeanRequset = new WuHuEditBeanRequset();
        wuHuEditBeanRequset.setContent(wuHuEditBean);
        wuHuEditBeanRequset.setId(UserUtil.meeting_record_id);
        String conten = new Gson().toJson(wuHuEditBeanRequset);

        NetWorkManager.getInstance().getNetWorkApiService().meeting(wuHuEditBeanRequset).compose(this.<BasicResponse<MeetingIdBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MeetingIdBean>>() {
                    @Override
                    protected void onFail(BasicResponse<MeetingIdBean> response) {
                        super.onFail(response);
                        Log.d("gtgwrtwwrtwt创建会议", "创建会议失败");
                    }

                    @Override
                    protected void onSuccess(BasicResponse<MeetingIdBean> response) {
                        Log.d("gtgwrtwwrtwt修改成功", "创建会议成功11111");
                        if (response != null) {
                            MeetingIdBean meetingIdBean = response.getData();
                            allFileNum=0;
                            upLoadFileNum=0;
                           */
/*  UserUtil.meeting_record_id=meetingIdBean.getId();
                             //开始上传文件
                             uploadFile();*//*


                        }
                    }
                });

    }

    private void wuHuFinishMeeting() {

        WuHuEditBean wuHuEditBean = null;
        if (Hawk.contains("WuHuFragmentData")) {
            wuHuEditBean = Hawk.get("WuHuFragmentData");
            String data = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.DATA_FORMAT_NO_HOURS_DATA7);//会议-年
            wuHuEditBean.setStartTime(data);
        }
        WuHuEditBeanRequset wuHuEditBeanRequset = new WuHuEditBeanRequset();
        wuHuEditBeanRequset.setContent(wuHuEditBean);

        NetWorkManager.getInstance().getNetWorkApiService().meeting(wuHuEditBeanRequset).compose(this.<BasicResponse<MeetingIdBean>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<MeetingIdBean>>() {
                    @Override
                    protected void onFail(BasicResponse<MeetingIdBean> response) {
                        super.onFail(response);
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

    */
/**
     * 获取文件MimeType
     *
     * @param filename 文件名
     * @return
     *//*

    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "multipart/form-data"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    private void cutfile(String filePath, String fileName) {
        try {
            long mBufferSize = size; //分片的大小，可自定义
            fileCutUtils = new FileCutUtils();
//            littlefilecount = 0;
//            littlefilelist.clear();
            littlefilecount = fileCutUtils.getSplitFile(new File(filePath), mBufferSize);
            littlefilelist = fileCutUtils.getLittlefilelist();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    */
/**
     * 显示加载框
     *//*

    public void showTip(String txt) {
        Log.i("孙", "显示框: " + txt);
        if (null == mSvp) {
            mSvp = new SVProgressHUD(this);
        }
        if (!mSvp.isShowing()) {
            mSvp.showWithStatus("上传中");
        } else {
            mSvp.setText(txt);
        }
    }

    */
/**
     * 隐藏加载框
     *//*

    public void dismissTip() {
        if (null == mSvp) {
            mSvp = new SVProgressHUD(this);
        }

        if (mSvp.isShowing()) {
            mSvp.dismiss();
        }
    }
}
*/
