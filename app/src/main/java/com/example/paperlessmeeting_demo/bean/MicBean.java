package com.example.paperlessmeeting_demo.bean;

public class MicBean {
    private String meetingRecordId;
    private String mac;
    private String status;

    public String getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(String meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
