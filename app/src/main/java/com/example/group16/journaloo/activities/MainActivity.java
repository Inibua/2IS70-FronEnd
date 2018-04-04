package com.example.group16.journaloo.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Toast;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.fragments.*;
import com.example.group16.journaloo.models.Journey;
import com.google.gson.Gson;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private final static Gson gson = new Gson();
    private static final String TAG = MainActivity.class.getName();
    private static final int requestCode = 20;

    private APIWrapper wrapper = APIWrapper.getWrapper();
    private Journey activeJourney;
    private DrawerLayout mDrawerLayout;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_active);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // set item as selected to persist highlight
                item.setChecked(true);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();
                return navItemSelected(item);
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        setupLandingFragment();
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupLandingFragment() {
        wrapper.getActiveJourney(new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                setTitle("No active journey");
                menu.setGroupVisible(R.id.active_journey_buttons, false);
                NoJourneyFragment frag = new NoJourneyFragment();
                setFragment(frag);
            }

            @Override
            public void onSuccess(String responseBody) {
                activeJourney = gson.fromJson(responseBody, Journey.class);
                wrapper.setActiveJourney(activeJourney);
                setTitle(activeJourney.title);
                menu.setGroupVisible(R.id.active_journey_buttons, true);

                JourneyEntriesRVFragment frag = JourneyEntriesRVFragment
                        .newInstance(activeJourney.id);
                setFragment(frag);
            }
        });
    }

    private void setupExploreFragment() {
        setTitle("Explore");
        menu.setGroupVisible(R.id.active_journey_buttons, false);

        AllEntriesRVFragment frag = new AllEntriesRVFragment();
        setFragment(frag);
    }

    private void setupHistoryFragment() {
        setTitle("History");
        menu.setGroupVisible(R.id.active_journey_buttons, false);

        JourneysRVFragment frag = JourneysRVFragment.newInstance(wrapper
                .getLoggedInUser().id);
        setFragment(frag);
    }

    private void setupProfileFragment() {
        setTitle("Profile");

        menu.setGroupVisible(R.id.active_journey_buttons, false);
        ViewProfileFragment frag = new ViewProfileFragment();
        setFragment(frag);
    }

    private void setFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.commit();
    }

    private boolean navItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_landing:
                setupLandingFragment();
                return true;

            case R.id.nav_explore:
                setupExploreFragment();
                return true;

            case R.id.nav_history:
                setupHistoryFragment();
                return true;

            case R.id.nav_profile:
                setupProfileFragment();
                return true;
        }

        return false;
    }

    private void stopJourney() {
        wrapper.endJourney(activeJourney, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to end journey", Toast
                        .LENGTH_LONG).show();
                setupLandingFragment();
            }

            @Override
            public void onSuccess(String responseBody) {
                Toast.makeText(getApplicationContext(), "Journey saved to history", Toast
                        .LENGTH_SHORT).show();
                setupLandingFragment();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (MainActivity.requestCode == requestCode && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap bmp = (Bitmap) extras.get("data");
            assert bmp != null;

            try {
                //Write file
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                bmp.recycle();

                //Pop intent
                Intent intent = new Intent(this, NewEntryActivity.class);
                intent.putExtra("image", filename);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.active_journey_actions, menu);
        menu.setGroupVisible(R.id.active_journey_buttons, false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.stopButton:
                stopJourney();
                return true;

            case R.id.addButton:
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    // unused required methods
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}


