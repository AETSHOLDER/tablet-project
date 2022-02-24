package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2021/7/20.
 */

public class CameraInfoBean implements Serializable {


    /**
     * code : 1
     * data : {"ip":"192.168.8.88","positionCount":30}
     */

    /**
     * ip : 192.168.8.88
     * positionCount : 30
     */

    private String ip;
    private int positionCount;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

}
