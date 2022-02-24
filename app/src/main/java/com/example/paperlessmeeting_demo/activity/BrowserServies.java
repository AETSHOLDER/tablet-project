package com.example.paperlessmeeting_demo.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.constant;
import com.orhanobut.hawk.Hawk;

import java.util.List;

/**
 * 白板工具
 * Created by gpy on 2015/6/2.
 */

public class BrowserServies extends Service {
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private MyReceiver myReceiver;
    private View displayView;

    @Override
    public void onCreate() {
        super.onCreate();

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
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.SHOW_BROWSER);
        registerReceiver(myReceiver, filter);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
      /*  layoutParams.x = 100;
        layoutParams.y = 300;*/
    }


    @Override
    public IBinder onBind(Intent intent) {
        showFloatingWindow();
        return new MyBroadBinder();
    }

    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.activity_browser, null);
            WebView webview = displayView.findViewById(R.id.webview);
            ImageView minIma = displayView.findViewById(R.id.min_image);
            ImageView closeIma = displayView.findViewById(R.id.close_image);
            webview.loadUrl("http://www.baidu.com/");
            minIma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayView.setVisibility(View.GONE);
                 //   Hawk.put("BrowserServies", true);
                }
            });
            windowManager.addView(displayView, layoutParams);
            closeIma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   displayView.setVisibility(View.GONE);
                    windowManager.removeView(displayView);
                    Intent intent = new Intent();
                    //设置intent的动作为com.example.broadcast，可以任意定义
                    intent.setAction(constant.CLOSE_BROWSER_BROADCAST);
                    //发送无序广播
                    sendBroadcast(intent);
                 //   Hawk.put("BrowserServies", false);
                }
            });
        }
    }

    public void showBroad() {
        if (displayView != null) {
            // windowManager.updateViewLayout(displayView, layoutParams);
            displayView.setVisibility(View.VISIBLE);
        //    Hawk.put("BrowserServies", false);
            Log.d("gfjjfghjfg", "广播促使布局显示~");
        }
    }

    public class MyBroadBinder extends Binder {
        public BrowserServies getService() {
            return BrowserServies.this;
        }

       /* public void setData(String data) {
            FloatingVideoService.this.data = data;
        }*/
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            showBroad();
        }
    }
}

