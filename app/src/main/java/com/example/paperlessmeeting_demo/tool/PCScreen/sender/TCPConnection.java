package com.example.paperlessmeeting_demo.tool.PCScreen.sender;


import android.util.Log;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.WSWriteThread;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.ISendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.EncodeV1;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * socket/tcp 连接
 */

public class TCPConnection {
//    private TcpConnectListener listener;
    private static final String TAG = "TCPConnection";
    private ISendQueue mSendQueue;
    private ISendQueue mWSSendQueue;
    private TCPWriteThread mWrite;     // tcp 发送线程

    private WSWriteThread mWSWrite;     // ws 发送线程
    private int width, height;
    private int maxBps;
    private int fps;
    private byte[] mSpsPps;


//    public void setConnectListener(TcpConnectListener listener) {
//        this.listener = listener;
//    }

    public void setSendQueue(ISendQueue sendQueue) {
        mSendQueue = sendQueue;
    }

    public void setmWSSendQueue(ISendQueue mWSSendQueue) {
        this.mWSSendQueue = mWSSendQueue;
    }

    public void setVideoParams(VideoConfiguration videoConfiguration) {
        width = videoConfiguration.width;
        height = videoConfiguration.height;
        fps = videoConfiguration.fps;
        maxBps = videoConfiguration.maxBps;
    }

    public void setSpsPps(byte[] spsPps) {
        this.mSpsPps = spsPps;
    }


    public void sendScreenData() {
        mWrite = new TCPWriteThread( mSendQueue);
        mWrite.start();
    }

    // 通过ws屏幕数据
    public void sendWSScreenData() {
        mWSWrite = new WSWriteThread( mWSSendQueue, SocketCmd.SocketCmd_ScreentData, "");
        mWSWrite.start();
    }
    public void finishWSSender() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (mWSWrite != null) {
//                    mWrite.setOnTcpWriteThread(null);
                    mWSWrite.shutDown();
                }
            }
        }.start();
    }

    public void stop() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (mWrite != null) {
//                    mWrite.setOnTcpWriteThread(null);
                    mWrite.shutDown();
                }
                if(mWSWrite != null){
                    mWSWrite.shutDown();
                }
            }
        }.start();
    }

}
