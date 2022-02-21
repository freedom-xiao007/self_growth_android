package com.example.selfgrowth.service.foregroud;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.selfgrowth.http.model.AppLog;
import com.example.selfgrowth.utils.GsonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppLogService {

    private static final AppLogService instance = new AppLogService();
    public static AppLogService getInstance() {
        return instance;
    }

    private final int cacheSize = 30;
    private List<AppLog> logCache = new ArrayList<>(cacheSize);
    private SharedPreferences sharedPreferences;
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public void add(final String packageName) {
        AppLog appLog = new AppLog(new Date(), packageName);
        logCache.add(appLog);
        save(appLog);
    }

    public void initSharedPreferences(Context applicationContext) {
        this.sharedPreferences = applicationContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    private void save(final AppLog appLog) {
        if (sharedPreferences == null) {
            throw new RuntimeException("sharedPreferences isn't init");
        }
        final String today = formatter.format(new Date());
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
}
