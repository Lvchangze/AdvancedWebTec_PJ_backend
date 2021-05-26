package com.fudan.webpj.websocket;

import com.alibaba.fastjson.JSON;

/**
 * WebSocket 聊天消息类
 */
public class Message {

    public static final String ENTER = "ENTER";
    public static final String SPEAK = "SPEAK";
    public static final String QUIT = "QUIT";
    public static final String MOVE = "MOVE";
    public static final String LIFT = "LIFT";
    public static final String DROP = "DROP";

    private String type;//消息类型

    private String userId;//发送人

    private String msg; //发送消息

    private String time;//发送时间

    private int onlineCount; //在线用户数

    public static String jsonStr(String type, String userId, String msg, String time, int onlineTotal) {
        return JSON.toJSONString(new Message(type, userId, msg, time, onlineTotal));
    }

    public Message(String type, String userId, String msg, String time, int onlineCount) {
        this.type = type;
        this.userId = userId;
        this.msg = msg;
        this.time = time;
        this.onlineCount = onlineCount;
    }

    public static String getENTER() {
        return ENTER;
    }

    public static String getSPEAK() {
        return SPEAK;
    }

    public static String getQUIT() {
        return QUIT;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}