package com.example.paperlessmeeting_demo.tool.SerialPortUtils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.paperlessmeeting_demo.tool.FLUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android_serialport_api.SerialPort;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.stick.AbsStickPackageHelper;
import tp.xmaihh.serialport.stick.BaseStickPackageHelper;
import tp.xmaihh.serialport.utils.ByteUtil;
public class mySerialHelper {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private String sPort = "/dev/ttyS1";
    private int iBaudRate = 9600;
    private int stopBits = 1;
    private int dataBits = 8;
    private int parity = 0;
    private int flowCon = 0;
    private int flags = 0;
    private boolean _isOpen = false;
    private byte[] _bLoopData = {48};
    private int iDelay = 800;  //等待800ms，无停止时间操作则记为超时
    private boolean isInterrupted = false;

    private SerialPortListener mlistener;

    public mySerialHelper(String sPort, int iBaudRate) {
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Activity topActivity = (Activity) ActivityUtils.getTopActivity();
            switch(msg.what){
                case 0:
                    Log.e("sucess", "收到数据");
                    timer.cancel();
                    isInterrupted = true;
                    String recMsg = (String) msg.obj;
                    if (topActivity != null) {
                        topActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mlistener.onMessageResponse(recMsg, true);
                            }
                        });
                    }
                    break;
                case 1:
                    Log.e("error", "发生错误");
                    timer.cancel();
                    isInterrupted = true;
                    if (topActivity != null) {
                        topActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mlistener.onMessageResponse("",false);
                            }
                        });
                    }

                    break;
                case 2:
                    Log.e("error", "超时");
                    timer.cancel();
                    isInterrupted = true;
                    if (topActivity != null) {
                        topActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mlistener.onMessageResponse("",false);
                            }
                        });
                    }
                    break;
            }

            return false;
        }
    });

    //超时
    private Timer timer;
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(2);
        }
    }

    private void checkTimeOut(){
        try{
            this.timer = new Timer();
            MyTimerTask task = new MyTimerTask();
            this.timer.schedule(task, iDelay); //如等待800ms，无停止时间操作则记为超时
        }catch(Exception e){
            Log.e("--------", e.getMessage());
        }
    }

    public void open()
            throws SecurityException, IOException, InvalidParameterException {
        this.mSerialPort = new SerialPort(new File(this.sPort), this.iBaudRate, this.stopBits, this.dataBits, this.parity, this.flowCon, this.flags);
        this.mOutputStream = this.mSerialPort.getOutputStream();
        this.mInputStream = this.mSerialPort.getInputStream();
        this._isOpen = true;
    }

    public void close() {
        isInterrupted = true;

        mHandler.removeCallbacksAndMessages(null);
        if (this.timer != null) {
            this.timer.cancel();
        }
        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
        try {
            if (this.mInputStream != null) {
                this.mInputStream.close();
            }
            if (this.mOutputStream != null) {
                this.mOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this._isOpen = false;
    }

    public void send(final byte[] bOutArray,final SerialPortListener listener) {
        mlistener = listener;
        Callable<Void> sendInfoCallable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                mOutputStream.write(bOutArray);
                return null;
            }
        };

        Callable<Void> readDataCallable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                isInterrupted = false;
                //设置超时
                checkTimeOut();
                Message message = new Message();

                while (!isInterrupted) {
                    Log.e("readDataCallable", "已执行循环操作");
                    try {
                        if (mySerialHelper.this.mInputStream == null) {
                            Log.e("readDataCallable", "无输入流");
                            continue;
                        }
                        byte[] buffer = getStickPackageHelper().execute(mySerialHelper.this.mInputStream);
                        if (buffer != null && buffer.length > 0) {
                            ComBean ComRecData = new ComBean(mySerialHelper.this.sPort, buffer, buffer.length);
                            String msg = ByteUtil.ByteArrToHex(ComRecData.bRec);

                            // 去除后面的多余的0
                            // 你也可以从最后一位开始判断，直到遇到一个字符不等于0的字符，然后记住这个位置
                            String reverseStr = new StringBuilder(msg).reverse().toString();
                            int index = FLUtil.getFirst(reverseStr,"0");

                            String finalMsg = msg.substring(0,msg.length() -index);

                            message.what = 0;
                            message.obj = finalMsg;
                            Log.e("readDataCallable", "收到数据");

//                            Activity topActivity = (Activity) ActivityUtils.getTopActivity();
//                            if (topActivity != null) {
//                                topActivity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        listener.onMessageResponse(msg, true);
//                                    }
//                                });
//                            }
                            mHandler.sendMessage(message);
                        }
//                        else {
//                            Log.e("readDataCallable", "buffer = null");
//                            listener.onMessageResponse("",false);
//                        }
                    } catch (Throwable e) {
                        Log.e("Throwable error", e.getMessage());


                        mHandler.sendEmptyMessage(1);
                    }
                }
                Log.e("readDataCallable","while 循环外是否执行");

                return null;
            }
        };
        FutureTask<Void> sendInfoFutureTask = new FutureTask<>(sendInfoCallable);
        FutureTask<Void> readDataFutureTask = new FutureTask<>(readDataCallable);

        new Thread(sendInfoFutureTask).start();
        new Thread(readDataFutureTask).start();


    }

    public void sendHex(String sHex,final SerialPortListener listener) {
        byte[] bOutArray = ByteUtil.HexToByteArr(sHex);
        send(bOutArray,listener);
    }

    public void sendTxt(String sTxt,final SerialPortListener listener) {
        byte[] bOutArray = sTxt.getBytes();
        send(bOutArray,listener);
    }

    public int getBaudRate() {
        return this.iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        if (this._isOpen) {
            return false;
        }
        this.iBaudRate = iBaud;
        return true;
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return setBaudRate(iBaud);
    }

    public int getStopBits() {
        return this.stopBits;
    }

    public boolean setStopBits(int stopBits) {
        if (this._isOpen) {
            return false;
        }
        this.stopBits = stopBits;
        return true;
    }

    public int getDataBits() {
        return this.dataBits;
    }

    public boolean setDataBits(int dataBits) {
        if (this._isOpen) {
            return false;
        }
        this.dataBits = dataBits;
        return true;
    }

    public int getParity() {
        return this.parity;
    }

    public boolean setParity(int parity) {
        if (this._isOpen) {
            return false;
        }
        this.parity = parity;
        return true;
    }

    public int getFlowCon() {
        return this.flowCon;
    }

    public boolean setFlowCon(int flowCon) {
        if (this._isOpen) {
            return false;
        }
        this.flowCon = flowCon;
        return true;
    }

    public String getPort() {
        return this.sPort;
    }

    public boolean setPort(String sPort) {
        if (this._isOpen) {
            return false;
        }
        this.sPort = sPort;
        return true;
    }

    public boolean isOpen() {
        return this._isOpen;
    }

    public byte[] getbLoopData() {
        return this._bLoopData;
    }

    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    public void setHexLoopData(String sHex) {
        this._bLoopData = ByteUtil.HexToByteArr(sHex);
    }

    public int getiDelay() {
        return this.iDelay;
    }

    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    private AbsStickPackageHelper mStickPackageHelper = new BaseStickPackageHelper();  // 默认不处理粘包，直接读取返回

    public AbsStickPackageHelper getStickPackageHelper() {
        return mStickPackageHelper;
    }

    public void setStickPackageHelper(AbsStickPackageHelper mStickPackageHelper) {
        this.mStickPackageHelper = mStickPackageHelper;
    }
}
