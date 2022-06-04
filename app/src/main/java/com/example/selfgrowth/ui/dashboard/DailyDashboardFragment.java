package com.example.selfgrowth.ui.dashboard;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.model.DailyLogModel;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.TaskLogService;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.MyTimeUtils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DailyDashboardFragment extends Fragment {

    private final TaskLogService taskLogService = TaskLogService.getInstance();
    private final DashboardService dashboardService = DashboardService.getInstance();
    private int yearCache;
    private int monthCache;
    private int dayCache;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_dashboard, container, false);
        initDateCache();
        loadData(new Date(), view, true);
        return view;
    }

    private void initDateCache() {
        Calendar calendar=Calendar.getInstance();
        yearCache = calendar.get(Calendar.YEAR);
        monthCache = calendar.get(Calendar.MONTH);
        dayCache = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(final Date date, final View view, final boolean refresh) {
        final DashboardResult result = dashboardService.getPeriodData(date, StatisticsTypeEnum.DAY, view, refresh);
        ((TextView) view.findViewById(R.id.learn_minutes)).setText(MyTimeUtils.toString(result.getLearnTime()));
        ((TextView) view.findViewById(R.id.running_minutes)).setText(MyTimeUtils.toString(result.getRunningTime()));
        ((TextView) view.findViewById(R.id.sleep_minutes)).setText(MyTimeUtils.toString(result.getSleepTime()));
        ((TextView) view.findViewById(R.id.task_complete)).setText(String.valueOf(result.getTaskComplete()));
        TextView dateText = view.findViewById(R.id.date);
        dateText.setText(DateUtils.dateShow(date));
        dateText.setOnClickListener(view1 -> {
            DatePickerDialog dialog=new DatePickerDialog(requireContext(),null, yearCache, monthCache, dayCache);
            //把日期对话框显示在界面上
            dialog.show();
            dialog.setOnDateSetListener((datePicker, year, month, day) -> {
                yearCache = year;
                monthCache = month;
                dayCache = day;
                Calendar selectDate = Calendar.getInstance();
                selectDate.set(year, month, day);
                loadData(selectDate.getTime(), view, false);
            });
        });

        if (result.getDailyLogs() != null) {
            List<DailyLogModel> dailyLogs = result.getDailyLogs()
                    .stream()
                    .filter(item -> !item.getLabel().equals(LabelEnum.DEFAULT))
                    .sorted(Comparator.comparing(DailyLogModel::getDate))
                    .collect(Collectors.toList());
            DailyLogAdapter adapter = new DailyLogAdapter(dailyLogs);
            RecyclerView timelineView = view.findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManage = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
            timelineView.setLayoutManager(layoutManage);
            timelineView.setAdapter(adapter);
        }
    }
}
