package com.group16.seeyaapp.activity.list.mainlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RadioButton;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.list.Filter;
import com.group16.seeyaapp.activity.list.TestHeadlineListActivity;
import com.group16.seeyaapp.navigation.DemoPage;
import com.group16.seeyaapp.navigation.TestCreatePage;

import java.util.HashMap;
import java.util.List;

public class TestMainListActivity extends AppCompatActivity implements MainListView {

    private MainListPresenterImpl presenter;
    private Filter currentListFilter;
    private RadioButton rbtnInvited;
    private RadioButton rbtnOwn;
    private Toolbar toolbar;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> mainHeaders;
    HashMap<String, List<String>> subHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main_list);

        toolbar = (Toolbar)findViewById(R.id.tbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.seeyalogo_smaller);

        if (savedInstanceState == null) {
            presenter = new MainListPresenterImpl();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                String selectedItem = subHeaders.get(
                        mainHeaders.get(groupPosition)).get(
                        childPosition);

                presenter.selectedListItem(selectedItem, currentListFilter);

                return false;
            }
        });

        rbtnInvited = (RadioButton)findViewById(R.id.rbtnInvitedTo);
        rbtnOwn = (RadioButton)findViewById(R.id.rbtnOwn);
        rbtnInvited.setChecked(true);

        rbtnInvited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        rbtnOwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });


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
            Intent intent = new Intent(this, TestMainListActivity.class);
            startActivity(intent);
        } else if(id == R.id.toolbaradd) {
            Intent intent = new Intent(this, TestCreatePage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setNestedListHeaders(List<String> mainHeaders, HashMap<String, List<String>> subHeaders) {

        this.mainHeaders = mainHeaders;
        this.subHeaders = subHeaders;
        listAdapter = new ExpandableListAdapter(this, mainHeaders, subHeaders);
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void navigateToHeadlineDisplay(int selectedItemId, Filter listFilter) {

        Intent intent = new Intent(this, TestHeadlineListActivity.class);

        // the headline list needs groupId and listFilter to know what kind of list to display
        intent.putExtra("groupId", selectedItemId);
        intent.putExtra("listFilter", listFilter);
        startActivity(intent);
    }

    @Override
    public void navigateToHeadlineDisplay(String headlines) {
        Intent intent = new Intent(this, TestHeadlineListActivity.class);

        // the headline list needs groupId and listFilter to know what kind of list to display
        intent.putExtra("headlines", headlines);
        intent.putExtra("listFilter", currentListFilter); // good to know if it is own or invited to activities
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);

//        // set filter based on GUI, default for now instead
//        currentListFilter = Filter.InvitedToActivitiesByCategories;

        if (rbtnOwn.isChecked())
            currentListFilter = Filter.OwnActivitiesByCategories;
        else
            currentListFilter = Filter.InvitedToActivitiesByCategories;

        presenter.aboutToPresentMainList(currentListFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        Filter tempFilter = currentListFilter;
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnInvitedTo:
                if (checked)
                    tempFilter = Filter.InvitedToActivitiesByCategories;
                    break;
            case R.id.rbtnOwn:
                if (checked)
                    tempFilter = Filter.OwnActivitiesByCategories;
                    break;
        }

        // Only reload if a new filtering has been selected
        if (!currentListFilter.equals(tempFilter)) {
            currentListFilter = tempFilter;
            presenter.aboutToPresentMainList(currentListFilter);
        }
    }


}