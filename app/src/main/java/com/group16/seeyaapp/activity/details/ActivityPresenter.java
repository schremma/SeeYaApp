package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

import java.util.List;

/**
 * Created by Andrea on 13/04/16.
 * Display a single activity.
 * It could be a still blank activity, if the user wishes to create a new activity.
 * It could also be an already created activity by the current user, which the user wishes to publish.
 */
public interface ActivityPresenter {

    // On pressing the button for creating a new activity
    void onCreateActivity(Activity activity);

    // On pressing the button for publishing an already created activity, to all users
    void onPublishActivity(long activityId);

    // On pressing the button for publishing an already created activity, for specific users
    void onPublishActivity(long activityId, List<String> invitees);

    // The user wants to view the already created activity, with the given id
    void aboutToDisplayActivity(int activityId);

    // The user wants to fill in an empty form to be able to create a new activity
    void aboutToCreateActivity();

    // every time a user wants to add a user on the list of invitees,
    // it needs to be checked if user name exists
    void checkIfUserExists(String userName);

}
