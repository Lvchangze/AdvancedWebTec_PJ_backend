package com.fudan.webpj.websocket;

import com.alibaba.fastjson.JSON;
import com.fudan.webpj.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ws/{roomId}")
@Slf4j
public class WebSocketServer {
    private static final Map<Integer, Map<String, Session>> roomList = new ConcurrentHashMap<>();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId, Session session) {
        //房间不存在时，创建房间
        if (!roomList.containsKey(roomId)) {
            Map<String, Session> room = new ConcurrentHashMap<>();
            roomList.put(roomId, room);
        }
        System.out.println(roomList.get(roomId).size());
    }

    @OnMessage
    public void onMessage(@PathParam("roomId") int roomId,
                          Session session, String msg) {
        Message message = JSON.parseObject(msg, Message.class);
        switch (message.getType()) {
            case "ENTER":
                logger.info("ENTER");
                roomList.get(roomId).put(message.getUserId(), session);
                broadcast(
                        roomId,
                        Message.jsonStr(
                                Message.ENTER,
                                message.getUserId(),
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis())),
                                roomList.get(roomId).size()
                        )
                );
                break;
            case "QUIT":
                logger.info("QUIT");
                roomList.get(roomId).remove(message.getUserId());
                broadcast(
                        roomId,
                        Message.jsonStr(
                                Message.QUIT,
                                message.getUserId(),
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis())),
                                roomList.get(roomId).size()
                        )
                );
                break;
            case "SPEAK":
                logger.info("SPEAK");
                broadcast(
                        roomId,
                        Message.jsonStr(
                                Message.SPEAK,
                                message.getUserId(),
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis())),
                                roomList.get(roomId).size()
                        )
                );
                break;

            case "MOVE":
                logger.info("MOVE");
                break;
        }
    }

    @OnClose
    public void onClose(@PathParam("roomId") int roomId, Session session) {
        //房间无人时，删除房间
        if(roomList.get(roomId).size() == 0){
            roomList.remove(roomId);
        }
    }

    private static void broadcast(int roomId, String msg) {
        Map<String, Session> room = roomList.get(roomId);
        room.forEach((userId, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}