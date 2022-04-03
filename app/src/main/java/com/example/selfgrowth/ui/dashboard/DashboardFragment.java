package com.example.selfgrowth.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.model.DashboardStatistics;
import com.example.selfgrowth.service.foregroud.AppStatisticsService;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

    private final AppStatisticsService appStatisticsService = AppStatisticsService.getInstance();
    private DashboardStatistics data;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initData(view);
        initListen(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData(View view) {
        this.data = appStatisticsService.statistics(new Date(), requireContext());
        if (data == null || data.getGroups() == null || data.getGroups().keySet().size() <= 0) {
            ((TextView) view.findViewById(R.id.note)).setText("今日暂无统计数据");
            return;
        }

        TextView groupShow = view.findViewById(R.id.dashboard_group_name);
        groupShow.setText("类型：所有");
        NiceSpinner spinnerOfGroup = view.findViewById(R.id.dashboard_group_spinner);
        List<String> labels = new ArrayList<>(LabelEnum.values().length + 1);
        labels.add("所有");
        for (LabelEnum label: LabelEnum.values()) {
            labels.add(label.getName());
        }
        spinnerOfGroup.attachDataSource(labels);

        todayOverview(view);
    }

    private void initListen(View view) {
        NiceSpinner spinnerOfGroup = view.findViewById(R.id.dashboard_group_spinner);
        TextView groupShow = view.findViewById(R.id.dashboard_group_name);
        spinnerOfGroup.setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);
            if (name.equals("所有")) {
                todayOverview(view);
                return;
            }

            List<DashboardStatistics.DashboardApp> appData;
            DashboardStatistics.DashboardGroup groupData = data.getGroups().get(name);
            if (groupData == null) {
                appData = new ArrayList<>(0);
            } else {
                appData = groupData.getApps();
            }
            ListView listView = view.findViewById(R.id.dashboard_group_view_id);
            final DashboardItemListViewAdapter adapter = new DashboardItemListViewAdapter(requireContext(), appData);
            listView.setAdapter(adapter);
            groupShow.setText("类型：" + name);
        });
    }

    private void todayOverview(View view) {
        if (data == null) {
            return;
        }

        DashboardStatistics.DashboardGroup learnData = data.getGroups().getOrDefault(LabelEnum.LEARN.getName(), null);
        DashboardStatistics.DashboardGroup runningData = data.getGroups().getOrDefault(LabelEnum.RUNNING.getName(), null);
        DashboardStatistics.DashboardGroup sleepData = data.getGroups().getOrDefault(LabelEnum.SLEEP.getName(), null);

        List<DashboardStatistics.DashboardApp> data = new ArrayList<>(20);
        if (learnData == null) {
            data.add(DashboardStatistics.DashboardApp.builder()
                    .name("学习总时长:")
                    .minutes(0)
                    .build());
        } else {
            data.add(DashboardStatistics.DashboardApp.builder()
                    .name("学习总时长:")
                    .minutes(learnData.getMinutes())
                    .build());
            data.addAll(learnData.getApps());
        }
        data.add(DashboardStatistics.DashboardApp.builder()
                .name("运动总时长:")
                .minutes(runningData == null ? 0 : runningData.getMinutes())
                .build());
        data.add(DashboardStatistics.DashboardApp.builder()
                .name("睡觉总时长:")
                .minutes(sleepData == null ? 0 : sleepData.getMinutes())
                .build());
        final DashboardItemListViewAdapter adapter = new DashboardItemListViewAdapter(requireContext(), data);
        final ListView listView = view.findViewById(R.id.dashboard_group_view_id);
        listView.setAdapter(adapter);
    }
}