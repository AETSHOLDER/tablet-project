package com.example.paperlessmeeting_demo.socket;

/**
 * Created by fengli on 2018/2/9.
 */

public class ClientManager {

    public static String currentUserId = "";
    public static String chattingUserId = "";

    public static boolean isChattingWithUser(String user) {

        return user.equals(ClientManager.chattingUserId);
    }
}