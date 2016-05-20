package com.group16.seeyaapp.register;

/**
 * Created by Andrea on 10/04/16.
 * Presenter that handles the logic behind registering a new user in the app.
 */
public interface RegisterPresenter {

    // Register new user with the provided credentials
    void registerNewUser(String username, String email, String password);
}
