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
 * Handles the logic behind presenting the browsable list of the main page of the application.
 * The list is nested, with a number of main categories (listHeaders), each of which may
 * have a number of subcategories (listChildren). The exact kind of list presented this way
 * may vary. E.g. the main categories might be main activity subcategories such as Sport, with
 * specific activity subcategories such as tennis under each. Alternatively, the main categories
 * might be Swedish regions such as Skåne, with specific towns under each.
 * The presenter also stores json strings containing a list of activity headlines associated with
 * each subcategory. Once the user has browsed to a specific subcategory, the approporiate headlines should be
 * sent for the next view displaying these.
 */
public class MainListPresenterImpl extends CommunicatingPresenter<MainListView, String> implements MainListPresenter {
    private static final String TAG = "MainListPresenter";

    private List<String> listHeader;
    private HashMap<String, List<String>> listChild; // key: header, value: subcategories under header
    private List<Map.Entry<String,Integer>> idStringPairs= new ArrayList<>(); //ids corresponding to subcategory string in listChild
    private HashMap<String, String> headlineJsonsForSubcategories; // associated each subcategory with a json string
                                                                    // containing a list of activity headlines

    private String categoriesVersion;



    /**
     * Handles the json String returned form the server, which might be:
     * 1. MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER: only those main categories with subcategories
     * that contain at least one activity that the user is invited to, and headlines for each such activity
     * 2. MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES: only those main categories with subcategories
     * that contain at least one activity that the user has created, and headlines for each such activity
     * 3. ARRAY_MAINCATEGORY: an up-to-date list with all main and subcategories
     * 4. a confirmation that we already have the right version of categories
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        Log.i(TAG, json);

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            //
            if (msgType.equals(ComConstants.MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER) ||
                msgType.equals(ComConstants.MAINCATEGORY_SUBCATEGORY_HEADLINES_FOR_USER_OWND_ACTIVITIES)) {
                jsonToCategories(jsonObject);
                onUpdatedListData();
            }
            else if (msgType.equals(ComConstants.ACTIVITY_CATEGORIES)) {

                Log.i(TAG, "Categories update...");

                jsonToCategories(jsonObject);

                String version = jsonObject.getString(ComConstants.CATEGORIES_VERSION_NUMBER);
                Log.i(TAG, "Categories version: " + version);

                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString(LocalConstants.SP_CATEGORIES, json).commit();
                preferences.edit().putString(LocalConstants.SP_CATEGORIES_CHECK_DATE, DateHelper.completeDateToString(new Date())).commit();
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

    /**
     * Extracts headings, subheadings and headlines under each subheading from json String.
     * The headlines stored as json Strings, associated with their subheading.
     * @param jsonObject
     * @throws JSONException
     */
    private void jsonToCategories(JSONObject jsonObject) throws JSONException{
        listHeader = new ArrayList<String>();
        listChild = new HashMap<String, List<String>>();
        headlineJsonsForSubcategories = new HashMap<String, String>();

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

                JSONObject headlineJsonObject = new JSONObject();
                headlineJsonObject.put(ComConstants.ARRAY_HEADLINE, subCat.getJSONArray(ComConstants.ARRAY_HEADLINE));
                String headlineJsonString = headlineJsonObject.toString();

                listChild.get(mainCatName).add(subCatName);
                headlineJsonsForSubcategories.put(subCatName, headlineJsonString);

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
                    Date lastCheck = DateHelper.completeStringDateToDate(lastCheckString);
                    if (new Date().getTime() - lastCheck.getTime() < LocalConstants.VERSION_CHECK_INTERVAL) {
                        performCheckWithServer = false;

                        Log.i(TAG, String.format("Categories version: %s, last check was: %s, have to check with server: %b", version, DateHelper.completeDateToString(lastCheck), performCheckWithServer));
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

    /**
     * Sends updated main- and subcategories list to the view.
     */
    private void onUpdatedListData() {
        synchronized (this) {
            if (view() != null) {
                view().setNestedListHeaders(listHeader, listChild);
            }
        }
    }

    private void onRetrievalError(String error) {
        // TODO call view to show error message
    }


    /**
     * Initiates retrieving list headings, subheadings and activity headlines based on the way
     * the list is to be filtered.
     * @param activityListFilter Specifies what kind of activities are to be browsed through
     *                           the displayed list, e.g. own activities or invited to activities
     */
    @Override
    public void aboutToPresentMainList(Filter activityListFilter) {

        SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString(LocalConstants.SP_CURRENT_USER, null);

        switch (activityListFilter) {
            case InvitedToActivitiesByCategories:
            case OwnActivitiesByCategories:
                // send json to get only those categories in which the user is invited to or has created at least one activity
                String json = JsonConverter.getFilteredCategoriesJson(activityListFilter, currentUser);
                sendJsonString(json);
                break;
            case PastActivtiesByCategories:
                break;
            default:
                retrieveAllCategories();
                break;
        }
    }

    // TODO remove list filter from parameters

    /**
     * Called when the user has selected a subcategory for the list.
     * Appropriate json string with the assoicated headlines under that
     * subcategory is retrieved and sent to the next view for display.
     * @param selectedItem
     * @param listFilter
     */
    @Override
    public void selectedListItem(String selectedItem, Filter listFilter) {

        String headlinesJson = headlineJsonsForSubcategories.get(selectedItem);
        if (headlinesJson != null) {
            view().navigateToHeadlineDisplay(headlinesJson);
            Log.i(TAG, "Sending headlines json to activity:" + headlinesJson);
        }
        else {
            // TODO call view to show error message
            Log.i(TAG, "could not find headline json for subcategory: " + selectedItem);
        }

    }
}
