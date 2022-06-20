package com.example.paperlessmeeting_demo.tool.TempMeetingTools.im;

import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;

import java.nio.ByteBuffer;

public class EventScreenMessage {
    private MessageReceiveType type;
    private ReceiveData receiveData;


    public EventScreenMessage(MessageReceiveType type, ReceiveData receiveData) {
        this.type = type;
        this.receiveData = receiveData;
    }

    @Override
    public String toString() {

        return "type="+type+"--message= "+"数据区域";
    }

    public MessageReceiveType getType() {
        return type;
    }

    public void setType(MessageReceiveType type) {
        this.type = type;
    }

    public ReceiveData getBytes() {
        return receiveData;
    }

    public void setBytes(ReceiveData receiveData) {
        this.receiveData = receiveData;
    }
}
