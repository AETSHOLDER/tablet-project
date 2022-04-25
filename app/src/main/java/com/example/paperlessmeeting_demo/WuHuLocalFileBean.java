package com.example.paperlessmeeting_demo;

import java.io.Serializable;

public class WuHuLocalFileBean implements Serializable {

    private String fragmentPos;
    private String flag;

    public String getFragmentPos() {
        return fragmentPos;
    }

    public void setFragmentPos(String fragmentPos) {
        this.fragmentPos = fragmentPos;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
