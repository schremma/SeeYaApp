package com.group16.seeyaapp.register;

import android.util.Log;

import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.CommunicatingPresenter;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 11/04/16.
 */
public class RegisterPresenterImpl extends CommunicatingPresenter<RegisterView, Account> implements RegisterPresenter {

    private static final String TAG = "RegisterPresenter";

    @Override
    protected void updateView() {

    }



    @Override
    public void registerNewUser(String username, String email, String password) {

        //TODO: local validation first
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

    @Override
    protected void communicationResult(String registerResultJson) {
        Log.i(TAG, registerResultJson);

        try {
            JSONObject jsonObject = new JSONObject(registerResultJson);
            String msgType = (String)jsonObject.get(ComConstants.TYPE);

            if (msgType.equals(ComConstants.CONFIRMATION)) {
                String confirmationType = (String)jsonObject.get(ComConstants.CONFIRMATION_TYPE);
                if (confirmationType.equals("OK")) {

                    registerSuccess();
                }
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


    private void registerSuccess() {
        view().navigateToHome();
    }

    // Registration error from server is shown as username error... for now
    private void registerFail(String errorMsg) {
        view().showUserNameError(errorMsg);
    }
}
