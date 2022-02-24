package com.example.paperlessmeeting_demo.tool.SerialPortUtils;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.util.SerialPortUtil;

public class SerialConstants {
    interface QUERYEnum {
        String heartBeat          = "4E080100E4";
    }

    interface SendCommandEum {
        String REBOOT = "4E010100E4";           // 设备重启

        String APPLYSPEECH  = "4E040101E4";      // 申请发言
        String CANCELSPEECH = "4E040100E4";      // 取消发言

        String MUTEALL = "4E050101E4";          // 全体禁言
        String CANCELMUTEALL = "4E050100E4";    // 取消全体禁言



    }

    //无纸化屏上升下降等
    /**
     * 无纸化屏：4E 09 01 cmd E4（cmd=0x01前仰，0x02后仰，0x03下降，0x04停止，0x05上升）
     * **/
    interface PingMove {
        String pingUP     = "4E090105E4";   // 上升
        String pingDOWN   = "4E090103E4";   // 下降
        String pingSTOP   = "4E090104E4";   // 停止
        String pingFRONT  = "4E090101E4";   // 前仰
        String pingBACK   = "4E090102E4";   // 后仰
    }

    /**
     *串口回调是否成功
     */
    public enum SetOkEnum {
        heartBeat,          // 心跳

        MUTEALL,            // 全体禁言
        CANCELMUTEALL,      // 取消全体禁言

        APPLYSPEECH,        // 申请发言
        CANCELSPEECH,       // 取消发言

        muteMic,            // 禁言指定mic
        cancelMuteMic,      // 取消禁言指定mic
        volume,             // 本地发言单元音量设置
    }

    static public boolean isSetOk(SetOkEnum type, String msg) {
        if(StringUtils.isEmpty(msg)){
            return false;
        }
        switch (type) {
            case heartBeat:
                /**
                 *  设备回复：4E 08 0F Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] IP[0] IP[1] IP[2] IP[3] N Mode Port[0] Port[1] Sta E4
                 *  （设备MAC地址+IP地址+机号+工作模式（0x00(1:1)，0x01(1:3)+当前音频端口号（0x0000空闲状态，Port发言状态下的音频端口号）+设备状态（Sta=0x01空闲，0x02发言，0x03禁言））
                 * */
                return SerialPortUtil.isHeartBeatOK(msg);

            case MUTEALL:
                return SerialPortUtil.isMuteAll(msg);

            case CANCELMUTEALL:
                return SerialPortUtil.isCancelMuteAll(msg);

            case APPLYSPEECH: //失败发言（未抢到通道）：Port[0]和Port[1]取值为0x00）4E 04 02 Port[0] Port[1] E4
                return !StringUtils.equalsIgnoreCase(msg,"4E04020000E4");

            case CANCELSPEECH:
                return StringUtils.equalsIgnoreCase(msg,SendCommandEum.CANCELSPEECH);

            case muteMic:
                return SerialPortUtil.isMuteMicOK(msg);

            case cancelMuteMic:
                return SerialPortUtil.isCancelMuteMicOK(msg);

            case volume:
                return SerialPortUtil.isVolumeSetOK(msg);


                default:
                    return false;
        }
    }

    /**
     * 主席台是否设置成功
     *  4E 07 06 Mac[0] Mac[1] Mac[2] Mac[3] Mac[4] Mac[5] E4
     * */
    static boolean isChairManSetOk(String mic ,String msg) {
        if(StringUtils.isEmpty(msg)){
            return false;
        }
        String returnMic = msg.substring(6,18).toLowerCase();
        return StringUtils.equalsIgnoreCase(mic,returnMic);
    }

}
