package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/6/10.
 */

public class AgreementBean implements Serializable{


    /**
     * data : [{"_id":"60c0290ed036f23b447df664","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"关闭设备","name":"关闭","command":"4321","cce_id":"60c01e90d036f23b447df663"}]
     * code : 0
     * msg : 成功！
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 60c0290ed036f23b447df664
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * note : 关闭设备
         * name : 关闭
         * command : 4321
         * cce_id : 60c01e90d036f23b447df663
         */

        private String _id;
        private String status;
        private String c_id;
        private String note;
        private String name;
        private String command;
        private String cce_id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getCce_id() {
            return cce_id;
        }

        public void setCce_id(String cce_id) {
            this.cce_id = cce_id;
        }
    }
}
