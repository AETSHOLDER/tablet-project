package com.example.paperlessmeeting_demo.activity.Sign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Sign.Adapter_holder.SignViewRecyclerViewAdapter;
import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignThumbBean;
import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignViewBean;
import com.example.paperlessmeeting_demo.activity.Sign.CallBack.OnRecyclerItemClickListener;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.util.SysUtils;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 查阅签批
 */
public class SignListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.sign_bg)
    ImageView signBg;
    @BindView(R.id.root_linear)
    LinearLayout mLinear;
    @BindView(R.id.content_rl)
    RelativeLayout contentRl;
    private SignViewRecyclerViewAdapter adapter;
    private int fileCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_list;
    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        data.clear();
        List<String> paths = new ArrayList<String>();
        File appDir = new File(Environment.getExternalStorageDirectory(), "无纸化会议");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        fileCount = getFilsSize(appDir);
//        Log.e("xxx","count1111 ====="+fileCount);
        SignViewBean signViewBean = new SignViewBean();
        GetFilePath(signViewBean,appDir,0);

    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 12345:
                    // 全部加载完毕
                    List<SignViewBean> data = (List<SignViewBean>) msg.obj;
                    init(data);
                    break;
            }
            return false;
        }
    });
    /**
     * 获取文件夹下指定文件的个数
     * 注意不能直接用file.listFiles() ，不会遍历子文件夹
     * */
    private int getFilsSize(File file) {
        File[] tempList = file.listFiles();
        int count = 0;
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String fileName = tempList[i].getName();
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    count++;
                }
            }else if(tempList[i].isDirectory()){
                count += getFilsSize(tempList[i]);
            }
        }
        return count;
    }


    private static int count = 0;
    private static List<SignViewBean> data = new ArrayList<>();
    private void GetFilePath(final SignViewBean signViewBean, File file, final int flag) {
        count=0;
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {// 文件
                    name = name.substring(i);
                    if ( name.equalsIgnoreCase(".png") || name.equalsIgnoreCase(".jpg") ){
                        //得到文件路径
                        String file_path = file.getAbsolutePath();
                        Point point = new Point();
                        point.set(SysUtils.PICTURE_WIDTH_DEFAULT, SysUtils.PICTURE_HEIGHT_DEFAULT);
                        NativeImageLoader.getInstance().loadNativeImage(file_path, point, new NativeImageLoader.NativeImageCallBack() {
                            @Override
                            public void onImageLoader(SignThumbBean signThumbBean, String path) {
                                List<SignThumbBean> listDatas = signViewBean.getListDatas();
                                count++;
                                listDatas.add(signThumbBean);
                                signViewBean.setListDatas(listDatas);

                                if(count==fileCount){
                                    handler.sendMessage(handler.obtainMessage(12345, data));
                                }
                            }
                        });
                        return true;
                    }
                }else if(file.isDirectory()){//如果是文件夹
                    Log.d("xxx","这是文件夹");
                    //  指定文件夹下还有子目录，直接返回
                    if(flag==1){
                        return false;
                    }
                    //继续递归搜索子目录
                    SignViewBean signViewBean = new SignViewBean();
                    signViewBean.setSignAuthor(name);
                    signViewBean.setListDatas(new ArrayList<SignThumbBean>());
                    if(name.equals(UserUtil.user_name)){
                        data.add(0,signViewBean);
                    }else {
                        data.add(signViewBean);
                    }
                    GetFilePath(signViewBean,file,1);
                }
                return false;
            }
        });
//        return data;
    }

    private void init(List<SignViewBean> data){
        for (int i=0;i<data.size();i++){
            final SignViewBean signViewBean = data.get(i);
            LayoutInflater inflater= LayoutInflater.from(this);
            View layout = (View) inflater.inflate(R.layout.sample_my_recycle_view, null);
            int id = 100+i;
            layout.setId(id );
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i != 0){
                params.addRule(RelativeLayout.BELOW, id-1);
            }
//            params.setMargins(0, 0, 0, 0);

            RecyclerView recyclerView = layout.getRootView().findViewById(R.id.main_recycleview);
            TextView textView = layout.getRootView().findViewById(R.id.re_text);

            textView.setText(signViewBean.getSignAuthor());
            recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            adapter = new SignViewRecyclerViewAdapter(this, signViewBean.getListDatas());
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onRecyclerItemClick(int position) {
                    SignThumbBean signThumbBean = signViewBean.getListDatas().get(position);
                    File file = new File(signThumbBean.getPath());
                    FileInputStream fis = null;
                    Bitmap mBitmap = null;
                    try {
                        fis = new FileInputStream(file.getPath());
                        mBitmap = BitmapFactory.decodeStream(fis);
                        MyImageDialog myImageDialog = new MyImageDialog(SignListActivity.this,R.style.mypopwindow_anim_style,0,-300,mBitmap);
                        myImageDialog.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("xxx","getCreatTime=="+signThumbBean.getCreatTime());
                }
            });
            recyclerView.setAdapter(adapter);
            mLinear.addView(layout,params);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
