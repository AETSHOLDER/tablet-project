package com.example.paperlessmeeting_demo.enums;

/**
 *  MessageServer server端接受的消息
 *  MessageClient client端接受的消息
 *  MessageClick  连点事件接受的消息
 *  MessageRefreshMicSta 发言单元状态更新
 *  ConnectClientFail  client 连接失败
 *  MessageCreatTempMeeting 创建临时会议
 * */
public enum MessageReceiveType {
    MessageClient,
    MessageServer,
    MessageClick,
    MessageRefreshMicSta,
    ConnectClientFail,
    MessageCreatTempMeeting,
}
