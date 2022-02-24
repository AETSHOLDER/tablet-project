package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2021/7/6.
 */

public class HandWritingBean implements Serializable {

    private String meetingRecordId;
    private String sendUserId;
    private byte[] data;
    private String baseData;

    public String getBaseData() {
        return baseData;
    }

    public void setBaseData(String baseData) {
        this.baseData = baseData;
    }
    /*    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
       private byte[] data;*/

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(String meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }
}
