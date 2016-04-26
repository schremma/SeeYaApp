package com.group16.seeyaapp.activity.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.details.TestNewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestHeadlineListActivity extends AppCompatActivity implements ActivityListView{

    private ActivityListPresenterImpl presenter;
    private ListView listview;
    private HashMap map = new HashMap<String, Integer>();

    private Filter listFilter;
    private int groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_headline_list);

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

                int idInt = (int)map.get(item);

                presenter.onActivitySelected(idInt);
            }

        });


        Intent intent = getIntent();
        if (intent.getExtras() != null) {


            groupId = intent.getIntExtra("groupId", -1);
            listFilter = (Filter) intent.getSerializableExtra("listFilter");

            Log.i("HeadlineListActivity", "Got extras: " + groupId + ", " + listFilter.name());

        }
    }

    @Override
    public void setHeadlineList(String[] headlines, String[] dates, int[] ids) {

        for (int i = 0; i < headlines.length; i++) {
            Log.i("ListView", String.format("%s, %s: %d", headlines[i], dates[i], ids[i]));
        }
        map.clear();
        List<String> idStrings = new ArrayList<String>();
        for(int i = 0; i < headlines.length; i++) {
            String content = headlines[i] + "    date:" + dates[i];
            idStrings.add(content);
            map.put(content, ids[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, idStrings);
        listview.setAdapter(adapter);

    }

    @Override
    public void navigateToActivityDisplay(int activityId) {
        Intent intent = new Intent(this, TestNewActivity.class);
        intent.putExtra("activityId", activityId);
        startActivity(intent);
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

        if (listFilter != null) {
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

    //region testlist

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
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

    //endregion
}
