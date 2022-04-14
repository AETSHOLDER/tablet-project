package com.example.paperlessmeeting_demo.bean.InitiaBean;

import java.io.Serializable;

public class InitiaMeeting implements Serializable {
    /**
     * meetingId : 605c21678be6c94e8c01fb56
     * meetingRoomInfo : {"_id":"5f9a2e7b98a9503ed4acfc10","name":"党建会议室"}
     * userInfo : {"_id":"605c21678be6c94e8c01fb57","dept_id":"5f6773f7bd0c1f2d906dad61","name":"东方不败","role":"0"}
     * startTime : 1616774400
     * duration : 60
     */

    private String meeting_id;
    private String meeting_name;
    private String start_time;
    private String end_time;

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getMeeting_name() {
        return meeting_name;
    }

    public void setMeeting_name(String meeting_name) {
        this.meeting_name = meeting_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public static class MeetingRoomInfoBean {
        /**
         * _id : 5f9a2e7b98a9503ed4acfc10
         * name : 党建会议室
         */

        private String _id;
        private String name;

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
    }

    public static class UserInfoBean {
        /**
         * _id : 605c21678be6c94e8c01fb57
         * dept_id : 5f6773f7bd0c1f2d906dad61
         * name : 东方不败
         * role : 0
         */

        private String _id;
        private String dept_id;
        private String name;
        private String role;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getDept_id() {
            return dept_id;
        }

        public void setDept_id(String dept_id) {
            this.dept_id = dept_id;
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
    }
}
