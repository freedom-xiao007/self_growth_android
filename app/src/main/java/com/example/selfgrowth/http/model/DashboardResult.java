package com.example.selfgrowth.http.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;
import com.example.selfgrowth.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResult {

    private long learnTime = 0;
    private long learnAverage = 0;
    private long runningTime = 0;
    private long runningAverage = 0;
    private long sleepTime = 0;
    private long sleepAverage = 0;
    private long taskComplete = 0;

    private Map<String, AtomicLong> appTimes = new HashMap<>();

    private Map<String, AtomicInteger> learnDurations = new HashMap<>();
    private Map<String, AtomicInteger> runningDurations = new HashMap<>();
    private Map<String, AtomicInteger> sleepDurations = new HashMap<>();

    private Map<LabelEnum, AtomicInteger> taskLabelStatistics = new HashMap<>();

    private List<String> readBooks = new ArrayList<>();

    public void addLearnTime(final long value) {
        this.learnTime += value;
    }

    public void addRunningTime(final long value) {
        this.runningTime += value;
    }

    public void addSleepTime(final long value) {
        this.sleepTime += value;
    }

    public void calAverage(final StatisticsTypeEnum type) {
        int days = 1;
        switch (type) {
            case WEEK:
                days = 7;
                break;
            case MONTH:
                days = 30;
                break;
            case YEAR:
                days = 365;
                break;
        }
        learnAverage = learnTime / days;
        runningAverage = runningTime / days;
        sleepAverage = sleepTime / days;
    }

    public void addAppTime(final String appName, final long speedTime) {
        appTimes.computeIfAbsent(appName, k -> new AtomicLong(0)).getAndAdd(speedTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addAppLog(final DashboardStatistics.AppUseLog log, final LabelEnum labelEnum) {
        final String start = DateUtils.hourString(log.getStartTime());
        final String end = DateUtils.hourString(log.getEndTime());
        final String key = String.join(":", start, end);
        switch (labelEnum) {
            case LEARN:
                learnDurations.computeIfAbsent(key, k -> new AtomicInteger(0)).getAndAdd(1);
                break;
            case RUNNING:
                runningDurations.computeIfAbsent(key, k -> new AtomicInteger(0)).getAndAdd(1);
                break;
            case SLEEP:
                sleepDurations.computeIfAbsent(key, k -> new AtomicInteger(0)).getAndAdd(1);
                break;
            default:
                break;
        }
    }

    public void addTaskLog(final TaskConfig config) {
        taskLabelStatistics.computeIfAbsent(config.getLabel(), k -> new AtomicInteger(0)).getAndAdd(1);
        if (config.getTaskTypeEnum().equals(TaskTypeEnum.BOOK)) {
            readBooks.add(config.getName());
        }
    }
}
