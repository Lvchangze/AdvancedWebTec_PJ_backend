package com.fudan.webpj.entity;

public class History {
    private String type;
    private String userId;
    private String message;
    private String time;
    private int roomId;

    public History() {
    }

    public History(String type, String userId, String message, String time, int roomId) {
        this.type = type;
        this.userId = userId;
        this.message = message;
        this.time = time;
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}

