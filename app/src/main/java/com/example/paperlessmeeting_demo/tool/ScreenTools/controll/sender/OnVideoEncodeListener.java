package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

public interface OnVideoEncodeListener {
    void onVideoEncode(ByteBuffer bb, MediaCodec.BufferInfo bi);
}
