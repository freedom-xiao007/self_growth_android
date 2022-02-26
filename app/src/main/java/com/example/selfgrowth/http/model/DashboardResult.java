package com.example.selfgrowth.http.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;
import com.example.selfgrowth.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResult {

    private long learnTime;
    private long learnAverage;
    private long runningTime;
    private long runningAverage;
    private long sleepTime;
    private long sleepAverage;
    private long taskComplete;

    @Builder.Default
    private Map<String, Long> appTimes = new HashMap<>();

    @Builder.Default
    private Map<Integer, Integer> learnHoursTimes = new HashMap<>(24);
    @Builder.Default
    private Map<Integer, Integer> learnHoursCount = new HashMap<>(24);

    @Builder.Default
    private Map<Integer, Integer> runningHoursTimes = new HashMap<>(24);
    @Builder.Default
    private Map<Integer, Integer> runningHoursCount = new HashMap<>(24);

    @Builder.Default
    private Map<Integer, Integer> sleepHoursTimes = new HashMap<>(24);
    @Builder.Default
    private Map<Integer, Integer> sleepHoursCount = new HashMap<>(24);

    @Builder.Default
    private Map<LabelEnum, Integer> taskLabelStatistics = new HashMap<>();

    @Builder.Default
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
        // todo 后面看看能不能不使用原子类型，而使用compute
        if (!appTimes.containsKey(appName)) {
            appTimes.put(appName, speedTime);
            return;
        }
        appTimes.put(appName, speedTime + appTimes.get(appName));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addAppLog(final DashboardStatistics.AppUseLog log, final LabelEnum labelEnum) {
        final Map<Integer, Integer> hourCount = DateUtils.getHourCount(log.getStartTime(), log.getEndTime());
        switch (labelEnum) {
            case LEARN:
                mergeLearnCount(hourCount);
                break;
            case RUNNING:
                mergeRunningCount(hourCount);
                break;
            case SLEEP:
                mergeSleepCount(hourCount);
                break;
            default:
                break;
        }
    }

    private void mergeLearnCount(final Map<Integer, Integer> hourCount) {
        for (Integer key: hourCount.keySet()) {
            learnHoursCount.put(key, learnHoursCount.getOrDefault(key, 0) + hourCount.get(key));
        }
    }

    private void mergeRunningCount(final Map<Integer, Integer> hourCount) {
        for (Integer key: hourCount.keySet()) {
            runningHoursCount.put(key, runningHoursCount.getOrDefault(key, 0) + hourCount.get(key));
        }
    }

    private void mergeSleepCount(final Map<Integer, Integer> hourCount) {
        for (Integer key: hourCount.keySet()) {
            sleepHoursCount.put(key, sleepHoursCount.getOrDefault(key, 0) + hourCount.get(key));
        }
    }

    public void addTaskLog(final TaskConfig config) {
        if (taskLabelStatistics.containsKey(config.getLabel())) {
            taskLabelStatistics.put(config.getLabel(), taskLabelStatistics.get(config.getLabel()) + 1);
        } else {
            taskLabelStatistics.put(config.getLabel(), 1);
        }
        if (config.getTaskTypeEnum().equals(TaskTypeEnum.BOOK)) {
            readBooks.add(config.getName());
        }
    }

    public void addLearnHourCount(Map<Integer, Integer> learnHoursCount) {
        for (Integer hour: learnHoursCount.keySet()) {
            this.learnHoursCount.put(hour, this.learnHoursCount.getOrDefault(hour, 0) + learnHoursCount.get(hour));
        }
    }

    public void addRunningHourCount(Map<Integer, Integer> runningHoursCount) {
        for (Integer hour: runningHoursCount.keySet()) {
            this.runningHoursCount.put(hour, this.runningHoursCount.getOrDefault(hour, 0) + runningHoursCount.get(hour));
        }
    }

    public void addSleepHourCount(Map<Integer, Integer> sleepHoursCount) {
        for (Integer hour: sleepHoursCount.keySet()) {
            this.sleepHoursCount.put(hour, this.sleepHoursCount.getOrDefault(hour, 0) + sleepHoursCount.get(hour));
        }
    }
}
