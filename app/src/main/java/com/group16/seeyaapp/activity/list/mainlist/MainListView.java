package com.group16.seeyaapp.activity.list.mainlist;

import com.group16.seeyaapp.activity.list.Filter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 26/04/16.
 * A view that shows the list with main and subcategories on the main page of the app.
 * The list can be used to browse to activities the user is invited to, or to browse to
 * activities the user has created.
 */
public interface MainListView {

    // Set the (a) main- and (b) sub- headers of the list.
    // These might be main categories, e.g. Sport and subcategories under each main category, e.g. Running.
    // The method argument types assume that ExpandableListAdapter is used to fill the list in the view.
    void setNestedListHeaders(List<String> mainHeaders, HashMap<String, List<String>> subHeaders);

    // navigate to the view were a list of activity headlines under a category is displayed
    // the headlines string provided as an argument should be sent to that view (e.g. as an Intent extra)
    void navigateToHeadlineDisplay(String headlines);

    // navigate to the view were a list of activity headlines under a category is displayed, and send along the given arguments
    void navigateToHeadlineDisplay(int selectedItemId, Filter listFilter); // TODO remove?

}
