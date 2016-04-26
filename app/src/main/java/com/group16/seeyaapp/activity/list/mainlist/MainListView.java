package com.group16.seeyaapp.activity.list.mainlist;

import com.group16.seeyaapp.activity.list.Filter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 26/04/16.
 */
public interface MainListView {

    // if ExpandableListAdapter is used to fill the list
    void setNestedListHeaders(List<String> mainHeaders, HashMap<String, List<String>> subHeaders);

    // navigate to the view were a list of activity headlines inder a category is displayed, and send along the given arguments
    void navigateToHeadlineDisplay(int selectedItemId, Filter listFilter);
}
