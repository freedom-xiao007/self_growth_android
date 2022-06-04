package com.example.selfgrowth.ui.user;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.http.HttpConfig;
import com.example.selfgrowth.http.api.DashboardApi;
import com.example.selfgrowth.http.request.DashboardRequest;
import com.example.selfgrowth.model.DailyLogModel;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.service.backend.AppLogService;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.TaskService;
import com.example.selfgrowth.utils.DateUtils;
import com.google.android.material.snackbar.Snackbar;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_sync_setting, container, false);

        boolean taskSyncOpen = taskService.syncIsOpen();
        boolean appSyncOpen = appLogService.syncIsOpen();
        ((TextView) view.findViewById(R.id.net_switch_status)).setText(HttpConfig.isOpenNetwork() ? "已开启" : "已关闭");
        ((TextView) view.findViewById(R.id.task_switch_status)).setText(taskSyncOpen ? "已开启" : "已关闭");
        ((TextView) view.findViewById(R.id.app_switch_status)).setText(appSyncOpen ? "已开启" : "已关闭");

        CompoundButton netSwitch = view.findViewById(R.id.net_sync_switch);
        netSwitch.setChecked(HttpConfig.isOpenNetwork());
        netSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                HttpConfig.openNetwork();
                Snackbar.make(view, "开启网络同步", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                HttpConfig.closeNetwork();
                Snackbar.make(view, "关闭网络同步", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        CompoundButton taskSwitch = view.findViewById(R.id.task_sync_switch);
        taskSwitch.setChecked(taskSyncOpen);
        taskSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskService.openSyncToWebServer();
                Snackbar.make(view, "开启任务数据同步", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                taskService.closeSyncToWebServer();
                Snackbar.make(view, "关闭任务数据同步", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        CompoundButton appSwitch = view.findViewById(R.id.app_sync_switch);
        appSwitch.setChecked(appSyncOpen);
        appSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                appLogService.openSyncToWebServer();
                Snackbar.make(view, "开启手机应用数据同步", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                appLogService.closeSyncToWebServer();
                Snackbar.make(view, "关闭手机应用数据同步", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        view.findViewById(R.id.statistics_data_upload_cloud).setOnClickListener(v -> uploadDailyStatisticsData());

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
                Log.d("upload:", "上传成功：" + data.size());
                Snackbar.make(requireView(), "上传成功：" + data.size(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            },
            failed -> {
                Log.d("upload:", "上传失败");
                Snackbar.make(requireView(), "上传失败", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
       );
    }
}
