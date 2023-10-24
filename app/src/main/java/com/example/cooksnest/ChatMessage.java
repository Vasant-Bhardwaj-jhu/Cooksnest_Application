package com.example.cooksnest;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String users;
    private long messageTime;
    private String fromUser;
    private String toUser;

    public ChatMessage(String messageText, String from, String to, String compound) {
        this.messageText = messageText;
        users = compound;
        fromUser = from;
        toUser = to;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public String getUsers() {
        return users;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }
}

