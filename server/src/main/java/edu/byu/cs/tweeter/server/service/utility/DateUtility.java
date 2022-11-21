package edu.byu.cs.tweeter.server.service.utility;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtility {
    public static Long getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mm a");
        return ZonedDateTime.parse(date, formatter).toInstant().getEpochSecond();
    }

    public static String getStringFromDate(Long date) {
        Instant instant = Instant.ofEpochSecond(date);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mm a");
        return zdt.format(formatter);
    }
}
