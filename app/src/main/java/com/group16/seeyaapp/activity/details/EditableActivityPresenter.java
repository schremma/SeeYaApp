package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 13/04/16.
 * Handles the logic behind displaying a single activity for editing
 * It could be a still empty activity, if the user wishes to create a new activity.
 * It could also be (in future implementations) an already created activity by the current user,
 * which the user wishes to edit.
 */
public interface EditableActivityPresenter {

    // Save new activity info stored int the provided Activity object
    void onCreateActivity(Activity activity);

    // Store provided activity information, without saving or updating in database
    void onSetActivity(Activity activity);

    // The user wants to view the already created activity, with the given id. Retrieve that
    // activity.
    void aboutToDisplayActivity(int activityId);

    // The user requested a view for creating a new activity
    void aboutToCreateActivity();

}
