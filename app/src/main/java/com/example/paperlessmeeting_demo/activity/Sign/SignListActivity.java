package com.example.paperlessmeeting_demo.activity.Sign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.activity.Sign.Adapter_holder.SignViewRecyclerViewAdapter;
import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignThumbBean;
import com.example.paperlessmeeting_demo.activity.Sign.Bean.SignViewBean;
import com.example.paperlessmeeting_demo.activity.Sign.CallBack.OnRecyclerItemClickListener;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.fragment.WuHUVoteListFragment;
import com.example.paperlessmeeting_demo.tool.CVIPaperDialogUtils;
import com.example.paperlessmeeting_demo.tool.ToastUtils;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.example.paperlessmeeting_demo.util.SysUtils;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
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
    RelativeLayout ivBack;
    @BindView(R.id.sign_bg)
    ImageView signBg;
    @BindView(R.id.root_linear)
    LinearLayout mLinear;
    @BindView(R.id.content_rl)
    RelativeLayout contentRl;
    @BindView(R.id.sign_delete)
    TextView sign_delete;
    @BindView(R.id.sign_sure)
    TextView sign_sure;
    @BindView(R.id.no_data)
    TextView noData;

    private SignListActivity.MyBroadcastReceiver myBroadcastReceiver;
    private List<SignViewBean> mDatas = new ArrayList<>();
    private List<SignViewRecyclerViewAdapter> adapterList = new ArrayList<>();
    private List<View> recycleViewList = new ArrayList<>();
    private int fileCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_list;
    }

    @Override
    protected void initView() {
        myBroadcastReceiver = new SignListActivity.MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.RUSH_SIGN_LIST_BROADCAST);
        registerReceiver(myBroadcastReceiver,filter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sign_delete.setSelected(false);
        sign_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_delete.setSelected(!sign_delete.isSelected());
                boolean isShow = sign_delete.isSelected();
                String text = isShow ? "取消" : "删除批注";
                sign_delete.setText(text);
                for (SignViewRecyclerViewAdapter adapter : adapterList) {
                    adapter.showDeletImg(isShow);
                }
                int vis = isShow ? View.VISIBLE : View.GONE;
                sign_sure.setVisibility(vis);
            }
        });
        sign_sure.setVisibility(View.GONE);
        sign_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isHaveDataSel = true;
                for (int i = 0; i < adapterList.size(); i++) {
                    SignViewRecyclerViewAdapter adapter = adapterList.get(i);
                    if (adapter.getSelectedItem().size() > 0) {
                        isHaveDataSel = false;
                    }
                }
                if (isHaveDataSel) {
                    CVIPaperDialogUtils.showConfirmDialog(SignListActivity.this, "当前没有选择要删除的数据", "知道了", false, null);
                    return;
                }
                CVIPaperDialogUtils.showCustomDialog(SignListActivity.this, "确定要删除选中的数据吗?", "", "确定", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                    @Override
                    public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                        if (clickCancel) {
                            return;
                        }
                        // 记录需要移除的adapter,recycleViewList 的下标
                        List<SignViewBean> removeSignBeans = new ArrayList<>();
                        List<SignViewRecyclerViewAdapter> removeAdapters= new ArrayList<>();
                        List<View> removerecycles = new ArrayList<>();

                        for (int i = 0; i < adapterList.size(); i++) {
                            SignViewRecyclerViewAdapter adapter = adapterList.get(i);
                            final SignViewBean signViewBean = mDatas.get(i);

                            List<SignThumbBean> indexList = adapter.getSelectedItem();
                            if (indexList.size() == 0) {
                                // 当前没有选中
                                continue;
                            }

                            for (SignThumbBean signThumbBean : indexList) {
//                                Log.e("222222","当前选中--"+i+"行,第几条数据---"+signViewBean.getListDatas().indexOf(signThumbBean));
                                File file = new File(signThumbBean.getPath());
                                if (file.delete()) {
                                    signViewBean.getListDatas().remove(signThumbBean);

                                    List<String> signFilePaths = null;
                                    if (Hawk.contains(constant.SignFilePath) ) {
                                        signFilePaths = Hawk.get(constant.SignFilePath);
                                        if(signFilePaths != null && signFilePaths.contains(signThumbBean.getPath())){
                                            signFilePaths.remove(signThumbBean.getPath());
                                            Hawk.put(constant.SignFilePath,signFilePaths);
                                        }
                                    }

                                }
                            }
                            adapter.setDatas(signViewBean.getListDatas());
                            if (signViewBean.getListDatas().size() == 0) {
                                //移除名字显示，整体layout
                                mLinear.removeView(recycleViewList.get(i));
                                removeSignBeans.add(signViewBean);
                                removeAdapters.add(adapter);
                                removerecycles.add(recycleViewList.get(i));

                            }else {
                                mDatas.remove(i);
                                mDatas.add(i, signViewBean);
                            }
                        }
                        // 清除数据,按下标移除会越界,笨办法
                        for (SignViewBean signViewBean : removeSignBeans){
                            mDatas.remove(signViewBean);

                        }
                        for (SignViewRecyclerViewAdapter adapter : removeAdapters){
                            adapterList.remove(adapter);
                        }
                        for (View view : removerecycles){
                            recycleViewList.remove(view);
                        }
//                        for (int index : integers) {
//                            // 按下标移除会越界
//                            mDatas.remove(index);
//                            adapterList.remove(index);
//                            recycleViewList.remove(index);
//                        }
                        // 取消选中状态
                        for(SignViewRecyclerViewAdapter adapter : adapterList){
                            adapter.cancelSelectAll();
                        }
                        // 处理无数据页面
                        if (mDatas.size() == 0) {
                            noData.setVisibility(View.VISIBLE);
                        }
                        sign_delete.performClick();
                    }
                });
            }
        });


    }

    // 重新加载数据
    private void refreshData() {
        mLinear.removeAllViews();
        data.clear();
        mDatas.clear();
        adapterList.clear();
        recycleViewList.clear();
        initData();
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
        if(fileCount==0){
            noData.setVisibility(View.VISIBLE);
        }
//        Log.e("xxx","count1111 ====="+fileCount);
        SignViewBean signViewBean = new SignViewBean();
        GetFilePath(signViewBean, appDir, 0);

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 12345:
                    List<String> signFilePaths = null;
                    if (Hawk.contains(constant.SignFilePath)) {
                        signFilePaths = Hawk.get(constant.SignFilePath);
                    }
                    // 全部加载完毕,处理子文件夹下没有文件的数据
                    List<SignViewBean> data = (List<SignViewBean>) msg.obj;
                    for (SignViewBean signViewBean : data) {
                        SignViewBean newSignViewBean = signViewBean; // 空对象添加匹配过的数据用

                        if (signViewBean.getListDatas().size() != 0) {
//                            mDatas.add(signViewBean); //原来只要不是空文件夹就直接加进去展示即可
                            List<SignThumbBean> signThumbBeanList = signViewBean.getListDatas();

                            List<SignThumbBean> NewSignThumbBeanList = new ArrayList<>(); // 空list添加匹配过的数据用

                            // 以下 for循环 签批文件区分不同议题 2022.09.15添加
                            for (SignThumbBean signThumbBean : signThumbBeanList){
                                if(signFilePaths!=null){
                                    //TODO 该文件包含在此场会议里面，需要加到mDatas里面展示
                                    if(signFilePaths.contains(signThumbBean.getPath())){
                                        NewSignThumbBeanList.add(signThumbBean);
                                    }
                                }

                            }
                            if(NewSignThumbBeanList.size()>0){
                                newSignViewBean.setListDatas(NewSignThumbBeanList);
                                mDatas.add(newSignViewBean);
                            }

                        }
                    }
                    init(mDatas);
                    break;
            }
            return false;
        }
    });

    /**
     * 获取文件夹下指定文件的个数
     * 注意不能直接用file.listFiles() ，不会遍历子文件夹
     */
    private int getFilsSize(File file) {
        File[] tempList = file.listFiles();
        int count = 0;
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String fileName = tempList[i].getName();
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                    count++;
                }
            } else if (tempList[i].isDirectory()) {
                count += getFilsSize(tempList[i]);
            }
        }
        return count;
    }


    private static int count = 0;
    private static List<SignViewBean> data = new ArrayList<>();

    private void GetFilePath(final SignViewBean signViewBean, File file, final int flag) {
        count = 0;
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {// 文件
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".png") || name.equalsIgnoreCase(".jpg")) {
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

                                if (count == fileCount) {
                                    handler.sendMessage(handler.obtainMessage(12345, data));
                                }
                            }
                        });
                        return true;
                    }
                } else if (file.isDirectory()) {//如果是文件夹
                    Log.d("xxx", "这是文件夹");
                    //  指定文件夹下还有子目录，直接返回
                    if (flag == 1) {
                        return false;
                    }
                    //继续递归搜索子目录
                    SignViewBean signViewBean = new SignViewBean();
                    signViewBean.setSignAuthor(name);
                    signViewBean.setListDatas(new ArrayList<SignThumbBean>());
                    if (name.equals(UserUtil.user_name)) {
                        data.add(0, signViewBean);
                    } else {
                        data.add(signViewBean);
                    }
                    GetFilePath(signViewBean, file, 1);
                }
                return false;
            }
        });
//        return data;
    }

    private void init(List<SignViewBean> data) {
        if (data.size() == 0) {
            noData.setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            final SignViewBean signViewBean = data.get(i);
            if (signViewBean.getListDatas().size() == 0) {
                continue;
            }
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = (View) inflater.inflate(R.layout.sample_my_recycle_view, null);
            int id = 100 + i;
            layout.setId(id);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.addRule(RelativeLayout.BELOW, id - 1);
            }
//            params.setMargins(0, 0, 0, 0);

            RecyclerView recyclerView = layout.getRootView().findViewById(R.id.main_recycleview);
            TextView textView = layout.getRootView().findViewById(R.id.re_text);

            textView.setText(signViewBean.getSignAuthor());
            recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
            SignViewRecyclerViewAdapter adapter = new SignViewRecyclerViewAdapter(this, signViewBean.getListDatas(), i);
            adapterList.add(adapter);
            recycleViewList.add(layout);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onRecyclerItemClick(int adapterIndex, int position) {
                    SignThumbBean signThumbBean = signViewBean.getListDatas().get(position);
                    File file = new File(signThumbBean.getPath());
                    FileInputStream fis = null;
                    Bitmap mBitmap = null;
                    try {
                        fis = new FileInputStream(file.getPath());
                        mBitmap = BitmapFactory.decodeStream(fis);
                        MyImageDialog myImageDialog = new MyImageDialog(SignListActivity.this, R.style.mypopwindow_anim_style, 0, -300, mBitmap);
                        myImageDialog.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("xxx", "getCreatTime==" + signThumbBean.getCreatTime());
                }

                @Override
                public void onRecyclerDeletClick(int adapterIndex, int position) {
                    CVIPaperDialogUtils.showCustomDialog(SignListActivity.this, "确定要删除吗？", "", "确定", false, new CVIPaperDialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onClickButton(boolean clickConfirm, boolean clickCancel) {
                            if (clickConfirm) {
                                SignThumbBean signThumbBean = signViewBean.getListDatas().get(position);
                                File file = new File(signThumbBean.getPath());
                                if (file.delete()) {
                                    ToastUtils.showShort("删除成功!");
                                    signViewBean.getListDatas().remove(position);

                                    adapter.notifyDataSetChanged();
                                    if (signViewBean.getListDatas().size() == 0) {
                                        //移除名字显示，整体layout
                                        mLinear.removeView(layout);
                                    }
//                                    sign_delete.performClick();
                                } else {
                                    ToastUtils.showShort("删除失败!");
                                }
                            }
                        }
                    });
                }
            });
            recyclerView.setAdapter(adapter);
            if(mLinear!=null){
                mLinear.addView(layout, params);
            }

        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent in) {
            if (in.getAction().equals(constant.RUSH_SIGN_LIST_BROADCAST) && UserUtil.ISCHAIRMAN){
                refreshData();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver!=null){
            unregisterReceiver(myBroadcastReceiver);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
