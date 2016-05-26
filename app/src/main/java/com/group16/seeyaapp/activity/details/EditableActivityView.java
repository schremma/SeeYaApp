package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 13/04/16.
 * Displays a single activity.
 * It could be a still blank activity, if the user wishes to create a new activity.
 * It could also be an already created activity by the current user, which the user wishes to edit.
 */
public interface EditableActivityView {

    // Set a list with all possible locations
    void setLocationList(String[] locations);

    // Display information stored in the provided Activity
    void displayActivityDetails(Activity activity);

    // if created == true: activity has just been successfully created
    void updateCreateStatus(boolean created);

    // navigate to the view for browsing activities
    void navigateToBrowseActivities();

    // Display the provided error message
    void showOnError(String errorMessage);

}
