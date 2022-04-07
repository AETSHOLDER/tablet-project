package com.example.paperlessmeeting_demo.activity.Sign.Bean;

import android.graphics.Bitmap;

public class SignThumbBean {
    private String creatTime;  // 创建时间
    private String path;      // 路径
    private Bitmap thumb;

    public SignThumbBean(String creatTime, String path, Bitmap thumb) {
        this.creatTime = creatTime;
        this.path = path;
        this.thumb = thumb;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }



    public SignThumbBean() {
    }




}
