package com.fudan.webpj.controller;

import com.fudan.webpj.entity.History;
import com.fudan.webpj.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @RequestMapping("/getUserHistory")
    public ResponseEntity<Object> getUserHistory(@RequestParam("userId") String userId){
        HashMap<String, Object> hashMap = new HashMap<>();
        List<History> histories = historyService.getUserHistory(userId);
        hashMap.put("list", histories);
        return ResponseEntity.ok(hashMap);
    }

}
