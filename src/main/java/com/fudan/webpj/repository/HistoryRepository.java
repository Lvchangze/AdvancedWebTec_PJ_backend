package com.fudan.webpj.repository;

import com.fudan.webpj.entity.History;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository {
    History[] findHistoryByUserId(String userId);

    void addNewHistory(String type, String userId, String message, String time , int roomId);
}
