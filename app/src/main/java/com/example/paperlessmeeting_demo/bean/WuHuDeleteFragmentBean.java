package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class WuHuDeleteFragmentBean implements Serializable {

    private String listSize;
    private String listPosition;

    public String getListSize() {
        return listSize;
    }

    public void setListSize(String listSize) {
        this.listSize = listSize;
    }

    public String getListPosition() {
        return listPosition;
    }

    public void setListPosition(String listPosition) {
        this.listPosition = listPosition;
    }
}
