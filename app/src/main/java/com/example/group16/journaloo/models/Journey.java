package com.example.group16.journaloo.models;

import java.util.Date;

/**
 * Created by s169096 on 14-3-2018.
 */

public class Journey {
    public int id;
    public int user_id;
    public String title;
    public Date start_date;
    public Date end_date;

    static public class NewJourney {
        public int user_id;
        public String title;

        public NewJourney(int user_id, String title) {
            this.user_id = user_id;
            this.title = title;
        }
    }
}



