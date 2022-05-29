package com.example.paperlessmeeting_demo.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.paperlessmeeting_demo.activity.Initialization.InitiaActivity;
import com.example.paperlessmeeting_demo.activity.LoginActivity;
import com.example.paperlessmeeting_demo.activity.MainActivity;
import com.example.paperlessmeeting_demo.activity.WuHuActivity;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.LoginBean;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.Md5Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.orhanobut.hawk.Hawk;
/**
 * @ProjectName: MyCutFileUpload
 * @Package: com.sun.mycutfileupload.utils
 * @ClassName: UploadFileUtils
 * @Author: littletree
 * @CreateDate: 2020/9/16/016 13:58
 */
public class UploadFileUtils {
    private String getfilepath;
    private String getfilename;
    private String getbustype;
    private Context mcontext;
    //  private String filemd5;

    private List<Integer> haveuploadfilenum;   //校验的返回的已上传的分片序号，已存在的分片无需再上传，可跳过循环

    private FileCutUtils fileCutUtils;  //文件切割工具类
    private int littlefilecount;  //切割文件个数
    private List<File> littlefilelist = new ArrayList<>();

    private OnUpLoadListener onUpLoadListener;

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private String getfilepath;
        private String name;
        private String getbustype;
        private Context mcontext;


        private List<Integer> haveuploadfilenum;

        private FileCutUtils fileCutUtils;  //文件切割工具类
        private int littlefilecount;  //切割文件个数
        private List<File> littlefilelist;

        private OnUpLoadListener onUpLoadListener;

        Builder(Context context) {
            this.mcontext = context;
            this.haveuploadfilenum = new ArrayList<>();
            this.littlefilelist = new ArrayList<>();
        }

        private UploadFileUtils build() {
            return new UploadFileUtils(this);
        }

        public Builder loadFile(String mfilepath, String mfilename) {
            this.getfilepath = mfilepath;
            this.name = mfilename;
            return this;
        }

        public Builder setBustype(String mbustype) {
            this.getbustype = mbustype;
            return this;
        }

        public Builder setUpLoadListener(OnUpLoadListener onUpLoadListener) {
            this.onUpLoadListener = onUpLoadListener;
            return this;
        }

        public void launch() {
            build().start();
        }
    }

    private UploadFileUtils(Builder builder) {
        this.getfilepath = builder.getfilepath;
        this.getfilename = builder.name;
        this.getbustype = builder.getbustype;
        this.mcontext = builder.mcontext;
        this.haveuploadfilenum = builder.haveuploadfilenum;
        this.fileCutUtils = builder.fileCutUtils;
        this.littlefilecount = builder.littlefilecount;
        this.littlefilelist = builder.littlefilelist;
        this.onUpLoadListener = builder.onUpLoadListener;
    }

    private void start() {
        if (null != onUpLoadListener) {
            onUpLoadListener.start();
        }
     /*   new Thread() {
            @Override
            public void run() {
                filemd5 = Md5Util.getFileMD5(getfilepath);   //文件转md5耗时操作，需要在子线程执行
                mHandler.sendEmptyMessage(0x123);
            }
        }.start();*/
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                // checkvideofile(getfilepath,getfilename,getbustype);
            }
        }
    };

    //秒传接口 校验文件
/*  private void checkvideofile(String mfilepath, String mfilename,String mbustype){
        EasyHttp.get("receiveChunk")
              //  .params("busType", mbustype)   //文件类型 固定为doc
              //  .params("checksum", filemd5)   //文件 md5加密
                .params("fileName", mfilename)   //文件名
             //   .params("size", ((int) FileSizeUtil.getFileOrFilesSize(mfilepath, FileSizeUtil.SIZETYPE_B)) + "")
                .execute(new SimpleCallBack<CheckFileEntity>() {
                    @Override
                    public void onError(ApiException e) {
                        Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(CheckFileEntity response) {
                        if (response.getData().isFileExists()){
                            if (null != onUpLoadListener) {
                                onUpLoadListener.onComplete(response.getData().getSourceId());
                            }
                        }else {
                            haveuploadfilenum = new ArrayList<>();
                            if (null!=response.getData().getChunkList()&&response.getData().getChunkList().size()>0){
                                haveuploadfilenum = response.getData().getChunkList();
                            }
                            cutfile(mfilepath);
                        }

                    }
                });
    }*/

    private void cutfile(String filePath, String fileName) {
        try {
            long mBufferSize = 1024 * 1024 * 2; //分片的大小，可自定义
            fileCutUtils = new FileCutUtils();
            littlefilecount = fileCutUtils.getSplitFile(new File(filePath), mBufferSize);
            littlefilelist = fileCutUtils.getLittlefilelist();
            uploadFile(0, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(int filenum, String getfilename) {
        boolean isRepeat = false;
        if (null != haveuploadfilenum && haveuploadfilenum.size() > 0) {   //之前传过的num就不用传
            for (int uploadfilenum : haveuploadfilenum) {
                if (filenum == uploadfilenum) {
                    isRepeat = true;
                }
            }
        }


        if (isRepeat && null != haveuploadfilenum && haveuploadfilenum.size() > 0) {
            uploadFile(filenum + 1, getfilename);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("hash", filenum);
            map.put("fileName", getfilename);
        }
    }

    public interface OnUpLoadListener {
        void start();

        void onUpload(int currentnum, int allnum);

        void onComplete(int filesourceId);

        void onUploadFailed(String errormessage);
    }

    public OnUpLoadListener getOnUpLoadListener() {
        return onUpLoadListener;
    }

    public void setOnUpLoadListener(OnUpLoadListener onUpLoadListener) {
        this.onUpLoadListener = onUpLoadListener;
    }
}
