package com.fudan.webpj.repository;

import com.fudan.webpj.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.*;


public interface UserRepository extends CrudRepository<User, String> {

    User findByUserId(String userId);

}
