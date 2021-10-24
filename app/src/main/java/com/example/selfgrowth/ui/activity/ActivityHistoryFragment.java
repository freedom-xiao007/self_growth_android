package com.example.selfgrowth.ui.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.ActivityRecordModel;
import com.example.selfgrowth.http.request.ActivityRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ActivityHistoryFragment extends Fragment {

    private final ActivityRequest activityRequest = new ActivityRequest();
    private ActivityHistoryListViewAdapter activityListViewAdapter;
    private ListView testLv;//ListView组件

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                R.layout.fragment_activity_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        testLv = (ListView) getView().findViewById(R.id.activity_history_list_view);
        activityRequest.activityHistory("", success -> {
            if (success == null) {
                return;
            }
            Log.d("获取活动列表：", "成功");
            List<Map<String, Object>> taskConfigs = (List<Map<String, Object>>) success;
            final List<ActivityRecordModel> dataList = new ArrayList<>(taskConfigs.size());
            taskConfigs.forEach(task -> {
                final String s = new Gson().toJson(task);
                dataList.add(new Gson().fromJson(s, ActivityRecordModel.class));
            });
            //设置ListView的适配器
            activityListViewAdapter = new ActivityHistoryListViewAdapter(this.getContext(), dataList);
            testLv.setAdapter(activityListViewAdapter);
            testLv.setSelection(4);
        }, failed -> {
            Snackbar.make(getView(), "获取活动列表失败:" + failed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("获取任务列表：", "失败");
        });
    }
}