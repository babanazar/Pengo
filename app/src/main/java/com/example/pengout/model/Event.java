package com.example.pengout.model;

import android.util.Log;

import com.google.firebase.database.core.utilities.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Event {

    private String name;
    private String date;
    private String place;
    private String time;
    private String category;
    private String url;
    private String desc;
    private Location location;

    public Event(){

    }

    public Event(String name, String date, String place, String time, String category, String url,String desc,Location location) {
        this.name = name;
        this.date = date;
        this.place = place;
        this.time = time;
        this.category = category;
        this.url = url;
        this.desc = desc;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

//    public ArrayList<String> getLocation() {
////        Log.d(TAG, "getLocation: ");
//        ArrayList<String> a = new ArrayList<>();
//        a.add(location.getLat());
//        a.add(location.getLon());
//        return a;
//    }
//
//    public void setLocation(Location location) {
//        this.location = location;
//    }


    public class Location implements Serializable {


        private String lat;
        private String lon;

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }
    }


}
