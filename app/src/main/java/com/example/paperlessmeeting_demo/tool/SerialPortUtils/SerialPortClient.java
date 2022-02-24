package com.example.paperlessmeeting_demo.tool.SerialPortUtils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.tool.FLUtil;

import java.io.IOException;
import android_serialport_api.SerialPortFinder;

public class SerialPortClient {
    private static final String TAG = "SerialPortClient";
    private String sPort = "/dev/ttyS4";
    private int botes = 9600;   // 设置波特率
    private SerialPortFinder serialPortFinder;
    private mySerialHelper serialHelper;
    private boolean isOpening = false;//是否正在连接
    public boolean isOpen = false;//判断是否连接了
    /**
     * 打开串口  [/dev/ttyUSB0, /dev/ttyUSB1, /dev/ttyS3, /dev/ttyS0, /dev/ttyS2, /dev/ttyS4, /dev/ttyS1, /dev/ttyFIQ0]
     *
     * 串口配置：1、波特率：9600；2、数据位：8；3、停止位：1；
     */
    public void openPort() { // String sPort ,int bauldRate
        if (isOpening) {
            return;
        }
        if (!isOpen) {
            isOpening = true;
            //连接监听
            if(serialHelper != null){
                serialHelper.close();
            }
            serialHelper = new mySerialHelper(sPort,botes);
            serialHelper.close();
            serialHelper.setPort(sPort);      //  串口
            serialHelper.setBaudRate(botes);  //  波特率
            serialHelper.setDataBits(8);      //  数据位
            serialHelper.setStopBits(1);      //  停止位

            try {
                serialHelper.open();
                isOpen = true;
//                mHandler.post(heartBeatRunnable);
                mHandler.postDelayed(heartBeatRunnable,10000);
            } catch (IOException e) {
                Log.d(TAG,"串口未打开成功!");
                e.printStackTrace();
                isOpen = false;
            } catch (SecurityException ex) {
                Log.d(TAG,"串口未打开成功! SecurityException"+ex.getMessage());
                isOpen = false;
            }
        }
    }

    //    -------------------------------------串口心跳检测------------------------------------------------
    private final long HEART_BEAT_RATE = 30 * 1000;//每隔10秒进行一次心跳检测
    private Handler mHandler = new Handler();
    private int reconnectCount = 10;
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "心跳包检测串口连接状态");
            if (serialHelper != null) {
                if (isOpen) {
                    if(reconnectCount==0){
                        isOpen = false;
                        closeSerialPort();
                        return;
                    }
                    //每隔一定的时间，对长连接进行一次心跳检测
                    mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
                    // 发送心跳
                    MeetingAPP.getInstance().getHexadecimal().setQUERYStatus(new SerialPortListener() {
                        @Override
                        public void onMessageResponse(String msg, boolean isComplete) {
                            Log.e(TAG,"心跳 isComplete=="+isComplete + " 心跳=="+msg);
                            if(isComplete){
                                if(!StringUtils.isEmpty(msg)){
                                    /**
                                     * 4E 08 0F Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] IP[0] IP[1] IP[2] IP[3] N Mode Port[0] Port[1] Sta E4
                                     * （设备MAC地址+IP地址+机号+工作模式（0x00(1:1)，0x01(1:3)+当前音频端口号（0x0000空闲状态，Port发言状态下的音频端口号）+设备状态（Sta=0x01空闲，0x02发言，0x03禁言））
                                     * */
                                    FLUtil.dealMacInfo(msg);
                                    reconnectCount = 10;
                                    isOpen = true;
                                }
                            }else {
                                reconnectCount--;
//                                // 未收到串口返回的心跳信息
////                                //  重连
////                                if(reconnectCount>0){
////                                    reconnectSP();
////                                }else {
////                                    closeSerialPort();
////                                }
                            }
                        }
                    });

                } else {
                    isOpen = false;
                    reconnectCount--;
//                    //  重连
//                    if(reconnectCount>0){
//                        reconnectSP();
//                    }else {
//                        closeSerialPort();
//                    }
                }
            } else {
                //如果serialHelper已为空，重新初始化连接
                openPort();
            }
        }
    };

    /**
     * 开启重连
     */
    private void reconnectSP() {
        closeSerialPort();
        isOpening = false;
        reconnectCount--;
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Log.d(TAG, "开启重连");
                openPort();
                Looper.loop();
            }
        }.start();
    }


    /**
     * 关闭串口
     * 关闭串口中的输入输出流
     */
    public void closeSerialPort() {
        Log.i("close", "关闭串口");
        isOpening = false;
        isOpen = false;
        if(serialHelper != null){
            serialHelper.close();
            serialHelper = null;
        }

        if(mHandler!=null){
            mHandler.removeCallbacks(heartBeatRunnable);
        }
    }

    /**
     *  发送消息  二进制类型  4E 01 01 00 E4
     *  串口未打开，尝试打开串口
     */
    public void sendMsgToPort(String msg,SerialPortListener listener ) {
        if (msg.length() > 0) {
            if (serialHelper.isOpen()) {
//                serialHelper.sendHex(msg);
                serialHelper.sendHex(msg,listener);

            } else {
                Log.e(TAG,"先打开串口!");
                openPort();  //  尝试打开串口
            }
        } else {
            Log.e(TAG,"先填数据!");
        }
    }

}
