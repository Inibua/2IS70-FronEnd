package com.example.group16.journaloo.models;

import android.media.Image;

import java.util.Date;

public class Entry {
    public int id;
    public Image image;
    public Date created;
    public String description;
    public String coordinates;
    public String location;
    public int journey_id;

    public Entry() {
    }

    public static class NewEntry {
        public int journey_id;
        public String location;
        public String coordinates;
        public String description;

        public NewEntry() {
        }
    }
}
