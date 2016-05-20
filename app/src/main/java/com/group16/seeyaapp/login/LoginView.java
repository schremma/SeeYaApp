package com.group16.seeyaapp.login;

/**
 * Created by Andrea on 10/04/16.
 * Displays a view for the user to be able to log into the application
 */
public interface LoginView {

    // Display visual indication that the log in is in progress
    void showLoading();

    // Stop displaying visual indication that the log in is in progress
    void hideLoading();

    // Show the provided error message
    void setError(String errorMessage);

    // Navigate to the home page of the application
    void navigateToHome();
}
