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

    //记录各个房间号下各个用户的session
    public static final Map<Integer, Map<String, Session>> roomList = new ConcurrentHashMap<>();
    // 记录各个房间号下各个用户的位置
    private static final Map<Integer, Map<String, String>> rolePositionList = new ConcurrentHashMap<>();
    // 记录各个房间号下各个盘子的位置
    private static final Map<Integer, String> diskPositionList = new ConcurrentHashMap<>();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId,
                       @PathParam("userId") String userId,
                       Session session) {
        roomService = applicationContext.getBean(RoomService.class);
        historyService = applicationContext.getBean(HistoryService.class);
        userService = applicationContext.getBean(UserService.class);

        if (!roomList.containsKey(roomId)) {//房间号不存在时，创建房间
            Map<String, Session> room = new ConcurrentHashMap<>();
            room.put(userId, session);
            roomList.put(roomId, room);
        } else {
            roomList.get(roomId).put(userId, session);
        }

        if (!rolePositionList.containsKey(roomId)) {
            Map<String, String> locations = new ConcurrentHashMap<>();
            locations.put(userId, "{\"x\":0,\"y\":0,\"z\":-170}");
            rolePositionList.put(roomId, locations);
        } else {
            rolePositionList.get(roomId).put(userId, "{\"x\":0,\"y\":0,\"z\":-170}");
        }

        if (!diskPositionList.containsKey(roomId)) {
            switch (roomId) {
                case 1:
                    diskPositionList.put(roomId, "[{\"location\":1,\"position\":3},{\"location\":1,\"position\":2},{\"location\":1,\"position\":1}]");
                    break;
                case 2:
                    diskPositionList.put(roomId, "[{\"location\":1,\"position\":4},{\"location\":1,\"position\":3},{\"location\":1,\"position\":2},{\"location\":1,\"position\":1}]");
                    break;
                case 3:
                    diskPositionList.put(roomId, "[{\"location\":1,\"position\":5},{\"location\":1,\"position\":4},{\"location\":1,\"position\":3},{\"location\":1,\"position\":2},{\"location\":1,\"position\":1}]");
                    break;
            }
        }

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

        //向所有人广播，哪个userId进入
        broadcastInsideRoom(
                roomId,
                Message.jsonStr(
                        Message.ENTER,
                        userId,
                        userId + "进入Room" + roomId,
                        formatter.format(new Date(System.currentTimeMillis()))
                )
        );
        //向所有人广播，哪个role进入了
        broadcastInsideRoom(
                roomId,
                Message.jsonStr(
                        Message.ROLE,
                        userId,
                        userService.getUserInfo(userId).getRole(),
                        formatter.format(new Date(System.currentTimeMillis()))
                )
        );
        //在刚连接时，需要向前端广播，当前状态下其他所有人物的信息
        Map<String, Session> room = roomList.get(roomId);
        room.forEach((_userId, _session) -> {
            if (!userId.equals(_userId)) {
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
                    Thread.sleep(5000);
                    System.out.println(_userId + ":" + rolePositionList.get(roomId).get(_userId));
                    //当前用户告诉前端，当前所有其他人的位置
                    session.getBasicRemote().sendText(
                            Message.jsonStr(
                                    Message.POSITION,
                                    _userId,
                                    rolePositionList.get(roomId).get(_userId),
                                    formatter.format(new Date(System.currentTimeMillis()))
                            )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(5000);
            //当前用户告诉前端，当前所有盘子的位置
            session.getBasicRemote().sendText(
                    Message.jsonStr(
                            Message.DISK,
                            userId,
                            diskPositionList.get(roomId),
                            formatter.format(new Date(System.currentTimeMillis()))
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                rolePositionList.get(roomId).put(userId, message.getMsg());
                break;
            case "DISK":
                logger.info("DISK");
                //更新盘子的位置
                diskPositionList.put(roomId, message.getMsg());
            case "LIFT":
                logger.info("LIFT");
                broadcastInsideRoom(
                        roomId,
                        Message.jsonStr(
                                Message.LIFT,
                                userId,
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis()))
                        )
                );
                //记录历史
                historyService.addNewHistory(
                        userId + "举起盘子",
                        roomId,
                        formatter.format(new Date(System.currentTimeMillis())),
                        Message.LIFT,
                        userId
                );
                break;
            case "DROP":
                logger.info("DROP");
                broadcastInsideRoom(
                        roomId,
                        Message.jsonStr(
                                Message.DROP,
                                userId,
                                message.getMsg(),
                                formatter.format(new Date(System.currentTimeMillis()))
                        )
                );
                //记录历史
                historyService.addNewHistory(
                        userId + "放下盘子",
                        roomId,
                        formatter.format(new Date(System.currentTimeMillis())),
                        Message.DROP,
                        userId
                );
                break;
        }
    }

    @OnClose
    public void onClose(@PathParam("roomId") int roomId,
                        @PathParam("userId") String userId,
                        Session session) {
        roomList.get(roomId).remove(userId);
        rolePositionList.get(roomId).remove(userId);
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

        //TODO:如果最后一个人退出房间，重置盘子的位置

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
