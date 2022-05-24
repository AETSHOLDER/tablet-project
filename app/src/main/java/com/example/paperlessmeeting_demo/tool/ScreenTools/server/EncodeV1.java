package com.example.paperlessmeeting_demo.tool.ScreenTools.server;

import android.text.TextUtils;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.ByteUtil;
import java.nio.ByteBuffer;

/**
 * Created by wt
 * Date on  2018/6/4  .
 * 传输数据格式
 */

public class EncodeV1 {
    private int mainCmd;
    private String sendBody;
    private byte[] sendBuffer;    //要发送的内容

    /**
     * by wt
     *
     * @param mainCmd  主指令
     * @param sendBody 文本内容
     * @param sendBuffer 音视频内容
     *   主指令 + 文本长度 +  音视频长度 + 文本内容 + 音视频内容
     */
    public EncodeV1(int mainCmd, String sendBody, byte[] sendBuffer) {
        this.mainCmd = mainCmd;
        this.sendBody = sendBody;
        this.sendBuffer = sendBuffer;
    }
    public byte[] buildSendContent() {
        int bodyLength = 0;
        int bodyByte = 0;
        ByteBuffer bb = null;
        //文本数据
        if (!TextUtils.isEmpty(sendBody)) {
            bodyLength = sendBody.getBytes().length;
        }
        //视频数据
        if (sendBuffer.length != 0) {
            bodyByte = sendBuffer.length;
        }
        //创建内存缓冲区
        bb = ByteBuffer.allocate(12 + bodyLength + bodyByte);

        bb.put(ByteUtil.int2Bytes(mainCmd));  //1-4  主指令
        bb.put(ByteUtil.int2Bytes(bodyLength));  //5-8位,文本数据长度
        bb.put(ByteUtil.int2Bytes(bodyByte));  //9-12位,音视频数据长度
        //数据字节数组
        if (bodyLength != 0) {
            bb.put(sendBody.getBytes());
        }
        if (sendBuffer.length != 0) {
            bb.put(sendBuffer);
        }
        return bb.array();
    }

}
