package com.fudan.webpj.repository;

import com.fudan.webpj.entity.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    List<Room> findAll();
}
