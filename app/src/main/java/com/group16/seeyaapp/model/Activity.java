package com.group16.seeyaapp.model;

import com.group16.seeyaapp.helpers.DateHelper;

import java.util.Date;

/**
 * Created by Andrea on 11/04/16.
 */
public class Activity {

    private long id;
    private String owner;
    private int subcategory;
    private String location;
    private Date date;
    private Date time;
    private String message;
    private String headline;
    private int minNbrOfParticipants;
    private int maxNbrOfParticipants;


    private String subcategoryString;

    private String locationDetails;
    private Date datePublished;

    private long nbrSignedUp;

    private String errorMsg;

    public String getValidationErrorMessage() {return errorMsg;}

    public boolean validateActivity() {
        errorMsg = "";
        if (date == null) {
            errorMsg += "Date cannot be empty\n";
        }
        if (time == null) {
            errorMsg += "Time cannot be empty\n";
        }
        if (location == null || location.isEmpty()) {
            errorMsg += "Location cannot be empty\n";
        }
        if (headline == null || headline.isEmpty()) {
            errorMsg += "Headline cannot be empty\n";
        }
        if (message == null || message.isEmpty()) {
            errorMsg += "Message cannot be empty\n";
        }

        if (errorMsg.length() > 0)
            return false;
        return true;
    }

    @Override
    public String toString() {
        String strOut = String.format("Activity: %s\n%s, %s at %s\nMessage: %s\nMinimum participants: %d\n" +
                        "Maximum participants: %d\n", headline, location, DateHelper.DateToDateOnlyString(date),
                DateHelper.DateToTimeOnlyString(time), message, minNbrOfParticipants, maxNbrOfParticipants);
        if (datePublished != null) {
            strOut += "Published on: " + datePublished.toString();
        }
        else
            strOut += "Unpublished";

        return strOut;
    }

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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getSubcategoryString() {
        return subcategoryString;
    }

    public void setSubcategoryString(String subcategoryString) {
        this.subcategoryString = subcategoryString;
    }

    public long getNbrSignedUp() {
        return nbrSignedUp;
    }

    public void setNbrSignedUp(long nbrSignedUp) {
        this.nbrSignedUp = nbrSignedUp;
    }
    //endregion


}
