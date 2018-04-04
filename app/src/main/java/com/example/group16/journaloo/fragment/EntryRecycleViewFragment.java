package com.example.group16.journaloo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.adapter.EntryCardAdapter;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.model.Journey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class EntryRecycleViewFragment extends Fragment {
    private final static Gson gson = new Gson();
    private static final String TAG = "RecyclerViewFragment";
    static int PAGE_SIZE = 10;
    private ArrayList<Entry> entryList;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private Journey activeJourney;
    private RecyclerView mRecyclerView;
    private EntryCardAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;
    private boolean isLastPage;
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    // TODO: implement item loading
//                    loadMoreItems();
                }
            }
        }
    };
    private int journeyId;

    public static EntryRecycleViewFragment newInstance(int journeyId) {
        EntryRecycleViewFragment frag = new EntryRecycleViewFragment();
        Bundle args = new Bundle();
        args.putInt("journeyId", journeyId);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entryList = new ArrayList<>();
        journeyId = getArguments().getInt("journeyId", -1);
        initEntryList(journeyId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.entry_cards_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EntryCardAdapter(entryList);
        initEntryList(journeyId);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        return rootView;
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initEntryList(int journeyId) {
        wrapper.getJourneyEntries(journeyId, 0, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(mRecyclerView.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                ArrayList<Entry> loaded = gson.fromJson(responseBody, new TypeToken<ArrayList<Entry>>() {
                }.getType());
                entryList.addAll(loaded);
                Log.d(TAG, entryList.toString());
            }
        });

    }
}