package com.example.paperlessmeeting_demo.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/6/23.
 */

public class MinuiteMeetBean implements Serializable {

    private List<Bitmap> bitmapList;
    private List<MeetingConten >meetingContens;

    public List<MeetingConten> getMeetingContens() {
        return meetingContens;
    }

    public void setMeetingContens(List<MeetingConten> meetingContens) {
        this.meetingContens = meetingContens;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public static class MeetingConten {
        private String tittle;


        public String getTittle() {
            return tittle;
        }

        public void setTittle(String tittle) {
            this.tittle = tittle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        private String content;

    }

}
