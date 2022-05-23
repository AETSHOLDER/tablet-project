package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class SharePushFileBean implements Serializable {
    private String pos;//议题编号
    private  boolean isHave;//是否有文件
    private String mac;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public boolean isHave() {
        return isHave;
    }

    public void setHave(boolean have) {
        isHave = have;
    }
}
