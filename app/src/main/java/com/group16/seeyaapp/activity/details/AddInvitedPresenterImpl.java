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
 */
public class AddInvitedPresenterImpl extends CommunicatingPresenter<AddInvitedView, List<String>> implements AddInvitedPresenter {
    private static final String TAG = "AddInvitedPresenter";


    @Override
    public void checkIfUserExists(String userName) {
        String json = JsonConverter.userExistsJson(userName);
        sendJsonString(json);
    }

    @Override
    public void setInvitedList(List<String> invited) {
        model = invited;
    }

    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);
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

    @Override
    protected void updateView() {

    }
}
