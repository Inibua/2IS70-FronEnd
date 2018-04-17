package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.model.Journey;
import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.R;

public class ExploreActivity extends AppCompatActivity {

    private APIWrapper wrapper = APIWrapper.getWrapper();
    Entry[] explore;
    ListView lst;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        lst = findViewById(R.id.listviewExplore);
        explore = wrapper.getAllEntries(page);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //explore = wrapper.getAllEntries(page);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (explore == null) {

        } else {
            String[] journeyNames = new String[explore.length];
            for (int i = 0; i < explore.length; i++) {
                Entry entry = explore[i];
                String titleJ = entry.description;
                journeyNames[i] = titleJ;
            }

            CustomListview customListview = new CustomListview(this, journeyNames);
            lst.setAdapter(customListview);
            for (Entry journey : explore) {
                final Integer journeyid = journey.id;
                final String journeyName = journey.description;
                lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ExploreActivity.this, ViewEntries.class);
                        intent.putExtra("JourneyId", journeyid);
                        intent.putExtra("JourneyName", journeyName);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}



