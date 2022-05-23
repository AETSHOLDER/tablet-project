package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender;

import android.util.Log;


import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.ISendQueue;
import com.example.paperlessmeeting_demo.tool.VideoConfiguration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by wt
 * Date on  2018/5/28
 *
 * @Desc socket/tcp 连接
 */

public class WSConnection  {
//    private TcpConnectListener listener;
    private static final String TAG = "WSConnection";
    private ISendQueue mSendQueue;
    private WSWriteThread mWrite;
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


    public void setVideoParams(VideoConfiguration videoConfiguration) {
        width = videoConfiguration.width;
        height = videoConfiguration.height;
        fps = videoConfiguration.fps;
        maxBps = videoConfiguration.maxBps;
    }

    public void setSpsPps(byte[] spsPps) {
        this.mSpsPps = spsPps;
    }


    public void sendScreenData(int mainCmd, String sendBody) {
        mWrite = new WSWriteThread( mSendQueue, mainCmd, sendBody);
        mWrite.start();
//        mWrite.setOnTcpWriteThread(this);
    }


    public void stop() {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                try {
//                    Log.i("TcpConnecttion", "stop");
//                    if (out != null) out.close();
//                    if (in != null) in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                clearSocket();
                if (mWrite != null) {
//                    mWrite.setOnTcpWriteThread(null);
                    mWrite.shutDown();
                }
            }
        }.start();
    }

//    private void clearSocket() {
//        if (socket != null && socket.isConnected()) {
//            try {
//                socket.close();
//                Log.i(TAG, "socket close");
//                socket = null;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
