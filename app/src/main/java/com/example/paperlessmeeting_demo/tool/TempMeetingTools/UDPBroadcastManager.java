package com.example.paperlessmeeting_demo.tool.TempMeetingTools;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UDPBroadcastManager {
    private static String TAG = "UDPBroadcastManager";

    private static class SingletonHolder {
        private static final UDPBroadcastManager INSTANCE = new UDPBroadcastManager();
    }

    public static final UDPBroadcastManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ScheduledExecutorService scheduledExecutorService;
    public static thdReceive MythdReceive;
    public boolean isBroadcasting = false;

    /**
     * 移除UDP广播
     */
    public void removeUDPBroastcast() {
        scheduledExecutorService.shutdownNow();
    }

    /**
     * UDP广播IP和四位数code
     */
    public void sendUDPWithCode(final String code) {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "CVI.PaperLess.[" + getIPAddress() + "].[" + code + "]";
                    byte[] sendBuffer = content.getBytes();

                    InetAddress bcIP = Inet4Address.getByName("255.255.255.255");

                    DatagramSocket udp = new DatagramSocket();
                    udp.setBroadcast(true);
                    DatagramPacket dp = new DatagramPacket(sendBuffer, sendBuffer.length, bcIP, 1661);
                    udp.send(dp);
                    //  Log.d(TAG, "[Tx]" + content);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, 0, 5, TimeUnit.SECONDS);
       /*
                * 通知会议发布APP  展示当前临时会议
                 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Hawk.contains("meetingId")) {
                        String meetingRoomName = "";
                        String meeting_id = Hawk.get("meetingId");
                        if (Hawk.contains("meetingRoomName")) {
                            meetingRoomName = Hawk.get("meetingRoomName");
                        }
                        String content = "MeetingRelease.create." + meeting_id + "." + meetingRoomName;
                        byte[] sendBuffer = content.getBytes();
                        InetAddress bcIP = Inet4Address.getByName("255.255.255.255");
                        DatagramSocket udp = new DatagramSocket();
                        udp.setBroadcast(true);
                        DatagramPacket dp = new DatagramPacket(sendBuffer, sendBuffer.length, bcIP, 1800);
                        udp.send(dp);
                        Log.d(TAG, "[Tx]" + content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * code销毁广播
     * 临时会议销毁时发送一次
     */
    public void sendDestroyCode(final String code) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {

                    String content = "CVI.PaperLess.[destroy].[" + code + "]";
                    byte[] sendBuffer = content.getBytes();

                    InetAddress bcIP = Inet4Address.getByName("255.255.255.255");

                    DatagramSocket udp = new DatagramSocket();
                    udp.setBroadcast(true);
                    DatagramPacket dp = new DatagramPacket(sendBuffer, sendBuffer.length, bcIP, 1661);
                    udp.send(dp);
//                    Log.d(TAG, "[Tx]" + content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                * 通知会议发布APP  移除当前临时会议
                *
                */
                try {
                    if (Hawk.contains("meetingId")) {
                        String meeting_id = Hawk.get("meetingId");
                        String meetingRoomName = "";
                        if (Hawk.contains("meetingRoomName")) {
                            meetingRoomName = Hawk.get("meetingRoomName");
                        }
                        String content = "MeetingRelease.destroy." + meeting_id+"."+meetingRoomName;
                        byte[] sendBuffer = content.getBytes();
                        InetAddress bcIP = Inet4Address.getByName("255.255.255.255");
                        DatagramSocket udp = new DatagramSocket();
                        udp.setBroadcast(true);
                        DatagramPacket dp = new DatagramPacket(sendBuffer, sendBuffer.length, bcIP, 1800);
                        udp.send(dp);
//                    Log.d(TAG, "[Tx]" + content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task).start();
    }

    public String getIPAddress() {
        String ipAddress;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ipAddress = inetAddress.getHostAddress().toString();
                            if (!ipAddress.contains("::")) {//ipV6的地址
                                return ipAddress;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPrefe", ex.toString());
        }

        return null;
    }

    /**
     * 接受UDP广播
     */
    public void receiveUDP() {
        if (MythdReceive != null) {
            MythdReceive.resumeThread();
            Log.d(TAG, "UDP receiver is running");
            return;
        }
        MythdReceive = new thdReceive();
        MythdReceive.start();
        isBroadcasting = true;
        Log.d(TAG, "UDP receiver started");
    }

    /**
     * 暂停接受UDP广播
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
