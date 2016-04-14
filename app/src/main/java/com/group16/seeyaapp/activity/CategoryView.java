package com.group16.seeyaapp.activity;

/**
 * Created by Andrea on 12/04/16.
 * Interface for the Activity/fragment where the user selects the main category
 * and the subcategory of the activity to be created.
 * When the user goes on to the next view to fill in details about the new activity,
 * the id of the subcategoy chosen on this view should be sent along.
 */
public interface CategoryView {

    void setMainCategories(String[] mainCategories); // show the list of all main categories
    void setSubcategories(String[] subCategories); // show the list of subcategories under the chosen main category
    void navigateToCreateActivityDetails(int subCategoryId); // navigates to the Activity/fragment for new activity details
    void showError(String errorMessage);
}
