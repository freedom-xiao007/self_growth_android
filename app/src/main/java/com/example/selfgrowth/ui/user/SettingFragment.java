package com.example.selfgrowth.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.service.foregroud.AppLogService;
import com.example.selfgrowth.service.foregroud.TaskService;
import com.google.android.material.snackbar.Snackbar;

public class SettingFragment extends Fragment {

    private final TaskService taskService = TaskService.getInstance();
    private final AppLogService appLogService = AppLogService.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_sync_setting, container, false);

        boolean taskSyncOpen = taskService.syncIsOpen();
        boolean appSyncOpen = appLogService.syncIsOpen();
        ((TextView) view.findViewById(R.id.task_switch_status)).setText(taskSyncOpen ? "已开启" : "已关闭");
        ((TextView) view.findViewById(R.id.app_switch_status)).setText(appSyncOpen ? "已开启" : "已关闭");

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

        return view;
    }
}
