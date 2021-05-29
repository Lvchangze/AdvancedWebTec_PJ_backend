package com.fudan.webpj.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Room {
    @Id
    private int roomId;
    private String name;
    private String description;
}
