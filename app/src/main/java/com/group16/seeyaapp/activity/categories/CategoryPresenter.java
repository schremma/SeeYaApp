package com.group16.seeyaapp.activity.categories;

/**
 * Created by Andrea on 12/04/16.
 * Presenter handling the logic behind displaying main categories and subcategories
 * under which activities can be created. When the user selects a main category,
 * the list of corresponding subcategories should be sent to the view for display.
 */
public interface CategoryPresenter {

    // Invoke when the user has selected one of the main categories from the list
    void mainCategorySelected(String mainCategory);

    // Invoke when the user indicated intention to proceed further after
    // the provided subcategory has been selected.
    void pressedNext(String selectedSubcategory);

}
