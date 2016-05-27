package com.group16.seeyaapp.activity.details;

import java.util.ArrayList;

/**
 * Created by Andrea on 17/05/16.
 * Interface for receiving information from a dialog through which users can be added
 * to be invited for an activity.
 */
public interface AddInvitedListener {

    // Update the list of invited users to the provided list
    void setListOfInvitedUsers(ArrayList<String> lstInvited);
}
