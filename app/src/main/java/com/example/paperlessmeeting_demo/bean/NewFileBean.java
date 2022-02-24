package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/4/2.
 */

public class NewFileBean implements Serializable {

    /**
     * code : 0
     * msg : 成功！
     * data : {"meetingFileList":[{"_id":"6070004212e0dc1304565343","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["606612bcb32fd612940ba25d"],"agenda":[""],"serveItem":[],"_id":"606681f5a4a81b2144d6c758","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"465464","purpose":"","remind":"15","sche_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 09:25:51 GMT+0800 (GMT+08:00)"},"type":"","user_id":{"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}},"upload_time":"2021-04-09 15:20:34","size":"100","file_path":"http://192.168.11.136:3000/directory/会议系统需求分析文档V1.0.1.docx","style":"","file_name":"会议系统需求分析文档V1.0.1.docx","suffix":""},{"_id":"606ffedb12e0dc1304565342","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["606612bcb32fd612940ba25d"],"agenda":[""],"serveItem":[],"_id":"606681f5a4a81b2144d6c758","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"465464","purpose":"","remind":"15","sche_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 09:25:51 GMT+0800 (GMT+08:00)"},"type":"","user_id":{"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}},"upload_time":"2021-04-09 15:14:35","size":"100","file_path":"http://192.168.11.136:3000/directory/会议系统需求分析文档V1.0.1.docx","style":"","file_name":"会议系统需求分析文档V1.0.1.docx","suffix":""}],"fileTotal":2}
     */

    /**
     * meetingFileList : [{"_id":"6070004212e0dc1304565343","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["606612bcb32fd612940ba25d"],"agenda":[""],"serveItem":[],"_id":"606681f5a4a81b2144d6c758","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"465464","purpose":"","remind":"15","sche_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 09:25:51 GMT+0800 (GMT+08:00)"},"type":"","user_id":{"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}},"upload_time":"2021-04-09 15:20:34","size":"100","file_path":"http://192.168.11.136:3000/directory/会议系统需求分析文档V1.0.1.docx","style":"","file_name":"会议系统需求分析文档V1.0.1.docx","suffix":""},{"_id":"606ffedb12e0dc1304565342","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["606612bcb32fd612940ba25d"],"agenda":[""],"serveItem":[],"_id":"606681f5a4a81b2144d6c758","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"465464","purpose":"","remind":"15","sche_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 09:25:51 GMT+0800 (GMT+08:00)"},"type":"","user_id":{"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}},"upload_time":"2021-04-09 15:14:35","size":"100","file_path":"http://192.168.11.136:3000/directory/会议系统需求分析文档V1.0.1.docx","style":"","file_name":"会议系统需求分析文档V1.0.1.docx","suffix":""}]
     * fileTotal : 2
     */

    private int fileTotal;
    private List<MeetingFileListBean> meetingFileList;

    public int getFileTotal() {
        return fileTotal;
    }

    public void setFileTotal(int fileTotal) {
        this.fileTotal = fileTotal;
    }

    public List<MeetingFileListBean> getMeetingFileList() {
        return meetingFileList;
    }

    public void setMeetingFileList(List<MeetingFileListBean> meetingFileList) {
        this.meetingFileList = meetingFileList;
    }

    public static class MeetingFileListBean {
        /**
         * _id : 6070004212e0dc1304565343
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * meeting_record_id : {"meeting_room_id":["606612bcb32fd612940ba25d"],"agenda":[""],"serveItem":[],"_id":"606681f5a4a81b2144d6c758","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"465464","purpose":"","remind":"15","sche_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 09:25:51 GMT+0800 (GMT+08:00)"}
         * type :
         * user_id : {"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/default.jpg","phone":"1237","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}}
         * upload_time : 2021-04-09 15:20:34
         * size : 100
         * file_path : http://192.168.11.136:3000/directory/会议系统需求分析文档V1.0.1.docx
         * style :
         * file_name : 会议系统需求分析文档V1.0.1.docx
         * suffix :
         */
        private String unclassified;

        public String getUnclassified() {
            return unclassified;
        }

        public void setUnclassified(String unclassified) {
            this.unclassified = unclassified;
        }

        private String _id;
        private String status;
        private String c_id;
        private MeetingRecordIdBean meeting_record_id;
        private String type;
        private UserIdBean user_id;
        private String upload_time;
        private String size;
        private String file_path;
        private String style;
        private String file_name;
        private String suffix;


        public MeetingFileListBean(String name, String path, String author, String time) {
            this.name = name;
            this.path = path;
            this.author = author;
            this.time = time;
        }

        private boolean isPlaying;

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        private String name;
        private String path;
        private int resImage;
        private String author;
        private String time;
        private String file_type;

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

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public MeetingRecordIdBean getMeeting_record_id() {
            return meeting_record_id;
        }

        public void setMeeting_record_id(MeetingRecordIdBean meeting_record_id) {
            this.meeting_record_id = meeting_record_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public UserIdBean getUser_id() {
            return user_id;
        }

        public void setUser_id(UserIdBean user_id) {
            this.user_id = user_id;
        }

        public String getUpload_time() {
            return upload_time;
        }

        public void setUpload_time(String upload_time) {
            this.upload_time = upload_time;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public static class MeetingRecordIdBean {
            /**
             * meeting_room_id : ["606612bcb32fd612940ba25d"]
             * agenda : [""]
             * serveItem : []
             * _id : 606681f5a4a81b2144d6c758
             * status : UNDERWAY
             * c_id : 5f67708fbd0c1f2d906dad5e
             * type : Z
             * name : 465464
             * purpose :
             * remind : 15
             * sche_start_time : Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)
             * sche_end_time : Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)
             * actual_start_time : Fri Apr 02 2021 10:30:00 GMT+0800 (GMT+08:00)
             * actual_end_time : Fri Apr 02 2021 11:30:00 GMT+0800 (GMT+08:00)
             * from : 5f677602bd0c1f2d906dad63
             * layout_data :
             * check : 0
             * check_status : ENABLE
             * failure_cause :
             * failure_time :
             * create_time : Fri Apr 02 2021 09:25:51 GMT+0800 (GMT+08:00)
             */

            private String _id;
            private String status;
            private String c_id;
            private String type;
            private String name;
            private String purpose;
            private String remind;
            private String sche_start_time;
            private String sche_end_time;
            private String actual_start_time;
            private String actual_end_time;
            private String from;
            private String layout_data;
            private String check;
            private String check_status;
            private String failure_cause;
            private String failure_time;
            private String create_time;
            private List<String> meeting_room_id;
            private List<String> agenda;
            private List<String> serveItem;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getC_id() {
                return c_id;
            }

            public void setC_id(String c_id) {
                this.c_id = c_id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPurpose() {
                return purpose;
            }

            public void setPurpose(String purpose) {
                this.purpose = purpose;
            }

            public String getRemind() {
                return remind;
            }

            public void setRemind(String remind) {
                this.remind = remind;
            }

            public String getSche_start_time() {
                return sche_start_time;
            }

            public void setSche_start_time(String sche_start_time) {
                this.sche_start_time = sche_start_time;
            }

            public String getSche_end_time() {
                return sche_end_time;
            }

            public void setSche_end_time(String sche_end_time) {
                this.sche_end_time = sche_end_time;
            }

            public String getActual_start_time() {
                return actual_start_time;
            }

            public void setActual_start_time(String actual_start_time) {
                this.actual_start_time = actual_start_time;
            }

            public String getActual_end_time() {
                return actual_end_time;
            }

            public void setActual_end_time(String actual_end_time) {
                this.actual_end_time = actual_end_time;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getLayout_data() {
                return layout_data;
            }

            public void setLayout_data(String layout_data) {
                this.layout_data = layout_data;
            }

            public String getCheck() {
                return check;
            }

            public void setCheck(String check) {
                this.check = check;
            }

            public String getCheck_status() {
                return check_status;
            }

            public void setCheck_status(String check_status) {
                this.check_status = check_status;
            }

            public String getFailure_cause() {
                return failure_cause;
            }

            public void setFailure_cause(String failure_cause) {
                this.failure_cause = failure_cause;
            }

            public String getFailure_time() {
                return failure_time;
            }

            public void setFailure_time(String failure_time) {
                this.failure_time = failure_time;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public List<String> getMeeting_room_id() {
                return meeting_room_id;
            }

            public void setMeeting_room_id(List<String> meeting_room_id) {
                this.meeting_room_id = meeting_room_id;
            }

            public List<String> getAgenda() {
                return agenda;
            }

            public void setAgenda(List<String> agenda) {
                this.agenda = agenda;
            }

            public List<?> getServeItem() {
                return serveItem;
            }

            public void setServeItem(List<String> serveItem) {
                this.serveItem = serveItem;
            }
        }

        public static class UserIdBean {
            /**
             * _id : 5f67767ebd0c1f2d906dad66
             * name : 江小鱼
             * role : M
             * avatar : upload/default.jpg
             * phone : 1237
             * dept_id : 5f6773f7bd0c1f2d906dad61
             * note : {"mac":"","number":"","type":""}
             */

            private String _id;
            private String name;
            private String role;
            private String avatar;
            private String phone;
            private String dept_id;
            private NoteBean note;

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

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDept_id() {
                return dept_id;
            }

            public void setDept_id(String dept_id) {
                this.dept_id = dept_id;
            }

            public NoteBean getNote() {
                return note;
            }

            public void setNote(NoteBean note) {
                this.note = note;
            }

            public static class NoteBean {
                /**
                 * mac :
                 * number :
                 * type :
                 */

                private String mac;
                private String number;
                private String type;

                public String getMac() {
                    return mac;
                }

                public void setMac(String mac) {
                    this.mac = mac;
                }

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
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


