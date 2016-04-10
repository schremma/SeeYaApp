package com.group16.seeyaapp.register;

/**
 * Created by Andrea on 10/04/16.
 */
public interface RegisterView {

    void showUserNameError(String errorMessage);

    void showPasswordError(String errorMessage);

    void showEmailError(String errorMessage);

    void navigateToHome();
}
