package com.group16.seeyaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.model.Activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestNewActivity extends AppCompatActivity implements ActivityView {

    private ActivityPresenterImpl presenter;
    private Activity activity;

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

        findViewById(R.id.btnCreateActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setLocation("Malmö");
                activity.setHeadline("A new activity");
                activity.setMessage("This is the message");
                activity.setLocationDetails("Street 23A");
                activity.setMinNbrOfParticipants(4);
                activity.setMaxNbrOfParticipants(12);

                try {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new GregorianCalendar(2016, 04, 30).getTime();
                    Date dateWithZeroTime = formatter.parse(formatter.format(date));
                    activity.setDate(dateWithZeroTime);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 10);
                    Date time = cal.getTime();

                    formatter = new SimpleDateFormat("hh:mm:ss");
                    Date timeWithZeroDate = formatter.parse(formatter.format(time));
                    activity.setTime(timeWithZeroDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

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
