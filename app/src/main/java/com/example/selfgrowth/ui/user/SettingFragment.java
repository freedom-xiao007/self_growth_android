package com.example.selfgrowth.ui.user;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.http.request.DashboardRequest;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.model.TaskConfig;
import com.example.selfgrowth.service.backend.AppLogService;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.TaskService;
import com.example.selfgrowth.utils.GsonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.internal.LinkedTreeMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingFragment extends Fragment {

    private final TaskService taskService = TaskService.getInstance();
    private final AppLogService appLogService = AppLogService.getInstance();
    private final DashboardService dashboardService = DashboardService.getInstance();
    private final DashboardRequest dashboardRequest = new DashboardRequest();
    private final TaskRequest taskRequest = new TaskRequest();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_sync_setting, container, false);
        view.findViewById(R.id.statistics_data_upload_cloud).setOnClickListener(v -> uploadDailyStatisticsData());
        view.findViewById(R.id.task_sync).setOnClickListener(this::syncTaskData);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadDailyStatisticsData() {
        Snackbar.make(requireView(), "后台进程上传中，可返回进行其他操作", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Set<String> days = appLogService.getDays();
        List<DashboardResult> data = new ArrayList<>(days.size());
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM/dd/yyyy");
        days.forEach(day -> {
            try {
                final DashboardResult result = dashboardService.getPeriodData(dayFormat.parse(day), StatisticsTypeEnum.DAY, requireView(), false);
                result.setDailyLogs(null);
                data.add(result);
                Log.d("upload day data:", result.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        dashboardRequest.upload(data,
            success -> {
                List<DashboardResult> records = convertToRecord((List<LinkedTreeMap>) success);
                records.forEach(dashboardService::add);
                Log.d("upload:", String.format("上传：%d, 拉取: %d", data.size(), records.size()));
                Snackbar.make(requireView(), String.format("上传：%d, 拉取: %d", data.size(), records.size()),
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            },
            failed -> {
                Log.d("upload:", "上传失败");
                Snackbar.make(requireView(), "上传失败:" + failed.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
       );
    }

    private List<DashboardResult> convertToRecord(List<LinkedTreeMap> success) {
        List<DashboardResult> configs = new ArrayList<>(success.size());
        success.forEach(t -> configs.add(GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(t), DashboardResult.class)));
        return configs;
    }

    private void syncTaskData(View v) {
        Snackbar.make(requireView(), "后台进程任务同步中，可返回进行其他操作", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        List<TaskConfig> tasks = taskService.getAllConfig();
        tasks.forEach(t -> t.setId(null));
        taskRequest.sync(tasks,
                success -> {
                    List<TaskConfig> newConfig = convert((List<LinkedTreeMap>) success);
                    Log.d("sync Task:", newConfig.toString());
                    newConfig.forEach(task -> taskService.add(task, v));
                    Snackbar.make(requireView(), "任务同步成功：" + newConfig.size(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                },
                failed -> {
                    Snackbar.make(requireView(), "任务同步失败:" + failed.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
        );
    }

    private List<TaskConfig> convert(List<LinkedTreeMap> success) {
        List<TaskConfig> configs = new ArrayList<>(success.size());
        success.forEach(t -> configs.add(GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(t), TaskConfig.class)));
        return configs;
    }
}
