package com.example.paperlessmeeting_demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaCodec;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;


import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseActivity;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.VideoPlayController;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.DecodeFrame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.AnalyticDataUtils;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.DecodeUtils;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventScreenMessage;
import com.example.paperlessmeeting_demo.tool.constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Arrays;


public class ScreenReceiveActivity extends BaseActivity{
    private static final String TAG = "ScreenReceiveActivity";
    private MediaCodec mediaCodec;
    private SurfaceView surface_view;
    private SurfaceHolder mSurfaceHolder;

    private boolean getFrameData = true;
    private TextView screen_device_name;
    private VideoPlayController mController;
    private ScreenReceiveActivity.MyBroadcastReceiver myBroadcastReceiver; // 断开2同屏
    int num = 0;
    private DecodeUtils mDecoderUtils;
    private AnalyticDataUtils mAnalyticDataUtils;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_screen_receive;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mDecoderUtils = new DecodeUtils();
        mAnalyticDataUtils = new AnalyticDataUtils();
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        // 保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //   setContentView(R.layout.activity_screen_receive);
        myBroadcastReceiver = new ScreenReceiveActivity.MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(constant.FINISH_SHARE_SCREEN_BROADCAST);
        registerReceiver(myBroadcastReceiver, filter);
        surface_view = (SurfaceView) findViewById(R.id.screen_share_re_surface_view);
        mSurfaceHolder = surface_view.getHolder();
        mController = new VideoPlayController();
//        mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);//优化花屏
        screen_device_name = (TextView) findViewById(R.id.tv_client_device_name);
        screen_device_name.setText("正在同屏");
        //  mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);//优化花屏
        //    android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" // 花屏可以考虑设置这个样式的acticity，不过尝试了效果有限~
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //Surface创建时激发，一般在这里调用画面的线程
                getFrameData = true;
//                creatMediaCodec(holder);

                mController.surfaceCreate(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //Surface的大小发生改变时调用。
//                Log.e(TAG, "surface change width = " + width + " height = " + height);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //销毁时激发，一般在这里将画面的线程停止、释放。
                mController.surfaceDestrory();
                getFrameData = false;
                if (mediaCodec != null) {
                    mediaCodec.stop();
                    mediaCodec.release();
                    mediaCodec = null;
                }
            }
        });

        mDecoderUtils.setOnVideoListener(new DecodeUtils.OnVideoListener() {
            @Override
            public void onSpsPps(byte[] sps, byte[] pps) {
                DecodeFrame spsPpsFrame = new DecodeFrame();
                spsPpsFrame.setType(DecodeFrame.SPSPPS);
                spsPpsFrame.setSps(sps);
                spsPpsFrame.setPps(pps);
                Log.d("AcceptH264MsgThread", "sps pps ...");
                mController.putBuff(spsPpsFrame);
            }

            @Override
            public void onVideo(byte[] video, int type) {
                DecodeFrame frame = new DecodeFrame();
                switch (type) {
                    case DecodeFrame.KEY_FRAME:
                        frame.setType(DecodeFrame.KEY_FRAME);
                        frame.setBytes(video);
                        mController.putBuff(frame);
                        break;
                    case DecodeFrame.NORMAL_FRAME:
                        frame.setType(DecodeFrame.NORMAL_FRAME);
                        frame.setBytes(video);
                        mController.putBuff(frame);
                        break;
                    case DecodeFrame.AUDIO_FRAME:
                        frame.setType(DecodeFrame.AUDIO_FRAME);
                        frame.setBytes(video);
                        mController.putBuff(frame);
//                        Log.e("AcceptH264MsgThread", "audio frame ...");
                        break;
                    default:
                        Log.e("AcceptH264MsgThread", "other video...");
                        break;
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(EventScreenMessage message) {
//        if (message.getType().equals(MessageReceiveType.MessageClient)) {
//            if(mDecoderUtils==null){
//                return;
//            }
//            byte[] aa = ByteUtil.decodeValue(message.getBytes()) ;
//            //区分音视频
//            mDecoderUtils.isCategory(aa);
//        }

        if (message.getType().equals(MessageReceiveType.MessageScreenData)) {
            if(mDecoderUtils==null){
                return;
            }

            ReceiveData receiveData = message.getBytes();
            if(receiveData.getHeader().getMainCmd() == SocketCmd.SocketCmd_ScreentData){
                Log.e("服务端收到数据","Arrays.toString(arr)==="+ Arrays.toString(receiveData.getBuff()));
                mDecoderUtils.isCategory(receiveData.getBuff());
            }
        }else if(message.getType().equals(MessageReceiveType.MessageScreenDisconnect)){
            getFrameData = false;
            if (mController != null) mController.stop();
            mAnalyticDataUtils.stop();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mSurfaceHolder != null) {
            mSurfaceHolder.getSurface().release();
        }
        mSurfaceHolder = null;
        //  surface_view = null;
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getFrameData = false;
            if (mController != null) mController.stop();
            mAnalyticDataUtils.stop();
            finish();
        }
    }
}
