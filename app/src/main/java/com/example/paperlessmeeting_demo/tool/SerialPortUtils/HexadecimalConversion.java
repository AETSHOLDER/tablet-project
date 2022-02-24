package com.example.paperlessmeeting_demo.tool.SerialPortUtils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.paperlessmeeting_demo.MeetingAPP;

public class HexadecimalConversion {
    private static final String TAG = "HexadecimalConversion";

    /**
     * 串口没打开发送失败消息
     * */
    private static void sendFailMsg(SerialPortListener listener) {
        Activity topActivity = (Activity) ActivityUtils.getTopActivity();
        if (topActivity != null) {
            topActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onMessageResponse("",false);
                }
            });
        }
    }
    /**
     *  心跳轮询
     * */
    public void  setQUERYStatus(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.QUERYEnum.heartBeat;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

    /**
     * 申请发言
    **/
    public void sendApplySpeech(SerialPortListener listener) {

        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.SendCommandEum.APPLYSPEECH;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }
    /**
     * 取消发言
     **/
    public void sendCancelSpeech(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.SendCommandEum.CANCELSPEECH;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

    /**
     * 全体禁言
     **/
    public void sendMuteAll(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.SendCommandEum.MUTEALL;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }
    /**
     * 取消全体禁言
     **/
    public void sendCancelMuteAll(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.SendCommandEum.CANCELMUTEALL;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }
    /**
     * 禁言指定mac
     **/
    public void sendMuteMac(String mac,SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = "4E060701"+mac+"E4";
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }
    /**
     * 取消禁言指定mac
     **/
    public void sendCancelMuteMac(String mac,SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = "4E060700"+mac+"E4";
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

    /**
     * 发言单元本地音量调节  vol =0x00~0x64
     **/
    public void sendVolume(String volume,SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = "4E0A01"+volume+"E4";
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
            Log.d("发言单元本地音量调节",setQUERY);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

    /**
     * 设置主席台
     * 4E 07 06 Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] E4
     **/
    public void setChairMan(String mic,SerialPortListener listener) {
        if(mic.length() != 12){
            return;
        }
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = "4E0706"+mic+"E4";
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }



    /**
     * 屏幕移动
     **/
    public void sendPingStop(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.PingMove.pingSTOP;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

    public void sendPingup(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.PingMove.pingUP;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
            Log.d("发言单元本地音量调节",setQUERY);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }

    }

    public void sendPingDown(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.PingMove.pingDOWN;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

    public void sendPingFront(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.PingMove.pingFRONT;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }
    public void sendPingBack(SerialPortListener listener) {
        if (MeetingAPP.getInstance().getSerialPortClient().isOpen) {
            String setQUERY = SerialConstants.PingMove.pingBACK;
            MeetingAPP.getInstance().getSerialPortClient().sendMsgToPort(setQUERY,listener);
        } else {
            Toast.makeText(MeetingAPP.getInstance(), "串口没打开", Toast.LENGTH_SHORT).show();
            sendFailMsg(listener);
        }
    }

}
