package com.example.group16.journaloo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Journey;
import com.example.group16.journaloo.models.User;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static com.example.group16.journaloo.activities.ViewEntries.gson;

public class HistoryActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 10;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    Journey[] journeys;
    ListView lst;
    private boolean isLoading;
    private boolean isLastPage;
    private LinearLayoutManager mLayoutManager;
    private int currentPage = 0;
    private User user;
    private ArrayList<Journey> userJourneys;
    private JourneyCardAdapter mAdapter;
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
    private RecyclerView mRecyclerView;

    private void loadMoreItems() {
        isLoading = true;
        currentPage += 1;
        wrapper.getUserJourneys(user.id, currentPage, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                Toast.makeText(getApplicationContext(), "Failed to load journeys", Toast.LENGTH_SHORT).show();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        int userId = getIntent().getIntExtra("id", -1);
        mRecyclerView = findViewById(R.id.journeyRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        userJourneys = new ArrayList<>();
        mAdapter = new JourneyCardAdapter(userJourneys);
        mRecyclerView.setAdapter(mAdapter);
        // Pagination
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        isLoading = false;

        wrapper.getUser(userId, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to retrieve user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                user = gson.fromJson(responseBody, User.class);
                currentPage = -1;
                loadMoreItems();
            }
        });
    }
}
