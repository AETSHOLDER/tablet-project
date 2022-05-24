package com.example.paperlessmeeting_demo.tool.ScreenTools.interf;


import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.DecodeFrame;

/**
 * Created by wt
 * 关于帧类型回调
 */

public interface OnAcceptBuffListener {
    void acceptBuff(DecodeFrame frame);
}
