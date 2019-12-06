package com.example.pengout.model;

public class Event {

    private String name;
    private String date;
    private String place;
    private String time;


    public Event(){

    }

    public Event(String name, String date, String place, String time) {
        this.name = name;
        this.date = date;
        this.place = place;
        this.time = time;
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

}
