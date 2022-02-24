package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/7/28.
 * <p>
 * 用户行为类
 */

public class UserBehaviorBean implements Serializable {

    private String userId;
    private String meetingId;
    private List<DataBean> data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        public String getTittile() {
            return tittile;
        }

        public void setTittile(String tittile) {
            this.tittile = tittile;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        private String tittile;
        private String time;
    }
}
