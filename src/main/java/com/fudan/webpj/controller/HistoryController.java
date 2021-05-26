package com.fudan.webpj.controller;

import com.fudan.webpj.entity.History;
import com.fudan.webpj.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @RequestMapping("/getUserHistory")
    public ResponseEntity<Object> login(@RequestParam("userId") String userId){
        HashMap<String, Object> hashMap = new HashMap<>();
        History[] histories = historyService.getUserHistory(userId);
        hashMap.put("list", histories);
        return ResponseEntity.ok(hashMap);
    }

}
