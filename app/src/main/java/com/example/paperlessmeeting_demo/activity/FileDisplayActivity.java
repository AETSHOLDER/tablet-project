/*
package com.example.paperlessmeeting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.paperlessmeeting.R;
import com.example.paperlessmeeting.fragment.SuperWebX5Fragment;
import com.fanneng.android.web.file.FileReaderView;

*/
/**
 * describe：文件阅读类
 *
 * @author ：鲁宇峰 on 2018/8/7 11：14
 *         email：luyufengc@enn.cn
 *//*

public class FileDisplayActivity extends AppCompatActivity {

    private FileReaderView mDocumentReaderView;
    public static final String TYPE_KEY = "type_key";
    private FragmentManager mFragmentManager;
    private SuperWebX5Fragment mSuperWebX5Fragment;
    FragmentTransaction ft;
    private Bundle mBundle = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        mFragmentManager = this.getSupportFragmentManager();
        ft = mFragmentManager.beginTransaction();
        ft.add(R.id.container_framelayout, mSuperWebX5Fragment = SuperWebX5Fragment.getInstance(mBundle = new Bundle()), SuperWebX5Fragment.class.getName());
        mBundle.putString(SuperWebX5Fragment.URL_KEY, "http://sj.qq.com/myapp/");
        ft.commit();
        init();
    }


    public void init() {
        mDocumentReaderView = findViewById(R.id.documentReaderView);
        mDocumentReaderView.show(getIntent().getStringExtra("path"));
        Log.d("sdadd",getIntent().getStringExtra("path"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDocumentReaderView != null) {
            mDocumentReaderView.stop();
        }
    }


    public static void show(Context context, String url) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
*/
