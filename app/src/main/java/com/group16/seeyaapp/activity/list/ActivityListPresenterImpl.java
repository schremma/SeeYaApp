package com.group16.seeyaapp.activity.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.primitives.Ints;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea on 15/04/16.
 */
public class ActivityListPresenterImpl extends CommunicatingPresenter<ActivityListView, JSONObject> implements ActivityListPresenter  {
    private List<String> headlines;
    private List<String> dates;
    private List<Integer> ids;

    private int listGroupId; // TODO remove?
    private Filter listFilter;

    private static final String TAG = "ActivityListPresenter";

    //TODO: some field that shows what kind of activity list to be presented, i.e. own or for a specific location etc.

    @Override
    public void onActivitySelected(int activityId) {

        // TODO navigate to different kinds of displays based on model (list filter)? - done in view now
        // i.e. if it concerns own editable activities (before publishing them) - EditableActivityView
        // but if it concerns non-editable activities - DetailView
        view().navigateToActivityDisplay(activityId);
    }


    // TODO remove?
    @Override
    public void aboutToListActivities(int groupId, Filter listFilter) {
        this.listFilter = listFilter;
        listGroupId = groupId;

        // implement retrieving headlines according to filter and groupId in retrieveHeadlines()
        // instead of just retrieving all activities owned by the current user
        retrieveHeadlines();
    }

    @Override
    public void aboutToListActivities(String headlines) {

        try {
            JSONObject jsonObject = new JSONObject(headlines);
            model = jsonObject;
            setHeadlinesFromJson(jsonObject);

            view().setHeadlineList(this.headlines.toArray(new String[0]), dates.toArray(new String[0]), Ints.toArray(ids));

        } catch (JSONException e) {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get activity headlines fomr json";

            onRetrievalError(failMsg);
        }
    }

    // TODO remove?
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

            // TODO remove this, or use setHeadlinesFromJson()
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
        catch (JSONException e) {
            Log.i(TAG, e.getMessage());
            String failMsg = "Cannot get activities";
            if (json != null)
                failMsg +=" : " + json;

            onRetrievalError(failMsg);
        }

    }

    private void setHeadlinesFromJson(JSONObject jsonObject) throws JSONException {

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

        // if state is already preserved for this view, reload the headlines
        // TODO make sure that listing headlines is not done 2X
        if (model != null) {
            try {
                setHeadlinesFromJson(model);
                view().setHeadlineList(headlines.toArray(new String[0]), dates.toArray(new String[0]), Ints.toArray(ids));
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
                String failMsg = "Cannot get headlines preserved in presenter";
            }
        }
    }

    // TODO remove retrieval of headlines
    private void retrieveHeadlines() {
        final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        String currentUser = preferences.getString("currentUser", null);

        //TODO some check to determine what kind of headlines to be retrieved
//        switch (listFilter) {
//            case InvitedToActivitiesByCategories:
//            case OwnActivitiesByCategories:
//                JsonConverter.getFilteredHeadlinesJson(filter, currentUser, listGroupId);
//                break;
//        }


        // TODO as a test now, we get all activities created by logged in user
        String json = JsonConverter.getOwnActivityHeadlinesJson(currentUser);
        sendJsonString(json);
    }

    @Override
    protected void updateView() {

    }
}
