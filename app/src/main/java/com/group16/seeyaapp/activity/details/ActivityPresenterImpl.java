package com.group16.seeyaapp.activity.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

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
 * If the presenter is to display a specific activity: it gets the whole activity from server
 * using the provided id
 * if it is to create a new activity, it gets the list of locations and sends it to the view.
 * The view communicates to the presenter which if these two scenarios are to take place,
 * by calling the aboutTOPublishActivity() or aboutToCreateActivity(), just before it is displayed.
 */
public class ActivityPresenterImpl extends CommunicatingPresenter<ActivityView, Activity> implements ActivityPresenter {
    private static final String TAG = "ActivityPresenter";
    private HashMap<String, List<Location>> locations;
    private boolean editing;
    private ActionType actionType;

    @Override
    public void onCreateActivity(Activity activity) {
        if (activity != null) {
            model = activity;

            if (activity.validateActivity()) {

                final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                String currentUser = preferences.getString("currentUser", null);
                model.setOwner(currentUser);

                //TODO find a better way to store locations to avoid this kind of iteration
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
    public void onPublishActivity(long activityId) {
        String json = JsonConverter.publishActivityJson(activityId);
        actionType = ActionType.Publish;
        sendJsonString(json);
    }

    @Override
    public void aboutToDisplayActivity(int activityId) {
        editing = false;
        String json = JsonConverter.getActivityJson(activityId);

        //TODO get json from server instead:
        sendJsonString(json);

    }

    @Override
    public void aboutToCreateActivity() {
        editing = true;
        if (locations == null)
            retrieveLocations();
    }

    /**
     * The json response could be:
     * 1. CONFIRMATION
     *      1a: confirmation that an activity has been created
     *      1b: confirmation that an activity has been published
     * 2. LOCATIONS
     *      2a: an array with locations
     *      2b: a confirmation that we already have the right version
     * 3. ACTIVITY: an already created activity that is to be displayed
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.CONFIRMATION)) {
                // This could be a confirmation for both creating an activity or for publishing an activity

                String confirmationType = (String)jsonObject.get(ComConstants.CONFIRMATION_TYPE);
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);

                onActionSuccess(message);
            }
            else if (msgType.equals(ComConstants.LOCATIONS)) {
                updateLocationList(json);
            }
            else if (msgType.equals(ComConstants.ACTIVITIY)) {
                setActivity(json);
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onActionFail(message);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Activity creation failed";
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

            //TODO main category and subcategory string
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
        if (actionType.equals(ActionType.Publish))
            view().updatePublishedStatus(true);
        else if (actionType.equals(ActionType.CreateNew)) {
            view().updateCreateStatus(true);
        }
    }

    private void onActionFail(String error) {
        view().showOnError(error);
    }

    private void retrieveLocations() {
        // TODO: get version number from local storage
        int versionNbr = 0;

        String json = JsonConverter.getLocationsJson(versionNbr);
        sendJsonString(json);
    }

    private void updateLocationList(String json) {
        locations = new HashMap<String, List<Location>>();
        Log.i(TAG, "Locations: " + json);


        // TODO this instead of test values
       try {
            JSONObject jsonObject = new JSONObject(json);
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
            //TODO store retrieved locations with version number somewhere locally
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


    // TODO now locations are only shown as simple list of specific location
    // changed it so that it is an embedded list with 'Landskap' and 'stad'
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
    public void bindView(@NonNull ActivityView view) {
        super.bindView(view);

        //we do not need to retrieve locations if the activity is just displayed without editing
        if (editing && locations == null)
            retrieveLocations();
    }


    @Override
    protected void updateView() {

    }

    private enum ActionType {
        CreateNew,
        Publish,
        Edit
    }
}
