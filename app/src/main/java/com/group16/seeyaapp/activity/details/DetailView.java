package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 22/04/16.
 *
 * Displays details of an activity, without providing editing possibilities.
 * It shows basic information about the activity, the number of users that are signed up
 * for the activity, and if the current user is signed up.
 * The user may sign up or unregister from the activity.
 */
public interface DetailView {

    // Display information stored in the provided Activity object
    void displayActivityDetails(Activity activity);

    // Update the counter that shows the number of users who have signed up.
    void updateNbrAttending(int attending);

    // The user has just successfully signed up for or
    // unregistered from an activity.
    // If signedUp == true: has just successfully signed up.
    void updateSignedUpStatus(boolean signedUp);

    // Display the provided error message.
    void showOnError(String errorMessage);

}
