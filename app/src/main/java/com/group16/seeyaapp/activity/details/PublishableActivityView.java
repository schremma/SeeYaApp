package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 */
public interface PublishableActivityView {

    // Display information stored in the provided Activity
    void displayActivityDetails(Activity activity);

    // if published == true: activity has just been successfully published
    void updatePublishedStatus(boolean published);

    void showOnError(String errorMessage);

    void setInvitedUserList(List<String> invitedUserList);

}
