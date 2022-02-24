package com.example.paperlessmeeting_demo.bean;

import java.util.List;

public class TeamDisagree {

    /**
     * packType : teamAccept
     * device : pc
     * user_id : 5f677602bd0c1f2d906dad63
     * body : {"result":"disagree","user_list":["5f67766bbd0c1f2d906dad65","5f677640bd0c1f2d906dad64","5f677640bd0c1f2d906dad63"]}
     */

    private String packType;
    private String device;
    private String user_id;
    private BodyBean body;

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * result : disagree
         * user_list : ["5f67766bbd0c1f2d906dad65","5f677640bd0c1f2d906dad64","5f677640bd0c1f2d906dad63"]
         */

        private String result;
        private List<String> user_list;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<String> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<String> user_list) {
            this.user_list = user_list;
        }
    }
}
