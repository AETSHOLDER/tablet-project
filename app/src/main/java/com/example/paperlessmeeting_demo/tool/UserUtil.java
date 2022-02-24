package com.example.paperlessmeeting_demo.tool;

/*暂存，进入APP首页，得到用户信息，判断是否是主席台或者普通用户*/
public class UserUtil {
    public static boolean ISCHAIRMAN = false;
    public static String user_name = "" ;                          //  江小鱼  普通用户
    public static String user_id = "" ;         //  江小鱼  普通用户
    public static String meeting_record_id = ""; //  当前会议id

//    public static String user_id = "611b50ded42ca32c38df7c7a";        //  令狐聪  定为主席台
//    public static String user_name = "陈伟";                         //  令狐聪  定为主席台

//    public static String user_name = "赵阳阳" ;                          //  江小鱼  普通用户
//    public static String user_id = "611b5049d42ca32c38df7c4a" ;         //  江小鱼  普通用户

//    public static String user_name = "花满楼" ;                        // 花满楼  普通用户
//    public static String user_id = "5f677640bd0c1f2d906dad64" ;       // 花满楼  普通用户

//    public static String user_name = "东方不败" ;                      // 东方不败  普通用户
//    public static String user_id = "5f677602bd0c1f2d906dad63" ;       //  东方不败  普通用户*/


/*    public static String user_id = "611b5049d42ca32c38df7c4a";        //  赵阳阳  主席
    public static String user_name = "赵阳阳";                         //  赵阳阳  主席*/
/*    public static String user_id = "60812f6320d26f47ac6d3396";        //  谢逊
    public static String user_name = "谢逊";                         //  谢逊*/

/*    public static String user_id = "5f67766bbd0c1f2d906dad65";        //  令狐聪
    public static String user_name = "令狐聪";                         //  令狐聪  */
/*    public static String user_id = "6065f471d80db139c887d512";        //  花无缺
    public static String user_name = "花无缺";                         //  花无缺*/
//    public static String user_name = "王连瑞";                          //  王连瑞
//    public static String user_id = "611b5049d42ca32c38df7c31";         //  王连瑞

  /*  public static String user_name = "花满楼";                        // 花满楼  普通用户
    public static String user_id = "5f677640bd0c1f2d906dad64";       // 花满楼  普通用户*/

/*    public static String user_name = "东方不败";                      // 东方不败
    public static String user_id = "5f677602bd0c1f2d906dad63";       //  东方不败  */

    //    public static final String c_id = "5f67708fbd0c1f2d906dad5e"; //  公司id
//    public static String meeting_record_id = "612d8a52eb64151790cf762a"; //  当前会议id

    public static boolean isFloating = false;    // 悬浮记事本是否打开

    public static boolean isTempMeeting = false;    // 是否是临时会议

    /*
    * "user_list": [
                {
                    "_id": "5f9fced7dfa5a41200ce2462",
                    "status": "UNCONFIRMED",
                    "c_id": "5f67708fbd0c1f2d906dad5e",
                    "meeting_record_id": "5f9fced7dfa5a41200ce2461",
                    "user_id": "5f677602bd0c1f2d906dad63",
                    "user_name": "东方不败",`
                    "user_avatar": "http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg",
                    "user_phone": "1234",
                    "role": "0",
                    "device_id": "",
                    "sign_time": "",
                    "chair_id": "",
                    "sign_code": "1337"
                },
                {
                    "_id": "5f9fced7dfa5a41200ce2464",
                    "status": "UNCONFIRMED",
                    "c_id": "5f67708fbd0c1f2d906dad5e",
                    "meeting_record_id": "5f9fced7dfa5a41200ce2461",
                    "user_id": "5f67766bbd0c1f2d906dad65",
                    "user_name": "令狐聪",
                    "user_avatar": "http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg",
                    "user_phone": "1236",
                    "role": "2",
                    "device_id": "",
                    "sign_time": "",
                    "chair_id": "",
                    "sign_code": "6919"
                },
                {
                    "_id": "5f9fced7dfa5a41200ce2465",
                    "_id": "5f9fced7dfa5a41200ce2465",
                    "status": "UNCONFIRMED",
                    "c_id": "5f67708fbd0c1f2d906dad5e",
                    "meeting_record_id": "5f9fced7dfa5a41200ce2461",
                    "user_id": "5f67767ebd0c1f2d906dad66",
                    "user_name": "江小鱼",
                    "user_avatar": "http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg",
                    "user_phone": "1237",
                    "role": "2",
                    "device_id": "",
                    "sign_time": "",
                    "chair_id": "",
                    "sign_code": "2473"
                },
                {
                    "_id": "5f9fced7dfa5a41200ce2463",
                    "status": "UNCONFIRMED",
                    "c_id": "5f67708fbd0c1f2d906dad5e",
                    "meeting_record_id": "5f9fced7dfa5a41200ce2461",
                    "user_id": "5f677640bd0c1f2d906dad64",
                    "user_name": "花满楼",
                    "user_avatar": "http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg",
                    "user_phone": "1235",
                    "role": "1",
                    "device_id": "",
                    "sign_time": "",
                    "chair_id": "",
                    "sign_code": "3947"
                },
                {
                    "_id": "5f9fced7dfa5a41200ce2467",
                    "status": "UNCONFIRMED",
                    "c_id": "5f67708fbd0c1f2d906dad5e",
                    "meeting_record_id": "5f9fced7dfa5a41200ce2461",
                    "user_id": "5f8d615975bd9c317c00a992",
                    "user_name": "云中鹤",
                    "user_avatar": "http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg",
                    "user_phone": "1239",
                    "role": "3",
                    "device_id": "",
                    "sign_time": "",
                    "chair_id": "",
                    "sign_code": "2622"
                },
                {
                    "_id": "5f9fced7dfa5a41200ce2466",
                    "status": "UNCONFIRMED",
                    "c_id": "5f67708fbd0c1f2d906dad5e",
                    "meeting_record_id": "5f9fced7dfa5a41200ce2461",
                    "user_id": "5f67768ebd0c1f2d906dad67",
                    "user_name": "风清扬",
                    "user_avatar": "http://192.168.11.136:3000/http://192.168.11.136:3000/upload/default.jpg",
                    "user_phone": "1238",
                    "role": "3",
                    "device_id": "",
                    "sign_time": "",
                    "chair_id": "",
                    "sign_code": "2427"
                }
            ]
    * */

}
