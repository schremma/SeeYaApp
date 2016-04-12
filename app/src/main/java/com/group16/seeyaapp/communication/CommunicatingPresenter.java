package com.group16.seeyaapp.communication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.group16.seeyaapp.BasePresenter;

/**
 * Created by Andrea on 12/04/16.
 */
public abstract class CommunicatingPresenter<V, M> extends BasePresenter<V, M> implements ComResultReceiver.Receiver {

    private ComResultReceiver mReceiver;
    protected Context ctx;

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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String resultJson = resultData.getString("result");
        communicationResult(resultJson);
    }

    protected abstract void communicationResult(String json);

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