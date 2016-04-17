package com.group16.seeyaapp.activity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrea on 14/04/16.
 */
public class CategoryPresenterImpl extends CommunicatingPresenter<CategoryView, HashMap<String, List<Category>>> implements CategoryPresenter {
    private static final String TAG = "CategoryPresenter";
    private String currentMainCat;

    /**
     * When the user has selected a main catgory,
     * a list with all the subcategories under this main category
     * is sent to the view.
     * @param mainCategory
     */
    @Override
    public void mainCategorySelected(String mainCategory) {
        List<Category> subcats = model.get(mainCategory);

        if (subcats != null) {
            currentMainCat = mainCategory;
            String[] subArr = new String[subcats.size()];
            for(int i = 0; i < subcats.size(); i++) {
                subArr[i] = subcats.get(i).getName();
            }

            view().setSubcategories(subArr);
        }
    }

    @Override
    public void pressedNext(String selectedSubcategory) {
        List<Category> subcats = model.get(currentMainCat);
        int id = -1;
        for (int i = 0; i < subcats.size() &&  id == -1; i++) {
            if (subcats.get(i).getName().equals(selectedSubcategory))
                id = subcats.get(i).getId();
        }

        if (id != -1) {
            view().navigateToCreateActivityDetails(id);
        }
        else
            view().showError("Select a main and a subcategory");

    }

    /**
     * The json String returned form the server might be:
     * 1. ARRAY_MAINCATEGORY: a list with up-t-date main and subcategories
     * 2. a confirmation that we already have the right version of categories
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        model = new HashMap<String, List<Category>>();

        //TODO: check message type - if it is just a confirmation that we have the right version of categories
        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.ACTIVITY_CATEGORIES)) {
                JSONArray mainArr = jsonObject.getJSONArray(ComConstants.ARRAY_MAINCATEGORY);

                for (int i = 0; i < mainArr.length(); i++) {
                    JSONObject mainCat = mainArr.getJSONObject(i);

                    int mainCatId = mainCat.getInt(ComConstants.ID);
                    String mainCatName = mainCat.getString(ComConstants.NAME);
                    model.put(mainCatName, new ArrayList<Category>());


                    JSONArray subArr = mainCat.getJSONArray(ComConstants.ARRAY_SUBCATEGORY);

                    for (int y = 0; y < subArr.length(); y++) {
                        JSONObject subCat = subArr.getJSONObject(y);

                        int subCatId = subCat.getInt(ComConstants.ID);
                        String subCatName = subCat.getString(ComConstants.NAME);

                        Category sCategory = new Category(subCatId, subCatName);
                        model.get(mainCatName).add(sCategory);
                    }

                }
                //TODO store somewehere the retrived categories with the version number
                onRetrievalSuccess();
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

    private void retrieveCategories() {
        // TODO: get version number from local storage
        int versionNbr = 0;

        String json = JsonConverter.getCategoriesJson(versionNbr);
        sendJsonString(json);
    }

    private void onRetrievalSuccess() {
        String[] keyArray = model.keySet().toArray(new String[model.keySet().size()]);
        view().setMainCategories(keyArray);
    }

    private void onRetrievalError(String error) {
        view().showError(error);
    }

    @Override
    public void bindView(@NonNull CategoryView view) {
        super.bindView(view);
        if (model == null)      // TODO or should we always check version number with the server?
            retrieveCategories();
    }

    @Override
    protected void updateView() {

    }
}
