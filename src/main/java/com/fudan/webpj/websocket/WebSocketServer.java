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
@ServerEndpoint("/{roomId}/{action}")
@Slf4j
public class WebSocketServer {
    private static final Map<Integer, Set<Session>> roomList = new ConcurrentHashMap<>();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId,
                       Session session) {
        if (!roomList.containsKey(roomId)) {//房间不存在时，创建房间
            Set<Session> room = new HashSet<>();
            room.add(session);
            roomList.put(roomId, room);
        } else {//房间已存在，直接添加用户到相应的房间
            roomList.get(roomId).add(session);
        }
        broadcast(
                roomId,
                ChatMessage.jsonStr(
                        ChatMessage.ENTER,
                        "",
                        "",
                        formatter.format(new Date(System.currentTimeMillis())),
                        roomList.get(roomId).size()
                )
        );
        System.out.println("A client has connected!");
    }

    @OnMessage
    public void onMessage(@PathParam("roomId") int roomId,
                          @PathParam("action") String action,
                          Session session, String msg) {
        switch (action) {
            case "SPEAK":
                logger.info("SPEAK");
                ChatMessage message = JSON.parseObject(msg, ChatMessage.class);
                broadcast(
                        roomId,
                        ChatMessage.jsonStr(
                                ChatMessage.SPEAK,
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
        roomList.get(roomId).remove(session);
        broadcast(
                roomId,
                ChatMessage.jsonStr(
                        ChatMessage.QUIT,
                        "",
                        "",
                        formatter.format(new Date(System.currentTimeMillis())),
                        roomList.get(roomId).size()
                )
        );
        System.out.println("A client has disconnected!");
    }

    private static void broadcast(int roomId, String msg) {
        for (Session session : roomList.get(roomId)) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}