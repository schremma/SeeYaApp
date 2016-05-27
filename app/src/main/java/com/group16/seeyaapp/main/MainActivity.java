package com.group16.seeyaapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.group16.seeyaapp.R;
import com.group16.seeyaapp.login.LoginActivity;
import com.group16.seeyaapp.register.RegisterActivity;

/**
 * Displays the main page of the application currently providing options for navigating to view
 * for registering a new user in the application or for logging in an as existing user.
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(v);
            }
        });

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(v);
            }
        });
    }

    public void signIn(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    public void signUp(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
