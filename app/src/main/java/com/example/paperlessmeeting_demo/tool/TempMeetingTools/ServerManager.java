package com.example.paperlessmeeting_demo.tool.TempMeetingTools;

import android.os.Handler;
import android.util.Log;

import com.example.paperlessmeeting_demo.WuHuLocalFileBean;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.FileBean;
import com.example.paperlessmeeting_demo.bean.PushBean;
import com.example.paperlessmeeting_demo.bean.SharePushFileBean;
import com.example.paperlessmeeting_demo.bean.TempWSBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.WuHuDeleteFragmentBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuNetWorkBean;
import com.example.paperlessmeeting_demo.enums.MessageReceiveType;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.EventMessage;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.MyWebSocketServer;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ServerManager {
    static MyWebSocketServer myWebsocketServer = null;
    private Map<WebSocket, String> userMap = new HashMap<WebSocket, String>();
    private Map<String, String> userMacMap = new HashMap<String, String>();  // 判断mac 之前是否添加，防止重复添加
    private String TAG = "ServerManager";
    private boolean serverIsOpen = false;  // 服务端是否开启
    private static List<AttendeBean> AttendeBeanList = new ArrayList<>(); // 参会人员列表

    public boolean isServerIsOpen() {
        return serverIsOpen;
    }

    public int attendSize = 0;

    private static class SingletonHolder {
        private static final ServerManager INSTANCE = new ServerManager();
    }

    private ServerManager() {
    }

    public static final ServerManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static WuHuEditBean staticwuHuEditBean = new WuHuEditBean();

    private static ArrayList<WuHuEditBean.EditListBean> editListBeans = new ArrayList<>();
    private static ArrayList<WuHuLocalFileBean.FileBean> fileBeanArrayList = new ArrayList<>();
    private static ArrayList<VoteListBean.VoteBean> voteList = new ArrayList<>();
    //    private static List<VoteListBean.VoteBean.UserListBean> userListBeans = new ArrayList<>();
    private static HashMap<String, ArrayList> voteMap = new HashMap<>();  //  通过key(meetingVoteId) value(userList)存储不同会议的用户投票数据

    /**
     * 服务器收到消息
     */
    public void MsgReceive(String message, WebSocket conn) {
        String user_mac_id = "未知ID";
        String flag2 = "-1";
        try {
            TempWSBean wsebean = new Gson().fromJson(message, TempWSBean.class);
            //  收到vote的websocket的信息
            if (wsebean != null) {
                user_mac_id = wsebean.getUserMac_id();
                flag2 = wsebean.getFlag();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        // 查询投票信息
        if (message.contains(constant.QUERYVOTE)) {
            try {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(1);
                bean.setUserMac_id(user_mac_id);
                bean.setPackType(constant.QUERYVOTE);
                bean.setFlag(flag2);
                bean.setBody(voteList);
                String strJson = new Gson().toJson(bean);
                //  谁查询，发送给谁
                SendMessageToUser(conn, strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 查询芜湖所有的fragment
        if (message.contains(constant.QUERYVOTE_WUHU_FRAGMENT)) {
            try {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(1);
                bean.setUserMac_id(user_mac_id);
                bean.setPackType(constant.QUERYVOTE_WUHU_FRAGMENT);
                bean.setBody(editListBeans);
                String strJson = new Gson().toJson(bean);
                //  谁查询，发送给谁
                SendMessageToUser(conn, strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 查询参会人员
        if (message.contains(constant.QUERYATTEND)) {

            try {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(1);
                bean.setUserMac_id(user_mac_id);
                bean.setPackType(constant.QUERYATTEND);
                bean.setBody(AttendeBeanList);
                String strJson = new Gson().toJson(bean);
                //  谁查询，发送给谁
                SendMessageToUser(conn, strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (message.contains(constant.QUERYATTENDSize)) {

            try {
                TempWSBean bean = new TempWSBean();
                bean.setReqType(1);
                bean.setUserMac_id(user_mac_id);
                bean.setPackType(constant.QUERYATTENDSize);
                bean.setBody(attendSize);
                String strJson = new Gson().toJson(bean);
                //  谁查询，发送给谁
                SendMessageToUser(conn, strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // 新增投票
        if (message.contains(constant.NEWVOTE)) {
            try {
                TempWSBean<VoteListBean.VoteBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<VoteListBean.VoteBean>>() {
                }.getType());
                VoteListBean.VoteBean voteBean = wsebean.getBody();
                // 创建投票id
                Random random = new Random();
                voteBean.set_id(System.currentTimeMillis() + "");


                voteBean.setUser_list(new ArrayList<>());
                voteMap.put(voteBean.get_id(), new ArrayList<>());
                voteList.add(voteBean);
                //SendVoteMsgToAll(wsebean.getFlag());

                // 新增投票多发送一条
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.NEWVOTE);
                wsebean1.setBody(voteBean);
                wsebean1.setFlag(wsebean.getFlag());
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 更新投票
        if (message.contains(constant.UPDATEVOTE)) {
            try {
                TempWSBean<VoteListBean.VoteBean.UserListBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<VoteListBean.VoteBean.UserListBean>>() {
                }.getType());
                VoteListBean.VoteBean.UserListBean userListBean = wsebean.getBody();
                String meeting_vote_id = userListBean.getMeeting_vote_id();
                String flag3 = wsebean.getFlag();
                boolean flag = false;
                int index = -1;
                ArrayList<VoteListBean.VoteBean.UserListBean> userListBeans = voteMap.get(meeting_vote_id);
                for (VoteListBean.VoteBean.UserListBean bean : userListBeans) {
                    // 如果用户ID相同，替换（基本不会存在这种情况，因为用户只准投一次票,保险起见）
                    if (bean.getUser_id().equals(userListBean.getUser_id())) {
                        flag = true;
                        index = userListBeans.indexOf(bean);
                    }
                }
                if (flag) {
                    userListBeans.remove(index);
                    userListBeans.add(userListBean);
                } else {
                    userListBeans.add(userListBean);
                }
                // 更新voteMap
                voteMap.put(meeting_vote_id, userListBeans);
                /**
                 *   替换投票模型的数据，并且用该模型替换voteList
                 * */
                for (VoteListBean.VoteBean voteBean : voteList) {
                    if (voteBean.get_id().equals(meeting_vote_id)) {

                        int index22 = voteList.indexOf(voteBean);

                        voteBean.setUser_list(userListBeans);
                        voteList.set(index22, voteBean);
                    }
                }

                SendVoteMsgToAll(flag3);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 结束投票
        if (message.contains(constant.FINISHVOTE)) {
            try {
                TempWSBean<VoteListBean.VoteBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<VoteListBean.VoteBean>>() {
                }.getType());
                VoteListBean.VoteBean voteBean = wsebean.getBody();
                String flag4 = wsebean.getFlag();
                for (VoteListBean.VoteBean bean : voteList) {
                    if (voteBean.get_id().equals(bean.get_id())) {
                        int index22 = voteList.indexOf(bean);
                        bean.setStatus(voteBean.getStatus());
                        voteList.set(index22, bean);
                    }
                }

                SendVoteMsgToAll(flag4);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*************************************白板模块****************************************/
        // 同屏
        if (message.contains(constant.TEMPSHARE) || message.contains(constant.TEMPSHAREREQ)) {
            SendWBMsgToAll(message);
        }
        // 协作
        if (message.contains(constant.TEMPTEAM) || message.contains(constant.TEMPTEAMREQ)) {
            SendWBMsgToAll(message);
        }
        // 芜湖新增fragment
        if (message.contains(constant.WUHUADDFRAGMENT)) {
            try {
                TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean>>() {
                }.getType());
                WuHuEditBean wuHuEditBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.WUHUADDFRAGMENT);
                wsebean1.setBody(wuHuEditBean);
                editListBeans.clear();
                editListBeans.addAll(wuHuEditBean.getEditListBeanList());
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 删除单个芜湖新增fragment
        if (message.contains(constant.DELETE_WUHU_FRAGMENT)) {
            try {
                TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean>>() {
                }.getType());
                WuHuEditBean wuHuEditBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.DELETE_WUHU_FRAGMENT);
                editListBeans.clear();
                editListBeans.addAll(wuHuEditBean.getEditListBeanList());

                wsebean1.setBody(wuHuEditBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 推送文件
        if (message.contains(constant.PUSH_FILE_WEBSOCK)) {
            try {
                TempWSBean<PushBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<PushBean>>() {
                }.getType());
                PushBean pushBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.PUSH_FILE_WEBSOCK);
                wsebean1.setBody(pushBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 改变背景颜色
        if (message.contains(constant.CHANGE_COLOR_BG)) {
            try {
                TempWSBean<String> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<String>>() {
                }.getType());
                String string = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.CHANGE_COLOR_BG);
                wsebean1.setBody(string);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        // 芜湖主席端界面初始化提交自己本地议题
        if (message.contains(constant.SUBMITANISSUE)) {
            try {
                TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean>>() {
                }.getType());
                WuHuEditBean wuHuEditBean = wsebean.getBody();
                editListBeans.clear();
                editListBeans.addAll(wuHuEditBean.getEditListBeanList());
                Log.d("fdsafadffadfazz3333 ,", editListBeans.size() + "");
                for (int i = 0; i < editListBeans.size(); i++) {

                    Log.d("fdsafadffadfazz5555   ", editListBeans.get(i).getLocalFiles().size() + "");
                }
                TempWSBean bean = new TempWSBean();
                bean.setReqType(1);
                bean.setUserMac_id("");
                bean.setPackType(constant.SUBMITANISSUE);
                bean.setBody(wsebean);
                String strJson = new Gson().toJson(bean);
                SendMessageToAll(strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 刷新单个芜湖新增fragment
        if (message.contains(constant.REFRASHWuHUSIGLEDATA)) {
            try {
                TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean>>() {
                }.getType());
                WuHuEditBean wuHuEditBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.REFRASHWuHUSIGLEDATA);
                editListBeans.clear();
                editListBeans.addAll(wuHuEditBean.getEditListBeanList());
                wsebean1.setBody(wuHuEditBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 更新芜湖所有fragment
        if (message.contains(constant.REFRASHWuHUALL)) {
            try {
                TempWSBean<WuHuEditBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean>>() {
                }.getType());
                WuHuEditBean wuHuEditBean = wsebean.getBody();

                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.REFRASHWuHUALL);
                editListBeans.clear();
                editListBeans.addAll(wuHuEditBean.getEditListBeanList());

                wsebean1.setBody(wuHuEditBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 更新芜湖所有fragment添加本地文件列表
        if (message.contains(constant.REFRESH_WUHU_FILE_FRAGMENT)) {
            try {
                TempWSBean<WuHuLocalFileBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuLocalFileBean>>() {
                }.getType());
                WuHuLocalFileBean wuHuLocalFileBean = wsebean.getBody();
                if (wuHuLocalFileBean != null) {
                    fileBeanArrayList.addAll(wuHuLocalFileBean.getFileBeanList());
                }
                wuHuLocalFileBean.setFileBeanList(fileBeanArrayList);
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.REFRESH_WUHU_FILE_FRAGMENT);
                wsebean1.setBody(wuHuLocalFileBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 文件推送时查询是否当前议题有该文件
        if (message.contains(constant.FILEMD5PUSH)) {
            try {
                TempWSBean<WuHuEditBean.EditListBean.FileListBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean.EditListBean.FileListBean>>() {
                }.getType());
                WuHuEditBean.EditListBean.FileListBean fileBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.FILEMD5PUSH);
                wsebean1.setBody(fileBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 文件分享时查询是否当前议题有该文件
        if (message.contains(constant.FILEMD5SHARE)) {
            try {
                TempWSBean<WuHuEditBean.EditListBean.FileListBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<WuHuEditBean.EditListBean.FileListBean>>() {
                }.getType());
                WuHuEditBean.EditListBean.FileListBean fileBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.FILEMD5SHARE);
                wsebean1.setBody(fileBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (message.contains(constant.START_SHARE_SCEEN) || message.contains(constant.FINISH_SHARE_SCEEN)) {
            SendMessageToAll(message);
        }

        // 文件分享时回应发送端是否当前议题有该文件
        if (message.contains(constant.FILERESPONDSHARE)) {
            try {
                TempWSBean<SharePushFileBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<SharePushFileBean>>() {
                }.getType());
                SharePushFileBean sharePushFileBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.FILERESPONDSHARE);
                wsebean1.setBody(sharePushFileBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 文件推送时回应发送端是否当前议题有该文件
        if (message.contains(constant.FILERESPONDPUSH)) {
            try {
                TempWSBean<SharePushFileBean> wsebean = new Gson().fromJson(message, new TypeToken<TempWSBean<SharePushFileBean>>() {
                }.getType());
                SharePushFileBean sharePushFileBean = wsebean.getBody();
                TempWSBean wsebean1 = new TempWSBean();
                wsebean1.setReqType(1);
                wsebean1.setUserMac_id("");
                wsebean1.setPackType(constant.FILERESPONDPUSH);
                wsebean1.setBody(sharePushFileBean);
                String strJson = new Gson().toJson(wsebean1);
                SendMessageToAll(strJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventMessage msg = new EventMessage(MessageReceiveType.MessageServer, message);
        EventBus.getDefault().post(msg);
    }

    /**
     * 服务器收到byte数组，解析同屏数据(直接转发)
     * 在本案例中，主席才能发起同屏
     */
    public void MsgReceiveByte(ByteBuffer message, WebSocket conn) {

        myWebsocketServer.broadcast(message);
    }

    /**
     * 广播投票数据
     */
    public void SendVoteMsgToAll(String flag) {
        TempWSBean bean = new TempWSBean();
        bean.setReqType(1);
        bean.setUserMac_id("");
        bean.setFlag(flag);
        bean.setPackType(constant.QUERYVOTE);
        bean.setBody(voteList);
        String strJson = new Gson().toJson(bean);
        SendMessageToAll(strJson);
    }
    /**-------------------------------白板模块-----------------------------------------**/
    /**
     * 广播白板数据
     */
    public void SendWBMsgToAll(String msg) {

        SendMessageToAll(msg);
    }


    /**-------------------------------连接模块-----------------------------------------**/
    /**
     * 用户通过add加入连接 机号即是用户名
     */
    public void UserLogin(String userMacID, String userName, WebSocket socket) {
        if (userName != null && socket != null) {
            if (userMap.values().contains(userName)) {
                //  存在重名现象，重新命名机号
                TempWSBean bean = new TempWSBean();
                bean.setReqType(1);
                bean.setPackType(constant.RESETNAME);
                bean.setBody("");
                String strJson = new Gson().toJson(bean);
                SendMessageToUser(socket, strJson);
                return;

            }

            TempWSBean bean = new TempWSBean();
            bean.setReqType(1);
            bean.setPackType(constant.SURENAME);
            bean.setBody("");
            String strJson = new Gson().toJson(bean);
            SendMessageToUser(socket, strJson);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            String signTime = simpleDateFormat.format(date);

            AttendeBean attendeBean = new AttendeBean();
            attendeBean.setSign_time(signTime);
            attendeBean.setAvatar("");
            if (userMacID.equals(FLUtil.getMacAddress())) {
                attendeBean.setRole("0");
            } else {
                attendeBean.setRole("2");
            }
            attendeBean.setName(userName + "号");
            AttendeBeanList.add(attendeBean);

            userMap.put(socket, userName);
            userMacMap.put(userName, userMacID);
            Log.i(TAG, "LOGIN:" + userName);


//            // 添加用户投票的用户组
//            VoteListBean.VoteBean.UserListBean userListBean  = new VoteListBean.VoteBean.UserListBean();
//            userListBean.setUser_id(userMacID);
//            userListBean.setUser_name(userName+"号");
//            userListBeans.add(userListBean);
        }
    }

    /**
     * 用户连接断开
     */
    public void UserLeave(WebSocket socket) {
        if (userMap.containsKey(socket)) {
            String userName = userMap.get(socket);
            Log.i(TAG, "Leave:" + userName);
            userMap.remove(socket);

            for (Iterator<AttendeBean> it = AttendeBeanList.iterator(); it.hasNext(); ) {
                AttendeBean attendeBean = it.next();
                if (attendeBean.getName().contains(userName)) {
                    it.remove();// 使用迭代器来进行安全的加锁操作
                }
            }
        }
    }

    /**
     * 通过socket连接向指定用户发送消息
     */
    public void SendMessageToUser(WebSocket socket, String message) {
        if (socket != null) {
            socket.send(message);
        }
    }

    /**
     * 通过用户名向指定用户发送消息
     */
    public void SendMessageToUser(String userName, String message) {
        Set<WebSocket> ketSet = userMap.keySet();
        for (WebSocket socket : ketSet) {
            String name = userMap.get(socket);
            if (name != null) {
                if (name.equals(userName)) {
                    socket.send(message);
                    break;
                }
            }
        }
    }

    /**
     * 向所有用户发送消息
     */
    public void SendMessageToAll(String message) {
        /*
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                socket.send(message);
            }
        }
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                myWebsocketServer.broadcast(message);
            }
        }).start();

    }

    /**
     * 开启socket server
     */
    public void startMyWebsocketServer(final int port) {
        if (port < 0) {
            Log.i("", "Port error...");
            return;
        }
        if (myWebsocketServer != null) {
            return;
        }
        Log.i("ServerManager", "Start ServerSocket...");
        new Thread() {
            @Override
            public void run() {
                try {
                    InetSocketAddress myHost = new InetSocketAddress(FLUtil.getIPAddress(), port);
                    if (myWebsocketServer == null) {
                        myWebsocketServer = new MyWebSocketServer(myHost);
                        myWebsocketServer.setReuseAddr(true);
                    } else {
                        StopMyWebsocketServer();
                    }
                    myWebsocketServer.start();
                    serverIsOpen = true;
                } catch (RuntimeException e) {
                    Log.i("", "Start Failed...");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 关闭socket server
     */
    public boolean StopMyWebsocketServer() {
        try {
            myWebsocketServer.stop();
            myWebsocketServer = null;
            serverIsOpen = false;
            Log.e("ServerManager", "Stop ServerSocket Success...");
            //  清空所有临时会议数据
            AttendeBeanList.clear();
            voteList.clear();
            voteMap.clear();
            userMap.clear();
            return true;
        } catch (Exception e) {
            Log.e("ServerManager", "Stop ServerSocket Failed...");
            e.printStackTrace();
            serverIsOpen = true;
            return false;
        }
    }

    /**
     * 重连机制
     */
    private final long HEART_BEAT_RATE = 5 * 1000;//每隔5秒进行一次重连
    private int reConnectCount = 2;               //重连2次
    private Handler mHandler = new Handler();
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (reConnectCount > 0) {
                reConnectCount--;
                try {
                    InetSocketAddress myHost = new InetSocketAddress(FLUtil.getIPAddress(), UrlConstant.port);

                    StopMyWebsocketServer();
                    myWebsocketServer = null;
                    myWebsocketServer = new MyWebSocketServer(myHost);
                    myWebsocketServer.setReuseAddr(true);
                    myWebsocketServer.start();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                //每隔一定的时间，对长连接进行一次心跳检测
                if (mHandler != null) {
                    mHandler.postDelayed(this, HEART_BEAT_RATE);
                }
            } else {
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                    reConnectCount = 2;
                }
            }
        }
    };

    /**
     * 服务端开启成功
     */
    public void serverOnStart() {
        removeHandler();
        EventMessage msg = new EventMessage(MessageReceiveType.MessageServer, constant.SERVERSTART);
        EventBus.getDefault().post(msg);

    }

    private void removeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        reConnectCount = 2;

    }

    /**
     * websocket服务器重连
     */
    public void reconnect() {
        if (reConnectCount == 2) {
            reconnectRunnable.run();
        }
    }

}
