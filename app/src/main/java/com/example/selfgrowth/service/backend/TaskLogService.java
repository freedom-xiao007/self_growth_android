package com.example.selfgrowth.service.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.model.TaskConfig;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskLogService {

    private SharedPreferences sharedPreferences;

    private static final TaskLogService instance = new TaskLogService();
    public static TaskLogService getInstance() {
        return instance;
    }

    public void init(final Context applicationContext) {
        this.sharedPreferences = applicationContext.getSharedPreferences("TASK_LOG", Context.MODE_PRIVATE);
    }

    public void add(final TaskConfig config) {
        config.setCompleteDate(new Date());

        final String day = DateUtils.toCustomDay(new Date());
        final Set<String> origin = sharedPreferences.getStringSet(day, new HashSet<>());
        final List<String> logs = new ArrayList<>(origin.size() + 1);
        logs.addAll(origin);
        logs.add(GsonUtils.getGson().toJson(config));

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(day, new HashSet<>(logs));
        edit.apply();
        Log.d("add task log: ", config.toString());
    }

    public void delete(final String day, final String taskName) {
        final Set<String> origin = sharedPreferences.getStringSet(day, new HashSet<>());
        Set<String> filter = origin.stream().filter(config -> {
            final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
            return !task.getName().equals(taskName);
        }).collect(Collectors.toSet());

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(day, filter);
        edit.apply();
        Log.d("delete task log: ", taskName);
    }

    public Set<String> list(final Date date) {
        final String day = DateUtils.toCustomDay(date);
        if (sharedPreferences.contains(day)) {
            return sharedPreferences.getStringSet(day, new HashSet<>(0));
        }
        return new HashSet<>(0);
    }

    public Set<TaskConfig> listLog(final Date date) {
        final String day = DateUtils.toCustomDay(date);
        if (sharedPreferences.contains(day)) {
            return sharedPreferences.getStringSet(day, new HashSet<>(0))
                    .stream()
                    .map(log -> GsonUtils.getGson().fromJson(log, TaskConfig.class))
                    .collect(Collectors.toSet());
        }
        return new HashSet<>(0);
    }

    public Boolean hasLog(Date date, String group, String name, TaskCycleEnum cycleType) {
        List<Date> periodDate = DateUtils.getPeriodDates(date, cycleType);
        for (Date day: periodDate) {
            if (hasCompleteLog(day, group, name)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCompleteLog(Date date, String group, String name) {
        final String day = DateUtils.toCustomDay(date);
        if (sharedPreferences.contains(day)) {
            return sharedPreferences.getStringSet(day, new HashSet<>(0))
                    .stream()
                    .map(log -> GsonUtils.getGson().fromJson(log, TaskConfig.class))
                    .anyMatch(log -> log.getGroup().equals(group) && log.getName().equals(name));
        }
        return false;
    }
}
