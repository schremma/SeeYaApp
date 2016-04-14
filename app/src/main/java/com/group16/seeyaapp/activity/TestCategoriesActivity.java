package com.group16.seeyaapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;

public class TestCategoriesActivity extends AppCompatActivity implements CategoryView {
    CategoryPresenterImpl presenter;
    Spinner spinnerMain;
    Spinner spinnerSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_categories);

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
    public void setMainCategories(String[] mainCategories) {
        Toast toast = Toast.makeText(this, mainCategories.toString(), Toast.LENGTH_SHORT);
        toast.show();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainCategories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMain.setAdapter(spinnerArrayAdapter);

    }

    @Override
    public void setSubcategories(String[] subCategories) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCategories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSub.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void navigateToCreateActivityDetails(int subCategoryId) {

    }

    @Override
    public void showError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
