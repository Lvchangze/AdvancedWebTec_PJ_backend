package com.fudan.webpj.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int historyId;
    private String type;
    private String userId;
    private String message;
    private String time;
    private int roomId;
}

