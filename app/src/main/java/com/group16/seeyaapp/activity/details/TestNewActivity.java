package com.group16.seeyaapp.activity.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
    private boolean newActivity;
    private int activityId;
    private Button btnCreate;
    private Button btnPublish;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new);

        if (savedInstanceState == null) {
            presenter = new ActivityPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        btnCreate = (Button)findViewById(R.id.btnCreateActivity);
        btnPublish = (Button)findViewById(R.id.btnPublishActivity);
        tv = (TextView)findViewById(R.id.txtActivity);
        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {


            int subCatId = intent.getIntExtra("subCatId", -1);

            if (subCatId != -1) {

                newActivity = true;
                //There is a new activity to be created, set up GUI for that
                btnCreate.setVisibility(View.VISIBLE);
                btnPublish.setVisibility(View.INVISIBLE);

                activity = new Activity();
                activity.setSubcategory(subCatId);

                spinnerLocations.setVisibility(View.VISIBLE);

                spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedLocation = parent.getItemAtPosition(position).toString();
                        activity.setLocation(selectedLocation);
                        tv.setText(activity.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });


                //This is just for testing
                activity.setLocation("Malmö");
                activity.setHeadline("Another test activity");
                activity.setMessage("Message here");
                activity.setLocationDetails("Street 23A");
                activity.setMinNbrOfParticipants(4);
                activity.setMaxNbrOfParticipants(12);
                Date date = new GregorianCalendar(2016, 04, 30).getTime();
                activity.setDate(date);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 10);
                Date time = cal.getTime();
                activity.setTime(time);
                tv.setText(activity.toString());

                findViewById(R.id.btnCreateActivity).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        presenter.onCreateActivity(activity);

                    }
                });
            }
            else {
                activityId = intent.getIntExtra("activityId", -1);
                newActivity = false;
                if (activityId != -1) {
                    //There is an activity to be displayed set up GUI for that
                    btnCreate.setVisibility(View.INVISIBLE);
                    btnPublish.setVisibility(View.VISIBLE);

                    btnPublish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.onPublishActivity(activity.getId());
                        }
                    });

                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);

        if (newActivity)
            presenter.aboutToCreateActivity();
        else
            presenter.aboutToDisplayActivity(activityId);

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


    // The view is to display an already created activity
    @Override
    public void displayActivityDetails(Activity activity) {

        this.activity = activity;
        spinnerLocations.setVisibility(View.INVISIBLE);

        if (activity.getDatePublished() != null) {
            btnPublish.setVisibility(View.INVISIBLE);
        }

        tv.setText(activity.toString());

    }

    @Override
    public void updatePublishedStatus(boolean published) {
        if (published) {
            activity.setDatePublished(new Date());
            tv.setText(activity.toString());
            btnPublish.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(this, "Activity has been published!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void updateCreateStatus(boolean created) {
        if (created) {
            btnCreate.setVisibility(View.INVISIBLE);
        }

        Toast toast = Toast.makeText(this, "Activity has been created!", Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
