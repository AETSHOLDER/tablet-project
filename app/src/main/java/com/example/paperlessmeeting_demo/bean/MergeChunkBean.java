package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

public class MergeChunkBean implements Serializable {

        private String url;
        private Integer index;
        private String meetingId;
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
        }
    }

