package com.group16.seeyaapp.login;

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
import com.group16.seeyaapp.model.Login;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrea on 10/04/16.
 */
public class LoginPresenterImpl extends BasePresenter<LoginView, Login> implements LoginPresenter, ComResultReceiver.Receiver {

    private ComResultReceiver mReceiver;
    private boolean loading = false;
    private Context ctx; //TODO: inject instead?

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

        if (ctx == null) {
            if (view instanceof Activity)
                ctx = ((Activity) view).getApplicationContext();
            else if (view instanceof Fragment)
                ctx = ((Fragment) view).getActivity().getApplicationContext();
        }

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

        mReceiver = new ComResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(ctx, CommunicationService.class);

        intent.putExtra("receiver", mReceiver);
        intent.putExtra("json", loginJson);
        intent.putExtra("requestId", 101);

        loading = true;
        ctx.startService(intent);
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
        catch(JSONException e) {Log.i(TAG, e.getMessage());}
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
