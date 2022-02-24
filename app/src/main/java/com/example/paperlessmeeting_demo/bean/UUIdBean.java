package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2021/7/19.
 */

public class UUIdBean implements Serializable {


    /**
     * code : 0
     * msg : 成功！
     * data : {"uuid":"bcb0aea1-9507-4f11-8957-e9d92b59ac5f","c_id":"5f67708fbd0c1f2d906dad5e","c_name":"","create_time":"Tue Jun 29 2021 09:04:49 GMT+0800 (GMT+08:00)"}
     */

    /**
     * uuid : bcb0aea1-9507-4f11-8957-e9d92b59ac5f
     * c_id : 5f67708fbd0c1f2d906dad5e
     * c_name :
     * create_time : Tue Jun 29 2021 09:04:49 GMT+0800 (GMT+08:00)
     */

    private String uuid;
    private String c_id;
    private String c_name;
    private String create_time;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

}
