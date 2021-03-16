package com.fudan.webpj.controller;

import com.fudan.webpj.entity.User;
import com.fudan.webpj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @RequestMapping("getUser/{id}")
    public User GetUser(@PathVariable int id){
        return userService.getUserById(id);
    }
}