package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2020/9/21.
 */

public class HomeGridBean implements Serializable {
    public HomeGridBean(int res, String name, int id) {
        this.res = res;
        this.name = name;
        this.id = id;
    }

    private int res;
    private String name;

    private int id;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
