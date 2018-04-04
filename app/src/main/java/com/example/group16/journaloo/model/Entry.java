package com.example.group16.journaloo.model;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

/**
 * Created by s169096 on 14-3-2018.
 */
// Is this an activity ir object template?
public class Entry extends AppCompatActivity {
    public int id;
    public Image image;
    public Date date;
    public String description;
    public String coordinates;
    public String location;
    public int journey_id;

    public Entry() {

    }

    Entry (int id, Image image, Date date, String description, String coordinates,
           String location) {
        this.id = id;
        this.image = image;
        this.date = date;
        this.description = description;
        this.coordinates = coordinates;
        this.location = location;
    }


    Entry (int entryid, Image image, Date date, String description, String location) {
        this.id = entryid;
        this.image = image;
        this.date = date;
        this.description = description;
        this.location = location;
    }

    public Entry(String location, String description) {
        this.location = location;
        this.description = description;
    }

    public static class NewEntry {
        public int journey_id;
        public String location;
        public String coordinates;
        public String description;
    }
}
