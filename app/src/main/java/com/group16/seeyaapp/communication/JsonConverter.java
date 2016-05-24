package com.group16.seeyaapp.communication;

import android.util.Log;

import com.group16.seeyaapp.activity.list.Filter;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.model.Account;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.model.UserLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
            jsonObject.put(ComConstants.ADDRESS, activity.getAddress());
            jsonObject.put(ComConstants.SUBCATEGORY, activity.getSubcategory());
            jsonObject.put(ComConstants.MAX_NBROF_PARTICIPANTS, activity.getMaxNbrOfParticipants());
            jsonObject.put(ComConstants.MIN_NBR_OF_PARTICIPANTS, activity.getMinNbrOfParticipants());
            jsonObject.put(ComConstants.DATE, DateHelper.dateToDateOnlyString(activity.getDate()));
            jsonObject.put(ComConstants.TIME, DateHelper.dateToTimeOnlyString(activity.getTime()));
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

    public static String publishActivityToSpecificUsersJson(long activityId, List<String> invitees) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();

            //TODO check correct message type:
            jsonObject.put(ComConstants.TYPE, ComConstants.PUBLISH_ACTIVITY_TO_SPECIFIC_USERS);

            jsonObject.put(ComConstants.ID, activityId);

            JSONArray jArray = new JSONArray();
            for(String invitee : invitees) {
                JSONObject inviteeJson = new JSONObject();

                inviteeJson.put(ComConstants.USERNAME, invitee);
                jArray.put(inviteeJson);
            }

            // TODO check correct constant for user name array: "ARRAY_USERNAME"
            jsonObject.put(ComConstants.ARRAY_USERNAME, jArray);
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
            jsonObject.put(ComConstants.TYPE, ComConstants.UNREGISTER_FROM_ACTIVITY);

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
                case InvitedToActivitiesByCategories:
                    jsonObject.put(ComConstants.TYPE, ComConstants.GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER);
                    break;
                case OwnActivitiesByCategories:
                    jsonObject.put(ComConstants.TYPE, ComConstants.GET_MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES);
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

    public static String getActivityJson(int activityId, String username) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ComConstants.TYPE, ComConstants.ACTIVITIY);
            jsonObject.put(ComConstants.ID, activityId);
            jsonObject.put(ComConstants.USERNAME, username);

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

    public static String userExistsJson(String userName) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.CHECK_IF_USER_EXISTS);
            jsonObject.put(ComConstants.USERNAME, userName);

            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }


}
