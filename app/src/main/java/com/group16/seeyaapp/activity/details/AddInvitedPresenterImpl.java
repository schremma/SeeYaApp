package com.group16.seeyaapp.activity.details;

import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 * Handles the logic behind adding invited users to an activity and it stores the current list of
 * invited users - the model.
 * For each name that the user enters, it needs to be checked with the server if the user actually
 * exists.
 */
public class AddInvitedPresenterImpl extends CommunicatingPresenter<AddInvitedView, List<String>> implements AddInvitedPresenter {
    private static final String TAG = "AddInvitedPresenter";


    /**
     * Initiates a request to the server to check if the provided user name represents a registered
     * user of the application
     * @param userName
     */
    @Override
    public void checkIfUserExists(String userName) {
        String json = JsonConverter.userExistsJson(userName);
        sendJsonString(json);
    }

    /**
     * Sets the stored list of users to the provided list
     * @param invited List of invited users with a string representing each user name
     */
    @Override
    public void setInvitedList(List<String> invited) {
        model = invited;
    }

    /**
     * Response to request from the server:
     * a) user name exists as a registered user -> add the name to the list of invited users
     * b) user name does not exist
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);
            // the server sends back the user name it checked for existence
            String username =  (String)jsonObject.get(ComConstants.MESSAGE);

            if (msgType.equals(ComConstants.USER_EXISTS)) {

                if (model == null)
                    model = new ArrayList<String>();
                model.add(username);

                view().onUserExistenceChecked(true, username);
            }
            else if (msgType.equals(ComConstants.INVALID_USERNAME)) {
                view().onUserExistenceChecked(false, username);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Error";
            if (json != null)
                failMsg +=" : " + json;

            view().showOnError(failMsg);
        }

    }

    @Override
    public void bindView(@NonNull AddInvitedView view) {
        super.bindView(view);

        if (model !=null) {
                view().setInvitedUserList(model);

        }
    }

}
