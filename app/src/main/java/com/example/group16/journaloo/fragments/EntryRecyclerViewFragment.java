package com.example.group16.journaloo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.group16.journaloo.adapters.EntryCardAdapter;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Entry;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class EntryRecyclerViewFragment extends RecyclerViewFragment {
    private ArrayList<Entry> entryList;
    private EntryCardAdapter mAdapter;
    private int journeyId;

    public static EntryRecyclerViewFragment newInstance(int journeyId) {
        EntryRecyclerViewFragment frag = new EntryRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt("journeyId", journeyId);
        frag.setArguments(args);

        return frag;
    }

    @Override
    protected void loadMoreItems() {
        super.loadMoreItems();
        wrapper.getJourneyEntries(journeyId, currentPage, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                Toast.makeText(getActivity(), "Failed to load entries", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }

            @Override
            public void onSuccess(String responseBody) {
                ArrayList<Entry> page = gson.fromJson(responseBody, new TypeToken<ArrayList<Entry>>() {
                }.getType());

                isLoading = false;
                if (page.size() < PAGE_SIZE) {
                    isLastPage = true;
                }

                entryList.addAll(page);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        journeyId = getArguments().getInt("journeyId", -1);
        entryList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mAdapter = new EntryCardAdapter(entryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        loadMoreItems();

        return rootView;
    }
}