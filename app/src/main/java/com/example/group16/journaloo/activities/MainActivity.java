package com.example.group16.journaloo.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
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
import com.example.group16.journaloo.fragments.JourneyEntryRecyclerViewFragment;
import com.example.group16.journaloo.models.Journey;
import com.google.gson.Gson;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private final static Gson gson = new Gson();
    private static final String TAG = MainActivity.class.getName();
    private static final int requestCode = 20;
    private GestureDetectorCompat detector;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private Journey activeJourney;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_active);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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


        wrapper.getActiveJourney(new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                setTitle("No active journey");
                // TODO: load button fragment
            }

            @Override
            public void onSuccess(String responseBody) {
                activeJourney = gson.fromJson(responseBody, Journey.class);
                wrapper.setActiveJourney(activeJourney);
                setTitle(activeJourney.title);

                detector = new GestureDetectorCompat(MainActivity.this, MainActivity.this);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                JourneyEntryRecyclerViewFragment frag = JourneyEntryRecyclerViewFragment.newInstance(activeJourney.id);
                fragmentTransaction.replace(R.id.fragment_container, frag);
                fragmentTransaction.commit();
            }
        });


    }

    private boolean navItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_landing:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.nav_explore:
                intent = new Intent(this, ExploreActivity.class);
                break;

            case R.id.nav_history:
                intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("userId", wrapper.getLoggedInUser().id);
                break;

            case R.id.nav_profile:
                intent = new Intent(this, ViewProfileActivity.class);
                break;

            case R.id.stopButton:
                stopJourney();
                return true;

            default:
                intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        return true;
    }

    private void stopJourney() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        wrapper.endJourney(activeJourney, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to end journey", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String responseBody) {
                Toast.makeText(getApplicationContext(), "Journey saved to history", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }


    public void openCreateJourney(View view) {
        Intent intent = new Intent(this, CreateJourneyActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (e2.getX() - e1.getX() <= 50) {
            return false;
        }
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent, requestCode);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector != null) {
            detector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_active_actionicons, menu);
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


