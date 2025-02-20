package com.worktree.hrms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateUtils {

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public final static String LICENSE_DATE_FORMAT = "dd-MM-yyyy";

    public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";

    //ist timezone
    private static final String ZONE_ID = "Asia/Kolkata";

    public Date getCurrentDate() {
        TimeZone istTimeZone = TimeZone.getTimeZone(ZONE_ID);

        // Create a Calendar instance with the IST timezone
        Calendar calendar = Calendar.getInstance(istTimeZone);

        // Get the current date and time in IST as java.util.Date
        return calendar.getTime();
    }

    public static boolean isDateExceeded(String date, String pattern) {
        try {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            // Parse the input date
            LocalDate inputLocalDate = LocalDate.parse(date, formatter);

            // Get the current date in IST
            LocalDate currentISTDate = ZonedDateTime.now(ZoneId.of(ZONE_ID)).toLocalDate();

            // Compare the dates
            return !inputLocalDate.isAfter(currentISTDate);
        } catch (DateTimeParseException e) {
            logger.error("Exception occurred", e);
            return true;
        }
    }

    public String format(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }
}
