package com.example.selfgrowth.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.request.DashboardRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private final DashboardRequest dashboardRequest = new DashboardRequest();
    private DashboardStatistics data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initData(view);
        return view;
    }

    private void initData(View view) {
        dashboardRequest.statistics(
                success -> {
                    String jsonStr = new Gson().toJson(success);
                    this.data = new Gson().fromJson(jsonStr, DashboardStatistics.class);
                    final ListView listView = view.findViewById(R.id.dashboard_group_view_id);
                    final DashboardItemListViewAdapter adapter = new DashboardItemListViewAdapter(requireContext(), data.getGroups().get("learn").getApps());
                    listView.setAdapter(adapter);
                    initComponent(view);
        },
                failed -> Snackbar.make(view, "仪表盘统计数据请求失败:" + failed, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    private void initComponent(View view) {
        if (data == null) {
            return;
        }

        TextView groupShow = view.findViewById(R.id.dashboard_group_name);
        groupShow.setText("类型：" +data.getGroups().keySet().toArray()[0].toString());
        NiceSpinner spinnerOfGroup = view.findViewById(R.id.dashboard_group_spinner);
        spinnerOfGroup.attachDataSource(Arrays.asList(data.getGroups().keySet().toArray()));
        spinnerOfGroup.setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);
            ListView listView = view.findViewById(R.id.dashboard_group_view_id);
            final DashboardItemListViewAdapter adapter = new DashboardItemListViewAdapter(requireContext(), data.getGroups().get(name).getApps());
            listView.setAdapter(adapter);
            groupShow.setText("类型：" + name);
        });
    }

    private DashboardStatistics createData() {
        final Map<String, DashboardStatistics.DashboardGroup> groups = ImmutableMap.<String, DashboardStatistics.DashboardGroup>builder()
                .put("overview", DashboardStatistics.DashboardGroup.builder()
                        .name("overview")
                        .minutes(100)
                        .apps(addApp("overview"))
                        .build())
                .put("learn", DashboardStatistics.DashboardGroup.builder()
                        .name("learn")
                        .minutes(100)
                        .apps(addApp("learn"))
                        .build())
                .put("running", DashboardStatistics.DashboardGroup.builder()
                        .name("running")
                        .minutes(100)
                        .apps(addApp("running"))
                        .build())
                .put("sleep", DashboardStatistics.DashboardGroup.builder()
                        .name("sleep")
                        .minutes(100)
                        .apps(addApp("sleep"))
                        .build())
                .put("task", DashboardStatistics.DashboardGroup.builder()
                        .name("task")
                        .minutes(100)
                        .apps(addApp("task"))
                        .build())
                .build();
        return DashboardStatistics.builder().groups(groups).build();
    }

    private List<DashboardStatistics.DashboardApp> addApp(String name) {
        final List<DashboardStatistics.DashboardApp> apps = new ArrayList<>(3);
        apps.add(DashboardStatistics.DashboardApp.builder()
                .name(name + "1")
                .minutes(100)
                .build());
        apps.add(DashboardStatistics.DashboardApp.builder()
                .name(name + "2")
                .minutes(100)
                .build());
        apps.add(DashboardStatistics.DashboardApp.builder()
                .name(name + "3")
                .minutes(100)
                .build());
        return apps;
    }
}