package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author cyj
 * @createDate 2022-04-14
 * @ClassName: WuHuNetFileBean
 * @Description: 描述
 * @Version: 1.0
 */
public class WuHuNetFileBean implements Serializable {


    /**
     * code : 1
     * msg : 成功！
     * data : [{"_id":"6257fb0279f52ae9365ee270","status":"ENABLE","name":"文件名","upload_name":"上传人","file_path":"upload/chrome_100_percent-1649932525834.pak","meeting_id":"6257faee79f52ae9365ee26b","upload_time":"2022-04-14T09:37:07.400Z","__v":0,"meeting_name":"测试会议定时4"}]
     */

    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 6257fb0279f52ae9365ee270
         * status : ENABLE
         * name : 文件名
         * upload_name : 上传人
         * file_path : upload/chrome_100_percent-1649932525834.pak
         * meeting_id : 6257faee79f52ae9365ee26b
         * upload_time : 2022-04-14T09:37:07.400Z
         * __v : 0
         * meeting_name : 测试会议定时4
         */
        private boolean isNet;

        public boolean isNet() {
            return isNet;
        }

        public void setNet(boolean net) {
            isNet = net;
        }

        private String  path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        private String _id;
        private String status;
        private String name;
        private String upload_name;
        private String file_path;
        private String meeting_id;
        private String upload_time;
        private int __v;
        private String meeting_name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUpload_name() {
            return upload_name;
        }

        public void setUpload_name(String upload_name) {
            this.upload_name = upload_name;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }

        public String getMeeting_id() {
            return meeting_id;
        }

        public void setMeeting_id(String meeting_id) {
            this.meeting_id = meeting_id;
        }

        public String getUpload_time() {
            return upload_time;
        }

        public void setUpload_time(String upload_time) {
            this.upload_time = upload_time;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getMeeting_name() {
            return meeting_name;
        }

        public void setMeeting_name(String meeting_name) {
            this.meeting_name = meeting_name;
        }
    }
}
