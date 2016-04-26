package com.group16.seeyaapp.communication;

import android.util.Log;

import com.group16.seeyaapp.activity.list.Filter;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.model.Account;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.model.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 10/04/16.
 */
public final class JsonConverter {

    private static final String TAG = "JsonConverter";

    public static String jsonify(UserLogin login) {
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

    public static String jsonify(Activity activity, long locationId) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.NEWACTIVITY);

            jsonObject.put(ComConstants.NAME, activity.getOwner());
            jsonObject.put(ComConstants.PLACE, locationId);
            jsonObject.put(ComConstants.SUBCATEGORY, activity.getSubcategory());
            jsonObject.put(ComConstants.MAX_NBROF_PARTICIPANTS, activity.getMaxNbrOfParticipants());
            jsonObject.put(ComConstants.MIN_NBR_OF_PARTICIPANTS, activity.getMinNbrOfParticipants());
            jsonObject.put(ComConstants.DATE, DateHelper.DateToDateOnlyString(activity.getDate()));
            jsonObject.put(ComConstants.TIME, DateHelper.DateToTimeOnlyString(activity.getTime()));
            jsonObject.put(ComConstants.MESSAGE, activity.getMessage());
            jsonObject.put(ComConstants.HEADLINE, activity.getHeadline());

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String publishActivityJson(long activityId) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.PUBLISH_ACTIVITY);

            jsonObject.put(ComConstants.ID, activityId);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String signUpForActivityJson(long activityId, String username) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.SIGNUP);

            jsonObject.put(ComConstants.ID, activityId);
            jsonObject.put(ComConstants.USERNAME, username);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String unregisterFromActivityJson(long activityId, String username) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.UNREGISTER);

            jsonObject.put(ComConstants.ID, activityId);
            jsonObject.put(ComConstants.USERNAME, username);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String getCategoriesJson(String version) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.CATEGORIES_VERSION_NUMBER);
            jsonObject.put(ComConstants.ID, version);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String getFilteredCategoriesJson(Filter filter, String userName) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();

            switch (filter) {
                case CategoriesForInvitedToActivities:
                    jsonObject.put(ComConstants.TYPE, ComConstants.GET_CATEGORIES_WITH_ACTIVITIES);
                    break;
                case CategoriesForOwnActivities:
                    jsonObject.put(ComConstants.TYPE, ComConstants.GET_CATEGORIES_WITH_OWNED_ACTIVITIES);
                    break;
            }

            jsonObject.put(ComConstants.USERNAME, userName);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String getLocationsJson(String version) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.LOCATIONS_VERSION_NBR);
            jsonObject.put(ComConstants.ID, version);

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

            jsonObject.put(ComConstants.TYPE, ComConstants.ACTIVITIY);
            jsonObject.put(ComConstants.ID, activityId);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
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


}
