package com.example.paperlessmeeting_demo.bean;

public class TempWSBean<T> {
    /*
   * 请求类型		reqType	        int	        0
   用户id		    user_id	        varchar 	5f677602bd0c1f2d906dad63
   包类型		    packType	    varchar 	add
   设备类型		    device	        varchar 	pc/app/ipad/paperless
   包体		        body	        varchar 	BFBF

   * */
    private int reqType;
    private String userMac_id;
    private String userMac_number;
    private String packType;
    private T body;
    //标识选项时文字还是图片1：文字  2：图片

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public String getUserMac_id() {
        return userMac_id;
    }

    public void setUserMac_id(String userMac_id) {
        this.userMac_id = userMac_id;
    }

    public void setUserMac_number(String userMac_number) {
        this.userMac_number = userMac_number;
    }

    public String getUserMac_number() {
        return userMac_number;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
