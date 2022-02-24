package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/6/7.
 */

public class DeviceTypeBean implements Serializable {


    /**
     * data : [{"_id":"60b45f05efc2d22778190304","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"","name":"空调","icon":"airconditioning"},{"_id":"60b45f05efc2d22778190305","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"","name":"灯光","icon":"light"},{"_id":"60b45f11efc2d22778190306","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"","name":"窗帘","icon":"curtain"},{"_id":"60b4ae6b99bbd133349826de","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"","name":"投影仪","icon":"other-1"},{"_id":"60b86c8cbf50f924ac217e53","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"","name":"投影幕布","icon":"projectionscreen"}]
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
         * _id : 60b45f05efc2d22778190304
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * note :
         * name : 空调
         * icon : airconditioning
         */

        private String _id;
        private String status;
        private String c_id;
        private String note;
        private String name;
        private String icon;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
