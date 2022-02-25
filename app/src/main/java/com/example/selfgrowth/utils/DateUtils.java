package com.example.selfgrowth.utils;

import com.example.selfgrowth.enums.StatisticsTypeEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH");

    public static String dateString(final Date date) {
        return dateFormat.format(date);
    }

    public static String hourString(final Date date) {
        return hourFormat.format(date);
    }

    public static String dayString(final Date date) {
        return dayFormat.format(date);
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
        return dayFormat.format(calendar.getTime());
    }

    public static List<Date> getPeriodDates(final Date date, final StatisticsTypeEnum statisticsType) {
        switch (statisticsType) {
            case WEEK:
                return getPeriodDatesOfWeek(date);
            case MONTH:
                return getPeriodDatesOfMonth(date);
            case YEAR:
                return getPeriodDatesOfYear(date);
            default:
                throw new RuntimeException("不支持该类型：" + statisticsType.getName());
        }
    }

    private static List<Date> getPeriodDatesOfYear(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        final int days = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        final List<Date> res = new ArrayList<>(days);
        for (int i=0; i < days; i++) {
            res.add(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return res;
    }

    private static List<Date> getPeriodDatesOfMonth(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        final int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        final List<Date> res = new ArrayList<>(days);
        for (int i=0; i < days; i++) {
            res.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return res;
    }

    private static List<Date> getPeriodDatesOfWeek(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 8;
        }
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayOfWeek);
        List<Date> dates = new ArrayList<>(7);
        dates.add(calendar.getTime());
        for (int i=0; i < 6; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dates.add(calendar.getTime());
        }
        return dates;
    }
}
