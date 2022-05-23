package com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender;

import android.util.Log;

import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.ISendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.Frame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.EncodeV1;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wt
 * Date on  2018/5/28
 *
 * @Desc
 */

public class WSWriteThread extends Thread {
    private ISendQueue iSendQueue;
    private volatile boolean startFlag;
    private int mainCmd;
    private String sendBody;
//    private OnTcpWriteListener mListener;
    private final String TAG = "WSWriteThread";
    private volatile int readLength = 0;
    private Timer timer;
    private boolean isCalculate = false;

    /**
     * by wt
     * @param sendQueue
     * @param mainCmd   主指令
     * @param sendBody  文本消息内容
     */
    public WSWriteThread( ISendQueue sendQueue, int mainCmd, String sendBody) {
        this.iSendQueue = sendQueue;
        this.mainCmd = mainCmd;
        this.sendBody = sendBody;
        startFlag = true;
    }

//    public void setOnTcpWriteThread(OnTcpWriteListener listener) {
//        this.mListener = listener;
//    }

    @Override
    public void run() {
        super.run();
//        startNetSpeedCalculate();
        while (startFlag) {
            Frame frame = iSendQueue.takeFrame();
            if (frame == null) {
                continue;
            }
            // TODO: 2018/5/29 wt修改
            if (frame.data.length != 0) {
                sendData(frame.data);
            }
        }
    }

    // TODO: 2018/6/4 wt 发送数据
    public void sendData(byte[] buff) {
        if ( JWebSocketClientService.client == null || !JWebSocketClientService.client.isOpen()) {
            return;
        }

        byte[] sendBuff = new EncodeV1(mainCmd, sendBody, buff).buildSendContent();
//        Log.e(TAG,"发送的sendBuff===="+new String(sendBuff));
        if (isCalculate) readLength += sendBuff.length;

//            JWebSocketClientService.sendByteBuffer(ByteUtil.encodeValue(sendBuff));
//        ByteBuffer byteBuffer = ByteUtil.encodeValue(buff);
//         JWebSocketClientService.sendByteBuffer(byteBuffer);


        JWebSocketClientService.sendByteArr(buff);
//            Log.e(TAG,"send data ");

    }

    /**
     * 停止写
     */
    public void shutDown() {
//        mListener = null;
        isCalculate = false;
        try {
            if (timer != null) timer.cancel();
        } catch (Exception e) {
        }
        startFlag = false;
        this.interrupt();
    }

    public void sendStartBuff() {
        sendData(new byte[0]);
    }

//    public void startNetSpeedCalculate() {
//        try {
//            if (timer != null) timer.cancel();
//        } catch (Exception e) {
//        }
//        readLength = 0;
//        isCalculate = true;
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (mListener != null) {
//                    mListener.netSpeedChange((readLength / 1024) + " kb/s");
//                    readLength = 0;
//                }
//            }
//        }, 1000, 1000);
//    }
}
