package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender;

public interface Sender {
    void start();
    void onData(byte[] data, int type);
    void stop();
}
