package com.group16.seeyaapp.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 11/04/16.
 * Presenter that handles the logic behind registering a new user in the app,
 * storing user information in a Account object - the model.
 * After local validation, user information is sent to the server,
 * and the associated RegisterView is notified of registration success or failure based
 * on the server's response.
 */
public class RegisterPresenterImpl extends CommunicatingPresenter<RegisterView, Account> implements RegisterPresenter {

    private static final String TAG = "RegisterPresenter";


    /**
     * Stores the provided credentials in an Account object through which
     * local validation is performed. If this validation has checked out,
     * it initiates sending a json string to the server requesting registration of the user.
     * Otherwise, the view is notified of specific validation errors.
     * @param username
     * @param email
     * @param password
     */
    @Override
    public void registerNewUser(String username, String email, String password) {

        model = new Account(username, password, email);

        boolean emailOk = model.validateEmail();
        boolean passwordOk = model.validatePassword();
        boolean userOk = model.validateUserName();

        if (emailOk && passwordOk) {
            String regJson = JsonConverter.jsonify(model);
            sendJsonString(regJson);
        }
        else {
            if (!userOk) {
                view().showUserNameError("Invalid user name format");
            }
            if (!emailOk) {
                view().showEmailError("Invalid email format");
            }
            if (!passwordOk) {
                view().showPasswordError("Invalid password format");
            }
        }

    }

    /**
     * Handles json response received from the server:
     * a) the user mught have been successfully registered
     * b) the registration failed
     * @param registerResultJson
     */
    @Override
    protected void communicationResult(String registerResultJson) {
        Log.i(TAG, registerResultJson);

        try {
            JSONObject jsonObject = new JSONObject(registerResultJson);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.NEW_USER_CONFIRMATION)) {
                // Store username locally for later use
                final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString("currentUser", model.getUserName()).commit();
                registerSuccess();
            }
            else if (msgType.equals(ComConstants.NEW_USER_ERROR)) {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                registerFail(message);
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                registerFail(message);
            }
        }
        catch(JSONException e) {
            Log.i(TAG, e.getMessage());
            String failMsg = "Registration error";
            if (registerResultJson != null) {
                failMsg +=" : " + registerResultJson;
            }
            registerFail(failMsg);
        }
    }


    /**
     * Notifies view of successful registration
     */
    private void registerSuccess() {
        view().navigateToHome();
    }

    /**
     * Notifies view of unsuccessful registration.
     * Registration error from server is shown as username error, for now.
     * @param errorMsg error message sent ot the view
     */
    private void registerFail(String errorMsg) {
        view().showUserNameError(errorMsg);
    }
}
