package com.fudan.webpj.service;

import com.fudan.webpj.security.JwtTokenUtil;
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
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        String[] result = new String[2];
        result[0] = jwtTokenUtil.generateToken(user);
        result[1] = user.getId();
        return result;
    }

    public User register(String id, String password, int age, int gender, String role) {
        //用户名已经存在
        if (userRepository.findUserById(id) != null) {
            return null;
        }
        userRepository.addNewUser(id, password, age, gender, role);
        return userRepository.findUserById(id);
    }

    public User getUserInfo(String id) {
        return userRepository.findUserById(id);
    }

    public User changeCharacter(String id, String role) {
        if (userRepository.findUserById(id) == null) {
            return null;
        }
        userRepository.changeCharacter(id, role);
        return userRepository.findUserById(id);
    }

}
