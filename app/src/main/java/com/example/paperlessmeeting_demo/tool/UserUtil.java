package com.example.paperlessmeeting_demo.tool;

import android.os.Environment;

/*暂存，进入APP首页，得到用户信息，判断是否是主席台或者普通用户*/
public class UserUtil {
    public static boolean ISCHAIRMAN = false;
    public static String user_name = "";                          //  江小鱼  普通用户
    public static String user_id = "";         //  江小鱼  普通用户
    public static String meeting_record_id = ""; //  当前会议id
    public static Object object = new Object();//芜湖线程锁
    public static Object object2 = new Object();//芜湖线程锁-文件上传
    public static boolean isFloating = false;    // 悬浮记事本是否打开

    public static boolean isTempMeeting = false;    // 是否是临时会议
    public static boolean isNetDATA = true;    // 芜湖-是否是网络数据
    public static boolean isNetworkOnline = false;  // 是否有外网
    public static String serverIP = "";             // 服务端IP
    public static  String VOTE_FILE = Environment.getExternalStorageDirectory() + constant.VOTE_FILE;
    public static boolean isShareScreen = false;    // 当前是否在投屏

}
