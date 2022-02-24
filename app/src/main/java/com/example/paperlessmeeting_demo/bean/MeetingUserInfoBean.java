package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class MeetingUserInfoBean implements Serializable {
    private String role;  //  0:主席  1：书记员  2 ：参会人员  3:服务人员  4：外来人员

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static class meeting_recordBean {

        private String meeting_record_id;

        public String getMeeting_record_id() {
            return meeting_record_id;
        }

        public void setMeeting_record_id(String meeting_record_id) {
            this.meeting_record_id = meeting_record_id;
        }
    }
}

