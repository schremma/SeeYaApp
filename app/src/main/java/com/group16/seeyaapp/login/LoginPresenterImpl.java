package com.group16.seeyaapp.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Login;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 10/04/16.
 */
public class LoginPresenterImpl extends CommunicatingPresenter<LoginView, Login> implements LoginPresenter {

    private boolean loading = false;

    private static final String TAG = "LoginPresenter";

    @Override
    protected void updateView() {

    }

    @Override
    public void validateCredentials(String username, String password) {
        if (view() != null) {
            view().showLoading();
        }

        Login login = new Login(username, password);
        startLogIn(login);

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

    private void startLogIn(Login login) {

        String loginJson = JsonConverter.jsonify(login);
        if (loginJson == null)
            throw new IllegalArgumentException("json string is null");

        sendJsonString(loginJson);

    }


    @Override
    protected void communicationResult(String loginResultJson) {
        Log.i(TAG, loginResultJson);

        try {
            JSONObject jsonObject = new JSONObject(loginResultJson);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.CONFIRMATION)) {
                String confirmationType = (String)jsonObject.get(ComConstants.CONFIRMATION_TYPE);
                if (confirmationType.equals("OK")) {

                    // on success, it would also contain an auth token (?) that we would save in shared preferences
                    /*String mockAuthToken = "whatever";

                    final SharedPreferences prefs = new ObscuredSharedPreferences(
                            ctx, ctx.getSharedPreferences("ObscuredSharedPreferences", Context.MODE_PRIVATE) );

                    prefs.edit().putString("authToken", mockAuthToken).commit();*/

                    loginSuccess();
                }
            }
            else {
                String message =  (String)jsonObject.get(ComConstants.MESSAGE);
                loginFail(message);
            }
        }
        catch(JSONException e)
        {
            Log.i(TAG, e.getMessage());
            loginFail("Login failed");
        }
    }

    private void loginSuccess() {
        loading = false;
        view().navigateToHome();
    }

    private void loginFail(String message) {
        loading = false;
        view().hideLoading();
        view().setError(message);
    }

}
