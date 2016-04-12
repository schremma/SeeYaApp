package com.group16.seeyaapp.model;

import java.util.Date;

/**
 * Created by Andrea on 11/04/16.
 */
public class Activity {

    private int id;
    private String owner;
    private int subcategory;
    private String location;
    private Date date;
    private Date time;
    private String message;
    private  String headline;
    private int minNbrOfParticipants;
    private int maxNbrOfParticipants;

    private boolean published;
    private String locationDetails;
    private Date datePublished;


    // True if the time of the activity has already passed
    public boolean expired() {
        Date today = new Date();
        return (today.after(date));
    }

    //region Getters and setters for all fields
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(int subcategory) {
        this.subcategory = subcategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getMinNbrOfParticipants() {
        return minNbrOfParticipants;
    }

    public void setMinNbrOfParticipants(int minNbrOfParticipants) {
        this.minNbrOfParticipants = minNbrOfParticipants;
    }

    public int getMaxNbrOfParticipants() {
        return maxNbrOfParticipants;
    }

    public void setMaxNbrOfParticipants(int maxNbrOfParticipants) {
        this.maxNbrOfParticipants = maxNbrOfParticipants;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //endregion


}
