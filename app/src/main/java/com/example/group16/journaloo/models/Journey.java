package com.example.group16.journaloo.models;

/**
 * Created by s169096 on 14-3-2018.
 */

public class Journey {
    public int id;
    public int user_id;
    public String title;
    public String start_date;
    public String end_date;

    Journey () {

    }

    Journey (int id, String title, String start_date, String end_date) {
        this.id = id;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Journey(int id, int user_id, String title) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
    }

    public Journey(String title) {
        this.title = title;
    }

    static public class NewJourney {
        public int user_id;
        public String title;

        public NewJourney(int user_id, String title) {
            this.user_id = user_id;
            this.title = title;
        }
    }
}



