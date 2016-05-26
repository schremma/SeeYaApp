package com.group16.seeyaapp.activity.details;

/**
 * Created by Andrea on 25/04/16.
 * Handles the logic of displaying details of an activity, without providing editing possibilities.
 * It retrieves activity information from the server based an activity id, and
 * it enables the user to sign up or unregister from the activity.
 */
public interface DetailPresenter {

    // Sign up the user for the current activity if it is possible
    void onPressedJoin();

    // Unregister the user from the current activity
    void onPressedUnjoin();

    // The user wants to view an already created activity, with the given id
    void aboutToDisplayActivity(int activityId);

}
