package com.group16.seeyaapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.main.MainActivity;

/**
 *  Displays a view for the user to register in the application.
 */
public class TestRegisterActivity extends AppCompatActivity implements RegisterView, View.OnClickListener  {
    private RegisterPresenterImpl presenter;
    private EditText username;
    private EditText password;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_register);

        if (savedInstanceState == null) {
            presenter = new RegisterPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        username = (EditText) findViewById(R.id.txtregusername);
        password = (EditText) findViewById(R.id.txtregpassword);
        email = (EditText) findViewById(R.id.txtregemail);
        findViewById(R.id.btnRegister).setOnClickListener(this);
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
     * Event handler for the clicking the register button.
     * @param v
     */
    @Override
    public void onClick(View v) {
        presenter.registerNewUser(username.getText().toString(), email.getText().toString(), password.getText().toString());
    }

    /**
     * Display error related to user name
     * @param errorMessage the message to show
     */
    @Override
    public void showUserNameError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Display error related to password
     * @param errorMessage the message to show
     */
    @Override
    public void showPasswordError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Display error related to email
     * @param errorMessage the message to show
     */
    @Override
    public void showEmailError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Navigate to the home page of the application
     */
    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
