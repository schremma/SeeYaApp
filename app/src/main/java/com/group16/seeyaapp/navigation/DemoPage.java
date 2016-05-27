package com.group16.seeyaapp.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.list.mainlist.MainListActivity;
import com.group16.seeyaapp.main.MainActivity;

/**
 * Activity for navigating to a set of different pages.
 */
public class DemoPage extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_page);

        toolbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.seeyalogo_smaller);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startMain();
            }
        });


        findViewById(R.id.btnGoToCreatePage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreatePage();
            }
        });


        findViewById(R.id.btnGoToBrowseActivities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBrowseByCategoryPage();
            }
        });
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.toolbarhome) {

        } else if(id == R.id.toolbarsettings) {

        } else if(id == R.id.toolbarinfo) {

        } else if(id == R.id.toolbaradd) {
            Intent intent = new Intent(this, CreatePage.class);
            startActivity(intent);
        } else if(id == R.id.toolbarbrowse) {
            Intent intent = new Intent(this, MainListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToCreatePage() {
        Intent intent = new Intent(this, CreatePage.class);
        startActivity(intent);
    }

    private void goToBrowseByCategoryPage() {
        Intent intent = new Intent(this, MainListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}