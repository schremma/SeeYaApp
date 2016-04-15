package com.group16.seeyaapp.communication;

import android.util.Log;

import com.group16.seeyaapp.model.Account;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.model.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            jsonObject.put(ComConstants.DATE, DateToDateOnlyString(activity.getDate()));
            jsonObject.put(ComConstants.TIME, DateToTimeOnlyString(activity.getTime()));
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

    public static String getCategoriesJson(int versionNbr) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.ACTIVITY_CATEGORIES);
            jsonObject.put(ComConstants.VERSION_NBR, versionNbr);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String getLocationsJson(int versionNbr) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.LOCATIONS);
            jsonObject.put(ComConstants.VERSION_NBR, versionNbr);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String getActivityJson(int activityId) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put(ComConstants.TYPE, ComConstants.ACTIVITY_CATEGORIES);
            jsonObject.put(ComConstants.ID, activityId);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        throw new UnsupportedOperationException();

        //return json;
    }

    public static String getOwnActivityHeadlinesJson(String ownerUserName) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.MY_ACTIVITIES);
            jsonObject.put(ComConstants.USERNAME, ownerUserName);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String DateToDateOnlyString(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String DateToTimeOnlyString(Date time) {
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        return formatter.format(time);
    }
}
