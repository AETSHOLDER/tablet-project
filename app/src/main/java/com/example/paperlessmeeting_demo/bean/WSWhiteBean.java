package com.example.paperlessmeeting_demo.bean;


import java.util.ArrayList;

public class WSWhiteBean {
    private String type;
    private ArrayList<String> user_list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getUser_list() {
        return user_list;
    }

    public void setUser_list(ArrayList<String> user_list) {
        this.user_list = user_list;
    }
}
