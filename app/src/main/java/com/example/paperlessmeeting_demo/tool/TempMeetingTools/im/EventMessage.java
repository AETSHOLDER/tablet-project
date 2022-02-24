package com.example.paperlessmeeting_demo.tool.TempMeetingTools.im;

import com.example.paperlessmeeting_demo.enums.MessageReceiveType;

public class EventMessage {
    private MessageReceiveType type;
    private String message;


    public EventMessage(MessageReceiveType type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {

        return "type="+type+"--message= "+message;
    }

    public MessageReceiveType getType() {
        return type;
    }

    public void setType(MessageReceiveType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
