package com.example.group16.journaloo;

import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

/**
 * Created by s169096 on 14-3-2018.
 */

public class Entry extends AppCompatActivity {
    public String entryid;
    public Image image;
    public Date date;
    public String description;
    public String coordinates;
    public String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
    }
}
