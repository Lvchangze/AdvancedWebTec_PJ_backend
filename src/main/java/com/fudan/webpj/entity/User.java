package com.fudan.webpj.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    private String id;
    private String password;
    private int age;
    private int gender;
    private String role;

    /*@Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", passWord='" + passWord + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", character" + character + '\'' +
                '}';
    }*/
}
