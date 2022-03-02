package com.example.paperlessmeeting_demo.bean;

public class ItemBean {
    private String text;
    //标识选项时文字还是图片1：文字  2：图片

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
