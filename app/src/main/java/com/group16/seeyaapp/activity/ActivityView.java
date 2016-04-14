package com.group16.seeyaapp.activity;

import com.group16.seeyaapp.model.Activity;

/**
 * Created by Andrea on 13/04/16.
 */
public interface ActivityView {

    void setLocationList(String[] locations);

    //void setNumberAttending();

    void displayActivityDetails(Activity activity);

}
