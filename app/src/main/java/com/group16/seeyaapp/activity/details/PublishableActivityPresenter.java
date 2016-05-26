package com.group16.seeyaapp.activity.details;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 * Handles the logic of publishing an already created activity,
 * which involves retrieving an activity with a specific id from the server and
 * potentially handling a list users that are to be invited to an activity.
 * Alternatively, an activity published without some list of invitees are
 * made visible for all usera of the application.
 */
public interface PublishableActivityPresenter {

    // Invoked as the user requests publishing an already created activity, to all users
    void onPublishActivity(long activityId);

    // Invoked as the user requests publishing an already created activity, for specific users
    void onPublishActivity(long activityId, List<String> invitees);

    // The user wants to view the already created activity, with the given id
    void aboutToDisplayActivity(int activityId);

    // Update the current list of users to be invited to the activity
    void setInvitedList(List<String> invited);

}
