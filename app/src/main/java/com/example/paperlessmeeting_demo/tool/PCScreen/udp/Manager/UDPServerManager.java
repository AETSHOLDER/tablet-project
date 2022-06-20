package com.example.paperlessmeeting_demo.tool.PCScreen.udp.Manager;

import android.text.TextUtils;
import android.util.Log;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.UDPThread.UDPServerReceiveThread;


public class UDPServerManager {
    private static class SingletonHolder {
        private static final UDPServerManager INSTANCE = new UDPServerManager();
    }

    public static final UDPServerManager getInstance() {
        return UDPServerManager.SingletonHolder.INSTANCE;
    }
    private static String TAG = "UDPServerManager";
    public static UDPServerReceiveThread MythdServerReceive;
    public boolean isBroadcasting = false;

    /**
     * 设置当前同屏码
     * */
    public void setServerCode(String currentCode) {
        if(TextUtils.isEmpty(currentCode)){
            return;
        }
        MythdServerReceive.setCurrentCode(currentCode);
    }
    /**
     * * 接受UDP单播
     */
    public void receiveUDP() {
        if (MythdServerReceive != null) {
            MythdServerReceive.resumeThread();
            Log.d(TAG, "UDP receiver is running");
            return;
        }
        MythdServerReceive = new UDPServerReceiveThread();
        isBroadcasting = true;
        Log.d(TAG, "UDP receiver started");
    }

    /**
     * 暂停接受UDP单播
     */
    public void stopReceiveUDP() {
        if (MythdServerReceive == null) {
            Log.d(TAG, "UDP receiver is running");
            return;
        }
        isBroadcasting = false;
        MythdServerReceive.pauseThread();
        Log.d(TAG, "stopReceiveUDP() is called");

    }
}
