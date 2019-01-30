package com.company;

import java.io.Serializable;

enum MessageType{
    LMessage,BMessage,CMessage,SMessage;
}

public class Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private String msg = null;
    private User user = null;
    private MessageType messageType = null;

    public Message(){

    }

    public Message(String msg, User user) {
        this.msg = msg;
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
