package com.group16.seeyaapp.login;

/**
 * Created by Andrea on 10/04/16.
 */
public interface LoginView {
    void showLoading();

    void hideLoading();

    void setError(String errorMessage);

    void navigateToHome();
}
