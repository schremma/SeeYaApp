package com.group16.seeyaapp.activity.details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.model.Activity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TestNewActivity extends AppCompatActivity implements ActivityView {

    private ActivityPresenterImpl presenter;
    private Activity activity;
    private Spinner spinnerLocations;
    private boolean newActivity;
    private int activityId;
    private Button btnCreate;
    private Button btnPublish;
    private TextView tv;
    private LinearLayout createViewContainer;

    //private DatePicker dpResult;
    private Button btnChangeDate;
    private TextView tvDisplayDate;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

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
        createViewContainer = (LinearLayout)findViewById(R.id.createActivityContainer);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {


            int subCatId = intent.getIntExtra("subCatId", -1);

            if (subCatId != -1) {

                newActivity = true;
                //There is a new activity to be created, set up GUI for that
                btnCreate.setVisibility(View.VISIBLE);
                btnPublish.setVisibility(View.INVISIBLE);
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


//                //This is just for testing
//                activity.setLocation("Malmö");
//                activity.setHeadline("Another test activity");
//                activity.setMessage("Message here");
//                activity.setLocationDetails("Street 23A");
//                activity.setMinNbrOfParticipants(4);
//                activity.setMaxNbrOfParticipants(12);
//                Date date = new GregorianCalendar(2016, 04, 30).getTime();
//                activity.setDate(date);
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.HOUR_OF_DAY, 10);
//                Date time = cal.getTime();
//                activity.setTime(time);
//                //tv.setText(activity.toString());

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
                    //There is an activity to be displayed set up GUI for that
                    btnCreate.setVisibility(View.INVISIBLE);
                    btnPublish.setVisibility(View.VISIBLE);
                    createViewContainer.setVisibility(View.GONE);

                    btnPublish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.onPublishActivity(activity.getId());
                        }
                    });

                }
            }
        }

        //dpResult = (DatePicker) findViewById(R.id.datePicker);
        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        tvDisplayDate = (TextView) findViewById(R.id.tvDate);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
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


    private void createNewActivity() {

        TextView tvMin = (TextView)findViewById(R.id.txtMinParticipants);
        TextView tvMax = (TextView)findViewById(R.id.txtMaxParticipants);
        int min = 0;
        int max = 0;

        try {
            min = Integer.parseInt(tvMin.getText().toString());
            max = Integer.parseInt(tvMax.getText().toString());


            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);

            Date date = cal.getTime();

            activity.setDate(date);

            TextView tvTime = (TextView)findViewById(R.id.txtTime);
            activity.setTime(DateHelper.StringTimeToDate(tvTime.getText().toString()));
            String location = spinnerLocations.getSelectedItem().toString();
            TextView tvHeadline = (TextView) findViewById(R.id.txtHeadline);
            TextView tvMessage = (TextView) findViewById(R.id.txtMessage);

            activity.setLocation(location);
            activity.setHeadline(tvHeadline.getText().toString());
            activity.setMessage(tvMessage.getText().toString());

            activity.setMinNbrOfParticipants(min);
            activity.setMaxNbrOfParticipants(max);

            presenter.onCreateActivity(activity);

        } catch(NumberFormatException e1) {
            Toast toast = Toast.makeText(this, "Invalid number format: " + e1.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        } catch (ParseException e2) {
            Toast toast = Toast.makeText(this, "Invalid time format: " + e2.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;


            //set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };
}
