package com.group16.seeyaapp.login;

/**
 * Created by Andrea on 10/04/16.
 * Presenter that handles the logic behind logging into the application.
 */
public interface LoginPresenter {

    // Check credentials with the server
    void validateCredentials(String username, String password);

}
