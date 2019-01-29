package com.company;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private String msg = null;
    private String user = null;

    public Message(){

    }

    public Message(String msg, String user) {
        this.msg = msg;
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
