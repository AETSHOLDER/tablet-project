package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2021/7/21.
 */

public class StreamStatusBean implements Serializable {


    /**
     * code : 1
     * data : pushing
     */

    private int code;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
