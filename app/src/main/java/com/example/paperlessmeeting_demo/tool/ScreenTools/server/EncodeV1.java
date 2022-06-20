package com.example.paperlessmeeting_demo.tool.ScreenTools.server;

import android.text.TextUtils;
import android.util.Log;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.SocketCmd;
import java.nio.ByteBuffer;

/**
 * 传输数据格式
 */

public class EncodeV1 {
    private int mainCmd;
    private String sendContent;
    private byte[] sendBuffer;    //要发送的内容

    /**
     * @param mainCmd  主指令
     * @param sendBuffer 音视频内容
     *   主指令 + 文本长度 +  音视频长度 + 文本内容 + 音视频内容
     */
    public EncodeV1(int mainCmd, byte[] sendBuffer) {
        this.mainCmd = mainCmd;
        switch (mainCmd){
            // 屏幕数据 请求屏幕
            case SocketCmd.SocketCmd_ReqReceiveScreen:
            case  SocketCmd.SocketCmd_ScreentData:
                // SocketCmd_ReqReceiveScreen（4字节）+ Size（4字节）
                this.sendContent = "";
                break;
            //  确认
            case SocketCmd.SocketCmd_RepAccept:
                // SocketCmd_RepAccept（4字节）+ Size（4字节）+“Ready to receive”（20字节）
                this.sendContent = "Ready to receive";
                break;
            //  拒绝
            case SocketCmd.SocketCmd_RepReject_001:
                // SocketCmd_RepAccept（4字节）+ Size（4字节）+“Receiving screen data”（25字节）
                this.sendContent = "Receiving screen data";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + mainCmd);
        }

        this.sendBuffer = sendBuffer;
    }
    public byte[] buildSendContent() {
        int contentLength = 0;
        int bodyByteSize = 0;
        int totalSize = 0;
//        StringBuffer stringBuffer=new StringBuffer();

        ByteBuffer bb = null;
        //文本数据
        if (!TextUtils.isEmpty(sendContent)) {
            contentLength = sendContent.getBytes().length;
        }
        //视频数据
        if (sendBuffer.length != 0) {
            bodyByteSize = sendBuffer.length;
            Log.e("111","bodyByteSize==="+bodyByteSize);
        }
        totalSize = contentLength+bodyByteSize+8;
        //创建内存缓冲区
        switch (mainCmd){
            case SocketCmd.SocketCmd_ReqReceiveScreen:
                // SocketCmd_ReqReceiveScreen（4字节）+ Size（4字节）
                bb = ByteBuffer.allocate(8);
                bb.put(ByteUtil.int2Bytes(mainCmd));  //0-4  主指令
                bb.put(ByteUtil.int2Bytes(totalSize)); //4-8  长度
                break;
            case SocketCmd.SocketCmd_RepAccept:
                // SocketCmd_RepAccept（4字节）+ Size（4字节）+“Ready to receive”（20字节）
                bb = ByteBuffer.allocate(28);
                bb.put(ByteUtil.int2Bytes(mainCmd));
                bb.put(ByteUtil.int2Bytes(totalSize));
                bb.put(ByteUtil.int2Bytes(contentLength));
                if (contentLength != 0) {
                    bb.put(sendContent.getBytes());
                }

                break;
            case SocketCmd.SocketCmd_RepReject_001:
                // SocketCmd_RepAccept（4字节）+ Size（4字节）+“Receiving screen data”（25字节）
                bb = ByteBuffer.allocate(33);
                bb.put(ByteUtil.int2Bytes(mainCmd));
                bb.put(ByteUtil.int2Bytes(totalSize));
                bb.put(ByteUtil.int2Bytes(contentLength));
                if (contentLength != 0) {
                    bb.put(sendContent.getBytes());
                }
                break;
            case SocketCmd.SocketCmd_ScreentData:
                // SocketCmd_ScreentData（4字节）+ Size（4字节）+ 鼠标X坐标（4字节）+ 鼠标Y坐标（4字节）+ AVPacket（未知字节）
                bb = ByteBuffer.allocate(20 + bodyByteSize);
                bb.put(ByteUtil.int2Bytes(mainCmd));
                bb.put(ByteUtil.int2Bytes(20 + bodyByteSize));
                bb.put(ByteUtil.int2Bytes(0));
                bb.put(ByteUtil.int2Bytes(0));
                bb.put(ByteUtil.int2Bytes(bodyByteSize));
                if (sendBuffer.length != 0) {
                    bb.put(sendBuffer);
                }
                break;
        }
        return bb.array();
    }

}
