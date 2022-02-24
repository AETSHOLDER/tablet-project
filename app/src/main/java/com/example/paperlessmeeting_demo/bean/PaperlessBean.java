package com.example.paperlessmeeting_demo.bean;

/**
 * @author cyj
 * @createDate 2022-01-12
 * @ClassName: PaperlessBean
 * @Description: 描述
 * @Version: 1.0
 */
public class PaperlessBean {

    /**
     * _id : 61dcea7d7bec7b27774a503f
     * status : ENABLE
     * c_id : 5f67708fbd0c1f2d906dad5e
     * name :
     * mac : 3af919952d06
     * number : 1
     * meeting_room_id : 60661559b32fd612940ba25e
     * create_time : Tue Jan 11 2022 10:25:01 GMT+0800 (中国标准时间)
     * place : N
     * rostrum : 0
     * mic : null
     * meeting_room_name :
     */

    private String _id;
    private String status;
    private String c_id;
    private String name;
    private String mac;
    private String number;
    private String meeting_room_id;
    private Object mic;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getMeeting_room_id() {
        return meeting_room_id;
    }

    public void setMeeting_room_id(String meeting_room_id) {
        this.meeting_room_id = meeting_room_id;
    }

    public Object getMic() {
        return mic;
    }

    public void setMic(Object mic) {
        this.mic = mic;
    }
}
