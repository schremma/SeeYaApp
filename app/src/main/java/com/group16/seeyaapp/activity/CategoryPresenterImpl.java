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

    @Override
    public void mainCategorySelected(String mainCategory) {
        List<Category> subcats = model.get(mainCategory);

        if (subcats != null) {
            String[] subArr = new String[subcats.size()];
            for(int i = 0; i < subcats.size(); i++) {
                subArr[i] = subcats.get(i).getName();
            }

            view().setSubcategories(subArr);
        }
    }

    @Override
    public void pressedNext() {

    }

    @Override
    protected void communicationResult(String json) {

        model = new HashMap<String, List<Category>>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray mainArr = jsonObject.getJSONArray(ComConstants.ARRAY_MAINCATEGORY);

            for (int i = 0; i < mainArr.length(); i++) {
                JSONObject mainCat = mainArr.getJSONObject(i);

                int mainCatId = mainCat.getInt(ComConstants.ID);
                String mainCatName = mainCat.getString(ComConstants.NAME);

                //Category mCategory = new Category(mainCatId, mainCatName);
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
        retrieveCategories();
    }

    @Override
    protected void updateView() {

    }
}
