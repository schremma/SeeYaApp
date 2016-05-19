package com.group16.seeyaapp.activity.details;

import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 */
public interface AddInvitedView {

    void onUserExistenceChecked(boolean userExists, String username);
    void setInvitedUserList(List<String> invitedUserList);
    void showOnError(String errorMessage);

}
