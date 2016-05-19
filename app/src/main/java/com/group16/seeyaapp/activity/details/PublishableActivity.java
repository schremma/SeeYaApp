package com.group16.seeyaapp.activity.details;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.model.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishable);

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.setInvitedList(lstInvited);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }



    // The view is to display an already created activity
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


    @Override
    public void updatePublishedStatus(boolean published) {
        if (published) {
            activity.setDatePublished(new Date());
            setGUIForPublishedStatus(true);
            Toast toast = Toast.makeText(this, "Activity has been published!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

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

    public void setListOfInvitedUsers(ArrayList<String> lstInvited) {
        this.lstInvited = lstInvited;
        displayListOfInvited();
    }

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
}
