package com.worktree.hrms.utils;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateUtils {

    //ist timezone
    private static final String ZONE_ID = "Asia/Kolkata";

    public Date getCurrentDate(){
        TimeZone istTimeZone = TimeZone.getTimeZone(ZONE_ID);

        // Create a Calendar instance with the IST timezone
        Calendar calendar = Calendar.getInstance(istTimeZone);

        // Get the current date and time in IST as java.util.Date
        return calendar.getTime();
    }

}
