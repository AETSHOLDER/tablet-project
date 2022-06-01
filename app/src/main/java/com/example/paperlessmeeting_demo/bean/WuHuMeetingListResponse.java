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
            private String topics;
            private String topic_type;
            private String startTime;
            private List<EditListBeanListDTO> editListBeanList;
            private VoteListBeanDTO voteListBean;

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

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public List<EditListBeanListDTO> getEditListBeanList() {
                return editListBeanList;
            }

            public void setEditListBeanList(List<EditListBeanListDTO> editListBeanList) {
                this.editListBeanList = editListBeanList;
            }

            public VoteListBeanDTO getVoteListBean() {
                return voteListBean;
            }

            public void setVoteListBean(VoteListBeanDTO voteListBean) {
                this.voteListBean = voteListBean;
            }

            public static class VoteListBeanDTO {
                private List<ContentDTO.VoteListBeanDTO.DataDTO> data;

                public List<ContentDTO.VoteListBeanDTO.DataDTO> getData() {
                    return data;
                }

                public void setData(List<ContentDTO.VoteListBeanDTO.DataDTO> data) {
                    this.data = data;
                }

                public static class DataDTO {
                    private String topic;
                    private List<ContentDTO.VoteListBeanDTO.DataDTO.TemporBeanListDTO> temporBeanList;
                    private String type;
                    private String anonymity;
                    private String status;
                    private String id;
                    private Integer mvoteStatus;
                    private Integer flag;

                    public String getTopic() {
                        return topic;
                    }

                    public void setTopic(String topic) {
                        this.topic = topic;
                    }

                    public List<ContentDTO.VoteListBeanDTO.DataDTO.TemporBeanListDTO> getTemporBeanList() {
                        return temporBeanList;
                    }

                    public void setTemporBeanList(List<ContentDTO.VoteListBeanDTO.DataDTO.TemporBeanListDTO> temporBeanList) {
                        this.temporBeanList = temporBeanList;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getAnonymity() {
                        return anonymity;
                    }

                    public void setAnonymity(String anonymity) {
                        this.anonymity = anonymity;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public Integer getMvoteStatus() {
                        return mvoteStatus;
                    }

                    public void setMvoteStatus(Integer mvoteStatus) {
                        this.mvoteStatus = mvoteStatus;
                    }

                    public Integer getFlag() {
                        return flag;
                    }

                    public void setFlag(Integer flag) {
                        this.flag = flag;
                    }

                    public static class TemporBeanListDTO {
                        private Boolean checked;
                        private String content;
                        private String orderNumb;

                        public Boolean getChecked() {
                            return checked;
                        }

                        public void setChecked(Boolean checked) {
                            this.checked = checked;
                        }

                        public String getContent() {
                            return content;
                        }

                        public void setContent(String content) {
                            this.content = content;
                        }

                        public String getOrderNumb() {
                            return orderNumb;
                        }

                        public void setOrderNumb(String orderNumb) {
                            this.orderNumb = orderNumb;
                        }
                    }
                }
            }

            public static class EditListBeanListDTO {
                private String reportingUnit;
                private String participantUnits;
                private String subTopics;
                private List<FileListBeanListDTO> fileListBeanList;

                public String getReportingUnit() {
                    return reportingUnit;
                }

                public void setReportingUnit(String reportingUnit) {
                    this.reportingUnit = reportingUnit;
                }

                public String getParticipantUnits() {
                    return participantUnits;
                }

                public void setParticipantUnits(String participantUnits) {
                    this.participantUnits = participantUnits;
                }

                public String getSubTopics() {
                    return subTopics;
                }

                public void setSubTopics(String subTopics) {
                    this.subTopics = subTopics;
                }

                public List<FileListBeanListDTO> getFileListBeanList() {
                    return fileListBeanList;
                }

                public void setFileListBeanList(List<FileListBeanListDTO> fileListBeanList) {
                    this.fileListBeanList = fileListBeanList;
                }

                public static class FileListBeanListDTO {
                    private String name;
                    private Boolean net;
                    private String path;
                    private String pos;
                    private Integer resImage;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public Boolean getNet() {
                        return net;
                    }

                    public void setNet(Boolean net) {
                        this.net = net;
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
                }
            }
        }
    }


