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
}
