package com.group16.seeyaapp.activity.list;

/**
 * Created by Andrea on 15/04/16.
 * Displays a list showing short information, referred to as headlines, about activities.
 * The user may select one of these headlines to be navigated to the next view
 * that displays detailed information about the activity.
 */
public interface ActivityListView {

    // Show a list of activities, displaying the headline and the date for each.
    // The view should keep track of the associated id as well to send it to presenter when an
    // activity has been selected.
    // Info for each activity is stored at the same index position in each array
    // i.e. for the first activity on the list has headlines[0], dates[0] and ids[0]
    // the second has headlines[1], dates[1] and ids[1] etc.
    void setHeadlineList(String[] headlines, String[] dates, int[] ids);

    // The user has selected a specific list item ->
    // navigate to the view that displays the selected activity
    // and send the id of the activity to that view
    void navigateToActivityDisplay(int activityId);

    // Display the provided error message
    void showOnError(String errorMessage);
}
