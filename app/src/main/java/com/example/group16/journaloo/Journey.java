package com.example.group16.journaloo;

import java.util.Date;

/**
 * Created by s169096 on 14-3-2018.
 */

public class Journey {
    public int journeyId;
    public int userId;
    public String title;
    public String startDate;
    public String endDate;
    public boolean privacy;

    Journey () {

    }

    Journey (int journeyId, String title, String startDate, String endDate, boolean privacy) {
        this.journeyId = journeyId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.privacy = privacy;
    }

    Journey(int journeyId, int userId, String title) {
        this.journeyId = journeyId;
        this.userId = userId;
        this.title = title;
    }

    Journey (String title) {
        this.title = title;
    }
}