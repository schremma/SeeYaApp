package com.group16.seeyaapp.activity.categories;

/**
 * Created by Andrea on 12/04/16.
 * Interface for the Activity/fragment where the user selects the main category
 * and the subcategory of the activity to be created.
 * When the user goes on to the next view to fill in details about the new activity,
 * the id of the subcategoy chosen on this view should be sent along.
 */
public interface CategoryView {

    // show the list of all main categories
    void setMainCategories(String[] mainCategories);

    // show the list of subcategories under the chosen main category
    void setSubcategories(String[] subCategories);

    // navigates to the view showing the form for filling in new activity details
    void navigateToCreateActivityDetails(int subCategoryId);

    void showError(String errorMessage);
}
