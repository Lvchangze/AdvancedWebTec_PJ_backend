package com.fudan.webpj.service;

import com.fudan.webpj.entity.User;
import com.fudan.webpj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(int id){
        return userRepository.findUserById(id);
    }
}
