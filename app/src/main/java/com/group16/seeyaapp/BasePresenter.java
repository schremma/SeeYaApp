package com.group16.seeyaapp;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by Andrea on 09/04/16.
 * The basic logical design of the app involving a BasePresenter and a PresenterManager is
 * based on the following code sample: https://github.com/remind101/android-arch-sample
 *
 * The logical design loosely follows the Model-View-Presenter (MVP) pattern in order to achieve
 * a certain degree of separation between GUI, logic and model classes.
 * The PresenterManager handles associating views with their Presenters (each inheriting BasePresenter)
 * across configuration changes (when the Activity might be destroyed, but the current Presenter is
 * retained).
 * Presenters and Views implement interfaces in order to make it explicit what kind of services are
 * expected on each side.
 *
 * Each view is an Activity or a Fragment that is associated with a Presenter-implementation.
 * The view is solely responsible for the GUI, while the presenter provides the view with data to
 * be displayed and coordinates processing of user input collected through the view.
 * Data is temporally stored in model classes in between presentation on the GUI and saving to
 * permanent storage.
 */
public abstract class BasePresenter<V, M> {
    protected M model;
    private WeakReference<V> view;

    public void setModel(M model) {
        resetState();
        this.model = model;
    }

    protected void resetState() {
    }

    public void bindView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    public void unbindView() {
        this.view = null;
    }

    protected V view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    protected boolean setupDone() {
        return view() != null && model != null;
    }

}
