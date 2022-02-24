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

    private String meetingId;
    private MeetingRoomInfoBean meetingRoomInfo;
    private UserInfoBean userInfo;
    private int startTime;
    private int duration;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public MeetingRoomInfoBean getMeetingRoomInfo() {
        return meetingRoomInfo;
    }

    public void setMeetingRoomInfo(MeetingRoomInfoBean meetingRoomInfo) {
        this.meetingRoomInfo = meetingRoomInfo;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
