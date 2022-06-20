package com.example.paperlessmeeting_demo.tool.PCScreen.sender;


import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.Sender;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.TcpPacker;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.ISendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.NormalSendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.Frame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.WeakHandler;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;

/**
 * tcp发送
 */
public class TCPSender implements Sender {
    private ISendQueue mSendQueue = new NormalSendQueue();
    private ISendQueue mWSSendQueue = new NormalSendQueue();  // ws 专开一个sendqueue,防止sendqueue取数据总量计算有误
    private static final String TAG = "TCPSender";
//    private OnSenderListener sendListener;
    private TCPConnection mTcpConnection;
    private WeakHandler weakHandler = new WeakHandler();
    private int mainCmd;
    private int subCmd;
    //文本消息
    private String sendBody = null;


    public TCPSender() {
        mTcpConnection = new TCPConnection();
    }

    public void setVideoParams(VideoConfiguration videoConfiguration) {
        mTcpConnection.setVideoParams(videoConfiguration);
    }

    // TODO 设置主指令
    public void setMianCmd(int mainCmd) {
        this.mainCmd = mainCmd;
    }

    // TODO 设置子指令
    public void setSubCmd(int subCmd) {
        this.subCmd = subCmd;
    }

    // TODO 设置要发送的文本内容
    public void setSendBody(String body) {
        this.sendBody = body;
    }

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
        mWSSendQueue.putFrame(frame);
    }

    /**
     * 开启连接
     */
    public void openConnect() {
        //设置缓存队列
        mTcpConnection.setSendQueue(mSendQueue);
        mTcpConnection.setmWSSendQueue(mWSSendQueue);
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectNotInUi();
            }
        }).start();

    }

    @Override
    public void start() {
        mSendQueue.start();
        mWSSendQueue.start();
    }

    @Override
    public void stop() {
        mTcpConnection.stop();
        mSendQueue.stop();
        mWSSendQueue.stop();
    }

    private synchronized void connectNotInUi() {
        // 直接发送屏幕数据
        mTcpConnection.sendScreenData();
    }

    public void sendWSScreenData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mTcpConnection.sendWSScreenData();
            }
        }).start();
    }
    public void finishWSSender() {
        if (mTcpConnection != null) mTcpConnection.finishWSSender();
    }

    /**
     *  为解决首次黑屏而加
     */
    public void setSpsPps(byte[] spsPps) {
        if (mTcpConnection != null) mTcpConnection.setSpsPps(spsPps);
    }

}
