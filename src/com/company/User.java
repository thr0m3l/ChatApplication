package com.company;

import java.io.Serializable;

public class User implements Serializable {
    String userName;
    String password;
    String userType;
    private static final long serialVersionUID = 3L;

    public User(String userName, String password, String userType) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public User(String userType) {
        this.userType = userType;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
