package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender;

import android.util.Log;

import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.ISendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.NormalSendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.Frame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.WeakHandler;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;


/**
 * Created by wt
 * tcp发送
 */

public class WSSender implements Sender {
    private ISendQueue mSendQueue = new NormalSendQueue();
    private static final String TAG = "WSSender";
//    private OnSenderListener sendListener;
    private WSConnection mTcpConnection;
    private WeakHandler weakHandler = new WeakHandler();
    private int mainCmd;
    private int subCmd;
    //文本消息
    private String sendBody = null;


    public WSSender() {
        mTcpConnection = new WSConnection();
//        this.ip = ip;
//        this.port = port;
    }

    public void setVideoParams(VideoConfiguration videoConfiguration) {
        mTcpConnection.setVideoParams(videoConfiguration);
    }

    // TODO: 2018/6/11 wt设置主指令
    public void setMianCmd(int mainCmd) {
        this.mainCmd = mainCmd;
    }

    // TODO: 2018/6/11 wt设置子指令
    public void setSubCmd(int subCmd) {
        this.subCmd = subCmd;
    }

    // TODO: 2018/6/11 wt设置要发送的文本内容
    public void setSendBody(String body) {
        this.sendBody = body;
    }

    // TODO: 2018/5/29 wt
    @Override
    public void onData(byte[] data, int type) {
        Frame frame = null;
        if (type == TcpPacker.FIRST_VIDEO) {
            frame = new Frame(data, type, Frame.FRAME_TYPE_CONFIGURATION);
        } else if (type == TcpPacker.KEY_FRAME) {
            frame = new Frame(data, type, Frame.FRAME_TYPE_KEY_FRAME);
        } else if (type == TcpPacker.INTER_FRAME) {
            frame = new Frame(data, type, Frame.FRAME_TYPE_INTER_FRAME);
        } else if (type == TcpPacker.AUDIO) {
            frame = new Frame(data, type, Frame.FRAME_TYPE_AUDIO);
        }
        if (frame == null) {
            return;
        }
        mSendQueue.putFrame(frame);
    }

    /**
     * 开启连接
     */
    public void openConnect() {
        //设置缓存队列
        mTcpConnection.setSendQueue(mSendQueue);
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectNotInUi();
            }
        }).start();

    }

    @Override
    public void start() {
//        mSendQueue.setSendQueueListener(this);
        mSendQueue.start();
    }

    @Override
    public void stop() {
        mTcpConnection.stop();
        mSendQueue.stop();
    }

    private synchronized void connectNotInUi() {
        //设置连接回调
//        mTcpConnection.setConnectListener(mTcpListener);
        //开始连接服务器
        mTcpConnection.sendScreenData(mainCmd, sendBody);
    }

    /**
     * add by wt 为解决首次黑屏而加
     */
    public void setSpsPps(byte[] spsPps) {
        if (mTcpConnection != null) mTcpConnection.setSpsPps(spsPps);
    }

}
