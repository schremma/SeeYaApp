package com.group16.seeyaapp.activity.details;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 * Handles the logic behind adding invited users to an activity.
 * For each name that the user enters, it needs to be checked if the user actually exists.
 */
public interface AddInvitedPresenter {

    // every time a user wants to add a user on the list of invitees,
    // it needs to be checked with if user name exists
    void checkIfUserExists(String userName);

    // set the list of users already added as invited
    void setInvitedList(List<String> invited);
}
