package com.group16.seeyaapp.activity.list.mainlist;

import com.group16.seeyaapp.activity.list.Filter;

/**
 * Created by gustav on 2016-04-20.
 * Handles the logic behind presenting the browsable list of the main page of the application.
 */
public interface MainListPresenter {

    // Invoked when the view is about to show or update the list on the main page.
    // The argument 'activityListFilter' specifies what kind of list is to be presented,
    // e.g. one for browsing to own activities or one for browsing to activities the user is invited to
    void aboutToPresentMainList(Filter activityListFilter);

    // Invoked when the user has selected a list item.
    // Arguments:
    // selectedItem: the heading of the item selected
    // Filter: the current filter used for presenting the list
    void selectedListItem(String selectedItem, Filter listFilter);

}
