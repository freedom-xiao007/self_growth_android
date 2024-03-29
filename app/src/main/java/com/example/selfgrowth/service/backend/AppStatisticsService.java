package com.example.selfgrowth.service.backend;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.model.AppInfo;
import com.example.selfgrowth.model.AppLog;
import com.example.selfgrowth.model.DashboardStatistics;
import com.example.selfgrowth.model.DailyLogModel;
import com.example.selfgrowth.utils.AppUtils;
import com.example.selfgrowth.utils.DateUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class AppStatisticsService {

    private static final AppStatisticsService instance = new AppStatisticsService();
    public static AppStatisticsService getInstance() {
        return instance;
    }

    private final AppLogService appLogService = AppLogService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DashboardStatistics statistics(final Date date, final Context context) {
        final String day = DateUtils.toCustomDay(date);
        final List<AppLog> appLogs = appLogService.getAppLogs(day);
        if (appLogs.isEmpty()) {
            return DashboardStatistics.builder().build();
        }
        appLogs.sort(new AppLog());
        Collections.reverse(appLogs);

        final Map<String, AppInfo> packageName2AppInfo = AppUtils.getPackageName2AppInfoMap(context);
        final Map<String, AtomicLong> labelTime = new HashMap<>(4);
        final Map<String, Map<String, AtomicLong>> appTime = new HashMap<>(4);
        final Map<String, List<DashboardStatistics.AppUseLog>> appUserLogs = new HashMap<>();
        final List<DailyLogModel> dailyLogs = new ArrayList<>(200);

        final AppRecord record = new AppRecord();
        appLogs.forEach(log -> {
            final String packageName = log.getPackageName();
            if (!packageName2AppInfo.containsKey(packageName)) {
                Log.w("this package don't match any app info", packageName);
                return;
            }

            record.updateEnd(log.getDate());
            if (record.isBegin()) {
                record.start(log.getDate(), packageName2AppInfo.get(packageName));
                return;
            }
            if (record.continueRecord(packageName)) {
                return;
            }

            final long interval = Duration.between(record.getStart().toInstant(), record.getEnd().toInstant()).toMinutes();
            if (interval < 1) {
                return;
            }

            final long speedTime = record.cal(log.getDate());
            final String label = record.getLabel();
            labelTime.computeIfAbsent(label, k -> new AtomicLong()).getAndAdd(speedTime);
            appTime.computeIfAbsent(label, k -> new HashMap<>()).computeIfAbsent(record.getAppName(), k -> new AtomicLong()).getAndAdd(speedTime);
            appUserLogs.computeIfAbsent(record.getAppName(), k -> new ArrayList<>())
                    .add(DashboardStatistics.AppUseLog.builder()
                            .startTime(record.getStart())
                            .endTime(record.getEnd())
                            .build());

            dailyLogs.add(new DailyLogModel("开始:" + record.getAppName(),
                    DateUtils.dateString(record.getStart()),
                    DailyLogModel.DailyLogType.ACTIVITY_BEGIN, record.getLabel()));
            dailyLogs.add(new DailyLogModel(String.format(Locale.CHINA, "结束: %s, 一共%d分钟", record.getAppName(), interval),
                          DateUtils.dateString(record.getEnd()),
                          DailyLogModel.DailyLogType.ACTIVITY_END, record.getLabel()));

            record.start(log.getDate(), packageName2AppInfo.get(packageName));
        });

        final long speedTime = record.cal(record.getEnd());
        labelTime.computeIfAbsent(record.getLabel(), k -> new AtomicLong()).getAndAdd(speedTime);
        appTime.computeIfAbsent(record.getLabel(), k -> new HashMap<>()).computeIfAbsent(record.getAppName(), k -> new AtomicLong()).getAndAdd(speedTime);
        appUserLogs.computeIfAbsent(record.getAppName(), k -> new ArrayList<>())
                .add(DashboardStatistics.AppUseLog.builder()
                        .startTime(record.getStart())
                        .endTime(record.getEnd())
                        .build());

        dailyLogs.add(new DailyLogModel("开始:" + record.getAppName(),
                DateUtils.dateString(record.getStart()),
                DailyLogModel.DailyLogType.ACTIVITY_BEGIN, record.getLabel()));
        dailyLogs.add(new DailyLogModel("结束:" + record.getAppName(),
                DateUtils.dateString(record.getEnd()),
                DailyLogModel.DailyLogType.ACTIVITY_END, record.getLabel()));

        return DashboardStatistics.builder()
                .groups(createDashboardGroups(labelTime, appTime, appUserLogs))
                .dailyLogs(dailyLogs)
                .build();
    }



    private Map<String, DashboardStatistics.DashboardGroup> createDashboardGroups(Map<String, AtomicLong> labelTime, Map<String, Map<String, AtomicLong>> appTime, Map<String, List<DashboardStatistics.AppUseLog>> appUserLogs) {
        final Map<String, DashboardStatistics.DashboardGroup> dashboardGroupMap = new HashMap<>(labelTime.size());
        for (String label : labelTime.keySet()) {
            dashboardGroupMap.put(label, DashboardStatistics.DashboardGroup.builder()
                    .name(label)
                    .minutes(Objects.requireNonNull(labelTime.get(label)).get())
                    .apps(createDashboardApps(Objects.requireNonNull(appTime.get(label)), appUserLogs))
                    .build());
        }
        return dashboardGroupMap;
    }

    private List<DashboardStatistics.DashboardApp> createDashboardApps(Map<String, AtomicLong> appTime, Map<String, List<DashboardStatistics.AppUseLog>> appUserLogs) {
        final List<DashboardStatistics.DashboardApp> apps = new ArrayList<>(appTime.size());
        for (String app: appTime.keySet()) {
            apps.add(DashboardStatistics.DashboardApp.builder()
                    .name(app)
                    .minutes(Objects.requireNonNull(appTime.get(app)).get())
                    .logs(appUserLogs.get(app))
                    .build());
        }
        return apps;
    }

    private static class AppRecord {
        private Date start = null;
        private Date end = null;
        private AppInfo appInfo;

        public boolean isBegin() {
            return start == null;
        }

        public void start(final Date date, final AppInfo appInfo) {
            start = date;
            this.appInfo = appInfo;
        }

        public void updateEnd(final Date date) {
            this.end = date;
        }

        public Date getEnd() {
            return end;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public long cal(Date end) {
            if (start == null || end == null) {
                return 0;
            }
            Log.d("app start time：", DateUtils.dateString(start));
            Log.d("app end time：", DateUtils.dateString(end));
            Log.d("app info", appInfo.toString());
            return Duration.between(start.toInstant(), end.toInstant()).toMinutes();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public boolean continueRecord(String packageName) {
            return packageName.equals(appInfo.getPackageName());
        }

        public String getAppName() {
            return appInfo.getAppName();
        }

        public String getLabel() {
            return appInfo.getLabel();
        }

        public Date getStart() {
            return start;
        }
    }
}
