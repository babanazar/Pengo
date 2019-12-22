package com.example.pengout.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/26/2017.
 */

@IgnoreExtraProperties
public class User {
    private String userId;
    private String username;
    private String imageURL;
    private String[] userInterest;

    public User(){
    }

    public User(String userId, String userName, String[] userInterest, String imageURL) {
        this.userId = userId;
        this.username = userName;
        this.userInterest = userInterest;
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String[] getUserInterest() {
        return userInterest;
    }

    public void setUserInterest(String[] userInterest) {
        this.userInterest = userInterest;
    }

}
