package com.group16.seeyaapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.navigation.DemoPage;

/**
 * Displays a view for the user to be able to log into the application.
 */
public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private ProgressBar progressBar;
    private EditText username;
    private EditText password;
    private LoginPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);

        if (savedInstanceState == null) {
            presenter = new LoginPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        progressBar = (ProgressBar) findViewById(R.id.barprogress);
        username = (EditText) findViewById(R.id.txtusername);
        password = (EditText) findViewById(R.id.txtpassword);
        findViewById(R.id.btnLogin).setOnClickListener(this);

    }

    /**
     * Display visual indication that the log in is in progress
     */
    @Override
    public void showLoading()  {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Stop displaying visual indication that the log in is in progress
     */
    @Override
    public void hideLoading()  {
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Show the provided error message
     * @param errorMessage
     */
    @Override
    public void setError(String errorMessage) {

        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    /**
     * Navigate to the home page of the application.
     */
    public void navigateToHome() {

        // TODO go to a home page instead
        Intent intent = new Intent(this, DemoPage.class);
        startActivity(intent);

        finish();
    }

    /**
     * Event handler for clicking the log-in button.
     * @param v
     */
    @Override
    public void onClick(View v) {
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }
}
