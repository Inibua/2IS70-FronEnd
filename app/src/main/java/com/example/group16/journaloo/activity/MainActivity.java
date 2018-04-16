package com.example.group16.journaloo.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.model.Journey;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    final static Gson gson = new Gson();
    private static final String TAG = MainActivity.class.getName();
    static final int requestCode = 20;
    public GestureDetectorCompat detector;
    private APIWrapper wrapper = APIWrapper.getWrapper();
    private Journey activeJourney;
    private ArrayList<Entry> activeJourneyEntries;
    private ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrapper.refreshActiveJourney(new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
                setContentView(R.layout.activity_main);

                // create custom toolbar
                Toolbar toolbar = findViewById(R.id.app_bar);
                setSupportActionBar(toolbar);
            }

            @Override
            public void onSuccess(String responseBody) {
                activeJourney = gson.fromJson(responseBody, Journey.class);
                wrapper.setActiveJourney(activeJourney);
                setContentView(R.layout.activity_main_active);
                TextView nameJourney = findViewById(R.id.nameJourney);
                nameJourney.setText(activeJourney.title);

                wrapper.getJourneyEntries(activeJourney.id, 0, new MainThreadCallback() {
                    @Override
                    public void onFail(Exception error) {
                        Toast.makeText(getApplicationContext(), "Failed to load entries", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String responseBody) {
                        activeJourneyEntries = new ArrayList<>();

                        ListView lst = findViewById(R.id.entryListView);
                        CustomEntryListview entryListview =
                                new CustomEntryListview(MainActivity.this, activeJourneyEntries);

                        lst.setAdapter(entryListview);

                        ArrayList<Entry> loaded = gson.fromJson(responseBody, new TypeToken<ArrayList<Entry>>(){}.getType());
                        entryListview.addAll(loaded);

                        // create custom toolbar
                        Toolbar toolbar = findViewById(R.id.app_bar);
                        setSupportActionBar(toolbar);

                        for (final Entry entry : activeJourneyEntries) {
                            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(MainActivity.this, EditEntryActivity.class);
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
            }
        });

        detector = new GestureDetectorCompat(this, this);
    }




    public void stopJourney(View view) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        wrapper.endJourney(activeJourney, new MainThreadCallback() {
            @Override
            public void onFail(Exception error) {
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
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (e2.getX() - e1.getX() <= 50) {
            return false;
        }

        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(getApplicationContext(), "SD card found", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "SD card not found", Toast.LENGTH_LONG).show();
        }

        startActivityForResult(photoCaptureIntent, requestCode);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
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

                bmp.compress(Bitmap.CompressFormat.PNG, 75, stream);

                //Cleanup
                stream.close();
                bmp.recycle();

                //Pop intent
                Intent in1 = new Intent(this, NewEntryActivity.class);
                in1.putExtra("image", filename);
                startActivity(in1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                if (activeJourney == null) {
                    Intent intentL = new Intent(this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Main", Toast.LENGTH_SHORT).show();
                    startActivity(intentL);
                } else {
                    Intent intentL2 = new Intent(this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Main", Toast.LENGTH_SHORT).show();
                    intentL2.putExtra("isActive", false);
                    startActivity(intentL2);
                }
                break;
            case R.id.explore:
                // fill in what should happen when clicked help
                Toast.makeText(getApplicationContext(), "Explore", Toast.LENGTH_SHORT).show();
                break; // break to end action only do the filled in actions
            case R.id.history:
                Intent intent2 = new Intent(this, ViewJourneyActivity.class);
                Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
                break;
            case R.id.profile:
                Intent intent = new Intent(this, ViewProfileActivity.class);
                Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
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


