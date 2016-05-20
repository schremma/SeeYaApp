package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 13/04/16.
 * Displays editable activities, i.e. activities the user himself or herself
 * created but has not published yet, or activities that are in the process of being created by the user.
 */
public interface EditableActivityView {

    // Set a list with all possible locations
    void setLocationList(String[] locations);

    // Display information stored in the provided Activity
    void displayActivityDetails(Activity activity);

    // if published == true: activity has just been successfully published
    void updateStatus(boolean changesSaved);

    // if created == true: activity has just been successfully created
    void updateCreateStatus(boolean created);

    void navigateToBrowseActivities();

    void showOnError(String errorMessage);

}
