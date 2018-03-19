package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public boolean journeyActive = false; // becomes true if journey is saved, is used to change layout of MainActivity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            journeyActive = getIntent().getExtras().getBoolean("isActive");
        }

        if (!journeyActive) {
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main_2);
        }


        // create custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

    }


    public void stopJourney(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void openCreateJourney(View view){
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
