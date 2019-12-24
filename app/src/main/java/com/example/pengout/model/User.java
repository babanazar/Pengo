package com.example.pengout.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/26/2017.
 */

@IgnoreExtraProperties
public class User {
    private String userId;
    private String name;
    private String image;
    private String userInterest;
    private String address;

    public User(){
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserInterest() {
        return userInterest;
    }

    public void setUserInterest(String userInterest) {
        this.userInterest = userInterest;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
