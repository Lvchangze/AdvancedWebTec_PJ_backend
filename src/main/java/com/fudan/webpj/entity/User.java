package com.fudan.webpj.entity;

public class User {
    private String id;
    private String passWord;
    private int age;
    private int gender;
    private String character;

    public User() {
    }

    public User(String id, String passWord, int age, int gender, String character) {
        this.id = id;
        this.passWord = passWord;
        this.age = age;
        this.gender = gender;
        this.character = character;
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

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", passWord='" + passWord + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", character" + character + '\'' +
                '}';
    }
}
