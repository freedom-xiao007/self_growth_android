package com.example.selfgrowth.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.service.foregroud.AppStatisticsService;
import com.example.selfgrowth.service.foregroud.TaskLogService;
import com.example.selfgrowth.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DailyDashboardFragment extends Fragment {

    private final AppStatisticsService appStatisticsService = AppStatisticsService.getInstance();
    private final TaskLogService taskLogService = TaskLogService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_dashboard, container, false);
        loadData(new Date(), view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(final Date date, final View view) {
        final DashboardStatistics data = appStatisticsService.statistics(new Date(), view.getContext());
        if (data == null || data.getGroups() == null || data.getGroups().keySet().size() <= 0) {
            ((TextView) view.findViewById(R.id.note)).setText("当日无统计数据");
            return;
        }
        Map<String, DashboardStatistics.DashboardGroup> groups = data.getGroups();
        DashboardStatistics.DashboardGroup learnGroup = groups.getOrDefault(LabelEnum.LEARN.getName(), DashboardStatistics.emptyGroup());
        DashboardStatistics.DashboardGroup runningGroup = groups.getOrDefault(LabelEnum.RUNNING.getName(), DashboardStatistics.emptyGroup());
        DashboardStatistics.DashboardGroup sleepGroup = groups.getOrDefault(LabelEnum.SLEEP.getName(), DashboardStatistics.emptyGroup());
        long learnTime = Objects.requireNonNull(learnGroup).getMinutes();
        long runningTime = Objects.requireNonNull(runningGroup).getMinutes();
        long sleepTime = Objects.requireNonNull(sleepGroup).getMinutes();
        int taskComplete = taskLogService.list(new Date()).size();
        ((TextView) view.findViewById(R.id.learn_minutes)).setText(MyTimeUtils.toString(learnTime));
        ((TextView) view.findViewById(R.id.running_minutes)).setText(MyTimeUtils.toString(runningTime));
        ((TextView) view.findViewById(R.id.sleep_minutes)).setText(MyTimeUtils.toString(sleepTime));
        ((TextView)view.findViewById(R.id.task_complete)).setText(String.valueOf(taskComplete));

        List<String> appUserTimes = new ArrayList<>(learnGroup.getApps().size());
        for (DashboardStatistics.DashboardApp app: learnGroup.getApps()) {
            appUserTimes.add(String.format("%s 使用时间： %s", app.getName(), MyTimeUtils.toString(app.getMinutes())));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, appUserTimes);
        ((ListView) view.findViewById(R.id.learn_app_info)).setAdapter(adapter1);

        List<String> taskNames = taskLogService.listLog(date)
                .stream()
                .map(log -> String.format("任务名称： %s", log.getName()))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, taskNames);
        ((ListView) view.findViewById(R.id.task_info)).setAdapter(adapter2);
    }
}
