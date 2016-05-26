package com.group16.seeyaapp.activity.list;

/**
 * Created by Andrea on 26/04/16.
 * Enum to indicate how a list of categories/activities is filtered.
 */
public enum Filter {
    OwnActivitiesByCategories,  // only categories in which the user has created at least one own activity
    InvitedToActivitiesByCategories, // only categories in which at least one activity is visible for the user (being invited to)
    PastActivtiesByCategories, // categories in which there are activities that the user attended in the past
    AllOwnActivties, // all activities the current user has created
}
