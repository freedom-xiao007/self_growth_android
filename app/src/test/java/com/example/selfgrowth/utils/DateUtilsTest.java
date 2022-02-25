package com.example.selfgrowth.utils;

import com.example.selfgrowth.enums.StatisticsTypeEnum;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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
}
