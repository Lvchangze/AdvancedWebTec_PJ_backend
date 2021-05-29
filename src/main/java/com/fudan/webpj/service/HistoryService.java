package com.fudan.webpj.service;

import com.fudan.webpj.entity.History;
import com.fudan.webpj.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<History> getUserHistory(String userId){
        return historyRepository.findAllByUserId(userId);
    }

}
