package com.example.group16.journaloo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.group16.journaloo.adapters.EntryCardAdapter;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Entry;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

abstract public class EntryRecyclerViewFragment extends RecyclerViewFragment {
    private ArrayList<Entry> entryList;
    private EntryCardAdapter mAdapter;


    final MainThreadCallback responseHandler = new MainThreadCallback() {
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
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entryList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mAdapter = new EntryCardAdapter(entryList);
        mRecyclerView.setAdapter(mAdapter);

        loadMoreItems();

        return rootView;
    }
}
