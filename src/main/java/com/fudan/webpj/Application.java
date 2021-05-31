package com.fudan.webpj;

import com.fudan.webpj.entity.History;
import com.fudan.webpj.entity.Room;
import com.fudan.webpj.entity.User;
import com.fudan.webpj.repository.HistoryRepository;
import com.fudan.webpj.repository.RoomRepository;
import com.fudan.webpj.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Application.class, args);
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

        User user1 = new User();
        user1.setUserId("lvchangze");
        user1.setPassword("lvchangze");
        user1.setGender(1);
        user1.setAge(22);
        user1.setRole("胡桃");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUserId("yangyuhan");
        user2.setPassword("yangyuhan");
        user2.setGender(1);
        user2.setAge(22);
        user2.setRole("莫娜");
        userRepository.save(user2);

    }
}
