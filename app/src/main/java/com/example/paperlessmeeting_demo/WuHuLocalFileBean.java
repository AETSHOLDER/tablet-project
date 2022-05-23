package com.example.paperlessmeeting_demo;

import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;

import java.io.Serializable;
import java.util.List;
//推送到每次新建传到服务器service的
public class WuHuLocalFileBean implements Serializable {


    private String flag;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    private List<FileBean> fileBeanList;
    private String pos;//用于标识哪个议题文件更新了
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
        private String fileMd5;

        public String getFileMd5() {
            return fileMd5;
        }

        public void setFileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
        }
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
