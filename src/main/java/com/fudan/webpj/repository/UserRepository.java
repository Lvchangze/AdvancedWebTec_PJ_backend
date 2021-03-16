package com.fudan.webpj.repository;

import com.fudan.webpj.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User findUserById(int id);
}