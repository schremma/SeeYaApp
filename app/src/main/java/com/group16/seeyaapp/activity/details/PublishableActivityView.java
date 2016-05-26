package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 * Displays an already created activity that has not been published yet.
 * Shows controls for adding a list of users to be invited to the activity
 * and control for publishing the activity.
 */
public interface PublishableActivityView {

    // Display information stored in the provided Activity
    void displayActivityDetails(Activity activity);

    // if published == true: activity has just been successfully published
    void updatePublishedStatus(boolean published);

    // Display the provided list of invited users
    void setInvitedUserList(List<String> invitedUserList);

    // Display the provided error message
    void showOnError(String errorMessage);

}
