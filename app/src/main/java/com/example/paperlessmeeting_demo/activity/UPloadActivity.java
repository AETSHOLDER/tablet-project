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

import com.example.paperlessmeeting_demo.bean.MergeChunkBean;
import com.orhanobut.hawk.Hawk;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.FileCutUtils;
import com.example.paperlessmeeting_demo.tool.UploadFileUtils;
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
    private String upLoadFileType;
    private FileCutUtils fileCutUtils;  //文件切割工具类
    private int littlefilecount;  //切割文件个数
    private List<File> littlefilelist = new ArrayList<>();
    private String endStrAll;
    private UploadFileUtils.OnUpLoadListener onUpLoadListener;
    private String path = "/storage/emulated/0/DCIM/Camera/VID_20220426_154506.mp4";
    private String videoname = "VID_20220426_154506.mp4";
    //   private int filenum = 0;
    private List<Integer> integers = new ArrayList<>();
    private int sucess = 0;
    private int i = 0;
    private int k = 0;
    private int pos = 0;
    private int fail = 0;
    private int size = 1024 * 1024 * 100;

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
                cutfile(path, videoname);
                upload();
            }
        });

    }

    private void upload() {
        sucess = 0;
        integers.clear();
        pos = 0;
        fail = 0;
        Log.d("gtgwrtwwrtwt大文件分片：", littlefilelist.size() + "");
        for (int i = 0; i < littlefilelist.size(); i++) {
            pos = i;
            upLoadFileType = getMimeType(littlefilelist.get(i).getName());
            endStrAll = littlefilelist.get(i).getName().substring(littlefilelist.get(i).getName().lastIndexOf(".") + 1);

            if (sucess < littlefilelist.size()) {
                for (int k = 0; k < integers.size(); k++) {
                    fail = k;
                    RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), littlefilelist.get(integers.get(k)));
                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", littlefilelist.get(integers.get(k)).getName() + "/" + integers.get(k), requestBody);
                    NetWorkManager.getInstance().getNetWorkApiService().receiveChunk(part).compose(this.<BasicResponse>bindToLifecycle())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<BasicResponse>() {
                                @Override
                                protected void onFail(BasicResponse response) {
                                    super.onFail(response);
                                    integers.add(fail);
                                }

                                @Override
                                protected void onSuccess(BasicResponse response) {
                                    if (response != null) {
                                        sucess++;
                                        Log.d("gtgwrtwwrtwt大文件上传", "上传路过");
                                        mergeShards();
                                    }
                                }
                            });
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), littlefilelist.get(i));
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", littlefilelist.get(i).getName() + "/" + i, requestBody);
                NetWorkManager.getInstance().getNetWorkApiService().receiveChunk(part).compose(this.<BasicResponse>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<BasicResponse>() {
                            @Override
                            protected void onFail(BasicResponse response) {
                                super.onFail(response);
                                integers.add(pos);
                            }

                            @Override
                            protected void onSuccess(BasicResponse response) {
                                if (response != null) {
                                    sucess++;
                                    mergeShards();
                                    Log.d("gtgwrtwwrtwt大文件上传", "上传路过   " + sucess);
                                }
                            }
                        });

            }


        }


    }

    private void mergeShards(){

        if (sucess == littlefilelist.size()) {
            Log.d("gtgwrtwwrtwt大文件上传", "合片");
            Toast.makeText(UPloadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", videoname);
            map.put("dirName", "1");
            map.put("size", size);
            map.put("updateFileList", 1);
            map.put("index", 1);
            NetWorkManager.getInstance().getNetWorkApiService().mergeChunk(map).compose(this.<BasicResponse<MergeChunkBean>>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<MergeChunkBean>>() {
                        @Override
                        protected void onFail(BasicResponse<MergeChunkBean> response) {
                            super.onFail(response);
                            Log.d("gtgwrtwwrtwt大文件上传", "上传失败");
                        }

                        @Override
                        protected void onSuccess(BasicResponse response) {
                            if (response != null) {

                                Log.d("gtgwrtwwrtwt大文件上传", "上传成功");
                            }
                        }
                    });
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

    private void cutfile(String filePath, String fileName) {
        try {
            long mBufferSize = size; //分片的大小，可自定义
            fileCutUtils = new FileCutUtils();
            littlefilecount = fileCutUtils.getSplitFile(new File(filePath), mBufferSize);
            littlefilelist = fileCutUtils.getLittlefilelist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    /**
     * 显示加载框
     */
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

    /**
     * 隐藏加载框
     */
    public void dismissTip() {
        if (null == mSvp) {
            mSvp = new SVProgressHUD(this);
        }

        if (mSvp.isShowing()) {
            mSvp.dismiss();
        }
    }
}
