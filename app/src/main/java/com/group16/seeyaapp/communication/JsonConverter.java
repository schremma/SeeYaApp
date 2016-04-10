package com.group16.seeyaapp.communication;

import android.util.Log;

import com.group16.seeyaapp.model.Account;
import com.group16.seeyaapp.model.Login;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 10/04/16.
 */
public final class JsonConverter {

    private static final String TAG = "JsonConverter";

    public static String jsonify(Login login) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Integer.toString(ComConstants.TYPE), ComConstants.LOGIN);
            jsonObject.put(Integer.toString(ComConstants.USERNAME), login.getUserName());
            jsonObject.put(Integer.toString(ComConstants.PASSWORD), login.getPassword());
            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }

    public static String jsonify(Account account) {
        String json = null;
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Integer.toString(ComConstants.TYPE), ComConstants.NEWUSER);
            jsonObject.put(Integer.toString(ComConstants.USERNAME), account.getUserName());
            jsonObject.put(Integer.toString(ComConstants.PASSWORD), account.getPassword());
            jsonObject.put(Integer.toString(ComConstants.EMAIL), account.getEmail());
            json = jsonObject.toString();
        }
        catch (JSONException e) {
            Log.d(TAG, e.getMessage());}

        return json;
    }
}
