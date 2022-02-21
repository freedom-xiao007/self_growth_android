package com.example.selfgrowth.service.foregroud;

import android.content.Context;
import android.util.Log;

import com.example.selfgrowth.http.model.AppInfo;
import com.example.selfgrowth.http.model.AppLog;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AppStatisticsService {

    private static final AppStatisticsService instance = new AppStatisticsService();
    public static AppStatisticsService getInstance() {
        return instance;
    }

    private final AppLogService appLogService = AppLogService.getInstance();
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public DashboardStatistics statistics(final Date date, final Context context) {
        final Map<String, AppInfo> packageName2AppInfo = getPackageName2AppInfoMap(context);
        final String day = formatter.format(date);
        final List<AppLog> appLogs = appLogService.getAppLogs(day);

        final Map<String, AtomicInteger> labelTime = new HashMap<>(4);
        final Map<String, Map<String, AtomicInteger>> appTime = new HashMap<>(4);

        appLogs.forEach(log -> {
            final String packageName = log.getPackageName();
            if (!packageName2AppInfo.containsKey(packageName)) {
                Log.w("this package don't match any app info", packageName);
                return;
            }
            final String appName = Objects.requireNonNull(packageName2AppInfo.get(packageName)).getAppName();
            final String label = Objects.requireNonNull(packageName2AppInfo.get(packageName)).getLabel();

            labelTime.computeIfAbsent(label, k -> new AtomicInteger()).getAndAdd(10);
            appTime.computeIfAbsent(label, k -> new HashMap<>()).computeIfAbsent(appName, k -> new AtomicInteger()).getAndAdd(10);
        });

        return DashboardStatistics.builder()
                .groups(createDashboardGroups(labelTime, appTime))
                .build();
    }

    private Map<String, AppInfo> getPackageName2AppInfoMap(final Context context) {
        final List<AppInfo> apps = AppUtils.getApps(context);
        final Map<String, AppInfo> appInfoMap = new HashMap<>(apps.size());
        for (AppInfo app: apps) {
            appInfoMap.put(app.getPackageName(), app);
        }
        return appInfoMap;
    }

    private Map<String, DashboardStatistics.DashboardGroup> createDashboardGroups(Map<String, AtomicInteger> labelTime, Map<String, Map<String, AtomicInteger>> appTime) {
        final Map<String, DashboardStatistics.DashboardGroup> dashboardGroupMap = new HashMap<>(labelTime.size());
        for (String label : labelTime.keySet()) {
            dashboardGroupMap.put(label, DashboardStatistics.DashboardGroup.builder()
                    .name(label)
                    .minutes(labelTime.get(label).get())
                    .apps(createDashboardApps(appTime.get(label)))
                    .build());
        }
        return dashboardGroupMap;
    }

    private List<DashboardStatistics.DashboardApp> createDashboardApps(Map<String, AtomicInteger> appTime) {
        final List<DashboardStatistics.DashboardApp> apps = new ArrayList<>(appTime.size());
        for (String app: appTime.keySet()) {
            apps.add(DashboardStatistics.DashboardApp.builder()
                    .name(app)
                    .minutes(appTime.get(app).get())
                    .build());
        }
        return apps;
    }
}
