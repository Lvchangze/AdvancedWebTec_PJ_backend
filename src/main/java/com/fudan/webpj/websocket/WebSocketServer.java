package com.fudan.webpj.websocket;

import com.alibaba.fastjson.JSON;
import com.fudan.webpj.controller.UserController;
import com.fudan.webpj.service.HistoryService;
import com.fudan.webpj.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@ServerEndpoint(value = "/ws/{roomId}/{userId}")
@Slf4j
public class WebSocketServer {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HistoryService historyService;

    public static final Map<Integer, Map<String, Session>> roomList = new ConcurrentHashMap<>();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId,
                       @PathParam("userId") String userId,
                       Session session) {
        //房间不存在时，创建房间
        if (!roomList.containsKey(roomId)) {
            Map<String, Session> room = new ConcurrentHashMap<>();
            room.put(userId, session);
            roomList.put(roomId, room);
        } else {
            roomList.get(roomId).put(userId, session);
        }
        //有新用户连接，更新room的在线人数
        roomService.addCount(roomId);
        //记录历史
        historyService.addNewHistory(
                "",
                roomId,
                formatter.format(new Date(System.currentTimeMillis())),
                Message.ENTER,
                userId
        );
        //向所有人广播，谁进入了
        broadcastInsideRoom(
                roomId,
                Message.jsonStr(
                        Message.ENTER,
                        userId,
                        userId + "进入Room" + roomId,
                        formatter.format(new Date(System.currentTimeMillis()))
                )
        );
    }

    @OnMessage
    public void onMessage(@PathParam("roomId") int roomId,
                          @PathParam("userId") String userId,
                          Session session,
                          String msg
    ) {
        Message message = JSON.parseObject(msg, Message.class);
        switch (message.getType()) {
            case "SPEAK":
                logger.info("SPEAK");
                broadcastInsideRoom(
                        roomId,
                        Message.jsonStr(
                                Message.SPEAK,
                                userId,
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis()))
                        )
                );
                //记录历史
                historyService.addNewHistory(
                        message.getMsg(),
                        roomId,
                        formatter.format(new Date(System.currentTimeMillis())),
                        Message.ENTER,
                        userId
                );
                break;
            case "MOVE":
                logger.info("MOVE");

                break;
        }
    }

    @OnClose
    public void onClose(@PathParam("roomId") int roomId,
                        @PathParam("userId") String userId,
                        Session session) {
        roomList.get(roomId).remove(userId);
        //有用户断开，更新room的在线人数
        roomService.minusCount(roomId);
        //记录历史
        historyService.addNewHistory(
                "",
                roomId,
                formatter.format(new Date(System.currentTimeMillis())),
                Message.QUIT,
                userId
        );
        //向所有人广播，谁退出了
        broadcastInsideRoom(
                roomId,
                Message.jsonStr(
                        Message.QUIT,
                        userId,
                        userId + "退出Room" + roomId,
                        formatter.format(new Date(System.currentTimeMillis()))
                )
        );
    }


    private static void broadcastInsideRoom(int roomId, String msg) {
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