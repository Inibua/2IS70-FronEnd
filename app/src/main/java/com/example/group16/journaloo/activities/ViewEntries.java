package com.example.group16.journaloo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.models.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ViewEntries extends AppCompatActivity {
    final static Gson gson = new Gson();
    private static final String TAG = ViewEntries.class.getName();
    ListView lst;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int journey_id = getIntent().getExtras().getInt("JourneyId");
        String journey_name = getIntent().getExtras().getString("JourneyName");

        setContentView(R.layout.activity_view_entries);
        TextView nameJourney = findViewById(R.id.nameJourney);
        nameJourney.setText(journey_name);
        //lst = findViewById(R.id.listviewent);
        wrapper.getJourneyEntries(journey_id, 0, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                Toast.makeText(getApplicationContext(), "Failed to load entries", Toast.LENGTH_SHORT).show();
                // create custom toolbar
                Toolbar toolbar = findViewById(R.id.app_bar);
                setSupportActionBar(toolbar);
            }

            @Override
            public void onSuccess(String responseBody) {
                entries = new ArrayList<>();

                ListView lst = findViewById(R.id.entryListView2);
                CustomEntryListview entryListview =
                        new CustomEntryListview(ViewEntries.this, entries);

                lst.setAdapter(entryListview);

                ArrayList<Entry> loaded = gson.fromJson(responseBody, new TypeToken<ArrayList<Entry>>(){}.getType());
                entryListview.addAll(loaded);

                // create custom toolbar
                Toolbar toolbar = findViewById(R.id.app_bar);
                setSupportActionBar(toolbar);

                for (final Entry entry : entries) {
                    lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(ViewEntries.this, EditEntryActivity.class);
                            Log.i(TAG, "Hi you clicked a button amigo");
                            intent.putExtra("id", entry.id);
                            intent.putExtra("location", entry.location);
                            intent.putExtra("description", entry.description);

                            startActivity(intent);
                        }
                    });
                }
            }
        });
        /*final CustomEntryListview customentryListview = new CustomEntryListview(this, entries);
        lst.setAdapter(customentryListview);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewEntries.this, EditEntryActivity.class);
                startActivity(intent);
            }
        });*/
    }
}