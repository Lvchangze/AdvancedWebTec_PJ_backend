package com.fudan.webpj.service;

import com.fudan.webpj.security.JwtTokenUtil;
import com.fudan.webpj.entity.User;
import com.fudan.webpj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    UserRepository userRepository;
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String[] login(String id, String password) {
        User user = userRepository.findByUserId(id);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        String[] result = new String[2];
        result[0] = jwtTokenUtil.generateToken(user);
        result[1] = user.getUserId();
        return result;
    }

    public User register(String id, String password, int age, int gender, String role) {
        //用户名已经存在
        if (userRepository.findByUserId(id) != null) {
            return null;
        }
        User user = new User();
        user.setUserId(id);
        user.setPassword(password);
        user.setAge(age);
        user.setGender(gender);
        user.setRole(role);
        userRepository.save(user);
        return userRepository.findByUserId(id);
    }

    public User getUserInfo(String id) {
        return userRepository.findByUserId(id);
    }

    public User changeCharacter(String id, String role) {
        User user = userRepository.findByUserId(id);
        if (user == null) {
            return null;
        }
        user.setRole(role);
        userRepository.save(user);
        return userRepository.findByUserId(id);
    }

}
