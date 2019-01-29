package com.company;

public class LMessage extends Message {
    private String userName;
    private String password;
    private String userType;

    public LMessage(String userName, String password, String userType) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        super.setMessageType(MessageType.LMessage);
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
