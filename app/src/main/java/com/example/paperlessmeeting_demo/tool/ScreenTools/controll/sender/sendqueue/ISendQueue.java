package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue;


import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.Frame;

public interface ISendQueue {
    void start();
    void stop();
    void setBufferSize(int size);
    void putFrame(Frame frame);
    Frame takeFrame();
//    void setSendQueueListener(SendQueueListener listener);
}
