package com.example.mensajeriaprogra.models;

import java.util.ArrayList;

public class Chat {

    private String id;
    private String writing;
    private long timestamp;
    private ArrayList<String> ids;
    private int numberMessages;
    private int idNotification;
    private boolean isMultiChat;
    private String groupName;
    private String groupImage;

    public Chat() {
    }

    public Chat(String id, String writing, long timestamp, ArrayList<String> ids, int numberMessages, int idNotification, boolean isMultiChat, String groupName, String groupImage) {
        this.id = id;
        this.writing = writing;
        this.timestamp = timestamp;
        this.ids = ids;
        this.numberMessages = numberMessages;
        this.idNotification = idNotification;
        this.isMultiChat = isMultiChat;
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public int getNumberMessages() {
        return numberMessages;
    }

    public void setNumberMessages(int numberMessages) {
        this.numberMessages = numberMessages;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public boolean isMultiChat() {
        return isMultiChat;
    }

    public void setMultiChat(boolean multiChat) {
        isMultiChat = multiChat;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }
}


   
