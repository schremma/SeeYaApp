package com.group16.seeyaapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 14/04/16.
 */
public class ActivityPresenterImpl extends CommunicatingPresenter<ActivityView, Activity> implements ActivityPresenter {
    private static final String TAG = "ActivityPresenter";
    private HashMap<String, List<Location>> locations;

    @Override
    public void onCreate(Activity activity) {
        if (activity != null) {
            model = activity;

            // TODO: validate activity contents

            final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
            String currentUser = preferences.getString("currentUser", null);
            model.setOwner(currentUser);

            String json = JsonConverter.jsonify(model);
            sendJsonString(json);
        }

    }

    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.CONFIRMATION)) {
                String confirmationType = (String)jsonObject.get(ComConstants.CONFIRMATION_TYPE);

                onCreationSuccess();
            }
            else if (msgType.equals(ComConstants.LOCATIONS)) {
                updateLocationList(json);
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                onCreationFail(message);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Activity creation failed";
            if (json != null)
                failMsg +=" : " + json;

            onCreationFail(failMsg);
        }

    }

    private void onCreationSuccess() {
        view().showOnSuccess("Activity has been created");
    }

    private void onCreationFail(String error) {
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

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray mainArr = jsonObject.getJSONArray(ComConstants.ARRAY_MAINCATEGORY);

            for (int i = 0; i < mainArr.length(); i++) {
                JSONObject mainCat = mainArr.getJSONObject(i);

                //int mainCatId = mainCat.getInt(ComConstants.ID);
                String mainCatName = mainCat.getString(ComConstants.NAME);
                locations.put(mainCatName, new ArrayList<Location>());


                JSONArray subArr = mainCat.getJSONArray(ComConstants.ARRAY_SUBCATEGORY);

                for (int y = 0; y < subArr.length(); y++) {
                    JSONObject subCat = subArr.getJSONObject(y);

                    int subCatId = subCat.getInt(ComConstants.ID);
                    String subCatName = subCat.getString(ComConstants.NAME);

                    Location sLocation = new Location(subCatId, subCatName);
                    locations.get(mainCatName).add(sLocation);
                }

            }
            onRetrievalSuccess();
        }
        catch (JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get categories";
            if (json != null)
                failMsg +=" : " + json;

            onRetrievalError(failMsg);
        }
    }


    private void onRetrievalSuccess() {
        String[] keyArray = locations.keySet().toArray(new String[locations.keySet().size()]);
        view().setLocationList(keyArray);
    }

    private void onRetrievalError(String error) {
        view().showOnError(error);
    }

    @Override
    public void bindView(@NonNull ActivityView view) {
        super.bindView(view);
        if (locations == null)      // or shoudl we always check version number with the server?
            retrieveLocations();
    }


    @Override
    protected void updateView() {

    }
}
