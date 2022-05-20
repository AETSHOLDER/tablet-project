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
   private VoteListBean voteListBean;

    public VoteListBean getVoteListBean() {
        return voteListBean;
    }

    public void setVoteListBean(VoteListBean voteListBean) {
        this.voteListBean = voteListBean;
    }

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
        private List<FileListBean> fileListBeanList;

        public List<FileListBean> getFileListBeanList() {
            return fileListBeanList;
        }

        public void setFileListBeanList(List<FileListBean> fileListBeanList) {
            this.fileListBeanList = fileListBeanList;
        }

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
        private String reportingUnit;//汇报单位
        private String participantUnits;//列席单位

        public String getParticipantUnits() {
            return participantUnits;
        }

        public void setParticipantUnits(String participantUnits) {
            this.participantUnits = participantUnits;
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

        public String getReportingUnit() {
            return reportingUnit;
        }

        public void setReportingUnit(String reportingUnit) {
            this.reportingUnit = reportingUnit;
        }
        public static class FileListBean {
            public FileListBean(String name, String path, String author, String time) {
                this.name = name;
                this.path = path;
                this.author = author;
                this.time = time;
            }
            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getResImage() {
                return resImage;
            }

            public void setResImage(int resImage) {
                this.resImage = resImage;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getFile_type() {
                return file_type;
            }

            public void setFile_type(String file_type) {
                this.file_type = file_type;
            }

            public String getPos() {
                return pos;
            }

            public void setPos(String pos) {
                this.pos = pos;
            }

            public String getSuffix() {
                return suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            private String name;
            private String path;
            private int resImage;
            private String author;
            private String time;
            private String file_type;
            private String pos;//芜湖-标识是哪一个fragment
            private String suffix;
            private boolean isNet;
            private String flag;//1  分享  2  推送
            private String fileMd5;
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public String getFileMd5() {
                return fileMd5;
            }

            public void setFileMd5(String fileMd5) {
                this.fileMd5 = fileMd5;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isNet() {
                return isNet;
            }

            public void setNet(boolean net) {
                isNet = net;
            }
        }
    }

}
