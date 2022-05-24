package com.example.paperlessmeeting_demo.tool.TempMeetingTools.im;

import com.example.paperlessmeeting_demo.enums.MessageReceiveType;

import java.nio.ByteBuffer;

public class EventScreenMessage {
    private MessageReceiveType type;
    private ByteBuffer bytes;


    public EventScreenMessage(MessageReceiveType type, ByteBuffer bytes) {
        this.type = type;
        this.bytes = bytes;
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

    public ByteBuffer getBytes() {
        return bytes;
    }

    public void setBytes(ByteBuffer bytes) {
        this.bytes = bytes;
    }
}
