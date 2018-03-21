package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.group16.journaloo.CustomListview;
import com.example.group16.journaloo.R;

public class ViewJourneyActivity extends AppCompatActivity {

    ListView lst;
    String[] journeyname={"Curacao", "Litouwen"};
    String[] desc={"3 maanden stage lopen", "Teamweekend met DA3"};
    Integer[] imigid={R.drawable.flag_curacao,R.drawable.flag_lithuania};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journey);
        lst =(ListView)findViewById(R.id.listview);
        CustomListview customListview=new CustomListview(this,journeyname,desc,imigid);
        lst.setAdapter(customListview);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewJourneyActivity.this, ViewEntries.class);
                startActivity(intent);
            }
        });
    }
}
