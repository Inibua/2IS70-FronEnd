package com.example.group16.journaloo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.group16.journaloo.adapters.JourneyCardAdapter;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Journey;
import com.example.group16.journaloo.models.User;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class JourneyRecyclerViewFragment extends RecyclerViewFragment {
    private ArrayList<Journey> userJourneys;
    private JourneyCardAdapter mAdapter;
    private User user;

    public static JourneyRecyclerViewFragment newInstance(int journeyId) {
        JourneyRecyclerViewFragment frag = new JourneyRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt("userId", journeyId);
        frag.setArguments(args);

        return frag;
    }

    @Override
    protected void loadMoreItems() {
        super.loadMoreItems();
        wrapper.getUserJourneys(user.id, currentPage, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                Toast.makeText(getContext(), "Failed to load journeys", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }

            @Override
            public void onSuccess(String responseBody) {
                ArrayList<Journey> page = gson.fromJson(responseBody, new TypeToken<ArrayList<Journey>>() {
                }.getType());

                isLoading = false;
                if (page.size() < PAGE_SIZE) {
                    isLastPage = true;
                }

                userJourneys.addAll(page);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int userId = getArguments().getInt("userId", -1);
        userJourneys = new ArrayList<>();

        wrapper.getUser(userId, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Failed to retrieve user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                user = gson.fromJson(responseBody, User.class);
                loadMoreItems();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mAdapter = new JourneyCardAdapter(userJourneys);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
