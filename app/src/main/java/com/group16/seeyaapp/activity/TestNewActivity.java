package com.group16.seeyaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.model.Activity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestNewActivity extends AppCompatActivity implements ActivityView {

    private ActivityPresenterImpl presenter;
    private Activity activity;
    private Spinner spinnerLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new);

        if (savedInstanceState == null) {
            presenter = new ActivityPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        Intent intent = getIntent();
        int subCatId = intent.getIntExtra("subCatId", -1);
        activity = new Activity();
        activity.setSubcategory(subCatId);


        spinnerLocations = (Spinner)findViewById(R.id.spinnerMain);

        findViewById(R.id.btnCreateActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setLocation("Malmö");
                activity.setHeadline("A new activity");
                activity.setMessage("This is the message");
                activity.setLocationDetails("Street 23A");
                activity.setMinNbrOfParticipants(4);
                activity.setMaxNbrOfParticipants(12);

                Date date = new GregorianCalendar(2016, 04, 30).getTime();
                activity.setDate(date);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 10);
                    Date time = cal.getTime();


                    activity.setTime(time);


                presenter.onCreate(activity);

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
    public void setLocationList(String[] locations) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(spinnerArrayAdapter);

    }

    @Override
    public void displayActivityDetails(Activity activity) {

    }

    @Override
    public void showOnSuccess(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
