package com.example.selfgrowth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");

    public static String dateString(final Date date) {
        return format.format(date);
    }
}
