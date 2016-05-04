package com.group16.seeyaapp.activity.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.group16.seeyaapp.LocalConstants;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.model.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Andrea on 25/04/16.
 */
public class DetailPresenterImpl extends CommunicatingPresenter<DetailView, Activity> implements DetailPresenter {
    private static final String TAG = "DetailPresenter";

    @Override
    public void onPressedJoin() {

        if (model != null) {

            if (model.stillHasSpace()) {
                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);

                if (currentUser != null) {
                    String json = JsonConverter.signUpForActivityJson(model.getId(), currentUser);
                    sendJsonString(json);
                }
                else {
                    view().showOnError("Error signing up for activity");
                    Log.i(TAG, "Error: current user is null");
                }
            }
            else {
                view().showOnError("Cannot sign up for activity: it is already filled");
            }
        }
        else {
            onError("Error, no activity to sign up for");
        }

    }

    @Override
    public void onPressedUnjoin() {
        if (model != null) {
            SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
            String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);

            if (currentUser != null) {
                String json = JsonConverter.unregisterFromActivityJson(model.getId(), currentUser);
                sendJsonString(json);
            }
            else {
                view().showOnError("Error unregistering from activity");
                Log.i(TAG, "Error: current user is null");
            }
        }
        else {
            onError("Error, no activity to unregister from");
        }
    }

    @Override
    public void aboutToDisplayActivity(int activityId) {
        String json = JsonConverter.getActivityJson(activityId);

        sendJsonString(json);
    }

    @Override
    protected void communicationResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.ACTIVITIY)) {
                setActivity(json);
            }
            else if (msgType.equals(ComConstants.SIGNUP_CONFIRMATION)) {

                // TODO update the number of attending here, get it from server as part of sign up confirmation?
                // for now, just increment the number of attendees
                model.setNbrSignedUp(model.getNbrSignedUp() +1);
                view().updateSignedUpStatus(true);
                view().updateNbrAttending((int)model.getNbrSignedUp());
            }
            else if (msgType.equals(ComConstants.SIGNUP_ERROR)) {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onError(message);
            }
            else if (msgType.equals(ComConstants.UNREGISTER_FROM_ACTIVITY_CONFIRMATION)) {

                view().updateSignedUpStatus(false);
            }
            else if (msgType.equals(ComConstants.UNREGISTER_FROM_ACTIVITY_ERROR)) {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onError(message);
            }

            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onError(message);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Error";
            if (json != null)
                failMsg +=" : " + json;

            onError(failMsg);
        }
    }

    private void setActivity(String json) {
        Log.i(TAG, json);

        model = new Activity();
        try {
            JSONObject jsonObject = new JSONObject(json);
            model.setId(jsonObject.getLong(ComConstants.ID));

            //TODO main category string (?)
            model.setSubcategoryString(jsonObject.getString(ComConstants.SUBCATEGORY));

            model.setLocation(jsonObject.getString(ComConstants.PLACE));
            model.setMaxNbrOfParticipants(jsonObject.getInt(ComConstants.MAX_NBROF_PARTICIPANTS));
            model.setMinNbrOfParticipants(jsonObject.getInt(ComConstants.MIN_NBR_OF_PARTICIPANTS));

            model.setMessage(jsonObject.getString(ComConstants.MESSAGE));
            model.setOwner(jsonObject.getString(ComConstants.ACTIVITY_OWNER));
            model.setHeadline(jsonObject.getString(ComConstants.HEADLINE));
            model.setNbrSignedUp(jsonObject.getLong(ComConstants.NBR_OF_SIGNEDUP));

            //Date published might be null, which means that Activity has not been published yet
            try {
                Date published = DateHelper.StringDateToDate(jsonObject.getString(ComConstants.DATE_PUBLISHED));
                model.setDatePublished(published);
            } catch (ParseException e) {}

            try {
                Date date = DateHelper.StringDateToDate(jsonObject.getString(ComConstants.DATE));
                Date time = DateHelper.StringTimeToDate(jsonObject.getString(ComConstants.TIME));
                model.setDate(date);
                model.setTime(time);

                view().displayActivityDetails(model);

            }catch (ParseException e) {
                Log.i(TAG, e.getMessage());

                onError("Could not display the activity: invalid date format");
            }
        }
        catch (JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get activity";
            if (json != null)
                failMsg +=" : " + json;

            onError(failMsg);
        }
    }

    private void onError(String error) {
        view().showOnError(error);
    }

    @Override
    protected void updateView() {

    }
}
