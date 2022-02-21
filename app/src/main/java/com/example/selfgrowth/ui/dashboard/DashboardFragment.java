package com.example.selfgrowth.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.service.foregroud.AppStatisticsService;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private final AppStatisticsService appStatisticsService = AppStatisticsService.getInstance();
    private DashboardStatistics data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initData(view);
        return view;
    }

    private void initData(View view) {
        this.data = appStatisticsService.statistics(new Date(), requireContext());
        if (data.getGroups().keySet().size() > 0) {
            final String firstGroup = (String) data.getGroups().keySet().toArray()[0];
            final DashboardItemListViewAdapter adapter = new DashboardItemListViewAdapter(requireContext(), data.getGroups().get(firstGroup).getApps());
            final ListView listView = view.findViewById(R.id.dashboard_group_view_id);
            listView.setAdapter(adapter);
            initComponent(view);
        }
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
}