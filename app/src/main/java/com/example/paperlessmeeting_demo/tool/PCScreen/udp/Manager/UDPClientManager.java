package com.example.paperlessmeeting_demo.tool.PCScreen.udp.Manager;


import android.util.Log;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.OnUdpConnectListener;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.UDPThread.UDPClientReceiveThread;
import com.example.paperlessmeeting_demo.tool.constant;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UDPClientManager {
    private static class SingletonHolder {
        private static final UDPClientManager INSTANCE = new UDPClientManager();
    }

    public static final UDPClientManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static String TAG = "UDPClientManager";
    private ScheduledExecutorService scheduledExecutorService;

    public static UDPClientReceiveThread MythdReceive;
    public boolean isBroadcasting = false;

    /**
     * 移除UDP广播
     */
    public void removeUDPBroastcast() {
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdownNow();
        }
    }

    /**
     * UDP广播IP和四位数code
     */
    public void sendUDPWithCode(final String code) {
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    // “.Cvi,ScreenCode=
                    String content = ".Cvi,ScreenCode=" + code ;
                    Log.e("111","content==="+content);
                    byte[] sendBuffer = content.getBytes();

                    InetAddress bcIP = Inet4Address.getByName(constant.BROADCAST_IP);

                    DatagramSocket udp = new DatagramSocket();
                    udp.setBroadcast(true);
                    DatagramPacket dp = new DatagramPacket(sendBuffer, sendBuffer.length, bcIP, constant.BROADCAST_PORT);
                    udp.send(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);


        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                // 15秒后停止广播
                try {
                    if(!scheduledExecutorService.awaitTermination(15, TimeUnit.SECONDS)){
                        removeUDPBroastcast();
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 接受UDP单播
     */
    public void receiveUDP(OnUdpConnectListener listener) {
        if (MythdReceive != null) {
            MythdReceive.resumeThread();
            Log.d(TAG, "UDP receiver is running");
            return;
        }
        MythdReceive = new UDPClientReceiveThread(listener);
        isBroadcasting = true;
        Log.d(TAG, "UDP receiver started");
    }

    /**
     * 暂停接受UDP单播
     */
    public void stopReceiveUDP() {
        if (MythdReceive == null) {
            Log.d(TAG, "UDP receiver is running");
            return;
        }
        isBroadcasting = false;
        MythdReceive.pauseThread();
        Log.d(TAG, "stopReceiveUDP() is called");
    }
}
