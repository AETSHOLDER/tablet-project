package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2020/11/5.
 */
//芜湖根据日期请求当天会议列表
public class WuHuMeetingListResponse implements Serializable {


        private String _id;
        private String name;
        private ContentDTO content;
        private String startTime;
        private Integer __v;

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

        public ContentDTO getContent() {
            return content;
        }

        public void setContent(ContentDTO content) {
            this.content = content;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Integer get__v() {
            return __v;
        }

        public void set__v(Integer __v) {
            this.__v = __v;
        }

        public static class ContentDTO {
            private List<EditListBeanListDTO> editListBeanList;
            private String line_color;
            private String startTime;
            private String them_color;
            private String topic_type;
            private String topics;

            public List<EditListBeanListDTO> getEditListBeanList() {
                return editListBeanList;
            }

            public void setEditListBeanList(List<EditListBeanListDTO> editListBeanList) {
                this.editListBeanList = editListBeanList;
            }

            public String getLine_color() {
                return line_color;
            }

            public void setLine_color(String line_color) {
                this.line_color = line_color;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getThem_color() {
                return them_color;
            }

            public void setThem_color(String them_color) {
                this.them_color = them_color;
            }

            public String getTopic_type() {
                return topic_type;
            }

            public void setTopic_type(String topic_type) {
                this.topic_type = topic_type;
            }

            public String getTopics() {
                return topics;
            }

            public void setTopics(String topics) {
                this.topics = topics;
            }

            public static class EditListBeanListDTO {
                private String line_color;
                private String participantUnits;
                private String pos;
                private String reportingUnit;
                private String subTopics;
                private String temporaryAttendeBean;
                private String temporaryAttendeBean2;
                private String temporarySubTopics;
                private String them_color;
                private String topic_type;
                private String topics;
                private List<LocalFilesDTO> localFiles;

                public String getLine_color() {
                    return line_color;
                }

                public void setLine_color(String line_color) {
                    this.line_color = line_color;
                }

                public String getParticipantUnits() {
                    return participantUnits;
                }

                public void setParticipantUnits(String participantUnits) {
                    this.participantUnits = participantUnits;
                }

                public String getPos() {
                    return pos;
                }

                public void setPos(String pos) {
                    this.pos = pos;
                }

                public String getReportingUnit() {
                    return reportingUnit;
                }

                public void setReportingUnit(String reportingUnit) {
                    this.reportingUnit = reportingUnit;
                }

                public String getSubTopics() {
                    return subTopics;
                }

                public void setSubTopics(String subTopics) {
                    this.subTopics = subTopics;
                }

                public String getTemporaryAttendeBean() {
                    return temporaryAttendeBean;
                }

                public void setTemporaryAttendeBean(String temporaryAttendeBean) {
                    this.temporaryAttendeBean = temporaryAttendeBean;
                }

                public String getTemporaryAttendeBean2() {
                    return temporaryAttendeBean2;
                }

                public void setTemporaryAttendeBean2(String temporaryAttendeBean2) {
                    this.temporaryAttendeBean2 = temporaryAttendeBean2;
                }

                public String getTemporarySubTopics() {
                    return temporarySubTopics;
                }

                public void setTemporarySubTopics(String temporarySubTopics) {
                    this.temporarySubTopics = temporarySubTopics;
                }

                public String getThem_color() {
                    return them_color;
                }

                public void setThem_color(String them_color) {
                    this.them_color = them_color;
                }

                public String getTopic_type() {
                    return topic_type;
                }

                public void setTopic_type(String topic_type) {
                    this.topic_type = topic_type;
                }

                public String getTopics() {
                    return topics;
                }

                public void setTopics(String topics) {
                    this.topics = topics;
                }

                public List<LocalFilesDTO> getLocalFiles() {
                    return localFiles;
                }

                public void setLocalFiles(List<LocalFilesDTO> localFiles) {
                    this.localFiles = localFiles;
                }

                public static class LocalFilesDTO {
                    private String author;
                    private String file_type;
                    private Boolean isNet;
                    private String name;
                    private String path;
                    private String pos;
                    private Integer resImage;
                    private String suffix;
                    private String time;
                    private String type;

                    public String getAuthor() {
                        return author;
                    }

                    public void setAuthor(String author) {
                        this.author = author;
                    }

                    public String getFile_type() {
                        return file_type;
                    }

                    public void setFile_type(String file_type) {
                        this.file_type = file_type;
                    }

                    public Boolean getIsNet() {
                        return isNet;
                    }

                    public void setIsNet(Boolean isNet) {
                        this.isNet = isNet;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

                    public String getPos() {
                        return pos;
                    }

                    public void setPos(String pos) {
                        this.pos = pos;
                    }

                    public Integer getResImage() {
                        return resImage;
                    }

                    public void setResImage(Integer resImage) {
                        this.resImage = resImage;
                    }

                    public String getSuffix() {
                        return suffix;
                    }

                    public void setSuffix(String suffix) {
                        this.suffix = suffix;
                    }

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }
            }
        }
    }

