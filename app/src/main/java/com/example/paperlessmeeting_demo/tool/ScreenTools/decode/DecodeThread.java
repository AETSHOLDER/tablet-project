package com.example.paperlessmeeting_demo.tool.ScreenTools.decode;

import android.media.MediaCodec;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.example.paperlessmeeting_demo.tool.ScreenTools.decode.play.VideoPlay;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.DecodeFrame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.NormalPlayQueue;
import java.nio.ByteBuffer;

/**
 * Created by wt
 * Date on  2017/11/29 14:04:57.
 *
 * @Desc 解码线程, 解码判断类型, 分开解析
 */

public class DecodeThread extends Thread {
    private NormalPlayQueue playQueue;
    private String TAG = "DecodeThread";
    private boolean isPlaying = true;
    private final VideoPlay videoPlay;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public DecodeThread(MediaCodec mediaCodec, NormalPlayQueue playQueue) {
        this.playQueue = playQueue;
        videoPlay = new VideoPlay(mediaCodec);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        while (isPlaying) {
            DecodeFrame frame = playQueue.takeByte();
            if (frame == null) {
                SystemClock.sleep(1);
                continue;
            }
            switch (frame.getType()) {
                case DecodeFrame.KEY_FRAME:
                case DecodeFrame.NORMAL_FRAME:
                    try {
                        videoPlay.decodeH264(frame.getBytes());
                        Log.i(TAG, "receive a frame count");
                    } catch (Exception e) {
                        Log.e(TAG, "frame Exception" + e.toString());
                    }
                    break;
                case DecodeFrame.SPSPPS:
                    try {
                        ByteBuffer bb = ByteBuffer.allocate(frame.getPps().length + frame.getSps().length);
                        bb.put(frame.getSps());
                        bb.put(frame.getPps());
                        Log.e(TAG, "receive Sps pps");
                        videoPlay.decodeH264(bb.array());
                    } catch (Exception e) {
                        Log.e(TAG, "sps pps Exception" + e.toString());
                    }
                    break;
                case DecodeFrame.AUDIO_FRAME:
                    try {
//                        audioPlay.playAudio(frame.getBytes(), 0, frame.getBytes().length);
                    } catch (Exception e) {
                        Log.e(TAG, "audio Exception" + e.toString());
                    }
                    break;
            }
        }
    }

    public void setOnFrameChangeListener(VideoPlay.OnFrameChangeListener listener) {
//        videoPlay.setOnFrameChangeListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void shutdown() {
        Log.e(TAG, "DecodeThread shutdown");
        isPlaying = false;
        this.interrupt();
//        if (audioPlay != null) audioPlay.release();
        if (videoPlay != null) videoPlay.release();
    }
}
