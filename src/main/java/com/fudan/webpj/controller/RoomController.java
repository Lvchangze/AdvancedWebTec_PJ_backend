package com.fudan.webpj.controller;

import com.fudan.webpj.service.HistoryService;
import com.fudan.webpj.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping("/onlinePlayerCount")
    public ResponseEntity<Object> onlinePlayerCount(){
        HashMap<String, Object> hashMap = new HashMap<>();

        return ResponseEntity.ok(hashMap);
    }
}
