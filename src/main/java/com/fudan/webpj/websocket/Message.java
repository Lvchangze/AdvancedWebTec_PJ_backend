package com.fudan.webpj.websocket;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * WebSocket 聊天消息类
 */
@Data
@AllArgsConstructor
public class Message {

    public static final String ENTER = "ENTER";
    public static final String ROLE = "ROLE";
    public static final String SPEAK = "SPEAK";
    public static final String QUIT = "QUIT";
    public static final String POSITION = "POSITION";
    public static final String LIFT = "LIFT";
    public static final String DROP = "DROP";

    private String type;//消息类型

    private String userId;//发送人

    private String msg; //发送消息

    private String time;//发送时间


    public static String jsonStr(String type, String userId, String msg, String time) {
        return JSON.toJSONString(new Message(type, userId, msg, time));
    }
}