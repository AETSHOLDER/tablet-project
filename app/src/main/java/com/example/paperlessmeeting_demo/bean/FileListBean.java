package com.example.paperlessmeeting_demo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2020/10/20.
 */

public class FileListBean implements Serializable {
    /**
     * data : [{"_id":"5fa37738bfb8771a14039ff4","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["5f9a2e7b98a9503ed4acfc10"],"agenda":["开发部日常会议"],"serveItem":["资料","草稿纸","笔"],"_id":"5fa1106aa41ba63b3c85d2b3","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"开发部日常会议","purpose":"开发部日常会议","remind":"5","sche_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","sche_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","actual_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","actual_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Tue Nov 03 2020 11:24:45 GMT+0800 (GMT+08:00)"},"type":"M","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/default.jpg","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61"},"upload_time":"2020-11-05 11:53:28","size":"100","file_path":"http://192.168.11.136:3000/directory/25629d0a858cc.docx","style":"directory","file_name":"声学在图书馆智慧空间建设中的应用.docx","suffix":""},{"_id":"5fa375debfb8771a14039ff3","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["5f9a2e7b98a9503ed4acfc10"],"agenda":["开发部日常会议"],"serveItem":["资料","草稿纸","笔"],"_id":"5fa1106aa41ba63b3c85d2b3","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"开发部日常会议","purpose":"开发部日常会议","remind":"5","sche_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","sche_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","actual_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","actual_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Tue Nov 03 2020 11:24:45 GMT+0800 (GMT+08:00)"},"type":"M","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/default.jpg","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61"},"upload_time":"2020-11-05 11:47:42","size":"100","file_path":"http://192.168.11.136:3000/directory/9735ae9bfea83.docx","style":"directory","file_name":"声学在图书馆智慧空间建设中的应用.docx","suffix":""},{"_id":"5fa1106aa41ba63b3c85d2c1","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["5f9a2e7b98a9503ed4acfc10"],"agenda":["开发部日常会议"],"serveItem":["资料","草稿纸","笔"],"_id":"5fa1106aa41ba63b3c85d2b3","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"开发部日常会议","purpose":"开发部日常会议","remind":"5","sche_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","sche_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","actual_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","actual_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Tue Nov 03 2020 11:24:45 GMT+0800 (GMT+08:00)"},"type":"M","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/default.jpg","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61"},"upload_time":"2020-11-03 16:10:18","size":"0.00MB","file_path":"http://192.168.11.136:3000/upload/hah - 副本 (3).docx","style":"img","file_name":"hah - 副本 (3).docx","suffix":"docx"}]
     * code : 0
     * msg : 成功！fileTotal
     */
    public FileListBean(String name, String path, String author, String time) {
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

        /**
         * _id : 5fa37738bfb8771a14039ff4
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * meeting_record_id : {"meeting_room_id":["5f9a2e7b98a9503ed4acfc10"],"agenda":["开发部日常会议"],"serveItem":["资料","草稿纸","笔"],"_id":"5fa1106aa41ba63b3c85d2b3","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"开发部日常会议","purpose":"开发部日常会议","remind":"5","sche_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","sche_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","actual_start_time":"Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)","actual_end_time":"Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Tue Nov 03 2020 11:24:45 GMT+0800 (GMT+08:00)"}
         * type : M
         * user_id : {"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/default.jpg","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61"}
         * upload_time : 2020-11-05 11:53:28
         * size : 100
         * file_path : http://192.168.11.136:3000/directory/25629d0a858cc.docx
         * style : directory
         * file_name : 声学在图书馆智慧空间建设中的应用.docx
         * suffix :
         */

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
             * meeting_room_id : ["5f9a2e7b98a9503ed4acfc10"]
             * agenda : ["开发部日常会议"]
             * serveItem : ["资料","草稿纸","笔"]
             * _id : 5fa1106aa41ba63b3c85d2b3
             * status : UNDERWAY
             * c_id : 5f67708fbd0c1f2d906dad5e
             * type : Z
             * name : 开发部日常会议
             * purpose : 开发部日常会议
             * remind : 5
             * sche_start_time : Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)
             * sche_end_time : Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)
             * actual_start_time : Tue Nov 03 2020 20:08:00 GMT+0800 (GMT+08:00)
             * actual_end_time : Tue Nov 03 2020 23:00:00 GMT+0800 (GMT+08:00)
             * from : 5f677602bd0c1f2d906dad63
             * layout_data :
             * check : 0
             * check_status : ENABLE
             * failure_cause :
             * failure_time :
             * create_time : Tue Nov 03 2020 11:24:45 GMT+0800 (GMT+08:00)
             */

            private String _id;
            private String status;
            private String c_id;
            private String type;
            @SerializedName("name")
            private String nameX;
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

            public String getNameX() {
                return nameX;
            }

            public void setNameX(String nameX) {
                this.nameX = nameX;
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

            public List<String> getServeItem() {
                return serveItem;
            }

            public void setServeItem(List<String> serveItem) {
                this.serveItem = serveItem;
            }
        }

        public static class UserIdBean {
            /**
             * _id : 5f677602bd0c1f2d906dad63
             * name : 东方不败
             * role : M
             * avatar : upload/default.jpg
             * phone : 1234
             * dept_id : 5f6773f7bd0c1f2d906dad61
             */

            private String _id;
            @SerializedName("name")
            private String nameX;
            private String role;
            private String avatar;
            private String phone;
            private String dept_id;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getNameX() {
                return nameX;
            }

            public void setNameX(String nameX) {
                this.nameX = nameX;
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
        }
    /**
     * _id : 5f97e54e146be531ac8cab9b
     * status : ENABLE
     * c_id : 5f561232595a5d3c34d6e94d
     * meeting_record_id : {"meeting_room_id":["5f968ceedfa33a25b8f98255"],"agenda":["1.数智未来","云计算"],"serveItem":["瓜子","花生","矿泉水"],"_id":"5f968cfcdfa33a25b8f98258","status":"FINISH","c_id":"5f67708fbd0c1f2d906dad5e","type":"S","name":"云栖大会2020-数智未来全速重构-阿里云","purpose":"数智未来全速重构","remind":"10","sche_start_time":"Fri Oct 09 2020 17:00:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Oct 09 2020 18:00:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Oct 09 2020 17:00:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Oct 09 2020 18:00:00 GMT+0800 (GMT+08:00)","from":"5f67767ebd0c1f2d906dad66","layout_data":"abcdefg","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Mon Oct 26 2020 15:51:28 GMT+0800 (GMT+08:00)"}
     * type : M
     * user_id : {"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/default.jpg","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61"}
     * upload_time : Tue Oct 27 2020 17:15:58 GMT+0800 (GMT+08:00)
     * publice : 0
     * file_path : http://192.168.11.136:3000/upload/hah.docx
     * style : img
     */


}


