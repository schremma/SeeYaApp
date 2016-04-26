package com.group16.seeyaapp.activity.details;

/**
 * Created by Andrea on 25/04/16.
 */
public interface DetailPresenter {

    void onPressedJoin();

    void onPressedUnjoin();

    // The user wants to view an already created activity, with the given id
    void aboutToDisplayActivity(int activityId);

}
