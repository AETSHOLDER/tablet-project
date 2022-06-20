package com.example.paperlessmeeting_demo.tool.ScreenTools.utils;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import com.example.paperlessmeeting_demo.tool.VideoConfiguration;

import java.util.Arrays;


@TargetApi(18)
public class VideoMediaCodec {

    public static MediaCodec getVideoMediaCodec(VideoConfiguration videoConfiguration) {
        int videoWidth = getVideoSize(videoConfiguration.width);
        int videoHeight = getVideoSize(videoConfiguration.height);
//        if (Build.MANUFACTURER.equalsIgnoreCase("XIAOMI")) {
//            videoConfiguration.maxBps = 1500;
//            videoConfiguration.fps = 10;
//            videoConfiguration.ifi = 3;
//        }
        MediaFormat format = MediaFormat.createVideoFormat(videoConfiguration.mime, videoWidth, videoHeight);
        //设置颜色格式
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        //设置比特率(设置码率，通常码率越高，视频越清晰)
        format.setInteger(MediaFormat.KEY_BIT_RATE, 8* 1920 * 1200);
      //  format.setInteger(MediaFormat.KEY_BIT_RATE, videoConfiguration.maxBps * 1024);
        int fps = videoConfiguration.fps;
        //设置摄像头预览帧率
        if (BlackListHelper.deviceInFpsBlacklisted()) {
            fps = 15;
        }
        //设置帧率
        format.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        //关键帧间隔时间，通常情况下，你设置成多少问题都不大。
        //比如你设置成10，那就是10秒一个关键帧。但是，如果你有需求要做视频的预览，那你最好设置成1
        //因为如果你设置成10，那你会发现，10秒内的预览都是一个截图
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, videoConfiguration.ifi);
        // -----------------ADD BY XU.WANG 当画面静止时,重复最后一帧--------------------------------------------------------
        format.setLong(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / 45);
        //------------------MODIFY BY XU.WANG 为解决MIUI9.5花屏而增加...-------------------------------
        if (Build.MANUFACTURER.equalsIgnoreCase("XIAOMI")) {
            //设置编码模式
            format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ);
        } else {
            format.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);
        }
        //设置复用模式
        format.setInteger(MediaFormat.KEY_COMPLEXITY, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);
        MediaCodec mediaCodec = null;

        try {
            mediaCodec = MediaCodec.createEncoderByType(videoConfiguration.mime);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaCodec != null) {
                mediaCodec.stop();
                mediaCodec.release();
                mediaCodec = null;
            }
        }
        return mediaCodec;
    }

    // We avoid the device-specific limitations on width and height by using values that
    // are multiples of 16, which all tested devices seem to be able to handle.
    // 不建议使用下面的16倍数的方法，pc同屏会花屏
    public static int getVideoSize(int size) {
//        int multiple = (int) Math.ceil(size / 16.0);
//        return multiple * 16;
        return size;
    }

    // 既然不同的手机支持的KEY_COLOR_FORMAT 不一样，这里就需要动态的考虑先获取到手机可支持的颜色格式值，在进行设置，如下代码也是参考网上的资料。
    private static int getSupportColorFormat() {
        int numCodecs = MediaCodecList.getCodecCount();
        MediaCodecInfo codecInfo = null;
        for (int i = 0; i < numCodecs && codecInfo == null; i++) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            String[] types = info.getSupportedTypes();
            boolean found = false;
            for (int j = 0; j < types.length && !found; j++) {
                if (types[j].equals("video/avc")) {
                    Log.d("编码器VIdeoMediaCodec", "found");
                    found = true;
                }
            }
            if (!found)
                continue;
            codecInfo = info;
        }
        Log.e("AvcEncoder", "Found " + codecInfo.getName() + " supporting " + "video/avc");
        // Find a color profile that the codec supports
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType("video/avc");
        Log.e("AvcEncoder",
                "length-" + capabilities.colorFormats.length + "==" + Arrays.toString(capabilities.colorFormats));
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            Log.d("编码器VIdeoMediaCodec", "MediaCodecInfo COLOR FORMAT :" + capabilities.colorFormats[i]);
            if ((capabilities.colorFormats[i] == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar) || (capabilities.colorFormats[i] == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar)) {
                return capabilities.colorFormats[i];
            }
        }
        return MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible;
    }
}
