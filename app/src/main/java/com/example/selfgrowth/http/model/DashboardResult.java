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
    private Map<String, Integer> learnDurations = new HashMap<>();
    @Builder.Default
    private Map<String, Integer> runningDurations = new HashMap<>();
    @Builder.Default
    private Map<String, Integer> sleepDurations = new HashMap<>();

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
        final String start = DateUtils.hourString(log.getStartTime());
        final String end = DateUtils.hourString(log.getEndTime());
        final String key = String.join(":", start, end);
        switch (labelEnum) {
            case LEARN:
                if (!learnDurations.containsKey(key)){
                    learnDurations.put(key, 1);
                    break;
                }
                learnDurations.put(key, learnDurations.get(key) + 1);
                break;
            case RUNNING:
                if (!runningDurations.containsKey(key)) {
                    runningDurations.put(key, 1);
                    break;
                }
                runningDurations.put(key, runningDurations.get(key) + 1);
                break;
            case SLEEP:
                if (!sleepDurations.containsKey(key)) {
                    sleepDurations.put(key, 1);
                    break;
                }
                sleepDurations.put(key, sleepDurations.get(key) +1);
                break;
            default:
                break;
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
}
