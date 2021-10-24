package com.example.selfgrowth.ui.task;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.CycleTypeConvert;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.model.TaskRecord;
import com.example.selfgrowth.http.model.TaskTypeConvert;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.ui.activity.ActivityListViewAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskHistoryFragment extends Fragment {

    private final TaskRequest taskRequest = new TaskRequest();
    private TaskHistoryListViewAdapter activityListViewAdapter;
    private ListView listView;//ListView组件

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                R.layout.fragment_task_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.task_history_listview);
        taskRequest.history(success -> {
            if (success == null) {
                return;
            }
            Log.d("获取任务历史列表：", "成功");
            List<Map<String, Object>> taskConfigs = (List<Map<String, Object>>) success;
            final List<TaskRecord> dataList = new ArrayList<>(taskConfigs.size());
            taskConfigs.forEach(task -> {
                final String s = new Gson().toJson(task);
                dataList.add(new Gson().fromJson(s, TaskRecord.class));
            });
            //设置ListView的适配器
            activityListViewAdapter = new TaskHistoryListViewAdapter(this.getContext(), dataList);
            listView.setAdapter(activityListViewAdapter);
            listView.setSelection(4);
        }, failed -> {
            Snackbar.make(getView(), "获取任务历史列表失败:" + failed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("获取任务列表：", "失败");
        });
    }
}
