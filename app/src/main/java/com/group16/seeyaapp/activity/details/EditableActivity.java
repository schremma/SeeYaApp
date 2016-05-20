package com.group16.seeyaapp.activity.details;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.list.mainlist.TestMainListActivity;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.main.MainActivity;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.navigation.DemoPage;
import com.group16.seeyaapp.navigation.TestCreatePage;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditableActivity extends AppCompatActivity implements EditableActivityView, DateTimeDialogListener {

    private EditableActivityPresenterImpl presenter;
    private Activity activity;
    private Spinner spinnerLocations;
    private int activityId;
    private Button btnCreate;
    private boolean newActivity;

    private TextView tvHeadline;
    private TextView tvMessage;
    private TextView tvAddress;
    private TextView tvTime;

    private LinearLayout createViewContainer;

    private TextView tvDisplayDate;
    private int year;
    private int month;
    private int day;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable);

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

        if (savedInstanceState == null) {
            presenter = new EditableActivityPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        btnCreate = (Button)findViewById(R.id.btnCreateActivity);
        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);
        tvHeadline = (TextView) findViewById(R.id.txtHeadline);
        tvMessage = (TextView) findViewById(R.id.txtMessage);
        tvAddress = (TextView)findViewById(R.id.txtAddress);
        tvTime = (TextView)findViewById(R.id.tvTime);


        createViewContainer = (LinearLayout)findViewById(R.id.createActivityContainer);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {


            int subCatId = intent.getIntExtra("subCatId", -1);

            if (subCatId != -1) {
                newActivity = true;
                //There is a new activity to be created, set up GUI for that
                btnCreate.setVisibility(View.VISIBLE);
                createViewContainer.setVisibility(View.VISIBLE);


                activity = new Activity();
                activity.setSubcategory(subCatId);

                spinnerLocations.setVisibility(View.VISIBLE);

                spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedLocation = parent.getItemAtPosition(position).toString();
                        activity.setLocation(selectedLocation);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                findViewById(R.id.btnCreateActivity).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        createNewActivity();

                    }
                });
            }
            else {
                activityId = intent.getIntExtra("activityId", -1);
                newActivity = false;
                if (activityId != -1) {
                    // TODO implement one day
                    //There is an activity to be displayed for editing set up GUI for that
                    btnCreate.setVisibility(View.INVISIBLE);

                }
            }
        }

        tvDisplayDate = (TextView) findViewById(R.id.tvDate);

        findViewById(R.id.btnTimePickerDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimePickerButtonClicked(v);
            }
        });
        findViewById(R.id.btnChangeDate).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onDatePickerButtonClicked(v);
            }

        });

        setDefaultDate();
        setDefaultTime();

    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Sets current date to be shown
    private void setDefaultDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        tvDisplayDate.setText(DateHelper.FormatDate(year, month, day));

    }
    //Sets current time to be shown
    private void setDefaultTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        tvTime.setText(DateHelper.FormatTime(hour, minute, 0));
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
        toolbar = null;
        finish();
        System.gc();
        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getActivityInput();
        presenter.onSetActivity(activity);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    /**
     * Gets the activity input from GUI and stores it in the Activity object,
     * without saving in into database.
     */
    private void getActivityInput() {
        TextView tvMin = (TextView)findViewById(R.id.txtMinParticipants);
        TextView tvMax = (TextView)findViewById(R.id.txtMaxParticipants);
        int min = 0;
        int max = 0; // 0 for max means unlimited

        try {
            if (tvMin.getText().length() > 0) {
                min = Integer.parseInt(tvMin.getText().toString());
            }
            if (tvMax.getText().length() > 0) {
                max = Integer.parseInt(tvMax.getText().toString());
            }
        } catch(NumberFormatException e1) {}

        Date activityDate = new Date();
        Date activityTime = new Date();

        try {
            activityDate = DateHelper.StringDateToDate(tvDisplayDate.getText().toString());
            activityTime = DateHelper.StringTimeToDate(tvTime.getText().toString());
        } catch (ParseException e) {}

        activity.setDate(activityDate);
        activity.setTime(activityTime);
        String location = spinnerLocations.getSelectedItem().toString();

        activity.setLocation(location);
        activity.setAddress(tvAddress.getText().toString());
        activity.setHeadline(tvHeadline.getText().toString());
        activity.setMessage(tvMessage.getText().toString());

        activity.setMinNbrOfParticipants(min);
        activity.setMaxNbrOfParticipants(max);

    }

    @Override
    public void setLocationList(String[] locations) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_item, locations);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(spinnerArrayAdapter);

        // set a preselected location from activity if there is any
        if (activity.getLocation() !=null) {

            int locationIndex = spinnerArrayAdapter.getPosition(activity.getLocation());
            if (locationIndex >= 0) {
                spinnerLocations.setSelection(locationIndex);
            }
        }
    }

    // The view is to display an already created activity for editing
    // Or a not yet saved activity, if a configiration change took place while entering input
    @Override
    public void displayActivityDetails(Activity activity) {
        this.activity = activity;

        TextView tvMin = (TextView)findViewById(R.id.txtMinParticipants);
        TextView tvMax = (TextView)findViewById(R.id.txtMaxParticipants);

        tvMin.setText(Integer.toString(activity.getMinNbrOfParticipants()));
        tvMax.setText(Integer.toString(activity.getMaxNbrOfParticipants()));
        tvDisplayDate.setText(DateHelper.DateToDateOnlyString(activity.getDate()));
        tvTime.setText(DateHelper.DateToTimeOnlyString(activity.getTime()));
        tvAddress.setText(activity.getAddress() != null ? activity.getAddress() : "");
        tvHeadline.setText(activity.getHeadline() != null ? activity.getHeadline() : "");
        tvMessage.setText(activity.getMessage() != null ? activity.getMessage() : "");
    }


    @Override
    public void updateStatus(boolean changesSaved) {
        if (changesSaved) {
            // TODO implement one day
            // displaying changed activity after editing
        }
    }

    @Override
    public void updateCreateStatus(boolean created) {
        if (created) {
            btnCreate.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(this, "Activity has been created!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void navigateToBrowseActivities() {
        Intent intent = new Intent(this, TestMainListActivity.class);
        startActivity(intent);
    }


    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }


    private void createNewActivity() {

        TextView tvMin = (TextView)findViewById(R.id.txtMinParticipants);
        TextView tvMax = (TextView)findViewById(R.id.txtMaxParticipants);
        int min = 0;
        int max = 0; // 0 for max means unlimited

        try {
            if (tvMin.getText().length() > 0){
                min = Integer.parseInt(tvMin.getText().toString());
            }
            if (tvMax.getText().length() > 0) {
                max = Integer.parseInt(tvMax.getText().toString());
            }


            activity.setDate(DateHelper.StringDateToDate(tvDisplayDate.getText().toString()));
            activity.setTime(DateHelper.StringTimeToDate(tvTime.getText().toString()));
            String location = spinnerLocations.getSelectedItem().toString();

            activity.setLocation(location);
            activity.setAddress(tvAddress.getText().toString());
            activity.setHeadline(tvHeadline.getText().toString());
            activity.setMessage(tvMessage.getText().toString());

            activity.setMinNbrOfParticipants(min);
            activity.setMaxNbrOfParticipants(max);

            presenter.onCreateActivity(activity);

        } catch(NumberFormatException e1) {
            Toast toast = Toast.makeText(this, "Invalid number format for participant limit(s): " + e1.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        } catch (ParseException e2) {
            Toast toast = Toast.makeText(this, "Invalid time format: " + e2.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onDatePickerButtonClicked(View v) {
        DialogFragment picker = new DatePickerFragment();
        picker.show(getFragmentManager(), "datePicker");
    }

    public void onTimePickerButtonClicked(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }


    @Override
    public void onDateSelected(int year, int month, int day) {
        tvDisplayDate.setText(DateHelper.FormatDate(year, month, day));
    }

    @Override
    public void onTimeSelected(int hour, int minute, int second) {
        TextView tv = (TextView)findViewById(R.id.tvTime);
        tv.setText(DateHelper.FormatTime(hour, minute, 0));
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
        } else if(id == R.id.toolbarsettings) {

        } else if(id == R.id.toolbarinfo) {

        } else if(id == R.id.toolbaradd) {
            Intent intent = new Intent(this, TestCreatePage.class);
            startActivity(intent);
        } else if(id == R.id.toolbarbrowse) {
            Intent intent = new Intent(this, TestMainListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
