package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2020/11/5.
 */

public class WuHuEditBean implements Serializable {


    /**
     * c_id :
     * meeting_record_id :
     * user_id :
     * file_list : [{"path":"","size":"","name":"","suffix":"","type":""}]
     */
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    private String topics;
    private String topic_type;
    private String line_color;
    private String them_color;
    private List<EditListBean> editListBeanList;

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public String getLine_color() {
        return line_color;
    }

    public void setLine_color(String line_color) {
        this.line_color = line_color;
    }

    public String getThem_color() {
        return them_color;
    }

    public void setThem_color(String them_color) {
        this.them_color = them_color;
    }

    public List<EditListBean> getEditListBeanList() {
        return editListBeanList;
    }

    public void setEditListBeanList(List<EditListBean> editListBeanList) {
        this.editListBeanList = editListBeanList;
    }

    public static class EditListBean {
        private String topics;
        private String topic_type;
        private String line_color;
        private String them_color;

        public String getTopics() {
            return topics;
        }

        public void setTopics(String topics) {
            this.topics = topics;
        }

        public String getTopic_type() {
            return topic_type;
        }

        public void setTopic_type(String topic_type) {
            this.topic_type = topic_type;
        }

        public String getLine_color() {
            return line_color;
        }

        public void setLine_color(String line_color) {
            this.line_color = line_color;
        }

        public String getThem_color() {
            return them_color;
        }

        public void setThem_color(String them_color) {
            this.them_color = them_color;
        }

        private String subTopics;
        private String attendeBean;//汇报单位
        private String attendeBean2;//列席单位

        public String getAttendeBean2() {
            return attendeBean2;
        }

        public void setAttendeBean2(String attendeBean2) {
            this.attendeBean2 = attendeBean2;
        }

        private String temporarySubTopics;

        public String getTemporaryAttendeBean2() {
            return temporaryAttendeBean2;
        }

        public void setTemporaryAttendeBean2(String temporaryAttendeBean2) {
            this.temporaryAttendeBean2 = temporaryAttendeBean2;
        }

        private String temporaryAttendeBean;
        private String temporaryAttendeBean2;
        public String getTemporarySubTopics() {
            return temporarySubTopics;
        }

        public void setTemporarySubTopics(String temporarySubTopics) {
            this.temporarySubTopics = temporarySubTopics;
        }

        public String getTemporaryAttendeBean() {
            return temporaryAttendeBean;
        }

        public void setTemporaryAttendeBean(String temporaryAttendeBean) {
            this.temporaryAttendeBean = temporaryAttendeBean;
        }

        public String getSubTopics() {
            return subTopics;
        }

        public void setSubTopics(String subTopics) {
            this.subTopics = subTopics;
        }

        public String getAttendeBean() {
            return attendeBean;
        }

        public void setAttendeBean(String attendeBean) {
            this.attendeBean = attendeBean;
        }
    }
}
