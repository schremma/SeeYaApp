package com.group16.seeyaapp.activity;

import android.content.Intent;
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
    private CategoryPresenterImpl presenter;
    private Spinner spinnerMain;
    private Spinner spinnerSub;

    private static final String SUBCATID = "subCatId";

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

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.pressedNext(spinnerSub.getSelectedItem().toString());

            }
        });
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
        Intent intent = new Intent(this, TestNewActivity.class);
        intent.putExtra(SUBCATID, subCategoryId);
        startActivity(intent);
    }

    @Override
    public void showError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
