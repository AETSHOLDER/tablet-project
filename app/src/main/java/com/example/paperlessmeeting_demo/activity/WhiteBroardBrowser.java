package com.example.paperlessmeeting_demo.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.tool.constant;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/9/30.
 * <p>
 * 网页白板
 */

public class WhiteBroardBrowser extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.close_image)
    ImageView close_image;
    @BindView(R.id.min_image)
    ImageView minImage;
    @BindView(R.id.root_ll)
    LinearLayout rootLl;

    private MyReceiver myReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    protected void initView() {
        minImage.setOnClickListener(this);
        close_image.setOnClickListener(this);
        Log.d("dfddssf", "dfddsg");
    }

    @Override
    protected void initData() {
        // 设置支持JavaScript
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
//支持插件
        //  webSettings.setPluginState();
        //   webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //  webview.addJavascriptInterface(new JavaScriptInterfaces(), "control");
//192.168.10.164:8077
        // 加载网页
        webview.loadUrl("file:///android_asset/test.html");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        webview.addJavascriptInterface(this, "justTest");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void testJS() {
        webview.loadUrl("javascript:test()");
    }

   /* @SuppressLint("SetJavaScriptEnabled")
    public void testJS() {
        webview.loadUrl("javascript:testSet(222,4555)");
    }*/

    @JavascriptInterface
    public void hello(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constant.MAX_BROWSER);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.min_image:
                // moveTaskToBack(true);
                rootLl.setVisibility(View.GONE);
                break;
            case R.id.max_image:
                finish();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }






   /* js调用android


    注册js对象名称为control，方法为toNews，传的是新闻id和新闻类型
*/


    class JavaScriptInterfaces {
        JavaScriptInterfaces() {
        }

        @JavascriptInterface
        public void toNews(long newsId, String accessUrl) {

        }
    }

    class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("asdfaf", "白板");
            if (constant.MAX_BROWSER.equals(intent.getAction())) {
                onRestart();
            }

        }
    }
}
