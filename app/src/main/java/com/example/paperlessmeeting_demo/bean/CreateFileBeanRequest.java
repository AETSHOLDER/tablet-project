package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2020/11/5.
 */

public class CreateFileBeanRequest implements Serializable {


    /**
     * c_id :
     * meeting_record_id :
     * user_id :
     * file_list : [{"path":"","size":"","name":"","suffix":"","type":""}]
     */

    private String c_id;
    private String meeting_record_id;
    private String user_id;
    private List<FileListBean> file_list;

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getMeeting_record_id() {
        return meeting_record_id;
    }

    public void setMeeting_record_id(String meeting_record_id) {
        this.meeting_record_id = meeting_record_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<FileListBean> getFile_list() {
        return file_list;
    }

    public void setFile_list(List<FileListBean> file_list) {
        this.file_list = file_list;
    }

    public static class FileListBean {
        /**
         * path :
         * size :
         * name :
         * suffix :
         * type :
         */
        private String unclassified;

        public String getUnclassified() {
            return unclassified;
        }

        public void setUnclassified(String unclassified) {
            this.unclassified = unclassified;
        }

        private String path;
        private String size;
        private String name;
        private String suffix;
        private String type;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
