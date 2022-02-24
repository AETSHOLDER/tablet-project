package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2021/4/2.
 */

public class File  implements Serializable {


    /**
     * code : 0
     * msg : 成功！
     * data : {"meetingFileList":[{"_id":"60661690b32fd612940ba26f","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"},"type":"doc","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}},"upload_time":"2021-04-02 02:53:04","size":"0.01MB","file_path":"http://192.168.11.136:3000/upload/新建 DOC 文档.doc","style":"","file_name":"新建 DOC 文档.doc","suffix":"doc"},{"_id":"60661690b32fd612940ba270","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"},"type":"docx","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}},"upload_time":"2021-04-02 02:53:04","size":"0.00MB","file_path":"http://192.168.11.136:3000/upload/新建 DOCX 文档.docx","style":"","file_name":"新建 DOCX 文档.docx","suffix":"docx"},{"_id":"60661690b32fd612940ba271","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"},"type":"ppt","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}},"upload_time":"2021-04-02 02:53:04","size":"0.02MB","file_path":"http://192.168.11.136:3000/upload/新建 PPT 演示文稿.ppt","style":"","file_name":"新建 PPT 演示文稿.ppt","suffix":"ppt"}],"fileTotal":3}
     */

        /**
         * meetingFileList : [{"_id":"60661690b32fd612940ba26f","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"},"type":"doc","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}},"upload_time":"2021-04-02 02:53:04","size":"0.01MB","file_path":"http://192.168.11.136:3000/upload/新建 DOC 文档.doc","style":"","file_name":"新建 DOC 文档.doc","suffix":"doc"},{"_id":"60661690b32fd612940ba270","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"},"type":"docx","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}},"upload_time":"2021-04-02 02:53:04","size":"0.00MB","file_path":"http://192.168.11.136:3000/upload/新建 DOCX 文档.docx","style":"","file_name":"新建 DOCX 文档.docx","suffix":"docx"},{"_id":"60661690b32fd612940ba271","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":{"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"},"type":"ppt","user_id":{"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}},"upload_time":"2021-04-02 02:53:04","size":"0.02MB","file_path":"http://192.168.11.136:3000/upload/新建 PPT 演示文稿.ppt","style":"","file_name":"新建 PPT 演示文稿.ppt","suffix":"ppt"}]
         * fileTotal : 3
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
             * _id : 60661690b32fd612940ba26f
             * status : ENABLE
             * c_id : 5f67708fbd0c1f2d906dad5e
             * meeting_record_id : {"meeting_room_id":["60661583b32fd612940ba25f"],"agenda":["测试"],"serveItem":["茶水","资料","草稿纸"],"_id":"60661690b32fd612940ba262","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"测试","purpose":"测试","remind":"15","sche_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","sche_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","actual_start_time":"Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)","actual_end_time":"Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)","from":"5f677602bd0c1f2d906dad63","layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)"}
             * type : doc
             * user_id : {"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/0a6fcc367bdf734d.png","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"020000000002","number":"1","type":"ppl"}}
             * upload_time : 2021-04-02 02:53:04
             * size : 0.01MB
             * file_path : http://192.168.11.136:3000/upload/新建 DOC 文档.doc
             * style :
             * file_name : 新建 DOC 文档.doc
             * suffix : doc
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
                 * meeting_room_id : ["60661583b32fd612940ba25f"]
                 * agenda : ["测试"]
                 * serveItem : ["茶水","资料","草稿纸"]
                 * _id : 60661690b32fd612940ba262
                 * status : UNDERWAY
                 * c_id : 5f67708fbd0c1f2d906dad5e
                 * type : Z
                 * name : 测试
                 * purpose : 测试
                 * remind : 15
                 * sche_start_time : Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)
                 * sche_end_time : Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)
                 * actual_start_time : Fri Apr 02 2021 02:48:00 GMT+0800 (GMT+08:00)
                 * actual_end_time : Fri Apr 02 2021 06:48:00 GMT+0800 (GMT+08:00)
                 * from : 5f677602bd0c1f2d906dad63
                 * layout_data :
                 * check : 0
                 * check_status : ENABLE
                 * failure_cause :
                 * failure_time :
                 * create_time : Fri Apr 02 2021 01:35:31 GMT+0800 (GMT+08:00)
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
                 * avatar : upload/0a6fcc367bdf734d.png
                 * phone : 1234
                 * dept_id : 5f6773f7bd0c1f2d906dad61
                 * note : {"mac":"020000000002","number":"1","type":"ppl"}
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
                     * mac : 020000000002
                     * number : 1
                     * type : ppl
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
