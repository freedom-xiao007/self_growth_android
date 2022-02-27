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
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.http.model.DashboardResult;
import com.example.selfgrowth.service.foregroud.DashboardService;
import com.example.selfgrowth.service.foregroud.TaskLogService;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailyDashboardFragment extends Fragment {

    private final TaskLogService taskLogService = TaskLogService.getInstance();
    private final DashboardService dashboardService = DashboardService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_dashboard, container, false);
        loadData(new Date(), view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(final Date date, final View view) {
        final DashboardResult result = dashboardService.getPeriodData(date, StatisticsTypeEnum.DAY, view, true);
        ((TextView) view.findViewById(R.id.learn_minutes)).setText(MyTimeUtils.toString(result.getLearnTime()));
        ((TextView) view.findViewById(R.id.running_minutes)).setText(MyTimeUtils.toString(result.getRunningTime()));
        ((TextView) view.findViewById(R.id.sleep_minutes)).setText(MyTimeUtils.toString(result.getSleepTime()));
        ((TextView) view.findViewById(R.id.task_complete)).setText(String.valueOf(result.getTaskComplete()));
        ((TextView) view.findViewById(R.id.date)).setText(DateUtils.dayString(date));

        final Map<String, Long> appTimes = result.getAppTimes();
        List<String> appUserTimes = new ArrayList<>(result.getAppTimes().size());
        for (String appName: appTimes.keySet()) {
            final long minutes = appTimes.get(appName);
            appUserTimes.add(String.format("%s 使用时间： %s", appName, MyTimeUtils.toString(minutes)));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, appUserTimes);
        ((ListView) view.findViewById(R.id.learn_app_info)).setAdapter(adapter1);

        List<String> taskNames = taskLogService.listLog(date)
                .stream()
                .map(log -> String.format("任务名称： %s     标签：%s    类型：%s", log.getName(), log.getLabel(), log.getTaskTypeEnum().getName()))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, taskNames);
        ((ListView) view.findViewById(R.id.task_info)).setAdapter(adapter2);
    }
}
