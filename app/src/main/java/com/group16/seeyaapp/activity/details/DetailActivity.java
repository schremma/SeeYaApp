package com.group16.seeyaapp.activity.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.list.mainlist.TestMainListActivity;
import com.group16.seeyaapp.model.Activity;
import com.group16.seeyaapp.navigation.DemoPage;
import com.group16.seeyaapp.navigation.TestCreatePage;

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

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.seeyalogo_smaller);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        toolbar = null;
        finish();
        System.gc();
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

        if (!activity.isAttending() && activity.expired()) {
            btnSignUp.setEnabled(false);
            btnSignUp.setText("Date passed");
        }

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