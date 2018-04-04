package com.example.group16.journaloo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.fragments.JourneyEntriesRVFragment;
import com.example.group16.journaloo.models.Journey;
import com.google.gson.Gson;

public class ViewEntriesActivity extends AppCompatActivity {
    private final static Gson gson = new Gson();
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private Journey journey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_appbar_recyclerview);

        final int journeyId = getIntent().getIntExtra("journeyId", -1);
        wrapper.getJourney(journeyId, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                Toolbar toolbar = findViewById(R.id.app_bar);
                setSupportActionBar(toolbar);
                // TODO: Error handling
            }

            @Override
            public void onSuccess(String responseBody) {
                journey = gson.fromJson(responseBody, Journey.class);

                Toolbar toolbar = findViewById(R.id.app_bar);
                setSupportActionBar(toolbar);
                setTitle(journey.title);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                JourneyEntriesRVFragment frag = JourneyEntriesRVFragment.newInstance(journey.id);
                fragmentTransaction.replace(R.id.fragment_container, frag);
                fragmentTransaction.commit();
            }
        });
    }
}