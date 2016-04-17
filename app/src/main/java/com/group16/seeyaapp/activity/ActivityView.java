package com.group16.seeyaapp.activity;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 13/04/16.
 */
public interface ActivityView {

    // Set a list with all possible locations
    void setLocationList(String[] locations);

    // Display information stored in the provided Activity
    void displayActivityDetails(Activity activity);

    // if published == true: activity has just been successfully published
    void updatePublishedStatus(boolean published);

    // if created == true: activity has just been successfully created
    void updateCreateStatus(boolean created);

    void showOnError(String errorMessage);

}
