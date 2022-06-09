package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class FileFragmentationBean implements Serializable {
    private int numberUploads;
    private int allFenPianNum;
    private String pos;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumberUploads() {
        return numberUploads;
    }

    public void setNumberUploads(int numberUploads) {
        this.numberUploads = numberUploads;
    }

    public int getAllFenPianNum() {
        return allFenPianNum;
    }

    public void setAllFenPianNum(int allFenPianNum) {
        this.allFenPianNum = allFenPianNum;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
