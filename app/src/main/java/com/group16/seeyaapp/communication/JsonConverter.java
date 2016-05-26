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
 * Helper class for converting data into json messages to be sent to the server in accordance with
 * to communication protocol.
 */
public final class JsonConverter {

    private static final String TAG = "JsonConverter";

    /**
     * Converts UserLogin object into a login request.
     * @param login UserLogin object with user credentials
     * @return converted json string
     */
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

    /**
     * Converts an Account object into a request for registering a new user.
     * @param account The Account object with information for registering a new user
     * @return converted json string
     */
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

    /**
     * Converts an Activity object into a request to create a new activity with the stored
     * information.
     * @param activity The activity to be created
     * @param locationId The is of the location where the activity is to take place
     * @return converted json string
     */
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

    /**
     * Creates a request for publishing an already created activity, based on the id of that
     * activity. The activity is made visible to all users of the application.
     * @param activityId The id of the already created activity to be published.
     * @return converted json string
     */
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

    /**
     * Creates a request for publishing an already created activity, based on the id of that
     * activity. The activity is made visible to the specified users of the application.
     * @param activityId The id of the already created activity to be published.
     * @param invitees Names of those users who should be invited to the activity.
     * @return converted json string
     */
    public static String publishActivityToSpecificUsersJson(long activityId, List<String> invitees) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ComConstants.TYPE, ComConstants.PUBLISH_ACTIVITY_TO_SPECIFIC_USERS);

            jsonObject.put(ComConstants.ID, activityId);

            JSONArray jArray = new JSONArray();
            for(String invitee : invitees) {
                JSONObject inviteeJson = new JSONObject();

                inviteeJson.put(ComConstants.USERNAME, invitee);
                jArray.put(inviteeJson);
            }
            jsonObject.put(ComConstants.ARRAY_USERNAME, jArray);
            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    /**
     * Creates a request for signing up the specified user to an activity with the given id.
     * @param activityId The is of the activity to sign the user up for
     * @param username The name of the user to be signed up for the activity
     * @return converted json string
     */
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

    /**
     * Creates a request to unregister the specified user from an activity with the given id.
     * @param activityId The id of the activity to unregister the user from
     * @param username THe name of the user to be unregistered.
     * @return converted json string
     */
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

    /**
     * Creates a request to get the updated list of all possible categories, if the app does
     * not already have the latest version as shown by the version identifier.
     * @param version The identifier showing what version of categories the app already has
     * @return converted json string
     */
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

    /**
     * Creates a request to get activity categories based on different filters:
     * all the categories in which there is at least one activity the user is invited to,
     * or all the categories in which there is at least one activity that has been created by
     * the specified user.
     * @param filter Condition by which the categories are to be filtered.
     * @param userName The user for which categories are to be requested
     * @return converted json string
     */
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


    /**
     * Creates a request to get the updated list of all possible activity locations, if the app does
     * not already have the latest version as shown by the version identifier.
     * @param version The identifier showing what version of locations the app already has
     * @return converted json string
     */
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

    /**
     * Creates a request to get details of the activity specified by its id.
     * @param activityId The id of the activity to be requested.
     * @param username The user for which the activity is to be requested
     * @return converted json string
     */
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

    /**
     * Creates a request to get headlines for all the activities the given user has created.
     * @param ownerUserName The user whose activity headlines are to be requested
     * @return converted json string
     */
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

    /**
     * Creates a request to check if the given user name denotes a registered user in the application.
     * @param userName The user name to be checked for existance
     * @return converted json string
     */
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
