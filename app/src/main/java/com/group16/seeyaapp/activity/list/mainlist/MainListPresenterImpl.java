package com.group16.seeyaapp.activity.list.mainlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.group16.seeyaapp.LocalConstants;
import com.group16.seeyaapp.activity.list.Filter;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.helpers.DateHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 25/04/16.
 */
public class MainListPresenterImpl extends CommunicatingPresenter<MainListView, String> implements MainListPresenter {
    private static final String TAG = "MainListPresenter";

    private List<String> listHeader;
    private HashMap<String, List<String>> listChild;
    private List<Map.Entry<String,Integer>> idStringPairs= new ArrayList<>(); //ids corresponding to strings in listChild

    private String categoriesVersion;



    /**
     * The json String returned form the server might be:
     * 1. ARRAY_MAINCATEGORY: a list with up-to-date main and subcategories
     * 2. a confirmation that we already have the right version of categories
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        Log.i(TAG, json);

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.ACTIVITY_CATEGORIES)) {

                Log.i(TAG, "Categories update...");

                jsonToCategories(jsonObject);

                String version = jsonObject.getString(ComConstants.CATEGORIES_VERSION_NUMBER);
                Log.i(TAG, "Categories version: " + version);

                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString(LocalConstants.SP_CATEGORIES, json).commit();
                preferences.edit().putString(LocalConstants.SP_CATEGORIES_CHECK_DATE, DateHelper.CompleteDateToString(new Date())).commit();
                preferences.edit().putString(LocalConstants.SP_VERSION_CATEGORIES, version).commit();
                onUpdatedListData();
            }
            else if (msgType.equals(ComConstants.CATEGORIES_CONFIRMATION)) {
                Log.i(TAG, "Categories already up-to-date");


                // We already have the right version, just get it from local storage
                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                String versionInPrefs = preferences.getString(LocalConstants.SP_VERSION_CATEGORIES, null);

                if (categoriesVersion != null && versionInPrefs != null && categoriesVersion.equals(versionInPrefs)) {
                    // We already have the right version loaded in this instance
                    onUpdatedListData();
                }
                else {
                    String categoriesJsonString = preferences.getString(LocalConstants.SP_CATEGORIES, null);
                    JSONObject jsonCategories = new JSONObject(categoriesJsonString);
                    jsonToCategories(jsonCategories);
                    categoriesVersion = versionInPrefs;
                    onUpdatedListData();
                }

            }
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

    private void jsonToCategories(JSONObject jsonObject) throws JSONException{
        listHeader = new ArrayList<String>();
        listChild = new HashMap<String, List<String>>();

        JSONArray mainArr = jsonObject.getJSONArray(ComConstants.ARRAY_MAINCATEGORY);

        for (int i = 0; i < mainArr.length(); i++) {
            JSONObject mainCat = mainArr.getJSONObject(i);

            //int mainCatId = mainCat.getInt(ComConstants.ID);
            String mainCatName = mainCat.getString(ComConstants.NAME);

            listHeader.add(mainCatName);
            listChild.put(mainCatName, new ArrayList<String>());


            JSONArray subArr = mainCat.getJSONArray(ComConstants.ARRAY_SUBCATEGORY);

            for (int y = 0; y < subArr.length(); y++) {
                JSONObject subCat = subArr.getJSONObject(y);

                int subCatId = subCat.getInt(ComConstants.ID);
                String subCatName = subCat.getString(ComConstants.NAME);

                listChild.get(mainCatName).add(subCatName);

                idStringPairs.add(new AbstractMap.SimpleEntry<>(subCatName, subCatId));
            }
        }
    }


    private void retrieveAllCategories() {

        SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        boolean performCheckWithServer = true;

        String version = "0";

        if (preferences.contains(LocalConstants.SP_VERSION_CATEGORIES)) {
            version = preferences.getString(LocalConstants.SP_VERSION_CATEGORIES, "0");

            // decide if we need to check our version with server
            if (preferences.contains(LocalConstants.SP_CATEGORIES_CHECK_DATE)) {
                String lastCheckString = preferences.getString(LocalConstants.SP_CATEGORIES_CHECK_DATE, null);

                try {
                    Date lastCheck = DateHelper.CompleteStringDateToDate(lastCheckString);
                    if (new Date().getTime() - lastCheck.getTime() < LocalConstants.VERSION_CHECK_INTERVAL) {
                        performCheckWithServer = false;

                        Log.i(TAG, String.format("Categories version: %s, last check was: %s, have to check with server: %b", version, DateHelper.CompleteDateToString(lastCheck), performCheckWithServer));
                    }

                } catch (ParseException e) {
                }

                if (!performCheckWithServer) {
                    if (preferences.contains(LocalConstants.SP_CATEGORIES)) {
                        String categoriesJson = preferences.getString(LocalConstants.SP_CATEGORIES, "null");
                        if (categoriesJson != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(categoriesJson);
                                jsonToCategories(jsonObject);
                                onUpdatedListData();
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
            String json = JsonConverter.getCategoriesJson(version);
            sendJsonString(json);
        }
    }

    private void onUpdatedListData() {

        view().setNestedListHeaders(listHeader, listChild);
    }

    private void onRetrievalError(String error) {
        // TODO call view to show error message
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void aboutToListActivities(Filter activityListFilter) {

        // TODO: add categoriesForInvitedTo

        SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);

        switch (activityListFilter) {
            case CategoriesForOwnActivities:
                // TODO send json to get only those categories in which the user has created at least one activity
                JsonConverter.getFilteredCategoriesJson(activityListFilter, currentUser);
                break;
            case CategoriesForPastActivties:
                break;
            default:
                retrieveAllCategories();
                break;
        }
    }

    @Override
    public void selectedListItem(String selectedItem, Filter listFilter) {

        // get id of selectedItem
        int id = -1;
        for (int i = 0; i < idStringPairs.size() && id == -1; i++) {
            if (idStringPairs.get(i).getKey().equals(selectedItem)) {
                id = idStringPairs.get(i).getValue();
                Log.i(TAG, "selected list item id for " + idStringPairs.get(i).getKey() + ": " + id);
            }
        }

        if (id != -1) {
            // call view to navigate to a list with activity headlines, send selectedItem id and listFilter
            view().navigateToHeadlineDisplay(id, listFilter);

        }
        else {
            // TODO call view to show error message
        }

    }
}
