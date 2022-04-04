package com.example.selfgrowth.service.foregroud;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.utils.GsonUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRequest taskRequest = new TaskRequest();
    private SharedPreferences taskConfigDb;
    private SharedPreferences isSyncToWebServerDb;
    private final String syncServerKey = "setting";
    private final TaskLogService taskLogService = TaskLogService.getInstance();
    /**
     * 是否同步到远程服务器
     */
    private boolean isSyncToWebServer;

    private static final TaskService instance = new TaskService();
    public static TaskService getInstance() {
        return instance;
    }

    public void init(final Context applicationContext) {
        this.taskConfigDb = applicationContext.getSharedPreferences("TASK_SERVICE", Context.MODE_PRIVATE);
        isSyncToWebServerDb = applicationContext.getSharedPreferences("isSyncToWebServer", Context.MODE_PRIVATE);
        isSyncToWebServer = isSyncToWebServerDb.getBoolean(syncServerKey, false);
    }

    public void openSyncToWebServer() {
        isSyncToWebServer = true;
        isSyncToWebServerDb.edit().putBoolean(syncServerKey, true).apply();
    }

    public void closeSyncToWebServer() {
        isSyncToWebServer = false;
        isSyncToWebServerDb.edit().putBoolean(syncServerKey, true).apply();
    }

    public boolean syncIsOpen() {
        return isSyncToWebServer;
    }

    public void add(final TaskConfig config, final View view) {
        final Set<String> origin = taskConfigDb.getStringSet(config.getGroup(), new HashSet<>());
        final List<String> tasks = new ArrayList<>(origin.size() + 1);
        tasks.addAll(origin);
        config.setId(UUID.randomUUID().toString());
        tasks.add(GsonUtils.getGson().toJson(config));

        saveDb(config.getGroup(), new HashSet<>(tasks));
        Log.d("add task: ", config.toString());
        Snackbar.make(view, "任务添加成功", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        if (isSyncToWebServer) {
            taskRequest.update(config,
                    s -> Snackbar.make(view, "任务同步到服务器成功", Snackbar.LENGTH_LONG).setAction("Action", null).show(),
                    f -> Snackbar.make(view, "任务同步到服务器失败：" + f, Snackbar.LENGTH_LONG).setAction("Action", null).show());
        }
    }

    public void delete(final String groupName, final String id) {
        final Set<String> origin = taskConfigDb.getStringSet(groupName, new HashSet<>());
        Set<String> filter = origin.stream().filter(config -> {
            final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
            return !task.getId().equals(id);
        }).collect(Collectors.toSet());

        saveDb(groupName, filter);
        Log.d("delete task: ", id);
    }

    public List<TaskConfig> query(final String groupName, final String taskName) {
        return taskConfigDb.getStringSet(groupName, new HashSet<>())
                .stream()
                .filter(config -> {
                    if (taskName == null || taskName.isEmpty()) {
                        return true;
                    }
                    final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
                    return task.getName().startsWith(taskName);
                })
                .map(config -> {
                    TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
                    task.setIsComplete(taskLogService.hasLog(new Date(), task.getGroup(), task.getName(), task.getCycleType()));
                    return task;
                })
                .sorted(Comparator.comparing(TaskConfig::getIsComplete))
                .collect(Collectors.toList());
    }

    public List<String> getAllGroup() {
        SharedPreferences.Editor edit = taskConfigDb.edit();
        List<String> groups = taskConfigDb.getAll().keySet().stream().filter(group -> {
            Set<String> tasks = taskConfigDb.getStringSet(group, null);
            if (tasks == null || tasks.isEmpty()) {
                edit.remove(group);
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        edit.apply();
        return groups;
    }

    public void complete(final String groupName, final String id) {
        TaskConfig taskConfig = taskConfigDb.getStringSet(groupName, new HashSet<>()).stream().filter(config -> {
            final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
            return task.getId().equals(id);
        }).map(config -> GsonUtils.getGson().fromJson(config, TaskConfig.class)).findAny().orElse(null);

        if (taskConfig == null) {
            return;
        }
        taskLogService.add(taskConfig);

        if (taskConfig.getCycleType().equals(TaskCycleEnum.DEFAULT)) {
            delete(groupName, id);
            return;
        }

        Set<String> updateConfig = taskConfigDb.getStringSet(groupName, new HashSet<>()).stream()
                .map(config -> {
                    final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
                    if (task.getId().equals(id)) {
                        task.setIsComplete(true);
                        return GsonUtils.getGson().toJson(task);
                    }
                    return config;
                }).collect(Collectors.toSet());
        saveDb(groupName, updateConfig);
    }

    private void saveDb(final String groupName, final Set<String> configs) {
        SharedPreferences.Editor edit = taskConfigDb.edit();
        edit.putStringSet(groupName, configs);
        edit.apply();
    }

    public void clearAll() {
        this.taskConfigDb.edit().clear().apply();
    }

    public TaskConfig queryOne(final String groupName, final String id) {
        return taskConfigDb.getStringSet(groupName, new HashSet<>()).stream().filter(config -> {
            final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
            return task.getId().equals(id);
        }).map(config -> GsonUtils.getGson().fromJson(config, TaskConfig.class)).findAny().orElse(null);
    }
}
