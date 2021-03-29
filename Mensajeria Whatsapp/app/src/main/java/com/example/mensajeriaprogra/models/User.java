package com.example.mensajeriaprogra.models;

public class User {

    private String id;
    private String username;
    private String phone;
    private String image;
    private String info;
    private String token;
    private long lastConnect;
    private boolean online;
    private boolean isSelected;

    public User() {

    }

    public User(String id, String username, String phone, String image, String info, String token, long lastConnect, boolean online, boolean isSelected) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.image = image;
        this.info = info;
        this.token = token;
        this.lastConnect = lastConnect;
        this.online = online;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLastConnect() {
        return lastConnect;
    }

    public void setLastConnect(long lastConnect) {
        this.lastConnect = lastConnect;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

