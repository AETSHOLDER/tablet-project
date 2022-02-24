package com.example.paperlessmeeting_demo.bean;

/**
 * Created by 梅涛 on 2020/9/23.
 */

public class FileBean {
    private String name;
    private String path;
    private int resImage;
    private String author;
    private String time;
    private String file_type;

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public FileBean(String name, String path, String author, String time) {
        this.name = name;
        this.path = path;
        this.author = author;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResImage() {
        return resImage;
    }

    public void setResImage(int resImage) {
        this.resImage = resImage;
    }
}
