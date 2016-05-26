package com.group16.seeyaapp.communication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.group16.seeyaapp.BasePresenter;

/**
 * Created by Andrea on 12/04/16.
 * A BasePresenter subclass that sends json requests through a Service,
 * and receives responses from the same service.
 */
public abstract class CommunicatingPresenter<V, M> extends BasePresenter<V, M> implements ComResultReceiver.Receiver {

    private ComResultReceiver mReceiver;
    protected Context ctx;

    /**
     * Starts a CommunicationService, if it is not already started, and forwards the provided
     * json string to it.
     * Response form the service will be received by setting this instance as the listener
     * of a ComResultReceiver.
     * @param json The json string the send to the service
     */
    protected void sendJsonString(String json) {

        if (json == null)
            throw new IllegalArgumentException("json string is null");

        if (mReceiver == null) {
            mReceiver = new ComResultReceiver(new Handler());
            mReceiver.setReceiver(this);
        }
        Intent intent = new Intent(ctx, CommunicationService.class);

        intent.putExtra("receiver", mReceiver);
        intent.putExtra("json", json);
        intent.putExtra("requestId", 101);

        ctx.startService(intent);
    }

    /**
     * Method invoked as a communication result have come back from the service.
     * The result is a json strign stored in the provided Bundle
     * @param resultCode code identifying the response from the service
     * @param resultData Bundle with the json representing result sent back from the service
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String resultJson = resultData.getString("result");
        if (resultJson != null) {
            communicationResult(resultJson);
        }
        else {
            Log.i("CommunicatingPresenter", "null json result from service, message handling is terminated.");
        }
    }

    protected abstract void communicationResult(String json);

    /**
     * For starting a service, a context is needed:
     * the application contex is obtained from the view that the presenter subclassing
     * this class binds to.
     * @param view
     */
    @Override
    public void bindView(@NonNull V view) {
        super.bindView(view);

        if (ctx == null) {
            if (view instanceof Activity)
                ctx = ((Activity) view).getApplicationContext();
            else if (view instanceof Fragment)
                ctx = ((Fragment) view).getActivity().getApplicationContext();
        }

    }
}