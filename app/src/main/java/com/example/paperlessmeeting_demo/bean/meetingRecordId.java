package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class meetingRecordId implements Serializable {
    private String meetingRecordId;

    public String getMeetingRecordId() {
        return meetingRecordId;
    }

    public void setMeetingRecordId(String meetingRecordId) {
        this.meetingRecordId = meetingRecordId;
    }
}
