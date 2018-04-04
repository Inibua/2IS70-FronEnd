package com.example.group16.journaloo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.google.gson.Gson;

public abstract class RecyclerViewFragment extends Fragment {
    static Gson gson = new Gson();
    static String TAG = "RecyclerViewFragment";
    static int PAGE_SIZE = 10;

    protected APIWrapper wrapper = APIWrapper.getWrapper();
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    boolean isLoading;
    boolean isLastPage;
    int currentPage = 0;

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

                    loadMoreItems();
                }
            }
        }
    };

    protected void loadMoreItems() {
        isLoading = true;
        currentPage += 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        currentPage = -1;
        isLastPage = false;
        isLoading = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recyclerview, container, false);
        rootView.setTag(TAG);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        return rootView;
    }
}
