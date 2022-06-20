package com.example.paperlessmeeting_demo.tool.ScreenTools.entity;

/**
 * 返回一组解析后的数据
 */

public class ReceiveData {
    private ReceiveHeader header;
    private String sendBody;
    private byte[] buff;

    public ReceiveHeader getHeader() {
        return header;
    }

    public void setHeader(ReceiveHeader header) {
        this.header = header;
    }

    public String getSendBody() {
        return sendBody;
    }

    public void setSendBody(String sendBody) {
        this.sendBody = sendBody;
    }

    public byte[] getBuff() {
        return buff;
    }

    public void setBuff(byte[] buff) {
        this.buff = buff;
    }
}
