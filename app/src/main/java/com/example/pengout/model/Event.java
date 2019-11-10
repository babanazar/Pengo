package com.example.pengout.model;

public class Event {

    private String eventName;
    private String eventDate;
    private String eventPlace;
    private String eventTime;


    public void setEventName(String name){
        this.eventName = name;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventName(){
        return eventName;
    }

    public String getEventDate(){
        return eventDate;
    }

    public String getEventPlace(){
        return eventPlace;
    }
    public String getEventTime(){
        return eventTime;
    }

}
