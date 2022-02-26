package com.example.selfgrowth.utils;

import com.example.selfgrowth.enums.StatisticsTypeEnum;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DateUtilsTest {

    @Test
    public void getPeriodDatesOfWeekTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse("2022-02-25 20:27:00", formatter);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        List<Date> res = DateUtils.getPeriodDates(date, StatisticsTypeEnum.WEEK);
        for (int i = 0; i < 7; i++) {
            Date item = res.get(i);
            System.out.println(DateUtils.dayString(item));
            assert DateUtils.dayString(item).equals("02/" + (21 + i) + "/2022");
        }


        ldt = LocalDateTime.parse("2022-02-28 20:27:00", formatter);
        date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        res = DateUtils.getPeriodDates(date, StatisticsTypeEnum.WEEK);
        for (int i = 1; i < 6; i++) {
            Date item = res.get(i);
            System.out.println(DateUtils.dayString(item));
            assert DateUtils.dayString(item).equals(String.format("03/%02d/2022", i));
        }
    }

    @Test
    public void getPeriodDatesOfMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse("2022-02-25 20:27:00", formatter);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        List<Date> res = DateUtils.getPeriodDates(date, StatisticsTypeEnum.MONTH);
        for (int i = 0; i < 28; i++) {
            Date item = res.get(i);
            System.out.println(DateUtils.dayString(item));
            assert DateUtils.dayString(item).equals(String.format("02/%02d/2022", i+1));
        }


        ldt = LocalDateTime.parse("2022-03-28 20:27:00", formatter);
        date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        res = DateUtils.getPeriodDates(date, StatisticsTypeEnum.MONTH);
        for (int i = 1; i < 31; i++) {
            Date item = res.get(i);
            System.out.println(DateUtils.dayString(item));
            assert DateUtils.dayString(item).equals(String.format("03/%02d/2022", i+1));
        }
    }


    @Test
    public void getPeriodDatesOfYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse("2022-02-25 20:27:00", formatter);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        List<Date> res = DateUtils.getPeriodDates(date, StatisticsTypeEnum.YEAR);
        assert res.size() == 365;
    }

    @Test
    public void getHourCountTest() throws ParseException {
        Date start = DateUtils.parse("2022-01-10 09:00:00");
        Date end = DateUtils.parse("2022-01-10 11:00:00");
        Map<Integer, Integer> hourCount = DateUtils.getHourCount(start, end);
        System.out.println(hourCount.toString());
        assert hourCount.get(9).equals(1);
        assert hourCount.get(10).equals(1);
    }
}
