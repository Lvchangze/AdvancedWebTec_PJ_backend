package com.fudan.webpj.websocket;

import com.alibaba.fastjson.JSON;
import com.fudan.webpj.service.HistoryService;
import com.fudan.webpj.service.RoomService;
import com.fudan.webpj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws/{roomId}/{userId}")
@Component
public class WebSocketServer {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        WebSocketServer.applicationContext = context;
    }

    private RoomService roomService;
    private HistoryService historyService;
    private UserService userService;

    public static final Map<Integer, Map<String, Session>> roomList = new ConcurrentHashMap<>();
    private final Map<String, String> locations = new ConcurrentHashMap<>(); // 记录各个用户的位置

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId,
                       @PathParam("userId") String userId,
                       Session session) {
        roomService = applicationContext.getBean(RoomService.class);
        historyService = applicationContext.getBean(HistoryService.class);
        userService = applicationContext.getBean(UserService.class);
        //房间不存在时，创建房间
        if (!roomList.containsKey(roomId)) {
            Map<String, Session> room = new ConcurrentHashMap<>();
            room.put(userId, session);
            roomList.put(roomId, room);
        } else {
            roomList.get(roomId).put(userId, session);
        }
        //记录初始位置
        locations.put(userId, "{\"x\":0,\"y\":0,\"z\":-170}");
        //有新用户连接，更新room的在线人数
        roomService.addCount(roomId);
        //记录历史
        historyService.addNewHistory(
                userId + "进入Room" + roomId,
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
                        userService.getUserInfo(userId).getRole(),
                        formatter.format(new Date(System.currentTimeMillis()))
                )
        );
        Map<String, Session> room = roomList.get(roomId);
        room.forEach((_userId, _session) -> {
            try {
                //当前用户告诉前端，当前所有其他人的形象
                session.getBasicRemote().sendText(
                        Message.jsonStr(
                                Message.ROLE,
                                _userId,
                                userService.getUserInfo(_userId).getRole(),
                                formatter.format(new Date(System.currentTimeMillis()))
                        )
                );
                Thread.sleep(500);
                //当前用户告诉前端，当前所有其他人的位置
                session.getBasicRemote().sendText(
                        Message.jsonStr(
                                Message.POSITION,
                                _userId,
                                locations.get(_userId),
                                formatter.format(new Date(System.currentTimeMillis()))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @OnMessage
    public void onMessage(@PathParam("roomId") int roomId,
                          @PathParam("userId") String userId,
                          Session session,
                          String msg
    ) {
        System.out.println(msg);
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
                        Message.SPEAK,
                        userId
                );
                break;
            case "POSITION":
                logger.info("POSITION");
                broadcastInsideRoom(
                        roomId,
                        Message.jsonStr(
                                Message.POSITION,
                                userId,
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis()))
                        )
                );
                locations.replace(userId, message.getMsg());
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
                userId + "退出Room" + roomId,
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
        locations.remove(userId);
    }

    //向指定房间内所有用户发送广播信息
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

    //向指定房间内指定用户发送信息
    private static void sendToSomeone(int roomId, String userId, String msg) {
        Session targetSession = roomList.get(roomId).get(userId);
        if (targetSession != null) {
            try {
                targetSession.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}