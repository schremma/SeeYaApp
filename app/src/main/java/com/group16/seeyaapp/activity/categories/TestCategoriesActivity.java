package com.group16.seeyaapp.activity.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
//import android.R;
import com.group16.seeyaapp.activity.details.EditableActivity;
import com.group16.seeyaapp.activity.list.mainlist.TestMainListActivity;
import com.group16.seeyaapp.navigation.DemoPage;
import com.group16.seeyaapp.navigation.TestCreatePage;

public class TestCategoriesActivity extends AppCompatActivity implements CategoryView {
    private CategoryPresenterImpl presenter;
    private Spinner spinnerMain;
    private Spinner spinnerSub;
    private Toolbar toolbar;

    private static final String SUBCATID = "subCatId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_categories);

        toolbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.seeyalogo_smaller);

        if (savedInstanceState == null) {
            presenter = new CategoryPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        spinnerMain = (Spinner)findViewById(R.id.spinnerMain);
        spinnerMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.mainCategorySelected(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSub = (Spinner)findViewById(R.id.spinnerSub);

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.pressedNext(spinnerSub.getSelectedItem().toString());

            }
        });
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
            Intent intent = new Intent(this, DemoPage.class);
            startActivity(intent);
        } else if(id ==R.id.toolbarsettings) {

        } else if(id == R.id.toolbarinfo) {

        } else if (id == R.id.toolbarbrowse) {
            Intent intent = new Intent(this, TestMainListActivity.class);
            startActivity(intent);
        } else if (id == R.id.toolbaradd) {
            Intent intent = new Intent(this, TestCreatePage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Menu menu = toolbar.getMenu();
        menu.clear();
        menu = null;
        toolbar = null;
        System.gc();
        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void setMainCategories(String[] mainCategories) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_item, mainCategories);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerMain.setAdapter(spinnerArrayAdapter);

    }

    @Override
    public void setSubcategories(String[] subCategories) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_sub, subCategories);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_sub_dropdown);
        spinnerSub.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void navigateToCreateActivityDetails(int subCategoryId) {
        Intent intent = new Intent(this, EditableActivity.class);
        intent.putExtra(SUBCATID, subCategoryId);
        startActivity(intent);
    }

    @Override
    public void showError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}