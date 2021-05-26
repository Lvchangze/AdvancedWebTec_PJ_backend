package com.fudan.webpj.service;

import com.fudan.webpj.entity.History;
import com.fudan.webpj.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public History[] getUserHistory(String userId){
        return historyRepository.findHistoryByUserId(userId);
    }

    public void addNewHistory(String type, String userId, String message, String time , int roomId){
        historyRepository.addNewHistory(type, userId, message, time , roomId);
    }

}
