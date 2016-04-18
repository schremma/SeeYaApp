package com.group16.seeyaapp.activity.list;

/**
 * Created by Andrea on 15/04/16.
 * Showing a list of activities: headline and date for each activity (with activity id)
 */
public interface ActivityListPresenter {

    // The user has chosen one of the listed activities to be able to see the details
    void onActivitySelected(int activityId);
}