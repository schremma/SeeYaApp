package com.group16.seeyaapp.activity.details;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 * Displays a view for selecting a number of user names, representing specific users that are to be
 * invited to an activity.
 */
public interface AddInvitedView {

    // display information after it has been checked if a user name exists.
    // userExists = true, if the user name exist
    void onUserExistenceChecked(boolean userExists, String username);

    // update/set the list of users to be invited
    void setInvitedUserList(List<String> invitedUserList);

    // display the provided error message
    void showOnError(String errorMessage);

}
