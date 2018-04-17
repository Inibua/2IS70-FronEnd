package com.example.group16.journaloo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.group16.journaloo.api.APIWrapper;
import com.example.group16.journaloo.api.MainThreadCallback;
import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

public class NewEntryActivity extends AppCompatActivity {
    private APIWrapper wrapper = APIWrapper.getWrapper();
    String filename;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue requestQueue;
    EditText descriptionEntry;
    EditText locationEntry;
    String coordinates;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        descriptionEntry = findViewById(R.id.descriptionEditEntryEditText);
        locationEntry = findViewById(R.id.locationEntryEditText);
        requestQueue = Volley.newRequestQueue(this);


        Bitmap bmp = null;
        filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView entryImageView = findViewById(R.id.entryImageView);
        entryImageView.setImageBitmap(bmp);
        

        /* Old internal storage staving image
        String path = "sdcard/camera_app/cam_image.jpg";
        entryView.setImageDrawable(Drawable.createFromPath(path));
        */

        // GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                coordinates = location.getLatitude() + "," + location.getLongitude();

                JsonObjectRequest request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + coordinates + "&key=AIzaSyCMuOH4q9f_FY8xjMHmhmInZLsvLRi09gE", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                            locationEntry.setText(address);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        }
                });
                requestQueue.add(request);



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                return;

        }
    }

    public void saveEntry(View view) {
        final Intent intent = new Intent(this, MainActivity.class);

        if (locationEntry.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please give the entry a location",
                    Toast.LENGTH_LONG).show();
        } else if (descriptionEntry.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please give the entry a description",
                    Toast.LENGTH_LONG).show();
        } else {
            Entry.NewEntry newEntry = new Entry.NewEntry();
            newEntry.description = descriptionEntry.getText().toString();
            newEntry.location = locationEntry.getText().toString();
            wrapper.createEntry(newEntry, filename, getApplicationContext(), new MainThreadCallback() {
                @Override
                public void onFail(Exception error) {
                    Toast.makeText(getApplicationContext(), "Failed to create entry",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String responseBody) {
                    finish();
                    startActivity(intent);
                }
            });



        }
    }
}