package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2020/10/7.
 */

public class AttendeBean implements Serializable {


    /**
     * data : [{"_id":"6076b6d837cfe6288c872588","status":"ENABLE","c_id":{"_id":"5f67708fbd0c1f2d906dad5e","status":"ENABLE","name":"中协智能","ip":"","boss":"1","phone":"18755555555","login_code":"18755555555"},"dept_id":{"_id":"5f6773f7bd0c1f2d906dad61","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"开发部"},"meeting_record_id":"6076b6d837cfe6288c872587","user_id":"5f677602bd0c1f2d906dad63","name":"东方不败","avatar":"http://192.168.11.136:3000/upload/default.jpg","phone":"1234","role":"0","device":{"type":"ppl0","mac":"c400ad789d9","number":"1"},"sign_time":"2021-04-16 10:44:43","sign_code":"5419","note":"IN"},{"_id":"6076b6d837cfe6288c87258a","status":"ENABLE","c_id":{"_id":"5f67708fbd0c1f2d906dad5e","status":"ENABLE","name":"中协智能","ip":"","boss":"1","phone":"18755555555","login_code":"18755555555"},"dept_id":{"_id":"5f6773f7bd0c1f2d906dad61","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"开发部"},"meeting_record_id":"6076b6d837cfe6288c872587","user_id":"5f677640bd0c1f2d906dad64","name":"花满楼","avatar":"http://192.168.11.136:3000/upload/default.jpg","phone":"1235","role":"2","device":{"type":"ppl0","mac":"c400ad789d8","number":"2"},"sign_time":"2021-04-16 10:44:43","sign_code":"6478","note":"IN"},{"_id":"6076b6d837cfe6288c87258c","status":"ENABLE","c_id":{"_id":"5f67708fbd0c1f2d906dad5e","status":"ENABLE","name":"中协智能","ip":"","boss":"1","phone":"18755555555","login_code":"18755555555"},"dept_id":{"_id":"5f6773f7bd0c1f2d906dad61","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"开发部"},"meeting_record_id":"6076b6d837cfe6288c872587","user_id":"5f67766bbd0c1f2d906dad65","name":"令狐聪","avatar":"http://192.168.11.136:3000/upload/default.jpg","phone":"1236","role":"2","device":{"type":"ppl0","mac":"c400ad789d1","number":"3"},"sign_time":"2021-04-16 13:50:46","sign_code":"9796","note":"IN"}]
     * code : 0
     * msg : 成功！
     */


    /**
     * _id : 6076b6d837cfe6288c872588
     * status : ENABLE
     * c_id : {"_id":"5f67708fbd0c1f2d906dad5e","status":"ENABLE","name":"中协智能","ip":"","boss":"1","phone":"18755555555","login_code":"18755555555"}
     * dept_id : {"_id":"5f6773f7bd0c1f2d906dad61","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"开发部"}
     * meeting_record_id : 6076b6d837cfe6288c872587
     * user_id : 5f677602bd0c1f2d906dad63
     * name : 东方不败
     * avatar : http://192.168.11.136:3000/upload/default.jpg
     * phone : 1234
     * role : 0
     * device : {"type":"ppl0","mac":"c400ad789d9","number":"1"}
     * sign_time : 2021-04-16 10:44:43
     * sign_code : 5419
     * note : IN
     */

    private String _id;
    private String status;
    private CIdBean c_id;
    private DeptIdBean dept_id;
    private String meeting_record_id;
    private String user_id;
    private String name;
    private String avatar;
    private String phone;
    private String role;
    private DeviceBean device;
    private String sign_time;
    private String sign_code;
    private String note;

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

    public CIdBean getC_id() {
        return c_id;
    }

    public void setC_id(CIdBean c_id) {
        this.c_id = c_id;
    }

    public DeptIdBean getDept_id() {
        return dept_id;
    }

    public void setDept_id(DeptIdBean dept_id) {
        this.dept_id = dept_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DeviceBean getDevice() {
        return device;
    }

    public void setDevice(DeviceBean device) {
        this.device = device;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_code() {
        return sign_code;
    }

    public void setSign_code(String sign_code) {
        this.sign_code = sign_code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static class CIdBean {
        /**
         * _id : 5f67708fbd0c1f2d906dad5e
         * status : ENABLE
         * name : 中协智能
         * ip :
         * boss : 1
         * phone : 18755555555
         * login_code : 18755555555
         */

        private String _id;
        private String status;
        private String name;
        private String ip;
        private String boss;
        private String phone;
        private String login_code;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getBoss() {
            return boss;
        }

        public void setBoss(String boss) {
            this.boss = boss;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLogin_code() {
            return login_code;
        }

        public void setLogin_code(String login_code) {
            this.login_code = login_code;
        }
    }

    public static class DeptIdBean {
        /**
         * _id : 5f6773f7bd0c1f2d906dad61
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * name : 开发部
         */

        private String _id;
        private String status;
        private String c_id;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class DeviceBean {
        /**
         * type : ppl0
         * mac : c400ad789d9
         * number : 1
         */

        private String type;
        private String mac;
        private String number;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}

