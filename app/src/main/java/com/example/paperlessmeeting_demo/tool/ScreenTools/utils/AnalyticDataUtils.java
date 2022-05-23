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
 * Created by wt on 2018/6/12.
 * 根据协议解析数据
 */
public class AnalyticDataUtils {
    private OnAnalyticDataListener mListener;
    private volatile int readLength = 0;
    private Timer timer;
    private boolean isCalculate = false;

    /**
     * 分析头部数据
     */
//    public ReceiveHeader analysisHeader(ByteBuffer bytes) {
//        byte[] header = ByteUtil.decodeValue(bytes);
//        //实现数组之间的复制
//        //bytes：源数组
//        //srcPos：源数组要复制的起始位置
//        //dest：目的数组
//        //destPos：目的数组放置的起始位置
//        //length：复制的长度
//        byte[] buff = new byte[4];
//        System.arraycopy(header, 1, buff, 0, 4);
//        final int mainCmd = ByteUtil.bytesToInt(buff);       //主指令  1`5
//        buff = new byte[4];
//        System.arraycopy(header, 5, buff, 0, 4);
//        int stringBodyLength = ByteUtil.bytesToInt(buff);//文本数据 9 ~ 13;
//        buff = new byte[4];
//        System.arraycopy(header, 9, buff, 0, 4);
//        int byteBodySize = ByteUtil.bytesToInt(buff);//byte数据 13^17
//
//        return new ReceiveHeader(mainCmd, stringBodyLength, byteBodySize);
//    }

    public ReceiveData analyticData(ByteBuffer bytes) throws IOException {
        byte[] header = ByteUtil.decodeValue(bytes);
        byte[] sendBody = null;
        byte[] buffData = null;

        //实现数组之间的复制
        //bytes：源数组
        //srcPos：源数组要复制的起始位置
        //dest：目的数组
        //destPos：目的数组放置的起始位置
        //length：复制的长度
        byte[] buff = new byte[4];
        System.arraycopy(header, 0, buff, 0, 4);
        final int mainCmd = ByteUtil.bytesToInt(buff);       //主指令  1`5
        buff = new byte[4];
        System.arraycopy(header, 4, buff, 0, 4);
        int stringBodyLength = ByteUtil.bytesToInt(buff);//文本数据 9 ~ 13;
        buff = new byte[4];
        System.arraycopy(header, 8, buff, 0, 4);
        int byteBodySize = ByteUtil.bytesToInt(buff);//byte数据 13^17

        ReceiveHeader receiveHeader = new ReceiveHeader(mainCmd, stringBodyLength, byteBodySize);

        //文本长度
        if (receiveHeader.getStringBodylength() != 0) {
            buff = new byte[stringBodyLength];
            Log.e("ttt", "analyticData: "+stringBodyLength);
            System.arraycopy(header, 8, buff, 0, stringBodyLength);
            sendBody = header;
        }
        //音视频长度
        if (receiveHeader.getBuffSize() != 0) {
            buff = new byte[byteBodySize];
            Log.e("ttt", "analyticData: "+byteBodySize);
            System.arraycopy(header, 8+stringBodyLength, buff, 0, byteBodySize);
            buffData = header;
        }
        ReceiveData data = new ReceiveData();
        data.setHeader(receiveHeader);
        data.setSendBody(sendBody == null ? "" : new String(sendBody));
        data.setBuff(buffData);
        return data;
    }

    /**
     * 保证从流里读到指定长度数据
     *
     * @return
     * @throws Exception
     */
//    public byte[] readByte(InputStream is, int readSize) throws IOException {
//        byte[] buff = new byte[readSize];
//        int len = 0;
//        int eachLen = 0;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        while (len < readSize) {
//            eachLen = is.read(buff);
//            if (eachLen != -1) {
//                if (isCalculate) readLength += eachLen;
//                len += eachLen;
//                baos.write(buff, 0, eachLen);
//            } else {
//                baos.close();
//                throw new IOException();
//            }
//            if (len < readSize) {
//                buff = new byte[readSize - len];
//            }
//        }
//        byte[] b = baos.toByteArray();
//        baos.close();
//        return b;
//    }


    public interface OnAnalyticDataListener {
//        void onSuccess(ReceiveData data);

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
