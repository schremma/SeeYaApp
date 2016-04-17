package com.group16.seeyaapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.primitives.Ints;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea on 15/04/16.
 */
public class ActivityListPresenterImpl extends CommunicatingPresenter<ActivityListView, List<Activity>> implements ActivityListPresenter  {
    private List<String> headlines;
    private List<String> dates;
    private List<Integer> ids;

    private static final String TAG = "ActivityListPresenter";

    //TODO: some field that shows what kind of activity list to be presented, i.e. own or for a specific location etc.

    @Override
    public void onActivitySelected(int activityId) {
        view().navigateToActivityDisplay(activityId);
    }

    /**
     * Json string can be
     * 1: an array with activity headlines.
     * @param json
     */
    @Override
    protected void communicationResult(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.ACTIVITY_HEADLINES)) {
                JSONArray arr = jsonObject.getJSONArray(ComConstants.ARRAY_HEADLINE);

                ids = new ArrayList<Integer>();
                headlines = new ArrayList<String>();
                dates = new ArrayList<String>();


                for (int i = 0; i < arr.length(); i++) {
                    JSONObject headline = arr.getJSONObject(i);

                    ids.add(headline.getInt(ComConstants.ID));
                    headlines.add(headline.getString(ComConstants.HEADLINE));
                    dates.add(headline.getString(ComConstants.DATE));

                }
                onRetrievalSuccess();
            }
        }
        catch (JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get activities";
            if (json != null)
                failMsg +=" : " + json;

            onRetrievalError(failMsg);
        }

    }

    private void onRetrievalError(String failMsg) {
        view().showOnError(failMsg);
    }

    private void onRetrievalSuccess() {

        view().setHeadlineList(headlines.toArray(new String[0]), dates.toArray(new String[0]), Ints.toArray(ids));
    }


    @Override
    public void bindView(@NonNull ActivityListView view) {
        super.bindView(view);

        if (ids == null)
            retrieveHeadlines();
    }

    private void retrieveHeadlines() {
        //TODO some check to determine what kind of headlines to be retrieved
        // here: all activities created by logged in user
        final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString("currentUser", null);

        String json = JsonConverter.getOwnActivityHeadlinesJson(currentUser);
        sendJsonString(json);
    }

    @Override
    protected void updateView() {

    }
}
