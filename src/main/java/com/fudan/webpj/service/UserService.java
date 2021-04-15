package com.fudan.webpj.service;

import com.fudan.webpj.Security.JwtTokenUtil;
import com.fudan.webpj.entity.User;
import com.fudan.webpj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findUserById(id);
        if (user == null || !user.getPassWord().equals(password)) {
            return null;
        }
        String[] result = new String[2];
        result[0] = jwtTokenUtil.generateToken(user);
        System.out.println(result[0]);
        result[1] = user.getId();
        return result;
    }

    public User register(String id, String password, int age, int gender) {
        //用户名已经存在
        if (userRepository.findUserById(id) != null) {
            return null;
        }
        userRepository.addNewUser(id, password, age, gender);
        return userRepository.findUserById(id);
    }

    public User[] listAllUsers() {
        return userRepository.listAll();
    }
}
