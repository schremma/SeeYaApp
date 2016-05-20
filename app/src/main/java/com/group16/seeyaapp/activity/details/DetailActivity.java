package com.group16.seeyaapp.activity.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.model.Activity;

// Displays an Activity in a non-editable form, i.e. the user can view the activity details and (un)register
public class DetailActivity extends AppCompatActivity implements DetailView {

    private DetailPresenterImpl presenter;
    private int activityId;
    private Button btnSignUp;
    private boolean signedUp;
    private TextView tvNbrAttending;

    private TextView tvHeadline;
    private TextView tvAddress;
    private TextView tvDateLocation;
    private TextView tvNbrOfParticipants;
    private TextView tvActivityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            presenter = new DetailPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            activityId = intent.getIntExtra("activityId", -1);
        }

        signedUp = false;
        btnSignUp = (Button)findViewById(R.id.btnSignUpForActivity);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!signedUp) {
                    presenter.onPressedJoin();
                }
                else {
                    presenter.onPressedUnjoin();
                }
            }
        });

        tvNbrAttending = (TextView)findViewById(R.id.tvNbrAttending);
        tvActivityInfo = (TextView)findViewById(R.id.tvActivityInfo);
        tvHeadline = (TextView) findViewById(R.id.tvHeadline);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tvDateLocation = (TextView)findViewById(R.id.tvDateLocationString);
        tvNbrOfParticipants = (TextView)findViewById(R.id.tvNbrOfParticipants);

        tvActivityInfo.setMovementMethod(new ScrollingMovementMethod());

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

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void displayActivityDetails(Activity activity) {
        tvNbrAttending.setText("Attending: " + activity.getNbrSignedUp());
        updateSignedUpStatus(activity.isAttending());

        tvNbrOfParticipants.setText(activity.participantInfoString());
        tvDateLocation.setText(activity.dateLocationString());
        tvActivityInfo.setText(activity.getMessage());
        tvHeadline.setText(activity.getHeadline());
        tvAddress.setText(activity.getAddress() != null ? "Address: " + activity.getAddress() : "");

    }

    @Override
    public void updateNbrAttending(int attending) {
        tvNbrAttending.setText("Attending: " + attending);
    }

    @Override
    public void updateSignedUpStatus(boolean signedUp) {
        if (signedUp) {
            this.signedUp = true;
            btnSignUp.setText("Unregister");
        }
        else {
            this.signedUp = false;
            btnSignUp.setText("Sign up");
        }
    }

    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}