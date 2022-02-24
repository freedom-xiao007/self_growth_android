package com.example.selfgrowth.utils;

public class MyTimeUtils {

    public static String toString(final long minutes) {
        long hours = minutes / 60;
        if (hours > 0) {
            return String.format("%d 小时 %d 分钟", hours, minutes % 60);
        }
        return String.format("%d 分钟", minutes);
    }
}
