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
 * Handles the logic of displaying details of an activity, without providing editing possibilities.
 * It retrieves activity information from the server based an activity id, and stores it an Activity
 * object - the model.
 * It enables the user to sign up or unregister from the activity.
 */
public class DetailPresenterImpl extends CommunicatingPresenter<DetailView, Activity> implements DetailPresenter {
    private static final String TAG = "DetailPresenter";

    /**
     * Check if it is possible for the user to sign up for the current activity,
     * i.e. if it has space, and the activity has not expired yet.
     */
    @Override
    public void onPressedJoin() {

        if (model != null) {

            if (model.stillHasSpace()) {
                if (!model.expired()) {
                    SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                    String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);

                    if (currentUser != null) {
                        String json = JsonConverter.signUpForActivityJson(model.getId(), currentUser);
                        sendJsonString(json);
                    } else {
                        view().showOnError("Error signing up for activity");
                        Log.i(TAG, "Error: current user is null");
                    }
                } else {
                    view().showOnError("Cannot sign up for activity: date has already passed");
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

    /**
     * Unregisters the current user from the current activity.
     */
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

    /**
     * Retrieves the activity with the provided id from the server.
     * The current user name is sent along, so that it can be checked on the server if the user is
     * already addending the activity.
     * @param activityId
     */
    @Override
    public void aboutToDisplayActivity(int activityId) {
        final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);
        String json = JsonConverter.getActivityJson(activityId, currentUser);

        sendJsonString(json);
    }

    /**
     * Communication result might be
     * 1. ACTIVITY: a specific existing activity
     * 2. SIGNUP_CONFIRMATION: user has successfully registered for an activity
     * 3. UNREGISTER_FROM_ACTIVITY_CONFIRMATION: user has successfully unregistered for an activity
     * 4. Error:
     *  a, signup error
     *  b, unregistering error
     *  c, other, such as activity retrieval error
     * @param json
     */
    @Override
    protected void communicationResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.ACTIVITIY)) {
                setActivity(json);
            }
            else if (msgType.equals(ComConstants.SIGNUP_CONFIRMATION)) {

                Log.i(TAG, "sign up confirmed");
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
                // TODO update the number of attending here, get it from server as part of sign up confirmation?
                model.setNbrSignedUp(model.getNbrSignedUp() -1);
                view().updateSignedUpStatus(false);
                view().updateNbrAttending((int)model.getNbrSignedUp());
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

    /**
     * Retrieves the content of the provided json string into Activity model object
     * @param json
     */
    private void setActivity(String json) {
        Log.i(TAG, json);

        model = new Activity();
        try {
            JSONObject jsonObject = new JSONObject(json);
            model.setId(jsonObject.getLong(ComConstants.ID));

            //TODO main category string (?)
            model.setSubcategoryString(jsonObject.getString(ComConstants.SUBCATEGORY));

            model.setLocation(jsonObject.getString(ComConstants.PLACE));
            model.setAddress(jsonObject.getString(ComConstants.ADDRESS));
            model.setMaxNbrOfParticipants(jsonObject.getInt(ComConstants.MAX_NBROF_PARTICIPANTS));
            model.setMinNbrOfParticipants(jsonObject.getInt(ComConstants.MIN_NBR_OF_PARTICIPANTS));

            model.setMessage(jsonObject.getString(ComConstants.MESSAGE));
            model.setOwner(jsonObject.getString(ComConstants.ACTIVITY_OWNER));
            model.setHeadline(jsonObject.getString(ComConstants.HEADLINE));
            model.setNbrSignedUp(jsonObject.getLong(ComConstants.NBR_OF_SIGNEDUP));

            String signedUpStatus = jsonObject.getString(ComConstants.SIGNED_UP);
            boolean attending = signedUpStatus.equals(ComConstants.YES);
            model.setAttending(attending);

            //Date published might be null, which means that Activity has not been published yet
            try {
                Date published = DateHelper.stringDateToDate(jsonObject.getString(ComConstants.DATE_PUBLISHED));
                model.setDatePublished(published);
            } catch (ParseException e) {}

            try {
                Date date = DateHelper.stringDateToDate(jsonObject.getString(ComConstants.DATE));
                Date time = DateHelper.stringTimeToDate(jsonObject.getString(ComConstants.TIME));
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

}
