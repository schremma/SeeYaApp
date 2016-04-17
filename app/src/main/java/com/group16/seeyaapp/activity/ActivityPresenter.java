package com.group16.seeyaapp.activity;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 13/04/16.
 * Display a single activity.
 * It could be a still blank activity, if the user wishes to create a new activity.
 * It could also be an already created activity by the current user, which the user wishes to publish.
 */
public interface ActivityPresenter {

    // On pressing the button for creating a new activity
    void onCreateActivity(Activity activity);

    // On pressing the button for publishing an already created activity
    void onPublishActivity(long activityId);

    // The user wants to view the already created activity, with the given id
    void aboutToDisplayActivity(int activityId);

    // The user wants to fill in an empty form to be able to create a new activity
    void aboutToCreateActivity();

}
