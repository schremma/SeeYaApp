package com.group16.seeyaapp.activity.list;

/**
 * Created by Andrea on 26/04/16.
 */
public enum Filter {
    CategoriesForOwnActivities,  // only categories in which the user has created at least one own activity
    CategoriesForInvitedToActivities, // only categories in which at least one activity is visible for the user (being invited to)
    CategoriesForPastActivties, // categories in which there are activities that the user attended in the past
    AllOwnActivties, // all activities the current user has created
    Location,
    Category,
    ActivityType // activity subcategory i.e. badminton, running etc.
}
