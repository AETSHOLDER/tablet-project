package com.example.paperlessmeeting_demo.tool.ScreenTools.utils;

import android.util.Log;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveHeader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 根据协议解析数据
 */
public class AnalyticDataUtils {
    private OnAnalyticDataListener mListener;
    private volatile int readLength = 0;
    private Timer timer;
    private boolean isCalculate = false;

    public ReceiveData analyticData(byte[] header) throws IOException {
        byte[] sendBody = null;
        byte[] buffData = null;
        if(header.length <8){
            return null;
        }
        byte[] buff = null;
        buff = ByteUtil.byteSub(header,0,4);
        int cmd =  ByteUtil.bytesToInt(buff);

        buff = ByteUtil.byteSub(header,4,4);
        int size =  ByteUtil.bytesToInt(buff);

        int byteBodySize = 0; //视频内容长度
        int x =0;
        int y =0;
        int stringContentLength = 0;
        String content = "";
        switch (cmd){
            case SocketCmd.SocketCmd_RepAccept:
            case SocketCmd.SocketCmd_RepReject_001:
                buff = ByteUtil.byteSub(header,8,4);
                stringContentLength = ByteUtil.bytesToInt(buff);//文本数据长度
                buff = ByteUtil.byteSub(header,12,size-12);
                content = ByteToString.convertByteArrToASC(buff);

                break;
            case SocketCmd.SocketCmd_ScreentData:
                stringContentLength = 0;
                buff = ByteUtil.byteSub(header,8,4);
                x = ByteUtil.bytesToInt(buff);
                buff = ByteUtil.byteSub(header,12,4);
                y = ByteUtil.bytesToInt(buff);


                byteBodySize = size-16;
                // avpacket 有4字节长度，要去掉
                buff = ByteUtil.byteSub(header,16,byteBodySize);
                break;

        }

        ReceiveHeader receiveHeader = new ReceiveHeader(cmd, stringContentLength, byteBodySize);
        ReceiveData data = new ReceiveData();
        data.setHeader(receiveHeader);
        data.setSendBody(content);
        data.setBuff(buff);
        Log.e("111","cmd=="+cmd+"  size==="+size);
        return data;
    }

    public ReceiveData analyticData(ByteBuffer bytes) throws IOException {
        byte[] header = ByteUtil.decodeValue(bytes);
        return analyticData(header);
    }

    public interface OnAnalyticDataListener {
        void netSpeed(String msg);
    }

    public void setOnAnalyticDataListener(OnAnalyticDataListener listener) {
        this.mListener = listener;
    }

    public void startNetSpeedCalculate() {
        stop();
        readLength = 0;
        isCalculate = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.netSpeed((readLength / 1024) + " kb/s");
                    readLength = 0;
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        isCalculate = false;
        try {
            if (timer != null) timer.cancel();
        } catch (Exception e) {
        }
    }
}
