package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.model.AppHistory;
import com.example.selfgrowth.model.AppInfo;
import com.example.selfgrowth.service.backend.AppLogService;
import com.example.selfgrowth.utils.AppUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppHistoryFragment extends Fragment {

    private final AppLogService appLogService = AppLogService.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_history, container, false);

        final SharedPreferences preferences = requireContext().getSharedPreferences(AppInfo.APP_INFO, Context.MODE_PRIVATE);
        List<AppInfo> apps = AppUtils.getApps(view.getContext());
        apps.forEach(app -> app.setLabel(preferences));

        Map<String, AppInfo> packageName2AppInfo = AppUtils.getPackageName2AppInfoMap(requireContext());
        List<AppHistory> history = appLogService.getAppLogs(new Date(), 30).stream().map(log -> {
            final String packageName = log.getPackageName();
            final AppInfo appInfo = packageName2AppInfo.get(packageName);
            return AppHistory.builder()
                    .packageName(packageName)
                    .createTime(log.getDate())
                    .appName(appInfo == null ? "未知" : appInfo.getAppName())
                    .label(appInfo == null ? "其他" : appInfo.getLabel())
                    .build();
        }).collect(Collectors.toList());

        ListView listView = view.findViewById(R.id.list_view);
        final AppHistoryViewAdapter adapter = new AppHistoryViewAdapter(view.getContext(), history);
        listView.setAdapter(adapter);

        return view;
    }
}
