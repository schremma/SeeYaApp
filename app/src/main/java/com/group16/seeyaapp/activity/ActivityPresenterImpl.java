package com.group16.seeyaapp.activity;

import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 14/04/16.
 */
public class ActivityPresenterImpl extends CommunicatingPresenter<ActivityView, Activity> implements ActivityPresenter {
    private static final String TAG = "CategoryPresenter";

    @Override
    public void onCreate(Activity activity) {
        if (activity != null) {
            model = activity;
            // TODO: validate activity contents
            // TODO: add user name from somewhere
            model.setOwner("test1");

            String json = JsonConverter.jsonify(model);
            sendJsonString(json);
        }

    }

    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.CONFIRMATION)) {
                String confirmationType = (String)jsonObject.get(ComConstants.CONFIRMATION_TYPE);

                onCreationSuccess();
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onCreationFail(message);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Activity creation failed";
            if (json != null)
                failMsg +=" : " + json;

            onCreationFail(failMsg);
        }

    }

    private void onCreationSuccess() {
        view().showOnSuccess("Activity has been created");
    }

    private void onCreationFail(String error) {
        view().showOnError(error);
    }

    @Override
    protected void updateView() {

    }
}
