package com.fudan.webpj.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roomId;
    private String name;
    private String desc;
}
