package com.example.paperlessmeeting_demo;

import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;

import java.io.Serializable;
import java.util.List;

public class WuHuLocalFileBean implements Serializable {


    private String flag;
    private List<FileBean> fileBeanList;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<FileBean> getFileBeanList() {
        return fileBeanList;
    }

    public void setFileBeanList(List<FileBean> fileBeanList) {
        this.fileBeanList = fileBeanList;
    }

    public static class FileBean {
        private String pos;
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }
    }
}
