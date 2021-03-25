package com.fudan.webpj.repository;

import com.fudan.webpj.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User findUserById(String id);

    void addNewUser(String id, String password, int age, int gender);
}