package com.example.selfgrowth.service.foregroud;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.http.model.DashboardResult;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.GsonUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardService {

    private final static DashboardService instance = new DashboardService();

    private final TaskLogService taskLogService = TaskLogService.getInstance();
    private final AppStatisticsService appStatisticsService = AppStatisticsService.getInstance();
    private SharedPreferences sharedPreferences;

    public static DashboardService getInstance() {
        return instance;
    }

    public void init(Context applicationContext) {
        this.sharedPreferences = applicationContext.getSharedPreferences("STATISTICS", Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DashboardResult getPeriodData(Date date, StatisticsTypeEnum statisticsType, final View view) {
        if (statisticsType.equals(StatisticsTypeEnum.DAY)) {
            return getDayDate(date, view);
        }

        List<Date> dates = DateUtils.getPeriodDates(date, statisticsType);
        Date firstDay = dates.get(0);
        DashboardResult periodData = getFromDb(firstDay, statisticsType);
        if (periodData != null) {
            return periodData;
        }

        periodData = new DashboardResult();
        for (Date dateItem: dates) {
            DashboardResult dailyData = getDayDate(dateItem, view);
            mergeDate(periodData, dailyData);
        }

        periodData.calAverage(statisticsType);

        saveToDb(periodData, firstDay, statisticsType);
        return periodData;
    }

    private void mergeDate(DashboardResult periodData, DashboardResult dailyData) {
        periodData.addLearnTime(dailyData.getLearnTime());
        periodData.addRunningTime(dailyData.getRunningTime());
        periodData.addSleepTime(dailyData.getSleepTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DashboardResult getDayDate(final Date date, final View view) {
        DashboardResult result = getFromDb(date, StatisticsTypeEnum.DAY);
        if (result != null) {
            return result;
        }

        final DashboardStatistics data = appStatisticsService.statistics(new Date(), view.getContext());
        if (data == null || data.getGroups() == null || data.getGroups().keySet().size() <= 0) {
            return new DashboardResult();
        }

        result = new DashboardResult();
        Map<String, DashboardStatistics.DashboardGroup> groups = data.getGroups();
        DashboardStatistics.DashboardGroup learnGroup = groups.getOrDefault(LabelEnum.LEARN.getName(), DashboardStatistics.emptyGroup());
        DashboardStatistics.DashboardGroup runningGroup = groups.getOrDefault(LabelEnum.RUNNING.getName(), DashboardStatistics.emptyGroup());
        DashboardStatistics.DashboardGroup sleepGroup = groups.getOrDefault(LabelEnum.SLEEP.getName(), DashboardStatistics.emptyGroup());
        result.setLearnTime(Objects.requireNonNull(learnGroup).getMinutes());
        result.setRunningTime(Objects.requireNonNull(runningGroup).getMinutes());
        result.setSleepTime(Objects.requireNonNull(sleepGroup).getMinutes());

        result.setTaskComplete(taskLogService.list(new Date()).size());
        for (DashboardStatistics.DashboardApp app: learnGroup.getApps()) {
            result.addAppTime(app.getName(), app.getMinutes());
            for (DashboardStatistics.AppUseLog log: app.getLogs()) {
                result.addAppLog(log, LabelEnum.LEARN);
            }
        }
        for (DashboardStatistics.DashboardApp app: runningGroup.getApps()) {
            for (DashboardStatistics.AppUseLog log: app.getLogs()) {
                result.addAppLog(log, LabelEnum.RUNNING);
            }
        }
        for (DashboardStatistics.DashboardApp app: sleepGroup.getApps()) {
            for (DashboardStatistics.AppUseLog log: app.getLogs()) {
                result.addAppLog(log, LabelEnum.SLEEP);
            }
        }

        for (TaskConfig config: taskLogService.listLog(date)) {
            result.addTaskLog(config);
        }

        saveToDb(result, date, StatisticsTypeEnum.DAY);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveToDb(final DashboardResult result, final Date date, final StatisticsTypeEnum type) {
        final String day = DateUtils.toCustomDay(date);
        final String dbKey = String.join("::", day, type.name());
        sharedPreferences.edit().putString(dbKey, GsonUtils.getGson().toJson(result)).apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private DashboardResult getFromDb(final Date date, final StatisticsTypeEnum type) {
        final String day = DateUtils.toCustomDay(date);
        final String dbKey = String.join("::", day, type.name());
        if (!sharedPreferences.contains(dbKey)) {
            return null;
        }
        return GsonUtils.getGson().fromJson(sharedPreferences.getString(dbKey, null), DashboardResult.class);
    }
}
