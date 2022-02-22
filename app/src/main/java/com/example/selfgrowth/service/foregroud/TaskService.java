package com.example.selfgrowth.service.foregroud;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.utils.GsonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRequest taskRequest = new TaskRequest();
    private SharedPreferences sharedPreferences;

    private static final TaskService instance = new TaskService();
    public static TaskService getInstance() {
        return instance;
    }

    public void init(final Context applicationContext) {
        this.sharedPreferences = applicationContext.getSharedPreferences("TASK_SERVICE", Context.MODE_PRIVATE);
    }

    public void add(final TaskConfig config) {
        final Set<String> origin = sharedPreferences.getStringSet(config.getGroup(), new HashSet<>());
        final List<String> tasks = new ArrayList<>(origin.size() + 1);
        tasks.addAll(origin);
        tasks.add(GsonUtils.getGson().toJson(config));

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(config.getGroup(), new HashSet<>(tasks));
        edit.apply();
        Log.d("add task: ", config.toString());
    }

    public void delete(final String groupName, final String taskName) {
        final Set<String> origin = sharedPreferences.getStringSet(groupName, new HashSet<>());
        Set<String> filter = origin.stream().filter(config -> {
            final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
            return !task.getName().equals(taskName);
        }).collect(Collectors.toSet());

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putStringSet(groupName, filter);
        edit.apply();
        Log.d("delete task: ", taskName);
    }

    public List<TaskConfig> query(final String groupName, final String taskName) {
        return sharedPreferences.getStringSet(groupName, new HashSet<>()).stream().filter(config -> {
            if (taskName == null || taskName.isEmpty()) {
                return true;
            }
            final TaskConfig task = GsonUtils.getGson().fromJson(config, TaskConfig.class);
            return task.getName().startsWith(taskName);
        }).map(config -> GsonUtils.getGson().fromJson(config, TaskConfig.class)).collect(Collectors.toList());
    }

    public Set<String> groups() {
        return sharedPreferences.getAll().keySet();
    }
}
