package com.group16.seeyaapp.register;

/**
 * Created by Andrea on 10/04/16.
 * Displays a view for the user to register in the application
 */
public interface RegisterView {

    // Display error related to user name
    void showUserNameError(String errorMessage);

    // Display error related to password
    void showPasswordError(String errorMessage);

    // Display error related to email
    void showEmailError(String errorMessage);

    // Navigate to the home page of the application
    void navigateToHome();
}
