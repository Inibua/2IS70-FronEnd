package com.example.group16.journaloo;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button goJournaloo;
    public boolean journeyActive = false; // becomes true if journey is saved, is used to change layout of MainActivity

    public void setJourneyActive(boolean isActive){
        this.journeyActive = isActive;
    }

    public boolean getJourneyActive(){
        return this.journeyActive;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!journeyActive) {
            setContentView(R.layout.activity_main);
            // find button and direct to create journey
            goJournaloo = (Button) findViewById(R.id.goJournaloo);
            goJournaloo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openCreateJourney();
                }
            });
        } else {
            setContentView(R.layout.activity_main_2);
        }


        // create custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

    }


    public void openCreateJourney() {
        Intent intent = new Intent(this, CreateJourneyActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.landing:
                Toast.makeText(getApplicationContext(), "Landing", Toast.LENGTH_SHORT).show();
                break;
            case R.id.explore:
                // fill in what should happen when clicked help
                Toast.makeText(getApplicationContext(), "Explore", Toast.LENGTH_SHORT).show();
                break; // break to end action only do the filled in actions
            case R.id.history:
                Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
