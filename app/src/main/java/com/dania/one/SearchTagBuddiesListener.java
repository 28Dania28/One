package com.dania.one;

import com.dania.one.Model.TagBuddyModel;

public interface SearchTagBuddiesListener {
    void OnSearchAction(Boolean isSelected);
    void OnSelect(TagBuddyModel tagBuddyModel);
    void OnDeSelect(TagBuddyModel tagBuddyModel);
}
