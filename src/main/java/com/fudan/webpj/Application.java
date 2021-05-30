package com.fudan.webpj;

import com.fudan.webpj.entity.History;
import com.fudan.webpj.entity.Room;
import com.fudan.webpj.repository.HistoryRepository;
import com.fudan.webpj.repository.RoomRepository;
import com.fudan.webpj.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HistoryRepository historyRepository;

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Application.class ,args);
        WebSocketServer.setApplicationContext(configurableApplicationContext);
    }

    @Override
    public void run(String... args) {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Room room = new Room();
            room.setRoomId(i + 1);
            room.setName("汉诺塔" + i + "级");
            room.setDescription("在这个房间中，用户可以进行 " + (i + 3) + " 层汉诺塔的相关教学实验");
            rooms.add(room);
        }
        roomRepository.saveAll(rooms);

        History history = new History();
        history.setType("test");
        history.setUserId("test");
        history.setMessage("test");
        history.setRoomId(1);
        history.setTime("test");
        historyRepository.save(history);
    }
}
