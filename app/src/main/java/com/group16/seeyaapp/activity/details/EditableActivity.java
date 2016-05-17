package com.group16.seeyaapp.activity.details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.helpers.DateHelper;
import com.group16.seeyaapp.model.Activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditableActivity extends AppCompatActivity implements EditableActivityView, AddInvitedListener {

    private EditableActivityPresenterImpl presenter;
    private Activity activity;
    private Spinner spinnerLocations;
    private boolean newActivity;
    private int activityId;
    private Button btnCreate;
    private Button btnPublish;
    private Button btnAddInvitee;
    private TextView tvInvitedList;
    private EditText txtInvitedUser;
    private List<String> lstInvited;

    private TextView tvHeadline;
    private TextView tvMessage;
    private TextView tvAddress;
    private TextView tvTime;

    private TextView tv;
    private LinearLayout createViewContainer;
    private LinearLayout addInviteesContainer;

    //private DatePicker dpResult;
    private Button btnChangeDate;
    private TextView tvDisplayDate;
    private Button btnChangeTime;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable);

        if (savedInstanceState == null) {
            presenter = new EditableActivityPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        btnCreate = (Button)findViewById(R.id.btnCreateActivity);
        btnPublish = (Button)findViewById(R.id.btnPublishActivity);
        btnPublish.setVisibility(View.INVISIBLE);
        btnChangeTime = (Button)findViewById(R.id.btnTimePickerDialog);
        tv = (TextView)findViewById(R.id.txtActivity);
        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);
        tvHeadline = (TextView) findViewById(R.id.txtHeadline);
        tvMessage = (TextView) findViewById(R.id.txtMessage);
        tvAddress = (TextView)findViewById(R.id.txtAddress);
        tvTime = (TextView)findViewById(R.id.tvTime);


        createViewContainer = (LinearLayout)findViewById(R.id.createActivityContainer);
        addInviteesContainer = (LinearLayout)findViewById(R.id.addInviteeContainer);
        btnAddInvitee = (Button)findViewById(R.id.btnAddInviteeDialog);
        txtInvitedUser = (EditText)findViewById(R.id.txtInvitedUserName);
        tvInvitedList = (TextView)findViewById(R.id.tvListOfInvited);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {


            int subCatId = intent.getIntExtra("subCatId", -1);

            if (subCatId != -1) {

                newActivity = true;
                //There is a new activity to be created, set up GUI for that
                btnCreate.setVisibility(View.VISIBLE);
                btnPublish.setVisibility(View.INVISIBLE);
                createViewContainer.setVisibility(View.VISIBLE);
                addInviteesContainer.setVisibility(View.GONE);


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
                    //There is an activity to be displayed set up GUI for that
                    btnCreate.setVisibility(View.INVISIBLE);
                    btnPublish.setVisibility(View.VISIBLE);
                    //createViewContainer.setVisibility(View.GONE);
                    addInviteesContainer.setVisibility(View.VISIBLE);
                    lstInvited = new ArrayList<>();

                    btnPublish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (lstInvited == null || lstInvited.size() < 1) {
                                presenter.onPublishActivity(activity.getId());
                            }
                            else {
                                presenter.onPublishActivity(activity.getId(), lstInvited);
                            }
                        }
                    });

                    btnAddInvitee.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //String usr = txtInvitedUser.getText().toString();
                            //checkIfInvitedUserExists(usr);
                            addInvitedUsers();
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

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // set current date and time into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
        tvTime.setText(hour + ":" + minute + ":00");
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void setLocationList(String[] locations) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(spinnerArrayAdapter);
        Log.i("Editableactivity", "Set location string: " + locations.toString());

    }


    // The view is to display an already created activity
    @Override
    public void displayActivityDetails(Activity activity) {

        this.activity = activity;

        if (activity.getDatePublished() != null) {
            btnPublish.setVisibility(View.INVISIBLE);
            addInviteesContainer.setVisibility(View.GONE);
        }
        else {
            btnPublish.setVisibility(View.VISIBLE);
            addInviteesContainer.setVisibility(View.VISIBLE);
            spinnerLocations.setVisibility(View.GONE);
            btnChangeDate.setVisibility(View.GONE);
            btnChangeTime.setVisibility(View.GONE);
        }

        TextView tvMin = (TextView)findViewById(R.id.txtMinParticipants);
        TextView tvMax = (TextView)findViewById(R.id.txtMaxParticipants);
        TextView tvLocation = (TextView)findViewById(R.id.tvLocationString);
        tvLocation.setVisibility(View.VISIBLE);
        tvLocation.setText(activity.getLocation());

        tvMin.setText(Integer.toString(activity.getMinNbrOfParticipants()));
        tvMax.setText(Integer.toString(activity.getMaxNbrOfParticipants()));
        tvMax.setEnabled(false);
        tvMin.setEnabled(false);

        tvDisplayDate.setText(DateHelper.DateToDateOnlyString(activity.getDate()));

        tvTime.setText(DateHelper.DateToTimeOnlyString(activity.getTime()));


//        ArrayAdapter<String> adapter = (ArrayAdapter<String>)spinnerLocations.getAdapter();
//        int spinnerPosition = adapter.getPosition(activity.getLocation());
//        spinnerLocations.setSelection(spinnerPosition);
//        spinnerLocations.setEnabled(false);

        tvAddress.setText(activity.getAddress() != null ? activity.getAddress() : "");
        tvHeadline.setText(activity.getHeadline());
        tvMessage.setText(activity.getMessage().toString());

        enableEditing(false);
    }

    private void enableEditing(boolean enabledStatus) {
        TextView tvMin = (TextView)findViewById(R.id.txtMinParticipants);
        TextView tvMax = (TextView)findViewById(R.id.txtMaxParticipants);
        TextView tvLocation = (TextView)findViewById(R.id.tvLocationString);

        tvMax.setEnabled(enabledStatus);
        tvMin.setEnabled(enabledStatus);
        tvLocation.setEnabled(enabledStatus);
        tvDisplayDate.setEnabled(enabledStatus);
        tvTime.setEnabled(enabledStatus);
        tvAddress.setEnabled(enabledStatus);
        tvHeadline.setEnabled(enabledStatus);
        tvMessage.setEnabled(enabledStatus);

    }

    @Override
    public void updatePublishedStatus(boolean published) {
        if (published) {
            activity.setDatePublished(new Date());
            tv.setText(activity.toString());
            btnPublish.setVisibility(View.INVISIBLE);
            addInviteesContainer.setVisibility(View.GONE);
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
    public void onUserExistenceChecked(boolean userExists, String username) {
        if (userExists) {
            Toast toast = Toast.makeText(this, "User confirmed", Toast.LENGTH_SHORT);
            toast.show();

            txtInvitedUser.setText("");
            tvInvitedList.setText(tvInvitedList.getText() + username + "; " );
            lstInvited.add(username);
        }
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
            Toast toast = Toast.makeText(this, "Invalid number format: " + e1.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        } catch (ParseException e2) {
            Toast toast = Toast.makeText(this, "Invalid time format: " + e2.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void onTimePickerButtonClicked(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
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

    private void checkIfInvitedUserExists(String userName) {
        if (userName != null) {
            presenter.checkIfUserExists(userName);
        }
    }

    private void addInvitedUsers() {
        DialogFragment invitedFragment = new AddInvitedFragment();
        invitedFragment.show(getFragmentManager(),"Add invited users");

    }

    public void setListOfInvitedUsers(List<String> lstInvited) {
        this.lstInvited = lstInvited;
    }
}
