package com.group16.seeyaapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.LocalConstants;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 10/04/16.
 * Presenter that handles the logic behind logging into the application,
 * storing login information in a UserLogin object, the model.
 * Validates the provided user credentials against a remote system and
 * notifies its associated LoginView of successful or failed authentication.
 */
public class LoginPresenterImpl extends CommunicatingPresenter<LoginView, UserLogin> implements LoginPresenter {

    private boolean loading = false;

    private static final String TAG = "LoginPresenter";


    /**
     * Stores the provided user credentials in a UserLogin object,
     * and locally validates their format calling the object's
     * validation methods. If local validation checks out,
     * remote authentication is initiated.
     * @param username
     * @param password
     */
    @Override
    public void validateCredentials(String username, String password) {
        UserLogin login = new UserLogin(username, password);
        model = new UserLogin();
        model.setUsername(username);

        if (login.ValidateFormat()) {
            view().showLoading();
            startLogIn(login);
        }
        else
            view().setError("Please fill in both your username and password");
    }

    @Override
    public void bindView(@NonNull LoginView view) {
        super.bindView(view);

        if (loading) {
            // load data
            if (view() != null) {
                view().showLoading();
            }

        }
    }

    /**
     * Sends a json string requesting validation of the stored
     * user credentials to the network interface of the app.
     * @param login UserLogin object with credentials
     */
    private void startLogIn(UserLogin login) {

        String loginJson = JsonConverter.jsonify(login);
        if (loginJson == null)
            throw new IllegalArgumentException("json string is null");

        sendJsonString(loginJson);

    }

    /**
     * Handles json response received from the server:
     * a) successful authentication
     * b) unsuccessful authentication
     * @param loginResultJson
     */
    @Override
    protected void communicationResult(String loginResultJson) {
        Log.i(TAG, loginResultJson);

        try {
            JSONObject jsonObject = new JSONObject(loginResultJson);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.LOGIN_OK)) {
                String message = (String)jsonObject.get(ComConstants.MESSAGE);

                final SharedPreferences preferences = ctx.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                preferences.edit().putString(LocalConstants.SP_CURRENT_USER, model.getUserName()).commit();

                loginSuccess();
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                loginFail("User name or password is incorrect");
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            String failMsg = "Login failed";
            if (loginResultJson != null)
                failMsg +=" : " + loginResultJson;

            loginFail(failMsg);
        }
    }

    /**
     * Notifies the view of successful login.
     */
    private void loginSuccess() {
        loading = false;
        view().navigateToHome();
    }

    /**
     * Notifies the view of unsuccessful login,
     * and sends an error message describing the problem
     * @param message error message to send to view
     */
    private void loginFail(String message) {
        loading = false;
        view().hideLoading();
        view().setError(message);
    }

}
