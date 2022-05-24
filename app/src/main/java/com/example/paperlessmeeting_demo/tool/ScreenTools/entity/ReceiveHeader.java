package com.example.paperlessmeeting_demo.tool.ScreenTools.entity;

/**
 * Created by wt
 * Date on  2018/6/12
 *
 * @Desc 接收并解析出数据的头信息
 */

public class ReceiveHeader {
    private int mainCmd;
    private int stringBodylength;
    private int buffSize;

    public ReceiveHeader(int mainCmd, int stringBodylength, int buffSize) {
        this.mainCmd = mainCmd;
        this.stringBodylength = stringBodylength;
        this.buffSize = buffSize;
    }

    public int getMainCmd() {
        return mainCmd;
    }

    public void setMainCmd(int mainCmd) {
        this.mainCmd = mainCmd;
    }

    public int getStringBodylength() {
        return stringBodylength;
    }

    public void setStringBodylength(int stringBodylength) {
        this.stringBodylength = stringBodylength;
    }

    public int getBuffSize() {
        return buffSize;
    }

    public void setBuffSize(int buffSize) {
        this.buffSize = buffSize;
    }
}
