package com.group16.seeyaapp.activity.details;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.list.mainlist.MainListActivity;
import com.group16.seeyaapp.main.MainActivity;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.navigation.DemoPage;
import com.group16.seeyaapp.navigation.CreatePage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Displays an already created activity that has not been published yet.
 * Shows controls for initiating adding a list of users to be invited to the activity through an
 * AddInvitedFragment and control for publishing the activity.
 */
public class PublishableActivity extends AppCompatActivity implements PublishableActivityView, AddInvitedListener {

    private PublishableActivityPresenterImpl presenter;
    private Activity activity;
    private int activityId;

    private LinearLayout addInvitedContainer;
    private Button btnPublish;
    private Button btnAddInvitee;
    private TextView tvInvitedList;
    private ArrayList<String> lstInvited;
    private TextView tvHeadline;
    private TextView tvAddress;
    private TextView tvDateLocation;
    private TextView tvNbrOfParticipants;
    private TextView tvActivityInfo;
    private TextView tvPublishedStatus;
    private TextView lblInvited;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishable);

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
            presenter = new PublishableActivityPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        addInvitedContainer = (LinearLayout)findViewById(R.id.containerAddInvitee);
        btnPublish = (Button)findViewById(R.id.btnPublishActivity);
        btnPublish.setVisibility(View.INVISIBLE);
        tvActivityInfo = (TextView)findViewById(R.id.tvActivityInfo);
        tvHeadline = (TextView) findViewById(R.id.tvHeadline);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tvDateLocation = (TextView)findViewById(R.id.tvDateLocationString);
        tvNbrOfParticipants = (TextView)findViewById(R.id.tvNbrOfParticipants);
        btnAddInvitee = (Button)findViewById(R.id.btnAddInviteeDialog);
        tvInvitedList = (TextView)findViewById(R.id.tvListOfInvited);
        tvPublishedStatus = (TextView)findViewById(R.id.tvPublishedStatus);
        lblInvited = (TextView)findViewById(R.id.lblListOfInvited);

        tvActivityInfo.setMovementMethod(new ScrollingMovementMethod());
        lblInvited.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();


        // Gets the id of the activity to be displayed later on
        if (intent.getExtras() != null) {

                activityId = intent.getIntExtra("activityId", -1);
                if (activityId != -1) {

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
                            addInvitedUsers();
                        }
                    });

                }
            }
    }

    /**
     * Starts the main page of the application.
     */
    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
        presenter.aboutToDisplayActivity(activityId);


    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
        //toolbar = null;
        //finish();
        System.gc();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        // Saves the current list of invited users in the presenter
        presenter.setInvitedList(lstInvited);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }


    /**
     * Displays information stored in the provided Activity.
     * It determines based on the value of datePublished if the GUI is tp be set up
     * for publishing the activity or not.
     * @param activity The activity information to be displayed.
     */
    @Override
    public void displayActivityDetails(Activity activity) {

        this.activity = activity;

        if (activity.getDatePublished() != null) {
            setGUIForPublishedStatus(true);
        }
        else {
            setGUIForPublishedStatus(false);
        }

        tvNbrOfParticipants.setText(activity.participantInfoString());
        tvDateLocation.setText(activity.dateLocationString());
        tvActivityInfo.setText(activity.getMessage());
        tvHeadline.setText(activity.getHeadline());
        tvAddress.setText(activity.getAddress() != null ? "Address: " + activity.getAddress() : "");
    }

    /**
     * Changes the GUI so that the user can publish or not the activity
     * @param published True if the GUI is to be set up for publishing
     */
    private void setGUIForPublishedStatus(boolean published) {
        if (published) {
            btnPublish.setVisibility(View.INVISIBLE);
            addInvitedContainer.setVisibility(View.INVISIBLE);
            tvPublishedStatus.setText("Published");
        }
        else {
            btnPublish.setVisibility(View.VISIBLE);
            addInvitedContainer.setVisibility(View.VISIBLE);
            tvPublishedStatus.setText("Unpublished");
        }
    }

    /**
     * Changes the GUI as the published status of the activity has been changed.
     * @param published True if the activity has just been published.
     */
    @Override
    public void updatePublishedStatus(boolean published) {
        if (published) {
            activity.setDatePublished(new Date());
            setGUIForPublishedStatus(true);
            Toast toast = Toast.makeText(this, "Activity has been published!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Displays error message as a Toast
     * @param errorMessage the error message to show
     */
    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * PublishableActivityView implementation.
     * Copies the content of the provided list of invited user onto its own local list
     * and initiates changing display on the GUI accordingly.
     * @param invitedUserList List of invited users
     */
    @Override
    public void setInvitedUserList(List<String> invitedUserList) {
        lstInvited = new ArrayList<>();
        for(String i : invitedUserList) {
            lstInvited.add(i);
        }
        displayListOfInvited();
    }


    private void addInvitedUsers() {
        DialogFragment invitedFragment = AddInvitedFragment.newInstance(lstInvited);

        invitedFragment.show(getFragmentManager(),"Add invited users");

    }

    /**
     * AddInvitedListener implementation.
     * Changes the currently stored and displayed list of invited users to the provided list.
     * @param lstInvited List of invited users
     */
    @Override
    public void setListOfInvitedUsers(ArrayList<String> lstInvited) {
        this.lstInvited = lstInvited;
        displayListOfInvited();
    }

    /**
     * Displayes the list of invited users on the GUI
     */
    private void displayListOfInvited() {
        if (lstInvited != null) {
            String invitedStr = TextUtils.join(", ", lstInvited);
            lblInvited.setVisibility(View.VISIBLE);
            tvInvitedList.setText(invitedStr);
        }
        else {
            lblInvited.setVisibility(View.INVISIBLE);
        }
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
            Intent intent = new Intent(this, CreatePage.class);
            startActivity(intent);
        } else if(id == R.id.toolbarbrowse) {
            Intent intent = new Intent(this, MainListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
