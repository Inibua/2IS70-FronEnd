package com.example.group16.journaloo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.group16.journaloo.R;
import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.api.APIWrapper;

public class ViewEntries extends AppCompatActivity {
    ListView lst;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    Entry[] entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);
        lst = findViewById(R.id.listviewent);
        final CustomEntryListview customentryListview = new CustomEntryListview(this, entries);
        lst.setAdapter(customentryListview);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewEntries.this, Entry.class);
                startActivity(intent);
            }
        });
    }
}