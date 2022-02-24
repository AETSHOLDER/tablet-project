package com.example.paperlessmeeting_demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * describe：功能列表
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class FileListActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    private String filePath;
    private List<String> datas = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_list;
    }

    @Override
    protected void initView() {

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
        copy();
        initDatas();
        initPaths();

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RecyclerView.ViewHolder(LayoutInflater.from(FileListActivity.this).inflate(R.layout.item_function, parent, false)) {
                };

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                holder.itemView.findViewById(R.id.bt_function).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filePath = getFilePath(position);
                     //   FileDisplayActivity.show(FileListActivity.this, filePath);
                    }
                });
                ((Button) holder.itemView.findViewById(R.id.bt_function)).setText(getDatas().get(position));
            }

            @Override
            public int getItemCount() {
                return getDatas().size();
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initPaths() {
    }

    private void initDatas() {
        datas.add("打开本地doc文件");

        datas.add("打开本地txt文件");

        datas.add("打开本地excel文件");

        datas.add("打开本地ppt文件");

        datas.add("打开本地pdf文件");
    }

    private List<String> getDatas() {

        if (datas != null && datas.size() > 0) {
            return datas;
        } else {
            datas = new ArrayList<>();
            initDatas();
            return datas;
        }

    }

    private String getFilePath(int position) {
        String path = null;
        switch (position) {
            case 0:
                path = "/storage/emulated/0/aa/会议系统需求分析文档V1.0.1.docx";
                //      path = getFilesDir().getAbsolutePath() + File.separator + "test.docx";
                break;

            case 1:
                path = getFilesDir().getAbsolutePath() + File.separator + "test.txt";
                break;

            case 2:
                path = getFilesDir().getAbsolutePath() + File.separator + "test.xlsx";
                break;

            case 3:
                path = getFilesDir().getAbsolutePath() + File.separator + "test.pptx";
                break;

            case 4:
                path = getFilesDir().getAbsolutePath() + File.separator + "test.pdf";
                break;

            default:
                break;
        }
        return path;
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, FileListActivity.class);
        context.startActivity(intent);
    }


    private void copy() {
        // 开始复制
        String path = "file://" + "/storage/emulated/0/aa/会议系统需求分析文档V1.0.1.docx";
        copyAssetsFileToAppFiles(path, "test.docx");
        copyAssetsFileToAppFiles(path + "test.pdf", "test.pdf");
        copyAssetsFileToAppFiles(path + "test.pptx", "test.pptx");
        copyAssetsFileToAppFiles(path + "test.txt", "test.txt");
        copyAssetsFileToAppFiles(path + "test.xlsx", "test.xlsx");
    }


    /**
     * 从assets目录中复制某文件内容
     *
     * @param assetFileName assets目录下的文件
     * @param newFileName   复制到/data/data/package_name/files/目录下文件名
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        int buffsize = 1024;

        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[buffsize];
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
