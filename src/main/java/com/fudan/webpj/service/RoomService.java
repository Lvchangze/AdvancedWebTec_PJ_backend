package com.fudan.webpj.service;

import com.fudan.webpj.entity.Room;
import com.fudan.webpj.repository.RoomRepository;
import com.fudan.webpj.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        for (Room room : rooms) {
            if (WebSocketServer.roomList.get(room.getRoomId()) == null) {
                room.setCount(0);
            } else {
                room.setCount(WebSocketServer.roomList.get(room.getRoomId()).size());
            }
            roomRepository.save(room);
        }
        return rooms;
    }
}
