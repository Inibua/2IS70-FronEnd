package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.model.Journey;
import com.example.group16.journaloo.R;

public class ViewJourneyActivity extends AppCompatActivity {

    Journey[] journeys;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journey);
        lst = findViewById(R.id.listview);
        if (journeys == null) {

        } else {
            String[] journeyNames = new String[journeys.length];
            for (int i = 0; i < journeys.length; i++) {
                Journey journey = journeys[i];
                String titleJ = journey.title;
                journeyNames[i] = titleJ;
            }

            CustomListview customListview = new CustomListview(this, journeyNames);
            lst.setAdapter(customListview);
            for (Journey journey : journeys) {
                final Integer journeyid = journey.id;
                lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ViewJourneyActivity.this, ViewEntries.class);
                        intent.putExtra("JourneyId", journeyid);
                        startActivity(intent);
                    }
                });
            }
        }
    }



}
