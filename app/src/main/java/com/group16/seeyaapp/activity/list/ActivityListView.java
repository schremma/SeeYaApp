package com.group16.seeyaapp.activity.list;

/**
 * Created by Andrea on 15/04/16.
 */
public interface ActivityListView {

    // Show a list of actvities, displaying the headline and the date for each.
    // Keep track of the associated id as well to send it to presenter when an
    // activity has been selected.
    // Info for each activity is stored at the same index position in each array
    // i.e. for the first activity on the list has headlines[0], dates[0] and ids[0]
    // the second has headlines[1], dates[1] and ids[1] etc.
    void setHeadlineList(String[] headlines, String[] dates, int[] ids);

    // The user has selected a specific list item ->
    // navigate to the view that dsiplayes the selected activity
    // and send the id of the activity to that view
    void navigateToActivityDisplay(int activityId);

    void showOnError(String errorMessage);
}