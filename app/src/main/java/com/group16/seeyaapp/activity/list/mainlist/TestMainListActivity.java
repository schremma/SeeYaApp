package com.group16.seeyaapp.activity.list.mainlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.group16.seeyaapp.PresenterManager;
import com.group16.seeyaapp.R;
import com.group16.seeyaapp.activity.list.Filter;
import com.group16.seeyaapp.activity.list.TestHeadlineListActivity;

import java.util.HashMap;
import java.util.List;

public class TestMainListActivity extends AppCompatActivity implements MainListView {

    private MainListPresenterImpl presenter;
    private Filter currentListFilter;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> mainHeaders;
    HashMap<String, List<String>> subHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main_list);

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

        // the headline list need groupId and listFilter to know what kind of list to display
        intent.putExtra("groupId", selectedItemId);
        intent.putExtra("listFilter", listFilter);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);

        // default for now
        currentListFilter = Filter.CategoriesForInvitedToActivities;
        presenter.aboutToListActivities(currentListFilter);
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


}
