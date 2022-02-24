package com.example.paperlessmeeting_demo.bean;

import com.github.guanpy.wblib.bean.DrawPenStr;
import java.util.ArrayList;

public class WSWhiteBoardBean {
    private String type;
    private String user_name;
    private DrawPenStr drawPenStr;
    private ArrayList<String> user_list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public DrawPenStr getDrawPenStr() {
        return drawPenStr;
    }

    public void setDrawPenStr(DrawPenStr drawPenStr) {
        this.drawPenStr = drawPenStr;
    }

    public ArrayList<String> getUser_list() {
        return user_list;
    }

    public void setUser_list(ArrayList<String> user_list) {
        this.user_list = user_list;
    }


}

