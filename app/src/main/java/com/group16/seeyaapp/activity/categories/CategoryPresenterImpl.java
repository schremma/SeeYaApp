package com.group16.seeyaapp.activity.categories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.LocalConstants;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.model.NestedCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrea on 14/04/16.
 * Presenter handling the logic behind displaying main categories and subcategories
 * under which activities can be created. When the user selects a main category,
 * the list of corresponding subcategories should be sent to the associated CategoryView for display.
 * It might not be necessary to retrieve the list of possible categories from the server
 * all the time when these are displayed, since they are not expected to change often. Therefore,
 * a version number for the current category list is stored locally in the app.
 * As certain intervals (defined in LocalConstants, as VERSION_CHECK_INTERVAL) have passed
 * and categories are requested for display by the view, the app checks with the server if
 * it still has the right version. If it does, the categories are loaded from local storage.
 * If it does not, an updated version of the category list received from the server is
 * loaded into the view and stored in local storage (with updated version number).
 */
public class CategoryPresenterImpl extends CommunicatingPresenter<CategoryView, String> implements CategoryPresenter {
    private static final String TAG = "CategoryPresenter";

    private List<NestedCategory> maincategoryList;
    private List<NestedCategory> subcategoryList;
    private String categoriesVersion;   // version number for the category list stored locally

    /**
     * When the user has selected a main category,
     * a list with all the subcategories under this main category is sent to the view.
     * @param mainCategory
     */
    @Override
    public void mainCategorySelected(String mainCategory) {

        if (mainCategory != null) {
            model = mainCategory;
            List<String> subcategories = new ArrayList<String>();

            for (NestedCategory c : subcategoryList) {
                if (c.getParentName().equals(mainCategory)) {
                    subcategories.add(c.getName());
                }
            }

            view().setSubcategories(subcategories.toArray(new String[subcategories.size()]));
        }
    }

    /**
     *  Invoked when the user indicated intention to proceed further after
     *  the provided subcategory has been selected.
     *  The id associated with the subcategory string is looked up and
     *  sent to the view, requesting it to navigate to displaying a view
     *  for creating an activity. This subcategory is is sent along to be
     *  preserved by the time when the activity under creation is to be saved on the server.
     * @param selectedSubcategory
     */
    @Override
    public void pressedNext(String selectedSubcategory) {

        int id = -1;

        for(int i = 0; i < subcategoryList.size() &&  id == -1; i++) {
            if(subcategoryList.get(i).getName().equals(selectedSubcategory)) {
                id = subcategoryList.get(i).getId();
                Log.i(TAG, "Selected subcategory: " + subcategoryList.get(i).getName());

                model = selectedSubcategory;
            }
        }

        if (id != -1) {
            view().navigateToCreateActivityDetails(id);
        }
        else
            view().showError("Select a main and a subcategory");

    }

    /**
     * The json String returned form the server might be:
     * 1. ACTIVITY_CATEGORIES: a list with up-to-date main and subcategories
     * 2. a confirmation that we already have the right version of categories
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        Log.i(TAG, json);

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);
            // an updated list of categories have arrived as we no longel have the current version in local storage
            if (msgType.equals(ComConstants.ACTIVITY_CATEGORIES)) {

                Log.i(TAG, "Categories update");

                jsonToCategories(jsonObject);
                String version = jsonObject.getString(ComConstants.CATEGORIES_VERSION_NUMBER);
                Log.i(TAG, "Categories version: " + version);

                // saved the updated list locally, add the new version number as well
                // and update the last date we checked the categories
                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString(LocalConstants.SP_CATEGORIES, json).commit();
                preferences.edit().putString(LocalConstants.SP_CATEGORIES_CHECK_DATE, DateHelper.CompleteDateToString(new Date())).commit();
                preferences.edit().putString(LocalConstants.SP_VERSION_CATEGORIES, version).commit();
                onRetrievalSuccess();
            }
            else if (msgType.equals(ComConstants.CATEGORIES_CONFIRMATION)) {
                Log.i(TAG, "Categories already up-to-date");

                // We already have the right version, just get it from local storage
                // and update the last check date for now
                SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                String versionInPrefs = preferences.getString(LocalConstants.SP_VERSION_CATEGORIES, null);
                preferences.edit().putString(LocalConstants.SP_CATEGORIES_CHECK_DATE, DateHelper.CompleteDateToString(new Date())).commit();

                if (categoriesVersion != null && versionInPrefs != null && categoriesVersion.equals(versionInPrefs)) {
                    // We already have the right version loaded in this instance
                    Log.i(TAG, "categories in instance are up-to-date");

                    onRetrievalSuccess();
                }
                else {
                    String categoriesJsonString = preferences.getString(LocalConstants.SP_CATEGORIES, null);
                    JSONObject jsonCategories = new JSONObject(categoriesJsonString);
                    jsonToCategories(jsonCategories);
                    categoriesVersion = versionInPrefs;
                    onRetrievalSuccess();
                }
            }
        }
        catch (JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get categories";
            if (json != null)
                failMsg +=" : " + json;

            // If it cannot get categories, try to load it from local storage
            // TODO is it ok?
            SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
            String categoriesJsonString = preferences.getString(LocalConstants.SP_CATEGORIES, null);
            if (categoriesJsonString != null) {
                String versionInPrefs = preferences.getString(LocalConstants.SP_VERSION_CATEGORIES, null);
                try {
                    JSONObject jsonCategories = new JSONObject(categoriesJsonString);
                    jsonToCategories(jsonCategories);
                    categoriesVersion = versionInPrefs;
                    failMsg +="; " + "categories are loaded from local storage";
                    onRetrievalSuccess();
                } catch (JSONException ex) {}
            }

            onRetrievalError(failMsg);
        }
    }

    /***
     * Parses the provided JSONObject into two list> one for main categories and for subcategories.
     * Each list item is an instance of a NestedCategory object, storing information about its name,
     * id and, if it has any, the name and id of its associated superordinate category.
     * @param jsonObject
     * @throws JSONException
     */
    private void jsonToCategories(JSONObject jsonObject) throws JSONException{
        maincategoryList = new ArrayList<NestedCategory>();
        subcategoryList = new ArrayList<NestedCategory>();

        JSONArray mainArr = jsonObject.getJSONArray(ComConstants.ARRAY_MAINCATEGORY);

        for (int i = 0; i < mainArr.length(); i++) {
            JSONObject mainCat = mainArr.getJSONObject(i);

            int mainCatId = mainCat.getInt(ComConstants.ID);
            String mainCatName = mainCat.getString(ComConstants.NAME);

            maincategoryList.add(new NestedCategory(mainCatId, mainCatName));


            JSONArray subArr = mainCat.getJSONArray(ComConstants.ARRAY_SUBCATEGORY);

            for (int y = 0; y < subArr.length(); y++) {
                JSONObject subCat = subArr.getJSONObject(y);

                int subCatId = subCat.getInt(ComConstants.ID);
                String subCatName = subCat.getString(ComConstants.NAME);

                subcategoryList.add(new NestedCategory(subCatId, subCatName, mainCatId, mainCatName));
            }
        }
    }

    /**
     * Category list needs to be retrieved for display. The method determines whether a check is needed
     * to be performed with the server, or categories can simply be loaded from local storage.
     * If too much time has passed since the last version check, or there is no category list in local
     * storage yet, a json string is sent to the server initiating the version check.
     * If there is no category list in local storage yet, the version number 0 is sent to the server,
     * which will lead to recieveing a category list.
     */
    private void retrieveCategories() {

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
                                onRetrievalSuccess();
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
     * Categories have been successfuly retrieved so the main category
     * list is sent to the view for display
     */
    private void onRetrievalSuccess() {
        String[] mainArr = new String[maincategoryList.size()];

        int counter = 0;
        for(NestedCategory c : maincategoryList) {
            mainArr[counter] = c.getName();
            counter++;
        }

        view().setMainCategories(mainArr);
    }

    /**
     * Notifies view of error getting category lists.
     * @param error The error message
     */
    private void onRetrievalError(String error) {
        view().showError(error);
    }

    @Override
    public void bindView(@NonNull CategoryView view) {
        super.bindView(view);
        retrieveCategories();
    }
}
