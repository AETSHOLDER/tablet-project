package com.example.paperlessmeeting_demo.tool.ScreenTools.controll;

import android.util.Log;
import android.view.SurfaceHolder;

import com.example.paperlessmeeting_demo.tool.ScreenTools.VIdeoMediaCodec;
import com.example.paperlessmeeting_demo.tool.ScreenTools.decode.DecodeThread;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.DecodeFrame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.interf.OnAcceptBuffListener;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.NormalPlayQueue;


/**
 * Created by wt
 * Date on  2018/7/3 18:59:52.
 *
 * @Desc
 */

public class VideoPlayController {
    private static final String TAG = "VideoPlayController";
    private VIdeoMediaCodec videoMediaCodec;
    private DecodeThread mDecodeThread;
    private NormalPlayQueue mPlayqueue;

    public VideoPlayController() {
        mPlayqueue = new NormalPlayQueue();
    }

    // TODO: 2018/7/5 开始解码
    public void surfaceCreate(SurfaceHolder holder) {
        //初始化解码器
        Log.e(TAG, "initial play queue");
        videoMediaCodec = new VIdeoMediaCodec(holder);
        //开启解码线程
        mDecodeThread = new DecodeThread(videoMediaCodec.getCodec(), mPlayqueue);
        videoMediaCodec.start();
        mDecodeThread.start();
    }


    public void surfaceDestrory() {
        mPlayqueue.stop();
        mDecodeThread.shutdown();
    }

    public void stop() {
        mPlayqueue.stop();
        mDecodeThread.shutdown();
    }

    public void putBuff(DecodeFrame frame) {
        mPlayqueue.putByte(frame);
    }
}
