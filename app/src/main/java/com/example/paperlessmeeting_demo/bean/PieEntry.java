package com.example.paperlessmeeting_demo.bean;

import java.util.List;

public class PieEntry {
    //颜色
    private int color;
    //比分比
    private float percentage;
    //条目名
    private String label;
    //扇区起始角度
    private float currentStartAngle;
    //扇区总角度
    private float sweepAngle;
    private List<String> chooseUserList;  // 选择当前选项的用户name集合
    public PieEntry(float percentage, String label) {
        this.percentage = percentage;
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public float getCurrentStartAngle() {
        return currentStartAngle;
    }

    public void setCurrentStartAngle(float currentStartAngle) {
        this.currentStartAngle = currentStartAngle;
    }

    public List<String> getChooseUserList() {
        return chooseUserList;
    }

    public void setChooseUserList(List<String> chooseUserList) {
        this.chooseUserList = chooseUserList;
    }
}
