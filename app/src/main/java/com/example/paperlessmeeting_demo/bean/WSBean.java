package com.example.paperlessmeeting_demo.bean;

public class WSBean<T> {
    /*
    * 请求类型		reqType	        int	        0
    用户id		    user_id	        varchar 	5f677602bd0c1f2d906dad63
    包类型		    packType	    varchar 	add
    设备类型		    device	        varchar 	pc/app/ipad/paperless
    包体		        body	        varchar 	BFBF

    * */
    private int reqType;
    private String user_id;
    private String packType;
    private String device;
    private T body;

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
