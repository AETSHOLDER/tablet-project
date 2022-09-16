package com.example.paperlessmeeting_demo.tool;

import android.Manifest;

/**
 * Created by 梅涛 on 2020/9/7.
 */

public class constant {
    public final static String TAG = "wt";
    public final static int MESSAGE = 0x0245;
    public final static String SHOW_UI = "SHOW_UI";
    public final static String WHITEBOARD_BROADCAST = "1";
    public final static String SHOW_WHITEBOARD_BROADCAST = "2";
    public final static String CLOSE_WHITEBOARD_BROADCAST = "3";
    public final static String MAX_BROWSER = "4";
    public final static String SHOW_BROWSER = "5";
    public final static String CLOSE_BROWSER_BROADCAST = "6";
    public final static String REMOVE_BROADCAST = "7";
    public final static String USB_PATH = "/aa";
    public final static String DOWNLOAD_PATH = "/meetings/";
    public final static String COPY_PATH = "/copy/";

    public final static String FILEHAVEIT = "HaveIt";//推送分享文件有

    public final static String meeting_role = "meeting_role";  //  会议角色
    public final static String role = "role";          //  公司角色
    public final static String token = "token";
    public final static String _id = "_id";       //  会议id
    public final static String c_id = "c_id";      // 公司id
    public final static String user_id = "user_id";   // 用户id
    public final static String user_name = "user_name";   // 用户名
    public final static String team_share = "team|share";   // 同屏还是协作

    public final static String myNumber = "myNumber";   // 本机机号
    public final static String attendeBeanList = "attendeBeanList";  //  参会人员信息
    public final static String attendeIDList = "attendeIDList";  //  参会人员信息
    public final static String FRESH_FILE = "file";    // 公开文件后通知网络碎片重新请求文件接口
    public final static String FRESH_TAB = "tab";    // 刷新切换文件-投票tab
    public final static String start_meeting = "start meeting";    // 会议开始事件
    public final static String get_server_ip = "get center server address";    // 获取服务器ip
    public final static String continusClick = "start continusClick";    // 连续点击事件
    public final static String isFirstInit = "isFirstInit";
    public final static String isLaunch = "isLaunch";    // APP每次启动,用于APP启动第一次打开签批文件, TODO 注意与上一个的区别
    public final static String DOCUMENT = "Document";//文件请求参数常量-文档
    public final static String IMAGE = "Image";//文件请求参数常量-图片
    public final static String VIDEO = "Video";//文件请求参数常量-视频
    public final static String OTHER = "Other";//文件请求参数常量-其他
    public final static String SUMMARY = "Summary";//会议纪要
    public final static String SHOW_CLOSE_SERVICE_BROADCAST = "closeService";//关闭同屏Servive
    public final static String FILE_PRIVATE = "1";//文件类型私有
    public final static String FILE_Public = "0";//文件类型公开
    public final static String TEMPMEETING = "TEMPMEETING";//临时会议server还是client
    public final static String InitiaMeeting = "InitiaMeeting";//推送过来的会议、人员信息
    public final static String CVI_PAPER_SCREEN_DATA = "CVI_PAPER_SCREEN_DATA";//同屏数据
    /**
     * websocket 字段定义
     */
    public final static String FILEMD5PUSH = "doesitexist";//查询推送文件是否存在
    public final static String FILEMD5SHARE = "Isitunique";//查询分享文件是否存在
    public final static String NETWORKSIDE = "network side";//芜湖网络请求数据主席进入主界面时向其他参会端发送自己身ip地址

    public final static String FILERESPONDSHARE = "responpdf";//回应分享文件是否存在
    public final static String FILERESPONDPUSH = "whetherornot";//回应推送文件是否存在

    public final static String QUERYVOTE = "queryVote";//查询投票数据
    public final static String REFRASHWuHUSIGLEDATA = "refreshdata";//更新单个芜湖数据
    public final static String WUHUADDFRAGMENT = "tinajiyiti";//普通参会人员新增芜湖fragment

    public final static String REFRASHWuHUALL = "saveALL";//更新所有芜湖数据fragment
    public final static String DELETE_WUHU_FRAGMENT = "deletelwuhufragment";//删除芜湖数据fragment
    public final static String REFRESH_WUHU_FILE_FRAGMENT = "qufenliebiao";//更新芜湖添加本地文件数据fragment
    public final static String QUERYVOTE_WUHU_FRAGMENT = "querywuhufragment";//查询芜湖之前的fragment

    public final static String SUBMITANISSUE = "SubmitAnIssue";//芜湖主席端界面初始化提交自己本地议题

    public final static String NEWVOTE = "newVote";//新增投票数据
    public final static String UPDATEVOTE = "updateVote";//更新投票数据
    public final static String FINISHVOTE = "finishVote";//结束投票数据
    public final static String ADD = "add";//增加client
    public final static String RESETNAME = "resetName";//add时重名，请重新添加
    public final static String SURENAME = "sureName";//add名称机号可以使用
    public final static String HEART = "heart";//心跳
    public final static String SERVERSTART = "serverStart";//服务端开启成功
    public final static String QUERYATTEND = "queryAttend";//查询参会人员
    public final static String QUERYATTENDSize = "queryAttendSize";//查询参会人员数量

    public final static String TEMPSHARE = "tempShare";//临时会议白板同屏
    public final static String TEMPSHAREREQ = "tempShareReq";//临时会议白板同屏请求
    public final static String TEMPSHAREDIS = "tempShareDis";//临时会议断开白板同屏
    public final static String RUSHFILELIST = "updateFileList";//无纸化会议文件公开私有 socket通知
    public final static String TEMPTEAM = "tempTeam";//临时会议白板协作
    public final static String TEMPTEAMREQ = "tempTeamReq";//临时会议白板协作请求
    public final static String TEMPTEAMDIS = "tempTeamDis";//临时会议断开白板协作

    public final static String SETMICSTATUS = "setMicStatus";//设置发言单元状态
    public final static String GETMICSTATUS = "getMicStatus";//获取发言单元状态

    public final static String MINUTE_MEETING = "pushSign";//签批
    public final static String MINUTE_MEETING_CONTENT = "meetingSummaryReceive";//签批内容接收

    public final static String MEETINGFINISH = "meetingFinish";//会议结束
    public final static String EXTRAORDINARY_MEETING_INETADDRESS = "255.255.255.255";//临时会议分享文件IP。
    public final static int EXTRAORDINARY_MEETING_PORT = 4127;//临时会议分享文件端口。
    public final static int WuHu_PORT = 8270;//临时会议分享文件端口。
    public final static String SHARE_FILE = "/shareFile/";//临时会议分享的文件存储文件夹
    public final static String WUHU_NET_FILE = "/netFile/";//网络文件储存文件夹
    public final static String VOTE_FILE = "/voteFile/";//临时会议投票文件夹
    public final static int SHARE_PORT = 9999;////临时会议分享的文件端口
    public final static String SHARE_FILE_BROADCAST = "sharefile";////临时会议分享文件成功后，通知会议更新文件列表
    public static String WUHUSHARE = "-share";//芜湖分享文件区分议题
    public static String WUHUPUSH = "-push";//芜湖推送文件区分议题
    public final static String RUSH_VOTE_LIST_BROADCAST = "reshvotelistui";////临时会议分享文件成功后，通知会议更新投票列表
    public final static String RUSH_SIGN_LIST_BROADCAST = "reshsignlistui";////临时会议分享文件成功后，通知会议更新投票列表
    public final static String SEE_IMA_BROADCAST = "seeima";////临时会议查看投票大图
    public final static String ADD_FRAGMENT_BROADCAST = "addfragment";////增加芜湖fragment
    public final static String CHANGE_CATALOG_BROADCAST = "catalogfragment";////芜湖点击item会议目录切换到对应的碎片
    public final static String SAVE_SEPARATELY_BROADCAST = "save_separately";//从议题列表中单独保存某个会议议题
    public final static String DELETE_FRAGMENT_BROADCAST = "save_separately";//从议题列表中单独保存某个会议议题
    public final static String REFRESH_BROADCAST = "refresh_ui";//自己刷新芜湖界面
    public final static String FINISH_SHARE_SCREEN_BROADCAST = "finishscreen";////结束同屏广播
    public final static String SHARE_FILE_IP = "share  file ip";//socket 临时会议分享文件收集设备IP全局标识
    public final static String TEMP_MEETINGSHARE_FILE = "TEMP_MEETINGSHARE_FILE";//socket 临时会议分享文件到其他设备标识
    public final static String TEMP_MEETINGPUSH_FILE = "AAAAAAAAAAAAAAAAA";//socket 临时会议推送文件到其他设备标识
    public final static String TEMP_VOTE_IMAGE_FILE = "TEMP_VOTEIMAGE_FILE";//socket 临时会议投票的图片文件到其他设备标识
    public final static String FINISH_SHARE_SCEEN = "finish  share screen";//socket 结束同屏标识
    public final static String START_SHARE_SCEEN = "LLLLLLvideokk";//socket 开始同屏标识
    public final static String INIATE_ENDORSEMENT = "initiate endorsement";//socket 秘书发起签批时，普通会员自动打开签名页面
    public final static String WUHU_IMAGE_FILE_BROADCAST = "WuHuImagefile";////芜湖版本，当用户位于图片页面时  再次检测到推送文件后，只替换文件路径，不需要重启新页面
    public final static String FINISH_WUHUVOTEACTIVITY_BROADCAST = "finishactivity";//自己刷新芜湖界面
    public final static String FRESH_CATalog_BROADCAST = "freshcatalogfragment";//刷新非主席芜湖目录列表
    public static String temp_code = "temp_code";//临时会议code
    public final static String WUHU_FILE_BROADCAST = "WuHufile";////芜湖版本，当用户位于签批页面时  再次检测到推送文件后，只替换文件路径，不需要重启新页面
    public final static String PUSH_FILE_WEBSOCK = "PUSH_FILE_WEBSOCK";//推送网络文件
    public final static String CHANGE_COLOR_BG = "Change_color_background";//改变背景颜色


    public final static String SignFilePath = "SignFilePath";
    /**
     * 发言单元
     */
    public final static String micMac = "micMac";     // mic的Mac地址
    public final static String micNumber = "micNumber";  // mic的机号
    public final static String micIP = "micIP";      // mic的ip地址
    public final static String micSta = "micSta";
    /**
     * 签批广播
     */
    public final static String MEMBER_SIGN_BROADCAST = "memberSign";
    /**
     * pc同屏模块
     */
    public static String serverResponse = ".Cvi,Respond='This is the Server'";//  服务应答
    public static final String BROADCAST_IP = "255.255.255.255";              //  UDP广播地址
    public static final int BROADCAST_PORT = 9707;                            //  UDP广播、单播端口
    public static final int TCP_PORT = 8606;                                  //  TCP端口
    /**
     * 写入权限的请求code,提示语，和权限码
     */
    public final static int WRITE_PERMISSION_CODE1 = 0x0244;
    public final static String WRITE_PERMISSION_TIP = "为了正常使用，请允许一下权限!";
    public final static String[] PERMS_WRITE = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public final static String[] PERMS_WRITE1 = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET
    };
}
