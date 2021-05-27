package com.fudan.webpj.controller;

import com.fudan.webpj.entity.User;
import com.fudan.webpj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public ResponseEntity<Object> login(
            @RequestParam("id") String id,
            @RequestParam("password") String password
    ) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String[] resp = userService.login(id, password);
        if (resp != null) {
            hashMap.put("token", resp[0]);
            hashMap.put("id", resp[1]);
            logger.info("login:" + resp[1]);
        } else {
            logger.info("用户名或密码错误");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(hashMap);
    }

    @RequestMapping("/register")
    public ResponseEntity<Object> register(
            @RequestParam("id") String id,
            @RequestParam("password") String password,
            @RequestParam("age") int age,
            @RequestParam("gender") int gender,
            @RequestParam("character") String character
    ) {
        HashMap<String, Object> hashMap = new HashMap<>();
        User user = userService.register(id, password, age, gender, character);
        hashMap.put("user", user);
        if (user != null) {
            logger.info("new user:" + user);
        } else {
            logger.info("用户名已存在");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(hashMap);
    }

    @RequestMapping("/getUserInfo")
    public ResponseEntity<Object> getUserInfo(@RequestParam("userId") String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        User user = userService.getUserInfo(id);
        hashMap.put("user", user);
        return ResponseEntity.ok(hashMap);
    }

    @RequestMapping("/changeCharacter")
    public ResponseEntity<Object> changeCharacter(
            @RequestParam("userId") String id,
            @RequestParam("character") String character
    ){
        HashMap<String, Object> hashMap = new HashMap<>();
        User user = userService.changeCharacter(id, character);
        if (user != null){
            hashMap.put("result", 0);
        }else {
            hashMap.put("result", -1);
        }
        return ResponseEntity.ok(hashMap);
    }
}