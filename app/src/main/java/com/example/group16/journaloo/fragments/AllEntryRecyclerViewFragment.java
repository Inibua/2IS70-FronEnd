package com.example.group16.journaloo.fragments;

public class AllEntryRecyclerViewFragment extends EntryRecyclerViewFragment {
    @Override
    protected void loadMoreItems() {
        super.loadMoreItems();
        wrapper.getAllEntries(currentPage, responseHandler);
    }
}
