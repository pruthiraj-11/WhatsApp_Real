package com.app.whatsappreal.Models;

public class CallList {
    private String userId;
    private String userName;
    private String date;
    private String callType;
    private String userProfileURL;

    public CallList() {
    }

    public CallList(String userId, String userName, String date, String callType, String userProfileURL) {
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.callType = callType;
        this.userProfileURL = userProfileURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getUserProfileURL() {
        return userProfileURL;
    }

    public void setUserProfileURL(String userProfileURL) {
        this.userProfileURL = userProfileURL;
    }
}
