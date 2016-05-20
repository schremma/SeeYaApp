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
 */
public class EditableActivityPresenterImpl extends CommunicatingPresenter<EditableActivityView, Activity> implements EditableActivityPresenter {
    private static final String TAG = "EditableActivityPres";
    private HashMap<String, List<Location>> locations;
    private String locationsVersion;

    private ActionType actionType;  // what kind of editing is done to the activity?

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

    @Override
    public void onSetActivity(Activity activity) {
        model = activity;
    }


    @Override
    public void aboutToDisplayActivity(int activityId) {

        final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);
        String json = JsonConverter.getActivityJson(activityId, currentUser);

        sendJsonString(json);

    }

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
     *      2b: a confirmation that we already have the right version
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

                String message = (String) jsonObject.get(ComConstants.MESSAGE);
                view().updateCreateStatus(true);
                view().navigateToBrowseActivities();
            }
            else if (msgType.equals(ComConstants.LOCATIONS)) {
                updateLocationList(json);
            }
            else if (msgType.equals(ComConstants.LOCATIONS_CONFIRMATION)) {
                Log.i(TAG, "Locations already up-to-date");

                // update the last check date for now
                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString(LocalConstants.SP_LOCATIONS_CHECK_DATE, DateHelper.CompleteDateToString(new Date())).commit();

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
                    Date lastCheck = DateHelper.CompleteStringDateToDate(lastCheckString);
                    if (new Date().getTime() - lastCheck.getTime() < LocalConstants.VERSION_CHECK_INTERVAL) {
                        performCheckWithServer = false;

                        Log.i(TAG, String.format("Locations version: %s, last check was: %s, have to check with server: %b", version, DateHelper.CompleteDateToString(lastCheck), performCheckWithServer));
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
           preferences.edit().putString(LocalConstants.SP_LOCATIONS_CHECK_DATE, DateHelper.CompleteDateToString(new Date())).commit();
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


    // TODO now locations are only shown as simple list of specific location
    // change it so that it is an embedded list with 'Landskap' and 'stad'
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




    private enum ActionType {
        CreateNew,
        Edit
    }
}