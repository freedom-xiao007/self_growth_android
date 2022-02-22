package com.example.selfgrowth.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");

    public static String dateString(final Date date) {
        return format.format(date);
    }

    /**
     * 转换日期为自定义日期
     * 原定义一天时间点为 00 - 00，在应用中更改为 22 - 22，即晚上10开始，晚上十点结束
     * 那日期计算时，加两个小时，得到自定义的日期
     *
     * 在应用日志记录时 需要调用该函数进行时间转换
     * 在应用日志统计时 需要调用该函数进行时间转换
     *
     * 比如如果当前日志记录时间是 2022-01-22 23:10:00
     * 正常逻辑得到天是：2021-01-22
     * 自定义转换后是： 2021-01-23
     * @param date 当前北京标准时间
     * @return APP 应用日志统计时间
     */
    public static String toCustomDay(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return format.format(calendar.getTime());
    }
}
