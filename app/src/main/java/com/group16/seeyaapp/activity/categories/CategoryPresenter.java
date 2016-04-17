package com.group16.seeyaapp.activity.categories;

/**
 * Created by Andrea on 12/04/16.
 */
public interface CategoryPresenter {

    // The user has selected one of the main categories from the list
    void mainCategorySelected(String mainCategory);

    void pressedNext(String selectedSubcategory);

}
