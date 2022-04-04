package com.example.selfgrowth.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;
import com.example.selfgrowth.utils.DateUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
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
    private int blogs;
    private int books;
    private String startDate;
    private String endDate;

    private List<DailyLogModel> dailyLogs;

    @Builder.Default
    private Map<String, Long> appTimes = new HashMap<>();

    @Builder.Default
    private Map<Integer, Integer> learnHourSpeed = new HashMap<>(24);
    @Builder.Default
    private Map<Integer, Integer> learnHoursCount = new HashMap<>(24);

    @Builder.Default
    private Map<Integer, Integer> runningHourSpeed = new HashMap<>(24);
    @Builder.Default
    private Map<Integer, Integer> runningHoursCount = new HashMap<>(24);

    @Builder.Default
    private Map<Integer, Integer> sleepHourSpeed = new HashMap<>(24);
    @Builder.Default
    private Map<Integer, Integer> sleepHoursCount = new HashMap<>(24);

    @Builder.Default
    private Map<LabelEnum, Integer> taskLabelStatistics = new HashMap<>();

    @Builder.Default
    private List<String> readBooks = new ArrayList<>();

    @Builder.Default
    private List<String> writeBlogs = new ArrayList<>();

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
        if (Duration.between(log.getStartTime().toInstant(), log.getEndTime().toInstant()).toMinutes() < 1) {
            return;
        }
        final Map<Integer, Integer> hourCount = DateUtils.getHourCount(log.getStartTime(), log.getEndTime());
        final Map<Integer, Integer> hourSpeed = DateUtils.getHourSpeed(log.getStartTime(), log.getEndTime());
        switch (labelEnum) {
            case LEARN:
                mergeLearnTime(hourCount, hourSpeed);
                break;
            case RUNNING:
                mergeRunningTime(hourCount, hourSpeed);
                break;
            case SLEEP:
                mergeSleepTime(hourCount, hourSpeed);
                break;
            default:
                break;
        }
    }

    public void addTaskLogToDailyLog(final TaskConfig config) {
        dailyLogs.add(new DailyLogModel("完成任务:" + config.getName(),
                DateUtils.dateString(config.getCompleteDate() == null ? new Date() : config.getCompleteDate()),
                DailyLogModel.DailyLogType.TASK_COMPLETE, config.getLabel()));
    }

    private void mergeLearnTime(final Map<Integer, Integer> speed, final Map<Integer, Integer> hourCount) {
        for (Integer key: hourCount.keySet()) {
            learnHoursCount.put(key, 1);
        }
        for (Integer key: speed.keySet()) {
            learnHourSpeed.put(key, learnHourSpeed.getOrDefault(key, 0) + speed.get(key));
        }
    }

    private void mergeRunningTime(final Map<Integer, Integer> speed, final Map<Integer, Integer> hourCount) {
        for (Integer key: hourCount.keySet()) {
            runningHoursCount.put(key, 1);
        }
        for (Integer key: speed.keySet()) {
            runningHourSpeed.put(key, runningHourSpeed.getOrDefault(key, 0) + speed.get(key));
        }
    }

    private void mergeSleepTime(final Map<Integer, Integer> speed, final Map<Integer, Integer> hourCount) {
        for (Integer key: hourCount.keySet()) {
            sleepHoursCount.put(key, 1);
        }
        for (Integer key: speed.keySet()) {
            sleepHourSpeed.put(key, sleepHourSpeed.getOrDefault(key, 0) + speed.get(key));
        }
    }

    public void addTaskLog(final TaskConfig config) {
        if (taskLabelStatistics.containsKey(config.getLabel())) {
            taskLabelStatistics.put(config.getLabel(), taskLabelStatistics.get(config.getLabel()) + 1);
        } else {
            taskLabelStatistics.put(config.getLabel(), 1);
        }
        if (config.getTaskTypeEnum().equals(TaskTypeEnum.BOOK)) {
            books += 1;
            readBooks.add(config.getName());
        } else if (config.getTaskTypeEnum().equals(TaskTypeEnum.NOTE)) {
            blogs += 1;
            writeBlogs.add(config.getName());
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

    public void addLearnHourSpeed(Map<Integer, Integer> learnHourSpeed) {
        for (Integer hour: learnHourSpeed.keySet()) {
            this.learnHourSpeed.put(hour, this.learnHourSpeed.getOrDefault(hour, 0) + learnHourSpeed.get(hour));
        }
    }

    public void addRunningHourSpeed(Map<Integer, Integer> runningHourSpeed) {
        for (Integer hour: runningHourSpeed.keySet()) {
            this.runningHourSpeed.put(hour, this.runningHourSpeed.getOrDefault(hour, 0) + runningHourSpeed.get(hour));
        }
    }

    public void addSleepHourSpeed(Map<Integer, Integer> sleepHourSpeed) {
        for (Integer hour: sleepHourSpeed.keySet()) {
            this.sleepHourSpeed.put(hour, this.sleepHourSpeed.getOrDefault(hour, 0) + sleepHourSpeed.get(hour));
        }
    }

    public void addAppTimes(Map<String, Long> appTimes) {
        for (String app: appTimes.keySet()) {
            this.appTimes.put(app, this.appTimes.getOrDefault(app, 0L) + appTimes.get(app));
        }
    }

    public void addCompleteTask(long taskComplete) {
        this.taskComplete += taskComplete;
    }

    public void addWriteBlogs(int blogs, List<String> writeBlogs) {
        this.blogs += blogs;
        this.writeBlogs.addAll(writeBlogs);
    }

    public void addReadBooks(int books, List<String> readBooks) {
        this.books += books;
        this.readBooks.addAll(readBooks);
    }
}
