package com.example.paperlessmeeting_demo.tool.PCScreen.udp.UDPThread;

import android.util.Log;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.Manager.UDPClientManager;
import com.example.paperlessmeeting_demo.tool.PCScreen.udp.OnUdpConnectListener;
import com.example.paperlessmeeting_demo.tool.constant;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author cyj
 * @createDate 4/24/22
 * @ClassName: UDPClientReceiveThread
 * @Description: 基于udp的通信 接受单播信息
 * @Version: 1.0
 */
public class UDPClientReceiveThread extends Thread {
    private final Object lock = new Object();
    public boolean pause = false;
    private String  TAG = "UDPClientReceiveThread";

    //服务端的局域网IP
    private static String ip;
    private OnUdpConnectListener mListener;

    public UDPClientReceiveThread(OnUdpConnectListener listener) {
        this.mListener = listener;
        this.start();
    }

    /**
     * 调用这个方法实现暂停线程
     */
    public void pauseThread() {
        pause = true;
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
            rSocket = new DatagramSocket(null);
            rSocket.setReuseAddress(true);
            rSocket.setBroadcast(true);
            rSocket.bind(new InetSocketAddress(constant.BROADCAST_PORT));
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
                rSocket.receive(dp);
                String recMsg = new String(dp.getData(), 0, dp.getLength());

                Log.e("","收到单播消息===="+recMsg);
                if(!recMsg.equals(constant.serverResponse)){
                    continue;
                }
                UDPClientManager.getInstance().removeUDPBroastcast();

                ip = dp.getAddress().getHostAddress();
                Log.e(TAG,"拿到server ip==="+ip);
                MeetingAPP.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.udpConnectSuccess(ip);
                    }
                });

            } catch (Exception e){
                final Exception exception = e;
                //  超时处理
                MeetingAPP.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.udpDisConnec(exception.getMessage());
                    }
                });
                pauseThread();
                rSocket.close();
                Log.d(TAG,"Socket.receive超时error");
            }
        }
    }
}
