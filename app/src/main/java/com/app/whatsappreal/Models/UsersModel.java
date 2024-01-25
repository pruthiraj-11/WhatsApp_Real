package com.app.whatsappreal.Models;

public class UsersModel {
    private String userId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String imageProfile;
    private String imageCover;
    private String bio;
    private String activityStatus;

    public UsersModel() {
    }

    public UsersModel(String userId, String userName, String userPhone, String imageProfile, String imageCover, String bio, String activityStatus) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
        this.bio = bio;
        this.activityStatus = activityStatus;
    }

    public UsersModel(String userId, String userName, String userPhone, String userEmail, String imageProfile, String imageCover, String bio, String activityStatus) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
        this.bio = bio;
        this.activityStatus = activityStatus;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }
}
