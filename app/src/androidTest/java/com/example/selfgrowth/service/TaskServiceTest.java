package com.example.selfgrowth.service;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.enums.TaskLearnTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;
import com.example.selfgrowth.http.HttpConfig;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.service.foregroud.TaskService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskServiceTest {

    private TaskService taskService;
    private final String taskName = "taskName";
    private final String group = "group";
    private final String desc = "desc";

    @Before
    public void init() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        HttpConfig.init(appContext);
        taskService = TaskService.getInstance();
        taskService.init(appContext);
        taskService.clearAll();
    }

    @Test
    public void addAndDeleteTest() {
        final String id = addTest(TaskCycleEnum.DEFAULT);
        deleteTest(id);
    }

    @Test
    public void addDefaultAndCompleteTest() {
        final String id = addTest(TaskCycleEnum.DEFAULT);
        taskService.complete(group, id);
        TaskConfig config = taskService.queryOne(group, id);
        assert config == null;
    }

    @Test
    public void addCycleAndCompleteTest() {
        final String id = addTest(TaskCycleEnum.DAILY);
        taskService.complete(group, id);
        TaskConfig config = taskService.queryOne(group, id);
        assert config.getId().equals(id);
        assert config.getIsComplete();
    }

    private String addTest(TaskCycleEnum cycle) {
        final TaskConfig taskConfig = TaskConfig.builder()
                .name(taskName)
                .description(desc)
                .label(LabelEnum.DEFAULT)
                .cycleType(cycle)
                .learnType(TaskLearnTypeEnum.DEFAULT)
                .group(group)
                .taskTypeEnum(TaskTypeEnum.DEFAULT)
                .isComplete(false)
                .build();
        taskService.add(taskConfig, null);
        List<TaskConfig> configs = taskService.query("group", "");
        assert configs.size() == 1;
        assert configs.get(0).getName().equals(taskConfig.getName());
        Log.d("config", configs.get(0).toString());
        return configs.get(0).getId();
    }

    private void deleteTest(final String id) {
        taskService.delete(group, id);
        List<TaskConfig> configs = taskService.query("group", "");
        assert configs.size() == 0;
    }
}
