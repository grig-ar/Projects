package com.artem.nsu.redditfeed.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    public static String formatEpochTime(long epochTime) {
        Date date = new Date(epochTime);
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        return dateFormat.format(date);
    }

}
