package com.example.group16.journaloo;

import java.util.Date;

/**
 * Created by s169096 on 14-3-2018.
 */

public class Journey {
    public Integer journeyId;
    public String userId;
    public String title;
    public String startDate;
    public String endDate;
    public boolean privacy;

    Journey () {

    }


    Journey (Integer journeyId, String title, String startDate, String endDate, boolean privacy) {
        this.journeyId = journeyId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.privacy = privacy;
    }

    Journey (String title) {
        this.title = title;
    }
}