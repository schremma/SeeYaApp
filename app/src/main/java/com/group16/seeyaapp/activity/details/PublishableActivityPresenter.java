package com.group16.seeyaapp.activity.details;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 */
public interface PublishableActivityPresenter {

    // On pressing the button for publishing an already created activity, to all users
    void onPublishActivity(long activityId);

    // On pressing the button for publishing an already created activity, for specific users
    void onPublishActivity(long activityId, List<String> invitees);

    // The user wants to view the already created activity, with the given id
    void aboutToDisplayActivity(int activityId);

    void setInvitedList(List<String> invited);


}
