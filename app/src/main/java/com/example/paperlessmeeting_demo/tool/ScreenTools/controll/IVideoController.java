package com.example.paperlessmeeting_demo.tool.ScreenTools.controll;


import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.OnVideoEncodeListener;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;

public interface IVideoController {
    void start();
    void stop();
    void pause();
    void resume();
    boolean setVideoBps(int bps);
    void setVideoEncoderListener(OnVideoEncodeListener listener);
    void setVideoConfiguration(VideoConfiguration configuration);
}
