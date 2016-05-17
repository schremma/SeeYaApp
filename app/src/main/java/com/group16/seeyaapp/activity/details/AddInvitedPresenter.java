package com.group16.seeyaapp.activity.details;

/**
 * Created by Andrea on 17/05/16.
 */
public interface AddInvitedPresenter {

    // every time a user wants to add a user on the list of invitees,
    // it needs to be checked if user name exists
    void checkIfUserExists(String userName);
}
