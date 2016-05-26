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
import com.group16.seeyaapp.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 14/04/16.
 * Manages the display of editable activities, i.e. activities that are in the process of being
 * created by the user, and as a future functionality, own activities being edited by the user.
 * If the presenter is to display a specific activity - earlier created by the user -
 * it gets the whole activity from the server using the provided id in onCreateActivity.
 * When the user wants to create a new activity,
 * the list of available locations should be sent to the associated EditableActivityView for display.
 * It might not be necessary to retrieve the list of possible locations from the server
 * all the time when these are displayed, since they are not expected to change often. Therefore,
 * a version number for the current  location list is stored locally in the app.
 * As certain intervals (defined in LocalConstants, as VERSION_CHECK_INTERVAL) have passed
 * and  locations are requested for display by the view, the app checks with the server if
 * it still has the right version. If it does, the locations are loaded from local storage.
 * If it does not, an updated version of the location list received from the server is
 * loaded into the view and stored in local storage (with updated version number).
 */
public class EditableActivityPresenterImpl extends CommunicatingPresenter<EditableActivityView, Activity> implements EditableActivityPresenter {
    private static final String TAG = "EditableActivityPres";
    private HashMap<String, List<Location>> locations;
    private String locationsVersion;

    private ActionType actionType;  // what kind of editing is done to the activity?

    /**
     * Initiates saving data in the provided Activity object as a new activity, after performing
     * local validation.
     * @param activity The activity to be saved as a new one.
     */
    @Override
    public void onCreateActivity(Activity activity) {
        if (activity != null) {
            model = activity;

            if (activity.validateActivity()) {

                final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);
                model.setOwner(currentUser);

                int locationId = -1;
                for(String k : locations.keySet()) {
                    for(Location l : locations.get(k)) {
                        if (l.getName().equals(model.getLocation())) {
                            locationId = l.getId();
                            break;
                        }
                    }
                }

                Log.i(TAG, "Location id: " + locationId);
                String json = JsonConverter.jsonify(model, locationId);
                actionType = ActionType.CreateNew;
                sendJsonString(json);
            }
            else {
                String errorMsg = activity.getValidationErrorMessage();
                if (errorMsg == null)
                    errorMsg = "Validation error";
                view().showOnError(errorMsg);
            }
        }

    }

    /**
     * Sets the provided Activity instance as the current activity.
     * @param activity
     */
    @Override
    public void onSetActivity(Activity activity) {
        model = activity;
    }

    /**
     * Retieves an activity with a specific id from the server, so that the user can edit that activity.
     * @param activityId The id of the activity to retrieve
     */
    @Override
    public void aboutToDisplayActivity(int activityId) {

        final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);
        String json = JsonConverter.getActivityJson(activityId, currentUser);

        sendJsonString(json);

    }

    /**
     * The user has requested a view for creating a new activity:
     * a list of avaialble activity locations need to be sent to the user.
     */
    @Override
    public void aboutToCreateActivity() {
        if (locations == null)
            retrieveLocations();
    }


    /**
     * The json response could be:
     * 1. CONFIRMATION
     *      1a: confirmation that an activity has been created
\     * 2. LOCATIONS
     *      2a: an array with locations
     *      2b: a confirmation that we already have the right version of locations
     * 3. ACTIVITY: an already created activity that is to be displayed
     * 4. ERROR
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.NEW_ACTIVTIY_CONFIRMATION)) {

                //String message = (String) jsonObject.get(ComConstants.MESSAGE);

                synchronized (this) {
                    if (view() != null) {
                        view().updateCreateStatus(true);
                    }
                }
                synchronized (this) {
                    if (view() != null) {
                        view().navigateToBrowseActivities();
                    }
                }
            }
            else if (msgType.equals(ComConstants.LOCATIONS)) {
                updateLocationList(json);
            }
            else if (msgType.equals(ComConstants.LOCATIONS_CONFIRMATION)) {
                Log.i(TAG, "Locations already up-to-date");

                // update the last check date for now
                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString(LocalConstants.SP_LOCATIONS_CHECK_DATE, DateHelper.completeDateToString(new Date())).commit();

                // since we already have the right version, just get it from local storage
                // Maybe we even already have the right version in this instance
                String versionInPrefs = preferences.getString(LocalConstants.SP_VERSION_LOCATIONS, null);

                if (locationsVersion != null && versionInPrefs != null && locationsVersion.equals(versionInPrefs)) {
                    // We already have the right version loaded in this instance
                    onLocationsRetrievalSuccess();
                }
                else {
                    String locationsJsonString = preferences.getString(LocalConstants.SP_LOCATIONS, null);

                    JSONObject jsonLocations = new JSONObject(locationsJsonString);
                    jsonToLocations(jsonLocations);
                    locationsVersion = versionInPrefs;
                    onLocationsRetrievalSuccess();
                }
            }
            else if (msgType.equals(ComConstants.ACTIVITIY)) {
                setActivity(json);
            }
            else if (msgType.equals(ComConstants.NEW_ACTIVITY_ERROR)){
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
     * Sets the model - Activity - based on information in the provided json string.
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

    private void onActionSuccess(String message) {
        if (actionType.equals(ActionType.CreateNew)) {
            view().updateCreateStatus(true);
        }
    }

    private void onActionFail(String error) {
        view().showOnError(error);
    }

    /**
     * Location list needs to be retrieved for display. The method determines whether a check is needed
     * to be performed with the server, or locations can simply be loaded from local storage.
     * If too much time has passed since the last version check, or there is no locations list in local
     * storage yet, a json string is sent to the server initiating the version check.
     * If there is no location list in local storage yet, the version number 0 is sent to the server,
     * which will lead to receiving a location list.
     */
    private void retrieveLocations() {

        SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        boolean performCheckWithServer = true;


        String version = "0";

        if (preferences.contains(LocalConstants.SP_VERSION_LOCATIONS)) {
            version = preferences.getString(LocalConstants.SP_VERSION_LOCATIONS, "0");

            // decide if we need to check our version with server
            if (preferences.contains(LocalConstants.SP_LOCATIONS_CHECK_DATE)) {
                String lastCheckString = preferences.getString(LocalConstants.SP_LOCATIONS_CHECK_DATE, null);

                try {
                    Date lastCheck = DateHelper.completeStringDateToDate(lastCheckString);
                    if (new Date().getTime() - lastCheck.getTime() < LocalConstants.VERSION_CHECK_INTERVAL) {
                        performCheckWithServer = false;

                        Log.i(TAG, String.format("Locations version: %s, last check was: %s, have to check with server: %b", version, DateHelper.completeDateToString(lastCheck), performCheckWithServer));
                    }

                } catch (ParseException e) {
                }

                if (!performCheckWithServer) {
                    if (preferences.contains(LocalConstants.SP_CATEGORIES)) {
                        String categoriesJson = preferences.getString(LocalConstants.SP_CATEGORIES, "null");
                        if (categoriesJson != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(categoriesJson);
                                jsonToLocations(jsonObject);
                                onLocationsRetrievalSuccess();
                            } catch (JSONException ex) {
                                performCheckWithServer = true;
                            }
                        }
                    } else {
                        performCheckWithServer = true;
                    }
                }
            }
        }


        if (performCheckWithServer) {
        String json = JsonConverter.getLocationsJson(version);
        sendJsonString(json);
        }
    }

    /**
     * A new list of locations have been received from the server, and the local stored list
     * has to be updated to this one.
     * @param json Json string with the updated list of locations.
     */
    private void updateLocationList(String json) {

        Log.i(TAG, "Locations: " + json);


       try {
            JSONObject jsonObject = new JSONObject(json);
            jsonToLocations(jsonObject);


           // add the version number as well, got from the server, now 0
           String version = jsonObject.getString(ComConstants.LOCATIONS_VERSION_NBR);
           Log.i(TAG, "Locations version: " + version);

           SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
           preferences.edit().putString(LocalConstants.SP_LOCATIONS, json).commit();
           preferences.edit().putString(LocalConstants.SP_LOCATIONS_CHECK_DATE, DateHelper.completeDateToString(new Date())).commit();
           preferences.edit().putString(LocalConstants.SP_VERSION_LOCATIONS, version).commit();

            onLocationsRetrievalSuccess();
        }
        catch (JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get locations";
            if (json != null)
                failMsg +=" : " + json;

            onRetrievalError(failMsg);
        }
    }

    /**
     * Retreives location information form the provided json string and stores it in its locations
     * hasmap.
     * @param jsonObject
     * @throws JSONException
     */
    private void jsonToLocations(JSONObject jsonObject) throws JSONException {
        locations = new HashMap<String, List<Location>>();

        JSONArray mainArr = jsonObject.getJSONArray(ComConstants.ARRAY_LANDSCAPE);

        for (int i = 0; i < mainArr.length(); i++) {
            JSONObject mainCat = mainArr.getJSONObject(i);

            //int mainCatId = mainCat.getInt(ComConstants.ID);
            String mainCatName = mainCat.getString(ComConstants.NAME);
            locations.put(mainCatName, new ArrayList<Location>());


            JSONArray subArr = mainCat.getJSONArray(ComConstants.ARRAY_CITY);

            for (int y = 0; y < subArr.length(); y++) {
                JSONObject subCat = subArr.getJSONObject(y);

                int subCatId = subCat.getInt(ComConstants.ID);
                String subCatName = subCat.getString(ComConstants.NAME);

                Location sLocation = new Location(subCatId, subCatName);
                locations.get(mainCatName).add(sLocation);
            }

        }
    }

    /**
     * Send the current stored location list to the view for display.
     *  TODO now locations are only shown as simple list of specific location
     *  change it so that it is an embedded list with 'Landskap' and 'stad'
     */
    private void onLocationsRetrievalSuccess() {
        List<String> loc = new ArrayList<String>();
        for (String k : locations.keySet()) {
            for (Location l : locations.get(k)) {
                loc.add(l.getName());
            }
        }

        String[] locationArr = loc.toArray(new String[locations.keySet().size()]);
        view().setLocationList(locationArr);
    }

    private void onRetrievalError(String error) {
        view().showOnError(error);
    }

    @Override
    public void bindView(@NonNull EditableActivityView view) {
        super.bindView(view);

        retrieveLocations();
        if (model != null) {
            view().displayActivityDetails(model);
        }
    }


    /**
     * Is an Activity is newly created one or an already existing one being edited?
     */
    private enum ActionType {
        CreateNew,
        Edit
    }
}
