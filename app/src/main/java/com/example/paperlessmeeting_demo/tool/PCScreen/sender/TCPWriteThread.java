package com.example.paperlessmeeting_demo.tool.PCScreen.sender;

import android.util.Log;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.tool.ScreenTools.controll.sender.sendqueue.ISendQueue;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.Frame;
import com.example.paperlessmeeting_demo.tool.ScreenTools.server.EncodeV1;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.AnalyticDataUtils;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import java.io.BufferedOutputStream;
import java.util.Arrays;
import java.util.Timer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class TCPWriteThread extends Thread {
    private ISendQueue iSendQueue;
    private volatile boolean startFlag;
    private final String TAG = "WSWriteThread";


    public TCPWriteThread(ISendQueue sendQueue) {
        this.iSendQueue = sendQueue;
        startFlag = true;
    }

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

    // TODO  发送数据
    public void sendData(byte[] buff) {
        EncodeV1 encodeV1 = new EncodeV1(SocketCmd.SocketCmd_ScreentData,buff);

//        Log.e("发送数据","data===="+ Arrays.toString(encodeV1.buildSendContent()));
        MeetingAPP.getInstance().getNettyClient().sendMsgToServer(encodeV1.buildSendContent(), new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {                //4
                    Log.d(TAG, "Write auth successful");
                } else {
                    Log.d(TAG, "Write auth error");
                }
            }
        });
    }

    /**
     * 停止写
     */
    public void shutDown() {
        startFlag = false;
        this.interrupt();
    }
}
