package com.example.paperlessmeeting_demo.tool.ScreenTools.utils;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author cyj
 * @createDate 5/30/22
 * @ClassName: VIdeoMediaDeCodec
 * @Description: 解码器
 * @Version: 1.0
 */

public class VIdeoMediaDeCodec {
    private MediaCodec mCodec;
    private static final int VIDEO_WIDTH = 1920;
    private static final int VIDEO_HEIGHT = 1080;
    private int FrameRate = 24;
    private boolean useSpsPPs = false;
    private SurfaceHolder mHolder;
    byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
    byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public VIdeoMediaDeCodec(SurfaceHolder holder) {
        this.mHolder = holder;
        initialCodec();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initialCodec() {
        try {
            //通过多媒体格式名创建一个可用的解码器
            mCodec = MediaCodec.createDecoderByType("video/avc");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化编码器
        final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", VIDEO_WIDTH, VIDEO_HEIGHT);
        //获取h264中的pps及sps数据
        if (useSpsPPs) {
            mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
            mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
        }
        //设置帧率
//        if (BlackListHelper.deviceInFpsBlacklisted()) {
//            FrameRate = 15;
//        }
        mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, FrameRate);


        // 经过网站一些资料的查找，原来是不同的手机支持的颜色格式不一样，这款oppo手机不支持COLOR_FormatYUV420SemiPlanar，需要设置成COLOR_FormatYUV420Planar
        // 指定编码器颜色格式
//        mediaformat.setInteger(MediaFormat.KEY_COLOR_FORMAT, getSupportColorFormat());
        //设置配置参数，参数介绍 ：
        // format	如果为解码器，此处表示输入数据的格式；如果为编码器，此处表示输出数据的格式。
        //surface	指定一个surface，可用作decode的输出渲染。
        //crypto	如果需要给媒体数据加密，此处指定一个crypto类.
        //   flags	如果正在配置的对象是用作编码器，此处加上CONFIGURE_FLAG_ENCODE 标签。
        mCodec.configure(mediaformat, mHolder.getSurface(), null, 0);
    }

    public MediaCodec getCodec() {
        return mCodec;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void start() {
        mCodec.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void release() {
        mCodec.stop();
        mCodec.release();
    }


}
