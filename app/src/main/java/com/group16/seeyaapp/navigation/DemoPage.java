package com.group16.seeyaapp.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.TestListActivity;

public class DemoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_page);

        findViewById(R.id.btnGoToCreatePage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreatePage();
            }
        });

        findViewById(R.id.btnGoToOwnActivitiesList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOwnActivitiesList();
            }
        });
    }

    private void goToCreatePage() {
        Intent intent = new Intent(this, TestCreatePage.class);
        startActivity(intent);
    }

    private void goToOwnActivitiesList() {
        Intent intent = new Intent(this, TestListActivity.class);
        startActivity(intent);
    }
}
