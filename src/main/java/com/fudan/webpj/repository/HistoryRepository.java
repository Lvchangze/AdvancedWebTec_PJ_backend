package com.fudan.webpj.repository;

import com.fudan.webpj.entity.History;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface HistoryRepository extends CrudRepository<History, Integer> {

    List<History> findAllByUserId(String userId);

}
