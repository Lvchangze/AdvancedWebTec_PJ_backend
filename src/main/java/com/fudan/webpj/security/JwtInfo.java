package com.fudan.webpj.security;

public class JwtInfo {
    private String uid;

    public JwtInfo(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "JwtInfo{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
