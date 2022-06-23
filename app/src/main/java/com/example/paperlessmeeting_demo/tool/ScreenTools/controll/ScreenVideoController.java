package com.example.paperlessmeeting_demo.tool.ScreenTools.controll;

import android.annotation.TargetApi;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.view.Surface;

import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ScreenRecordEncoder;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.OnVideoEncodeListener;
import com.example.paperlessmeeting_demo.tool.SopCastLog;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.VideoMediaCodec;

import static android.os.Build.VERSION_CODES.LOLLIPOP;


@TargetApi(LOLLIPOP)
public class ScreenVideoController implements IVideoController {
    private String TAG = "ScreenVideoController";
    private MediaProjectionManager mManager;
    private int resultCode;
    private Intent resultData;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjection mMediaProjection;
    private VideoConfiguration mVideoConfiguration = VideoConfiguration.createDefault();
    private ScreenRecordEncoder mEncoder;
    private OnVideoEncodeListener mListener;
    private boolean isWSScreen = false;

    /**
     * isWS  是否销毁MediaProjection
     * 如果是二次投屏，不能销毁
     * */
    public ScreenVideoController(MediaProjection mediaProjection,boolean isWS) {
        isWSScreen = isWS;
        mMediaProjection = mediaProjection;
//        mManager = manager;
//        this.resultCode = resultCode;
//        this.resultData = resultData;
    }

    @Override
    public void start() {
        //关于屏幕采集
        mEncoder = new ScreenRecordEncoder(mVideoConfiguration);
        final Surface surface = mEncoder.getSurface();
        mEncoder.start();
        mEncoder.setOnVideoEncodeListener(mListener);
//        mMediaProjection = mManager.getMediaProjection(resultCode, resultData);
        int width = VideoMediaCodec.getVideoSize(mVideoConfiguration.width);
        int height = VideoMediaCodec.getVideoSize(mVideoConfiguration.height);
        //实例化VirtualDisplay,这个类的主要作用是用来获取屏幕信息并保存在里。
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenRecoder",
                width, height, 1, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface,
                null, null);
    }

    @Override
    public void stop() {
        if (mEncoder != null) {
            mEncoder.setOnVideoEncodeListener(null);
            mEncoder.stop();
            mEncoder = null;
        }
        if (mMediaProjection != null && !isWSScreen ) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
    }

    @Override
    public void pause() {
        if (mEncoder != null) {
            mEncoder.setPause(true);
        }
    }

    @Override
    public void resume() {
        if (mEncoder != null) {
            mEncoder.setPause(false);
        }
    }

    @Override
    public boolean setVideoBps(int bps) {
        //重新设置硬编bps，在低于19的版本需要重启编码器
        boolean result = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //由于重启硬编编码器效果不好，此次不做处理
            SopCastLog.d(TAG, "Bps need change, but MediaCodec do not support.");
        } else {
            if (mEncoder != null) {
                SopCastLog.d(TAG, "Bps change, current bps: " + bps);
                mEncoder.setRecorderBps(bps);
                result = true;
            }
        }
        return result;
    }

    @Override
    public void setVideoEncoderListener(OnVideoEncodeListener listener) {
        mListener = listener;
    }

    @Override
    public void setVideoConfiguration(VideoConfiguration configuration) {
        mVideoConfiguration = configuration;
    }
}
