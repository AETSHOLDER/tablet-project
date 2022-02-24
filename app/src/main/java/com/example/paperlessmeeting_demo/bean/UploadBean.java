package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2020/11/4.
 */

public class UploadBean implements Serializable{
    /**
     * code : 0
     * msg : 成功！
     * data : {"file_name":"声学在图书馆智慧空间建设中的应用.docx","uid":"","url":"directory/38588077be9c9.docx","suffix":"docx"}
     */
        /**
         * file_name : 声学在图书馆智慧空间建设中的应用.docx
         * uid :
         * url : directory/38588077be9c9.docx
         * suffix : docx
         */

        private String file_name;
        private String uid;
        private String url;
        private String suffix;

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
}
