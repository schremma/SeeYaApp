package com.group16.seeyaapp.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.categories.TestCategoriesActivity;

public class TestCreatePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_create_page);

        findViewById(R.id.btnGoToCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateActivity();
            }
        });
    }

    private void goToCreateActivity() {
        Intent intent = new Intent(this, TestCategoriesActivity.class);
        startActivity(intent);
    }
}
