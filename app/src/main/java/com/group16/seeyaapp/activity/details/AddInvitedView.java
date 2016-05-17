package com.group16.seeyaapp.activity.details;

/**
 * Created by Andrea on 17/05/16.
 */
public interface AddInvitedView {

    void onUserExistenceChecked(boolean userExists, String username);

    void showOnError(String errorMessage);

}
