package com.example.paperlessmeeting_demo.tool.PCScreen.udp.UDPThread;

import android.util.Log;

import com.example.paperlessmeeting_demo.tool.PCScreen.udp.OnUdpConnectListener;
import com.example.paperlessmeeting_demo.tool.constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cyj
 * @createDate 4/24/22
 * @ClassName: UDPServerReceiveThread
 * @Description: 服务端接受广播处理
 * @Version: 1.0
 */

public class UDPServerReceiveThread extends Thread {
    private final Object lock = new Object();
    public boolean pause = false;
    private String  TAG = "UDPServerReceiveThread";
    public static List<String> receiveIPArr = new ArrayList<>();

    private InetAddress inetAddress = null;
    //服务端的局域网IP
    private static String danboip;
    private OnUdpConnectListener mListener;
    private String currentTotolCode;   // 当前完整投屏协议码


    // .Cvi,ScreenCode=9999”
    public void setCurrentCode(String currentCode) {
        this.currentTotolCode = ".Cvi,ScreenCode=" + currentCode;
    }

    public UDPServerReceiveThread() {
        this.start();
    }

    /**
     * 调用这个方法实现暂停线程
     */
    public void pauseThread() {
        pause = true;
        receiveIPArr.clear();
    }

    /**
     * 调用这个方法实现恢复线程的运行
     */
    public void resumeThread() {
        pause = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
     */
    void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        super.run();
        Log.d(TAG, " is running");
        DatagramSocket rSocket = null;
        try {
            rSocket = new DatagramSocket(new InetSocketAddress(constant.BROADCAST_PORT));
            rSocket.setReuseAddress(true);
            rSocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG,"接受数据DatagramSocket启动失败");
            return;
        }

        while (true) {
            // 让线程处于暂停等待状态
            while (pause) {
                onPause();
            }
            try {
                byte[] buffer = new byte[1024];
                final DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                // 注意 此超时时间要超过广播发送的频率，否则会经常超时
                rSocket.setSoTimeout(2300);
                rSocket.receive(dp);
                String recMsg = new String(dp.getData(), dp.getOffset(), dp.getLength(), "UTF-8");

                String clientIp = dp.getAddress().getHostAddress();
                if(currentTotolCode != null && currentTotolCode.length() != 0 && recMsg.equals(currentTotolCode)){
                    // 服务端响应 单播	“.Cvi, Respond='This is the Server'”
                    receiveIPArr.add(clientIp);
                    // 只取最先接受的
                    danboip = receiveIPArr.get(0);
                    try {
                        // “.Cvi,ScreenCode=
                        String content = constant.serverResponse;
                        byte[] sendBuffer = content.getBytes();
                        InetAddress bcIP = InetAddress.getByName(danboip);

                        DatagramSocket udp = new DatagramSocket();
                        DatagramPacket dp1 = new DatagramPacket(sendBuffer, sendBuffer.length, bcIP, constant.BROADCAST_PORT);
                        udp.send(dp1);
                        udp.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "[Rx]" + recMsg);
            } catch (Exception e){
//                e.printStackTrace();
                receiveIPArr.clear();
            }
        }
    }
}
