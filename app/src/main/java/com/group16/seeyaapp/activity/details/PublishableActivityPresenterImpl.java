package com.group16.seeyaapp.activity.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import java.util.List;

/**
 * Created by Andrea on 17/05/16.
 * Handles the logic of publishing an already created activity,
 * which involves retrieving an activity with a specific id from the server - stored as an Activity
 * object, the model.
 * The presenter might also store a list of users that are to be invited to an activity.
 * Alternatively, an activity published without some list of invitees are
 * made visible for all usera of the application.
 */
public class PublishableActivityPresenterImpl extends CommunicatingPresenter<PublishableActivityView, Activity> implements PublishableActivityPresenter {

    private static final String TAG = "PublishableActivityPres";
    private List<String> lstInvitedUsers; //list of users that are to be invited to an activity

    /**
     * Publishes an already created activity with the provided id, to all users.
     * @param activityId The id of the activity to be published.
     */
    @Override
    public void onPublishActivity(long activityId) {
        String json = JsonConverter.publishActivityJson(activityId);
        sendJsonString(json);
    }

    /**
     * Publishes an already created activity with the provided id
     * to the users specified in the list of invitees.
     * @param activityId The id of the activity to be published.
     * @param invitees List of user names to invite for the activity.
     */
    @Override
    public void onPublishActivity(long activityId, List<String> invitees) {
        String json = JsonConverter.publishActivityToSpecificUsersJson(activityId, invitees);
        sendJsonString(json);
    }

    /**
     * Sends request to retrieve an already created activity form the server with the given id.
     * @param activityId The id of the acitivty to retrieve.
     */
    @Override
    public void aboutToDisplayActivity(int activityId) {

        final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);
        String json = JsonConverter.getActivityJson(activityId, currentUser);

        sendJsonString(json);
    }

    /**
     * Stores the current list of invited users, in case this would need to be preserved betweeon
     * orientation changes of the view.
     * @param invited list of invited user names
     */
    @Override
    public void setInvitedList(List<String> invited) {
        lstInvitedUsers = invited;
    }


    /**
     * The json response could be:
     * 1. CONFIRMATION
     *      1a: confirmation that an activity has been published
     * 2. ACTIVITY: an already created activity that is to be displayed
     * 3. ERROR
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.PUBLISH_ACTIVITY_CONFIRMATION)) {

//                String message =  (String)jsonObject.get(ComConstants.MESSAGE);

                view().updatePublishedStatus(true);
            }
            else if (msgType.equals(ComConstants.ACTIVITIY)) {
                setActivity(json);
            }
            else if (msgType.equals(ComConstants.PUBLISH_ACTIVITY_ERROR)){
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onActionFail(message);
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onActionFail(message);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Error";
            if (json != null)
                failMsg +=" : " + json;

            onActionFail(failMsg);
        }

    }


    /**
     * Loads information from a json string into the Activity object stored as the model.
     * @param json The json string with activity information.
     */
    private void setActivity(String json) {
        Log.i(TAG, json);

        model = new Activity();
        try {
            JSONObject jsonObject = new JSONObject(json);
            model.setId(jsonObject.getLong(ComConstants.ID));
            model.setSubcategoryString(jsonObject.getString(ComConstants.SUBCATEGORY));

            model.setLocation(jsonObject.getString(ComConstants.PLACE));
            model.setAddress(jsonObject.getString(ComConstants.ADDRESS));
            model.setMaxNbrOfParticipants(jsonObject.getInt(ComConstants.MAX_NBROF_PARTICIPANTS));
            model.setMinNbrOfParticipants(jsonObject.getInt(ComConstants.MIN_NBR_OF_PARTICIPANTS));

            model.setMessage(jsonObject.getString(ComConstants.MESSAGE));
            model.setOwner(jsonObject.getString(ComConstants.ACTIVITY_OWNER));
            model.setHeadline(jsonObject.getString(ComConstants.HEADLINE));
            model.setNbrSignedUp(jsonObject.getLong(ComConstants.NBR_OF_SIGNEDUP));

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

                onRetrievalError("Could not display the activity: invalid date format");
            }
        }
        catch (JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get activity";
            if (json != null)
                failMsg +=" : " + json;

            onRetrievalError(failMsg);
        }
    }

    /**
     * Send an error message to the view when an action failed.
     * @param error
     */
    private void onActionFail(String error) {
        view().showOnError(error);
    }


    /**
     * Sends an error message to the view when the retrieval of some information failed.
     * @param error
     */
    private void onRetrievalError(String error) {
        view().showOnError(error);
    }

    /**
     * If the presenter preserved the current list of invited users acrosss orientation
     * changes, reload this into the view.
     * @param view
     */
    @Override
    public void bindView(@NonNull PublishableActivityView view) {
        super.bindView(view);

        if (lstInvitedUsers != null) {
            view().setInvitedUserList(lstInvitedUsers);
        }
    }


}
