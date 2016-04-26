package com.group16.seeyaapp;

/**
 * Created by Andrea on 21/04/16.
 */
public final class LocalConstants {

    // ms value to check if it is time to perform a location/catgoory version check with server
    // [date2.getTime() - date1.getTime() returns difference in long and ms]
    //public static final long VERSION_CHECK_INTERVAL = 43200000; // 12 hours
    //public static final long VERSION_CHECK_INTERVAL = 3600000; // 2 hours
    public static final long VERSION_CHECK_INTERVAL = 360; // test

    // current user name in SharedPreferences
    public static final String SP_CURRENT_USER = "currentUser";
    public static final String SP_LOCATIONS = "storedLocations";
    public static final String SP_CATEGORIES = "storedCategories";
    public static final String SP_LOCATIONS_CHECK_DATE = "locationsCheckDate";
    public static final String SP_CATEGORIES_CHECK_DATE = "categoriesCheckDate";
    public static final String SP_VERSION_LOCATIONS = "versionLocations";
    public static final String SP_VERSION_CATEGORIES = "versionCategories";
}
