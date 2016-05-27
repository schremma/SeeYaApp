package com.group16.seeyaapp.activity.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.details.DetailActivity;
import com.group16.seeyaapp.activity.details.PublishableActivity;
import com.group16.seeyaapp.activity.list.mainlist.MainListActivity;
import com.group16.seeyaapp.navigation.DemoPage;
import com.group16.seeyaapp.navigation.CreatePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//<<<<<<< Updated upstream
//=======
//>>>>>>> Stashed changes

/**
 * Displays a list showing short information, referred to as headlines, about activities.
 * The user may click on one of these headlines to be navigated to the next view
 * that displays detailed information about the activity.
 * The instance variable listFilter keeps track of what kind of activity list is displayed, e.g
 * own activities or invited to activities. Based on the value of the filter, different views might
 * be displayed when the user selects a list item.
 */
public class HeadlineListActivity extends AppCompatActivity implements ActivityListView {

    private ActivityListPresenterImpl presenter;
    private ListView listview;
    private HashMap map = new HashMap<String, Integer>();

    private Filter listFilter;
    private int groupId;
    private String headlines;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_headline_list);

        toolbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.seeyalogo_smaller);

        if (savedInstanceState == null) {
            presenter = new ActivityListPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }


        listview = (ListView) findViewById(R.id.lstActivities);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                int idInt = (int) map.get(item);

                presenter.onActivitySelected(idInt);
            }

        });


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.hasExtra("listFilter")) {
                listFilter = (Filter) intent.getSerializableExtra("listFilter");

                Log.i("HeadlineListActivity", "Got extra: " + listFilter.name());
            }

            if (intent.hasExtra("headlines")) {
                headlines = intent.getStringExtra("headlines");

                Log.i("HeadlineListActivity", "Got extra: " + headlines);

            } else if (intent.hasExtra("groupId")) { // TODO remove this?

                groupId = intent.getIntExtra("groupId", -1);
                Log.i("HeadlineListActivity", "Got extras: " + groupId + ", " + listFilter.name());

            }
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

        } else if(id == R.id.toolbarbrowse) {
            Intent intent = new Intent(this, MainListActivity.class);
            startActivity(intent);
        } else if(id == R.id.toolbaradd) {
            Intent intent = new Intent(this, CreatePage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  Shows a list of activities, displaying the headline and the date for each.
     * It keeps track of the associated id as well to send it to presenter when an
     * activity has been selected.
     * It is assumed that info for each activity is stored at the same index position in each array
     * i.e. for the first activity on the list has headlines[0], dates[0] and ids[0]
     * the second has headlines[1], dates[1] and ids[1] etc.
     * @param headlines List of headlines to be displayed
     * @param dates List of dates to be displayed each belonging to a headline in the same position
     * @param ids List of activity ids linked to headlines by their index position
     */
    @Override
    public void setHeadlineList(String[] headlines, String[] dates, int[] ids) {

        for (int i = 0; i < headlines.length; i++) {
            Log.i("ListView", String.format("%s, %s: %d", headlines[i], dates[i], ids[i]));
        }
        map.clear();
        List<String> idStrings = new ArrayList<String>();
        for (int i = 0; i < headlines.length; i++) {
            String content = headlines[i] + "\n    date:" + dates[i];
            idStrings.add(content);
            map.put(content, ids[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, idStrings);
        listview.setAdapter(adapter);

    }

    /**
     * Navigates to the view that displays the activity with the given id
     * and sends the id of the activity to that view.
     * Depending on the list filter value, different kinds of activities might be started here.
     * E.g. if these are the user's own activities, the details should be displayed giving
     * opportunities for editing/publishing. If these are invited to activities, the user
     * might wish to sign up.
     * @param activityId The id to send along to the next view
     */
    @Override
    public void navigateToActivityDisplay(int activityId) {
        if (listFilter.equals(Filter.InvitedToActivitiesByCategories)) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("activityId", activityId);
            startActivity(intent);
        }
        // TODO only editable own activities should start this Activity
        else if (listFilter.equals(Filter.OwnActivitiesByCategories)) {
            Intent intent = new Intent(this, PublishableActivity.class);
            intent.putExtra("activityId", activityId);
            startActivity(intent);
        }
    }

    @Override
    public void showOnError(String errorMessage) {
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);


        if (headlines != null) {
            presenter.aboutToListActivities(headlines);
        } else if (listFilter != null) {    // TODO remove?
            presenter.aboutToListActivities(groupId, listFilter);
        }
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


    /**
     * Adapter class for the headline list.
     * It can return the id associated with a selected string on the list.
     */
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, R.layout.list_layout_a, R.id.tvlist, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
