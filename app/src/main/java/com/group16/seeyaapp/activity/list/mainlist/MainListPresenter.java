package com.group16.seeyaapp.activity.list.mainlist;

import com.group16.seeyaapp.activity.list.Filter;

/**
 * Created by gustav on 2016-04-20.
 */
public interface MainListPresenter {

    void aboutToListActivities(Filter activityListFilter);

    void selectedListItem(String selectedItem, Filter listFilter);

}
