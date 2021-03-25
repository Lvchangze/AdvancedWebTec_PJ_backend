package com.fudan.webpj.service;

import com.fudan.webpj.entity.User;
import com.fudan.webpj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String id, String password) {
        User user = userRepository.findUserById(id);
        if (user == null || !user.getPassWord().equals(password)) {
            return null;
        }
        return user;
    }

    public User register(String id, String password, int age, int gender) {
        //用户名已经存在
        if (userRepository.findUserById(id) != null) {
            return null;
        }
        userRepository.addNewUser(id, password, age, gender);
        return userRepository.findUserById(id);
    }

}
