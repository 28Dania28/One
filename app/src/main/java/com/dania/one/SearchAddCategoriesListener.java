package com.dania.one;

import com.dania.one.Model.CategoriesPublicModel;
import com.dania.one.Model.TagBuddyModel;

public interface SearchAddCategoriesListener {
    void OnSearchAction(Boolean isSelected);
    void OnSelect(CategoriesPublicModel categoriesPublicModel);
    void OnDeSelect(CategoriesPublicModel categoriesPublicModel);
}
