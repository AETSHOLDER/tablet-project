package com.example.paperlessmeeting_demo.util;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;

public class SerialPortUtil {
    /**
     * 协议基础
     * 起始帧（0x4E）+ 命令码 + 数据长度 + 数据 + 结尾（0xE4）（数据类型为uchar）
     */
    public static boolean isOK(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return false;
        }
        if(!msg.contains("4E") ||  !msg.contains("E4")){
            return false;
        }

        int dataLenth = Integer.parseInt(msg.substring(4, 6), 16) * 2;
        int startIndex = 6 + dataLenth;
        int endIndex = startIndex + 2;

        if (msg.substring(0, 2).equalsIgnoreCase("4E") && msg.substring(startIndex, endIndex).equalsIgnoreCase("E4")) {
            return true;
        } else {
            return false;
        }
    }


    //  warning 发言单元没联网初始化，串口会自己返回发送的命令  4E 08 01 00 E4

    /**
     * 单片机回复的是否是心跳
     * 设备回复：4E 08 0F Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] IP[0] IP[1] IP[2] IP[3] N Mode Port[0] Port[1] Sta E4
     * （设备MAC地址+IP地址+机号+工作模式（0x00(1:1)，0x01(1:3)+当前音频端口号（0x0000空闲状态，Port发言状态下的音频端口号）+设备状态（Sta=0x01空闲，0x02发言，0x03禁言））
     */
    public static boolean isHeartBeatOK(String msg) {
        if (isOK(msg)) {
            Log.e("msg", msg);
            Log.e("isHeartBeatOK", msg.substring(2, 4).equalsIgnoreCase("08") + "");
            return msg.substring(2, 4).equalsIgnoreCase("08");
        } else {
            return false;
        }
    }

    /**
     * 全体禁言
     * 4E050101E4
     */
    public static boolean isMuteAll(String msg) {
        if (isOK(msg)) {
            return msg.substring(2, 8).equalsIgnoreCase("050101");
        } else {
            return false;
        }
    }

    /**
     * 取消全体禁言
     * 4E050100E4
     */
    public static boolean isCancelMuteAll(String msg) {
        if (isOK(msg)) {
            return msg.substring(2, 8).equalsIgnoreCase("050100");
        } else {
            return false;
        }
    }

    /**
     * 指定禁言
     * 4E 06 07 sta Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] E4（主席台成功发送命令回复，非主席台不回复）
     * sta：取值0x00为取消指定禁言，取值0x01为执行指定禁言
     */
    public static boolean isMuteMicOK(String msg) {
        if (isOK(msg)) {
            return msg.substring(2, 4).equalsIgnoreCase("06") && msg.substring(6, 8).equalsIgnoreCase("01");
        } else {
            return false;
        }
    }

    /**
     * 取消指定禁言
     */
    public static boolean isCancelMuteMicOK(String msg) {
        if (isOK(msg)) {
            return msg.substring(2, 4).equalsIgnoreCase("06") && msg.substring(6, 8).equalsIgnoreCase("00");
        } else {
            return false;
        }
    }


    /**
     * 音量调节 4E 0A 01 vol E4（vol =0x00~0x64）
     */
    public static boolean isVolumeSetOK(String msg) {
        if (isOK(msg)) {
            return msg.substring(2, 6).equalsIgnoreCase("0A01");
        } else {
            return false;
        }
    }

    /**
     * 主席台是否设置成功
     * sendMsg 发送的信息 msg：接受的信息
     * 4E 07 06 Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] E4（MAC地址一致则设备成为主席台，否则退出主席台位置）
     */
    public static boolean isChairManSetOK(String sendMsg, String msg) {
        if (isOK(msg)) {
            return msg.substring(6, 18).equalsIgnoreCase(sendMsg);
        } else {
            return false;
        }
    }

}
