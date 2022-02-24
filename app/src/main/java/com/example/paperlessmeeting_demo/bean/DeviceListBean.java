package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/6/9.
 */

public class DeviceListBean implements Serializable {


    /**
     * data : [{"_id":"60c01e90d036f23b447df663","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","note":"备注","name":"会议室灯","mode":"串口/网络","serialPort":"COM3","meeting_room_id":"60769375d7f9a931206be75f","type_id":"60b45f05efc2d22778190305"}]
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
         * _id : 60c01e90d036f23b447df663
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * note : 备注
         * name : 会议室灯
         * mode : 串口/网络
         * serialPort : COM3
         * meeting_room_id : 60769375d7f9a931206be75f
         * type_id : 60b45f05efc2d22778190305
         */


        private String _id;
        private String status;
        private String c_id;
        private String note;
        private String name;
        private String mode;
        private String serialPort;
        private String meeting_room_id;
        private String type_id;
        private String deviceIcon;

        public String getDeviceIcon() {
            return deviceIcon;
        }

        public void setDeviceIcon(String deviceIcon) {
            this.deviceIcon = deviceIcon;
        }

        private List<AgreementBean.DataBean> agreementList;

        public List<AgreementBean.DataBean> getAgreementList() {
            return agreementList;
        }

        public void setAgreementList(List<AgreementBean.DataBean> agreementList) {
            this.agreementList = agreementList;
        }

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

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getSerialPort() {
            return serialPort;
        }

        public void setSerialPort(String serialPort) {
            this.serialPort = serialPort;
        }

        public String getMeeting_room_id() {
            return meeting_room_id;
        }

        public void setMeeting_room_id(String meeting_room_id) {
            this.meeting_room_id = meeting_room_id;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }
    }
}
