package com.group16.seeyaapp.communication;

import android.util.Log;

import com.group16.seeyaapp.model.Account;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.model.Login;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 10/04/16.
 */
public final class JsonConverter {

    private static final String TAG = "JsonConverter";

    public static String jsonify(Login login) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.LOGIN);
            jsonObject.put(ComConstants.USERNAME, login.getUserName());
            jsonObject.put(ComConstants.PASSWORD, login.getPassword());
            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String jsonify(Account account) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.NEWUSER);
            jsonObject.put(ComConstants.USERNAME, account.getUserName());
            jsonObject.put(ComConstants.PASSWORD, account.getPassword());
            jsonObject.put(ComConstants.EMAIL, account.getEmail());
            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String jsonify(Activity activity) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.NEWACTIVITY);

            jsonObject.put(ComConstants.NAME, activity.getOwner());
            jsonObject.put(ComConstants.PLACE, activity.getLocation());
            jsonObject.put(ComConstants.SUBCATEGORY, activity.getSubcategory());
            jsonObject.put(ComConstants.MAX_NBROF_PARTICIPANTS, activity.getMaxNbrOfParticipants());
            jsonObject.put(ComConstants.MIN_NBR_OF_PARTICIPANTS, activity.getMinNbrOfParticipants());
            jsonObject.put(ComConstants.DATE, activity.getDate());
            jsonObject.put(ComConstants.TIME, activity.getTime());
            jsonObject.put(ComConstants.MESSAGE, activity.getMessage());
            jsonObject.put(ComConstants.HEADLINE, activity.getHeadline());

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String publishActivityJson(int activityId) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.PUBLISH_ACTIVITY);

            jsonObject.put(ComConstants.NAME, activityId);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }
}
