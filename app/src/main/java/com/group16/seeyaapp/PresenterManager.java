package com.group16.seeyaapp;

import android.os.Bundle;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Andrea on 09/04/16.
 * Based on code sample: https://github.com/remind101/android-arch-sample
 *
 * The PresenterManager handles associating views with their Presenters (each inheriting BasePresenter)
 * across configuration changes. It stores presenters passed into the savePresenter() method,
 * storing the key identifying that presenter into the savedInstanceState Bundle of the invoking
 * view. If, onCreate(), the view finds that it has a key in its savedInstanceState,
 * it can retrieve its original presenter through invoking restorePresenter().
 * PresenterManager is a singleton and reference to it can be obtained through the
 * getInstance() method.
 */
public class PresenterManager {
    private static final String SIS_KEY_PRESENTER_ID = "presenter_id";
    private static PresenterManager instance;

    private final AtomicLong currentId;

    private final Cache<Long, BasePresenter<?, ?>> presenters;

    PresenterManager(long maxSize, long expirationValue, TimeUnit expirationUnit) {
        currentId = new AtomicLong();

        presenters = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expirationValue, expirationUnit)
                .build();
    }

    public static PresenterManager getInstance() {
        if (instance == null) {
            instance = new PresenterManager(10, 30, TimeUnit.SECONDS);
        }
        return instance;
    }

    /**
     * Retrieves and returns a stored presenter using the key in the provided Bundle.
     * @param savedInstanceState The Bundle with key for the stored presenter
     * @return Presenter instance that has been stored
     */
    public <P extends BasePresenter<?, ?>> P restorePresenter(Bundle savedInstanceState) {
        Long presenterId = savedInstanceState.getLong(SIS_KEY_PRESENTER_ID);
        P presenter = (P) presenters.getIfPresent(presenterId);
        presenters.invalidate(presenterId);
        return presenter;
    }

    /**
     * Saves the provided presenter for later use, and
     * stores the key identifying that presenter into the passed in Bundle.
     * @param presenter The presenter instance to save
     * @param outState The Bundle to store the id for later identification of the presenter
     */
    public void savePresenter(BasePresenter<?, ?> presenter, Bundle outState) {
        long presenterId = currentId.incrementAndGet();
        presenters.put(presenterId, presenter);
        outState.putLong(SIS_KEY_PRESENTER_ID, presenterId);
    }
}
