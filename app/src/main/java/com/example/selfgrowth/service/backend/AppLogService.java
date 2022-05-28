package com.example.selfgrowth.service.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.selfgrowth.model.AppInfo;
import com.example.selfgrowth.model.AppLog;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AppLogService {

    private static final AppLogService instance = new AppLogService();
    public static AppLogService getInstance() {
        return instance;
    }

    private final int cacheSize = 30;
    private List<AppLog> logCache = new ArrayList<>(cacheSize);
    private SharedPreferences sharedPreferences;
    private final String syncServerKey = "isSync";
    /**
     * 是否同步到远程服务器
     */
    private boolean isSyncToWebServer;

    public boolean syncIsOpen() {
        return isSyncToWebServer;
    }

    public void openSyncToWebServer() {
        isSyncToWebServer = true;
        sharedPreferences.edit().putBoolean(syncServerKey, true).apply();
    }

    public void closeSyncToWebServer() {
        isSyncToWebServer = false;
        sharedPreferences.edit().putBoolean(syncServerKey, true).apply();
    }

    public void add(final String packageName) {
        AppLog appLog = new AppLog(new Date(), packageName);
        logCache.add(appLog);
        save(appLog);
    }

    public void initSharedPreferences(Context applicationContext) {
        this.sharedPreferences = applicationContext.getSharedPreferences(AppInfo.APP_INFO, Context.MODE_PRIVATE);
    }

    private void save(final AppLog appLog) {
        if (sharedPreferences == null) {
            throw new RuntimeException("sharedPreferences isn't init");
        }
        final String today = DateUtils.toCustomDay(new Date());
        Set<String> origin = sharedPreferences.getStringSet(today, new HashSet<>());
        List<String> logs = new ArrayList<>(origin.size() + 1);
        logs.addAll(origin);
        logs.add(GsonUtils.getGson().toJson(appLog));
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(today, new HashSet<>(logs));
        edit.apply();
        Log.d("app log -- " + today, appLog.toString());
    }

    public List<AppLog> getAppLogs(final String day) {
        Set<String> origin = sharedPreferences.getStringSet(day, new HashSet<>());
        List<AppLog> logs = new ArrayList<>(origin.size());
        origin.forEach(log -> logs.add(GsonUtils.getGson().fromJson(log, AppLog.class)));
        return logs;
    }

    public List<AppLog> getAppLogs(final Date date, final int limit) {
        final String day = DateUtils.toCustomDay(date);
        List<AppLog> logs = sharedPreferences.getStringSet(day, new HashSet<>()).stream()
                .map(log -> GsonUtils.getGson().fromJson(log, AppLog.class)).collect(Collectors.toList());
        if (logs.size() <= limit) {
            return logs;
        }
        return logs.subList(0, limit);
    }
}
