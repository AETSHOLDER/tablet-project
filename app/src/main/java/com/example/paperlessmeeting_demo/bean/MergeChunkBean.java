package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class MergeChunkBean implements Serializable {

   private String fileName;
   private String dirName;
   private int size;
   private String updateFileList;
   private int  index;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUpdateFileList() {
        return updateFileList;
    }

    public void setUpdateFileList(String updateFileList) {
        this.updateFileList = updateFileList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
