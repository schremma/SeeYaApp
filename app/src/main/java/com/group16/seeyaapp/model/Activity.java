package com.group16.seeyaapp.model;

import com.group16.seeyaapp.helpers.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String address;
    private Date datePublished;

    private long nbrSignedUp;



    private List<String> lstInvited;

    private boolean attending;

    private String errorMsg;

    public String getValidationErrorMessage() {return errorMsg;}

    //TODO add checking if date has passed
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

    public String dateLocationString() {
        return String.format("%s, %s at %s", location, DateHelper.DateToDateOnlyString(date),
                DateHelper.DateToTimeOnlyString(time));
    }

    public String participantInfoString() {
        String maximumParticipants = "";
        if (maxNbrOfParticipants != 0) {
            maximumParticipants = Integer.toString(maxNbrOfParticipants);
        }
        else {
            maximumParticipants = "no limit is defined";
        }
        String minimumParticipants = "";
        if (minNbrOfParticipants != 0) {
            minimumParticipants = Integer.toString(minNbrOfParticipants);
        }
        else {
            minimumParticipants = "no minimum is defined";
        }
        return String.format("Minimum participants: %s\nMaximum participants: %s", minimumParticipants, maximumParticipants);
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

    // True if at least one more participant can still sign up.
    // Assumes that 0 or negative upper limit means no limit
    public boolean stillHasSpace() {
        if (maxNbrOfParticipants > 0) {
            return maxNbrOfParticipants > nbrSignedUp;
        }
        return true;
    }

    // TODO use this
    public void addInvitedUser(String username) {
        if (lstInvited == null)
                lstInvited = new ArrayList<>();
        lstInvited.add(username);
    }

    public boolean isUserAlreadyInvited(String username) {
        if (lstInvited == null)
            return false;
        return lstInvited.contains(username);
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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<String> getLstInvited() {
        if (lstInvited != null)
            return lstInvited;
        else
            return new ArrayList<>();
    }
    public boolean isAttending() {
        return attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

    //endregion


}
