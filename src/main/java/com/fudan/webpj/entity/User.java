package com.fudan.webpj.entity;

import javax.persistence.Entity;
import java.util.Objects;

public class User {
    private String id;
    private String passWord;
    private int age;
    private int gender;

    public User() {
    }

    public User(String id, String passWord, int age, int gender) {
        this.id = id;
        this.passWord = passWord;
        this.age = age;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", passWord='" + passWord + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
