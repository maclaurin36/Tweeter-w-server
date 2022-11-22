package edu.byu.cs.tweeter.server.service.utility;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtility {
    public static Long getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mm:ss a");
        return LocalDateTime.parse(date, formatter).toEpochSecond(ZoneOffset.UTC);
    }

    public static String getStringFromDate(Long date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy h:mm:ss a");
        return LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC).format(formatter);
    }

    public static Long getDate() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}
