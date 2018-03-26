package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewEntries extends AppCompatActivity {
    ListView lst;
    String[] entryname={"Dag 1", "Dag 2", "Dag 3"};
    String[] entrydesc={"berg beklommen", "een dagje chillen", "mijn eerste duik"};
    Integer[] entryimigid={R.drawable.curacao1,R.drawable.curacao2, R.drawable.curacao3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);
        lst =(ListView)findViewById(R.id.listviewent);
        final CustomEntryListview customentryListview=new CustomEntryListview(this,entryname,entrydesc,entryimigid);
        lst.setAdapter(customentryListview);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewEntries.this, Entry.class);
                //intent.putExtra("EntryName", lst.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });

    }
}
