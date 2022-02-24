package com.example.paperlessmeeting_demo.activity.Note;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanRequest;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.db.NoteDb;
import com.example.paperlessmeeting_demo.network.DefaultObserver;
import com.example.paperlessmeeting_demo.network.NetWorkManager;
import com.example.paperlessmeeting_demo.tool.FileUtil;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.orhanobut.hawk.Hawk;
import com.trello.rxlifecycle2.components.RxActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddContent extends RxActivity {
    private final int mActivityWindowWidth = ScreenUtils.getScreenWidth() * 2 / 3;
    private final int mActivityWindowHeight = ScreenUtils.getScreenHeight() / 2;
    private EditText mEt;
    private UploadBean uploadBean;
    private NoteDb mDb;
    private String filePath = Environment.getExternalStorageDirectory() + "/note/";
    private String upLoadFileType;
    private CreateFileBeanRequest createFileBeanRequest;
    private SQLiteDatabase mSqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_content);
        mEt = (EditText) this.findViewById(R.id.text);
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();

        mEt.setText(getIntent().getStringExtra(NoteDb.CONTENT));

    }

    public void save(View v) {
        DbAdd();
        finish();
    }

    public void upLoad(View v) {
        String fileName = "note.txt";
        if (FileUtil.writeTxtToFile(mEt.getText().toString(), filePath, fileName)) {
            String path = filePath + fileName;
            //把文件封装在RequestBody里
            Log.d("upLoad", path);
            File file = new File(path);
            if (file == null) {
                return;
            }

            upLoadFileType = "multipart/form-data";
            RequestBody requestBody = RequestBody.create(MediaType.parse(upLoadFileType), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            NetWorkManager.getInstance().getNetWorkApiService().upLoadFile("note", "", "", part).compose(this.<BasicResponse<UploadBean>>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<BasicResponse<UploadBean>>() {
                        @Override
                        protected void onSuccess(BasicResponse<UploadBean> response) {
                            if (response != null) {
                                //创建文件请求体类

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
                                createFileBeanRequest.setMeeting_record_id(_id);
                                List<CreateFileBeanRequest.FileListBean> fileListBeans = new ArrayList<>();
                                CreateFileBeanRequest.FileListBean fileListBean = new CreateFileBeanRequest.FileListBean();
                                fileListBean.setName(file.getName());
                                fileListBean.setPath(uploadBean.getUrl());
                                fileListBean.setSize("30");
                                fileListBean.setSuffix("txt");
                                fileListBean.setType("Document");
                                fileListBean.setUnclassified("0");
                                fileListBeans.add(fileListBean);
                                createFileBeanRequest.setFile_list(fileListBeans);
                                creatFile(createFileBeanRequest);
                            } else {
                                finish();
                            }

                        }

                        @Override
                        protected void onFail(BasicResponse response) {
                            super.onFail(response);
                            Toast.makeText(AddContent.this, "上传失败！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }


    }

    private void creatFile(CreateFileBeanRequest createFileBeanRequest) {

        NetWorkManager.getInstance().getNetWorkApiService().createMeetingFile(createFileBeanRequest).compose(this.<BasicResponse<CreateFileBeanResponse>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicResponse<CreateFileBeanResponse>>() {
                    @Override
                    protected void onSuccess(BasicResponse<CreateFileBeanResponse> response) {
                        if (response != null) {
                            Toast.makeText(AddContent.this, "上传成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    protected void onFail(BasicResponse<CreateFileBeanResponse> response) {
                        super.onFail(response);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        finish();
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


    public void cancle(View v) {
        mEt.setText("");
        finish();
    }

    public void DbAdd() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.ID, "0");
        cv.put(NoteDb.CONTENT, mEt.getText().toString());
        cv.put(NoteDb.TIME, getTime());

        try {
            mSqldb.replaceOrThrow(NoteDb.TABLE_NAME, null, cv);
        } catch (SQLException e) {
            e.getLocalizedMessage();
        }
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        final View view = getWindow().getDecorView();
        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        lp.width = mActivityWindowWidth;
        lp.height = mActivityWindowHeight;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserUtil.isFloating = false;
    }
}