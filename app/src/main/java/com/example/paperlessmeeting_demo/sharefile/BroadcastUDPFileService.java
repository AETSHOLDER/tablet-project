package com.example.paperlessmeeting_demo.sharefile;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.paperlessmeeting_demo.tool.constant;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BroadcastUDPFileService extends Service {
    /**
     *
     */
    public Boolean flag = true;
    /**
     * 文件格式 BYTE
     */
    public byte[] Controller_byte = new byte[248];
    /**
     * 广播地址
     */
    public InetAddress serverAddress;
    /**
     * UDP对象
     */
    public DatagramSocket socketController;
    /**
     * 发送文件P对象
     */
    public DatagramSocket socketAudio;

    public final IBinder binder = new BroadcastUDPFileServiceBinder();

    private errorMsgListener listener;
    private int CONTROL_PORT = 0;


    private byte volumes[] = new byte[248];

    private void init() {
        CONTROL_PORT = constant.EXTRAORDINARY_MEETING_PORT;
        try {
            serverAddress = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setListener(errorMsgListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class BroadcastUDPFileServiceBinder extends Binder {
        public BroadcastUDPFileService getService() {
            return BroadcastUDPFileService.this;
        }
    }

    public interface errorMsgListener {
        void getErrorMsg(String msg);
    }

    public boolean getIsRecord() {
        return flag;
    }

}
