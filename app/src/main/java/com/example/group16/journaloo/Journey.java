package com.example.group16.journaloo;

import java.util.Date;

/**
 * Created by s169096 on 14-3-2018.
 */

public class Journey {
    public String journeyId;
    public String userId;
    public String title;
    public Date startDate;
    public Date endDate;
    public boolean privacy;

    Journey () {

    }

    Journey (String journeyId, String title, Date startDate, Date endDate, boolean privacy) {
        this.journeyId = journeyId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.privacy = privacy;
    }
}