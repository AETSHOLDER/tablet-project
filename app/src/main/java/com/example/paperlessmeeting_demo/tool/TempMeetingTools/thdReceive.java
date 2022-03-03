package com.example.paperlessmeeting_demo.tool.TempMeetingTools;

import android.app.Activity;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class thdReceive extends Thread {
    private final Object lock = new Object();
    public boolean pause = false;
    private String  TAG = "thdReceive";
    public static List<String> receiveIPArr = new ArrayList<>();
    private String  currentIP_code = null;
    /**
     * 调用这个方法实现暂停线程
     */
    void pauseThread() {
        pause = true;
        receiveIPArr.clear();
    }

    /**
     * 调用这个方法实现恢复线程的运行
     */
    void resumeThread() {
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

        Log.d(TAG, "thdReceive is running");
        DatagramSocket rSocket = null;
        try {
            rSocket = new DatagramSocket(null);
            rSocket.setReuseAddress(true);
            rSocket.setBroadcast(true);
            rSocket.bind(new InetSocketAddress(1661));
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG,"UDPBroadcastManager 接受数据DatagramSocket启动失败");
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
                rSocket.setSoTimeout(5010);
                rSocket.receive(dp);
                String recMsg = new String(dp.getData(), dp.getOffset(), dp.getLength(), "UTF-8");

                if(recMsg.contains("destroy") ){
                    List<String> ipcodeInfo = FLUtil.dealUDPMsg(recMsg);

                    List<String> newList = new ArrayList<>();
                    newList.addAll(receiveIPArr);
                    String code = ipcodeInfo.get(1);
                    for (String ms : newList){
                       if(ms.contains(code)){
                           receiveIPArr.remove(ms);
                       }
                    }
                }else if(!receiveIPArr.contains(recMsg)){
                    receiveIPArr.add(recMsg);
                    String ip_code;
                    if(receiveIPArr.size()>1){
                        // 超过一个取最先收到的
                        List<String> ipcodeInfo = FLUtil.dealUDPMsg(receiveIPArr.get(0));
                        ip_code = ipcodeInfo.get(0)+","+ipcodeInfo.get(1);
                    }else {
                        List<String> ipcodeInfo = FLUtil.dealUDPMsg(recMsg);
                        ip_code = ipcodeInfo.get(0)+","+ipcodeInfo.get(1);
                    }
                    // 如果IPcode 都是上一个，直接返回，不再发送
//                    if(currentIP_code != null && currentIP_code.equals(ip_code)){
//                        return;
//                    }
//                    currentIP_code = ip_code;
                    Activity topActivity = (Activity) ActivityUtils.getTopActivity();
                    if (topActivity != null) {
                        topActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  发黏性事件
                                Log.e(TAG, "[发黏性事件");
                                EventMessage msg = new EventMessage(MessageReceiveType.MessageCreatTempMeeting, ip_code);
                                EventBus.getDefault().post(msg);
                            }
                        });
                    }

                }
                Log.d(TAG, "[Rx]" + recMsg);
                Log.e(TAG,"receiveIPArr====="+ Arrays.toString(receiveIPArr.toArray()) );

            } catch (SocketTimeoutException e){
                //  超时处理
                receiveIPArr.clear();
                Log.d(TAG,"Socket.receive超时error");
            }catch (IOException e) {
                Log.d(TAG,"IOException error");
                e.printStackTrace();
            }
        }
    }
}
