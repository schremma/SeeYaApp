package com.group16.seeyaapp.register;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.BasePresenter;
import com.group16.seeyaapp.communication.ComConstants;
import com.group16.seeyaapp.communication.ComResultReceiver;
import com.group16.seeyaapp.communication.CommunicationService;
import com.group16.seeyaapp.communication.JsonConverter;
import com.group16.seeyaapp.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 11/04/16.
 */
public class RegisterPresenterImpl extends BasePresenter<RegisterView, Account> implements RegisterPresenter, ComResultReceiver.Receiver {

    private ComResultReceiver mReceiver;
    private Context ctx; //TODO: inject instead?
    private static final String TAG = "RegisterPresenter";

    @Override
    protected void updateView() {

    }

    @Override
    public void bindView(@NonNull RegisterView view) {
        super.bindView(view);

        if (ctx == null) {
            if (view instanceof Activity)
                ctx = ((Activity) view).getApplicationContext();
            else if (view instanceof Fragment)
                ctx = ((Fragment) view).getActivity().getApplicationContext();
        }

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String loginResultJson = resultData.getString("result");
        Log.i(TAG, loginResultJson);

        try {
            JSONObject jsonObject = new JSONObject(loginResultJson);
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
        catch(JSONException e) {Log.i(TAG, e.getMessage());}
    }

    @Override
    public void registerNewUser(String username, String email, String password) {

        //TODO: local validation first
        model = new Account(username, password, email);

        // if local validation checks out:
        registerAccountOnServer();

    }

    private void registerAccountOnServer() {
        String regJson = JsonConverter.jsonify(model);
        if (regJson == null)
            throw new IllegalArgumentException("json string is null");

        mReceiver = new ComResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(ctx, CommunicationService.class);

        intent.putExtra("receiver", mReceiver);
        intent.putExtra("json", regJson);
        intent.putExtra("requestId", 102);

        ctx.startService(intent);
    }

    private void registerSuccess() {
        view().navigateToHome();
    }

    // Registration error from server is ahown as username error... for now
    private void registerFail(String errorMsg) {
        view().showUserNameError(errorMsg);
    }
}
