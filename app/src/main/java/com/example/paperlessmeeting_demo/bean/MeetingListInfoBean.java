package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2020/11/10.
 */

public class MeetingListInfoBean implements Serializable{


    /**
     * data : [{"meeting_room_id":[{"_id":"5f9a2e7b98a9503ed4acfc10","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"党建会议室","address":"蓝莓7栋101室","seating":"10","realisticImg":"upload/img-0e12f314e9bf147c9bd9b0fd002ac185.jpg","guideMap":"upload/tiankong.jpg","layoutImg":"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2Q==","layoutData":null,"check":"0"}],"agenda":["1.数智未来","云计算"],"serveItem":["瓜子","花生","矿泉水"],"user_list":[{"_id":"5fa90f55aac1a70864c41ff3","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"5fa90f4faac1a70864c41ff2","user_id":"5f677602bd0c1f2d906dad63","user_name":"东方不败","user_avatar":"http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg","user_phone":"1236","role":"0","device_id":"","sign_time":"","chair_id":"","sign_code":"1093"},{"_id":"5fa90f56aac1a70864c41ff4","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"5fa90f4faac1a70864c41ff2","user_id":"5f67767ebd0c1f2d906dad66","user_name":"江小鱼","user_avatar":"htt11-10 09:36:07.208 10057-10151/com.example.paperlessmeeting D/OkHttp: p://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg","user_phone":"1236","role":"2","device_id":"","sign_time":"","chair_id":"","sign_code":"7760"},{"_id":"5fa90f57aac1a70864c41ff5","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"5fa90f4faac1a70864c41ff2","user_id":"5f67766bbd0c1f2d906dad65","user_name":"令狐聪","user_avatar":"http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg","user_phone":"1236","role":"2","device_id":"","sign_time":"","chair_id":"","sign_code":"5548"}],"_id":"5fa90f4faac1a70864c41ff2","status":"FINISH","c_id":"5f67708fbd0c1f2d906dad5e","type":"S","name":"云栖大会2020-数智未来全速重构-阿里云","purpose":"数智未来全速重构","remind":"10","sche_start_time":"2020-11-10 17:00:00","sche_end_time":"2021-01-09 18:00:00","actual_start_time":"2020-11-10 17:00:00","actual_end_time":"2021-01-09 18:00:00","from":{"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61"},"layout_data":"abcdefg","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"2020-11-09 17:42:49","receiving_status":""}]
     * code : 0
     * msg : 成功！
     */

        /**
         * meeting_room_id : [{"_id":"5f9a2e7b98a9503ed4acfc10","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"党建会议室","address":"蓝莓7栋101室","seating":"10","realisticImg":"upload/img-0e12f314e9bf147c9bd9b0fd002ac185.jpg","guideMap":"upload/tiankong.jpg","layoutImg":"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2Q==","layoutData":null,"check":"0"}]
         * agenda : ["1.数智未来","云计算"]
         * serveItem : ["瓜子","花生","矿泉水"]
         * user_list : [{"_id":"5fa90f55aac1a70864c41ff3","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"5fa90f4faac1a70864c41ff2","user_id":"5f677602bd0c1f2d906dad63","user_name":"东方不败","user_avatar":"http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg","user_phone":"1236","role":"0","device_id":"","sign_time":"","chair_id":"","sign_code":"1093"},{"_id":"5fa90f56aac1a70864c41ff4","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"5fa90f4faac1a70864c41ff2","user_id":"5f67767ebd0c1f2d906dad66","user_name":"江小鱼","user_avatar":"htt11-10 09:36:07.208 10057-10151/com.example.paperlessmeeting D/OkHttp: p://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg","user_phone":"1236","role":"2","device_id":"","sign_time":"","chair_id":"","sign_code":"7760"},{"_id":"5fa90f57aac1a70864c41ff5","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"5fa90f4faac1a70864c41ff2","user_id":"5f67766bbd0c1f2d906dad65","user_name":"令狐聪","user_avatar":"http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg","user_phone":"1236","role":"2","device_id":"","sign_time":"","chair_id":"","sign_code":"5548"}]
         * _id : 5fa90f4faac1a70864c41ff2
         * status : FINISH
         * c_id : 5f67708fbd0c1f2d906dad5e
         * type : S
         * name : 云栖大会2020-数智未来全速重构-阿里云
         * purpose : 数智未来全速重构
         * remind : 10
         * sche_start_time : 2020-11-10 17:00:00
         * sche_end_time : 2021-01-09 18:00:00
         * actual_start_time : 2020-11-10 17:00:00
         * actual_end_time : 2021-01-09 18:00:00
         * from : {"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61"}
         * layout_data : abcdefg
         * check : 0
         * check_status : ENABLE
         * failure_cause :
         * failure_time :
         * create_time : 2020-11-09 17:42:49
         * receiving_status :
         */

        private String _id;
        private String status;
        private String c_id;
        private String type;
        private String name;
        private String purpose;
        private String remind;
        private String sche_start_time;
        private String sche_end_time;
        private String actual_start_time;
        private String actual_end_time;
        private FromBean from;
        private String layout_data;
        private String check;
        private String check_status;
        private String failure_cause;
        private String failure_time;
        private String create_time;
        private String receiving_status;
        private List<MeetingRoomIdBean> meeting_room_id;
        private List<String> agenda;
        private List<String> serveItem;
        private List<UserListBean> user_list;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getRemind() {
            return remind;
        }

        public void setRemind(String remind) {
            this.remind = remind;
        }

        public String getSche_start_time() {
            return sche_start_time;
        }

        public void setSche_start_time(String sche_start_time) {
            this.sche_start_time = sche_start_time;
        }

        public String getSche_end_time() {
            return sche_end_time;
        }

        public void setSche_end_time(String sche_end_time) {
            this.sche_end_time = sche_end_time;
        }

        public String getActual_start_time() {
            return actual_start_time;
        }

        public void setActual_start_time(String actual_start_time) {
            this.actual_start_time = actual_start_time;
        }

        public String getActual_end_time() {
            return actual_end_time;
        }

        public void setActual_end_time(String actual_end_time) {
            this.actual_end_time = actual_end_time;
        }

        public FromBean getFrom() {
            return from;
        }

        public void setFrom(FromBean from) {
            this.from = from;
        }

        public String getLayout_data() {
            return layout_data;
        }

        public void setLayout_data(String layout_data) {
            this.layout_data = layout_data;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getCheck_status() {
            return check_status;
        }

        public void setCheck_status(String check_status) {
            this.check_status = check_status;
        }

        public String getFailure_cause() {
            return failure_cause;
        }

        public void setFailure_cause(String failure_cause) {
            this.failure_cause = failure_cause;
        }

        public String getFailure_time() {
            return failure_time;
        }

        public void setFailure_time(String failure_time) {
            this.failure_time = failure_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getReceiving_status() {
            return receiving_status;
        }

        public void setReceiving_status(String receiving_status) {
            this.receiving_status = receiving_status;
        }

        public List<MeetingRoomIdBean> getMeeting_room_id() {
            return meeting_room_id;
        }

        public void setMeeting_room_id(List<MeetingRoomIdBean> meeting_room_id) {
            this.meeting_room_id = meeting_room_id;
        }

        public List<String> getAgenda() {
            return agenda;
        }

        public void setAgenda(List<String> agenda) {
            this.agenda = agenda;
        }

        public List<String> getServeItem() {
            return serveItem;
        }

        public void setServeItem(List<String> serveItem) {
            this.serveItem = serveItem;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class FromBean {
            /**
             * _id : 5f67767ebd0c1f2d906dad66
             * name : 江小鱼
             * role : M
             * avatar : upload/default.jpg
             * phone : 1237
             * dept_id : 5f6773f7bd0c1f2d906dad61
             */

            private String _id;
            private String name;
            private String role;
            private String avatar;
            private String phone;
            private String dept_id;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
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

            public String getDept_id() {
                return dept_id;
            }

            public void setDept_id(String dept_id) {
                this.dept_id = dept_id;
            }
        }

        public static class MeetingRoomIdBean {
            /**
             * _id : 5f9a2e7b98a9503ed4acfc10
             * status : ENABLE
             * c_id : 5f67708fbd0c1f2d906dad5e
             * name : 党建会议室
             * address : 蓝莓7栋101室
             * seating : 10
             * realisticImg : upload/img-0e12f314e9bf147c9bd9b0fd002ac185.jpg
             * guideMap : upload/tiankong.jpg
             * layoutImg : data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2Q==
             * layoutData : null
             * check : 0
             */

            private String _id;
            private String status;
            private String c_id;
            private String name;
            private String address;
            private String seating;
            private String realisticImg;
            private String guideMap;
            private String layoutImg;
            private Object layoutData;
            private String check;

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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getSeating() {
                return seating;
            }

            public void setSeating(String seating) {
                this.seating = seating;
            }

            public String getRealisticImg() {
                return realisticImg;
            }

            public void setRealisticImg(String realisticImg) {
                this.realisticImg = realisticImg;
            }

            public String getGuideMap() {
                return guideMap;
            }

            public void setGuideMap(String guideMap) {
                this.guideMap = guideMap;
            }

            public String getLayoutImg() {
                return layoutImg;
            }

            public void setLayoutImg(String layoutImg) {
                this.layoutImg = layoutImg;
            }

            public Object getLayoutData() {
                return layoutData;
            }

            public void setLayoutData(Object layoutData) {
                this.layoutData = layoutData;
            }

            public String getCheck() {
                return check;
            }

            public void setCheck(String check) {
                this.check = check;
            }
        }

        public static class UserListBean {
            /**
             * _id : 5fa90f55aac1a70864c41ff3
             * status : UNCONFIRMED
             * c_id : 5f67708fbd0c1f2d906dad5e
             * meeting_record_id : 5fa90f4faac1a70864c41ff2
             * user_id : 5f677602bd0c1f2d906dad63
             * user_name : 东方不败
             * user_avatar : http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg
             * user_phone : 1236
             * role : 0
             * device_id :
             * sign_time :
             * chair_id :
             * sign_code : 1093
             */

            private String _id;
            private String status;
            private String c_id;
            private String meeting_record_id;
            private String user_id;
            private String user_name;
            private String user_avatar;
            private String user_phone;
            private String role;
            private String device_id;
            private String sign_time;
            private String chair_id;
            private String sign_code;

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

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_phone() {
                return user_phone;
            }

            public void setUser_phone(String user_phone) {
                this.user_phone = user_phone;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getDevice_id() {
                return device_id;
            }

            public void setDevice_id(String device_id) {
                this.device_id = device_id;
            }

            public String getSign_time() {
                return sign_time;
            }

            public void setSign_time(String sign_time) {
                this.sign_time = sign_time;
            }

            public String getChair_id() {
                return chair_id;
            }

            public void setChair_id(String chair_id) {
                this.chair_id = chair_id;
            }

            public String getSign_code() {
                return sign_code;
            }

            public void setSign_code(String sign_code) {
                this.sign_code = sign_code;
            }
        }
}
