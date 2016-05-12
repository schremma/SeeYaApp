package com.group16.seeyaapp.activity.details;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 22/04/16.
 *
 * Show details about an activity ("View activity"). The user can join the activity,
 * or unjoin if he or she has already signed up.
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

    void showOnError(String errorMessage);

}
