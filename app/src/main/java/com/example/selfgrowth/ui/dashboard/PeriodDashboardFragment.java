package com.example.selfgrowth.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
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
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.service.foregroud.DashboardService;
import com.example.selfgrowth.service.foregroud.TaskLogService;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.MyTimeUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

// todo 日期选择的部分看怎么抽取一下，形成一个公用的
public class PeriodDashboardFragment extends Fragment {

    private final TaskLogService taskLogService = TaskLogService.getInstance();
    private final DashboardService dashboardService = DashboardService.getInstance();
    private final StatisticsTypeEnum statisticsType;
    private int yearCache;
    private int monthCache;
    private int dayCache;

    public PeriodDashboardFragment(final StatisticsTypeEnum type) {
        this.statisticsType = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.period_dashboard, container, false);
        initDateCache();
        loadData(new Date(), view);
        return view;
    }

    private void initDateCache() {
        Calendar calendar=Calendar.getInstance();
        yearCache = calendar.get(Calendar.YEAR);
        monthCache = calendar.get(Calendar.MONTH);
        dayCache = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(final Date date, final View view) {
        final DashboardResult result = dashboardService.getPeriodData(date, statisticsType, view, true);
        ((TextView) view.findViewById(R.id.learn_minutes)).setText(MyTimeUtils.toString(result.getLearnTime()));
        ((TextView) view.findViewById(R.id.learn_average)).setText(MyTimeUtils.toString(result.getLearnAverage()));
        ((TextView) view.findViewById(R.id.running_minutes)).setText(MyTimeUtils.toString(result.getRunningTime()));
        ((TextView) view.findViewById(R.id.running_average)).setText(MyTimeUtils.toString(result.getRunningAverage()));
        ((TextView) view.findViewById(R.id.sleep_minutes)).setText(MyTimeUtils.toString(result.getSleepTime()));
        ((TextView) view.findViewById(R.id.sleep_average)).setText(MyTimeUtils.toString(result.getSleepAverage()));
        ((TextView) view.findViewById(R.id.task_complete)).setText(String.valueOf(result.getTaskComplete()));
        ((TextView) view.findViewById(R.id.blogs)).setText(String.valueOf(result.getBlogs()));
        ((TextView) view.findViewById(R.id.books)).setText(String.valueOf(result.getBooks()));

        TextView dateText = ((TextView) view.findViewById(R.id.date));
        final List<Date> periodDate = DateUtils.getPeriodDates(date, statisticsType);
        if (periodDate.isEmpty()) {
            dateText.setText(String.join(" - ", DateUtils.dateShow(date), DateUtils.dateShow(date)));
        } else {
            dateText.setText(String.join(" - ", DateUtils.dateShow(periodDate.get(0)), DateUtils.dateShow(periodDate.get(periodDate.size() - 1))));
        }
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
                loadData(selectDate.getTime(), view);
            });
        });

        List<String> appUserTimes = new ArrayList<>(result.getAppTimes().size());
        for (String appName: result.getAppTimes().keySet()) {
            final long minutes = result.getAppTimes().get(appName).longValue();
            appUserTimes.add(String.format("%s 使用时间： %s", appName, MyTimeUtils.toString(minutes)));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, appUserTimes);
        ((ListView) view.findViewById(R.id.learn_detail)).setAdapter(adapter1);

        initHourCountBar(view, result);
        initHourSpeedBar(view, result);
        setFrequentlyInfo(view, result);

        List<String> blogs = result.getWriteBlogs();
        ArrayAdapter<String> blogsAdapter = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, blogs);
        ((ListView) view.findViewById(R.id.blogs_detail)).setAdapter(blogsAdapter);

        List<String> books = result.getReadBooks();
        ArrayAdapter<String> booksAdapter = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, books);
        ((ListView) view.findViewById(R.id.books_detail)).setAdapter(booksAdapter);
    }

    private void setFrequentlyInfo(View view, DashboardResult result) {
        Map<Integer, Integer> learnCount = result.getLearnHoursCount();
        Map<Integer, Integer> runningCount = result.getRunningHoursCount();
        Map<Integer, Integer> sleepCount = result.getSleepHoursCount();
        Map<Integer, Integer> learnSpeed = result.getLearnHourSpeed();
        Map<Integer, Integer> runningSpeed = result.getRunningHourSpeed();
        Map<Integer, Integer> sleepSpeed = result.getSleepHourSpeed();
        List<Integer> learnFre = hourFre(result.getLearnHoursCount(), result.getLearnHourSpeed());
        List<Integer> runningFre = hourFre(result.getRunningHoursCount(), result.getRunningHourSpeed());
        List<Integer> sleepFre = hourFre(result.getSleepHoursCount(), result.getSleepHourSpeed());
        StringBuilder learnStr = new StringBuilder("学习常用时点：");
        StringBuilder runningStr = new StringBuilder("运动常用时点：");
        StringBuilder sleepStr = new StringBuilder("睡觉常用时点：");

        for (int hour: learnFre) {
            if (countIsMax(learnCount.getOrDefault(hour, 0), runningCount.getOrDefault(hour, 0), sleepCount.getOrDefault(hour, 0)) &&
                speedIsMax(learnSpeed.getOrDefault(hour, 0), runningSpeed.getOrDefault(hour, 0), sleepSpeed.getOrDefault(hour, 0))) {
                learnStr.append(hour).append(",");
            }
        }

        for (int hour: runningFre) {
            if (countIsMax(runningCount.getOrDefault(hour, 0), learnCount.getOrDefault(hour, 0), sleepCount.getOrDefault(hour, 0)) &&
                    speedIsMax(runningSpeed.getOrDefault(hour, 0), learnSpeed.getOrDefault(hour, 0), sleepSpeed.getOrDefault(hour, 0))) {
                runningStr.append(hour).append(",");
            }
        }

        for (int hour: sleepFre) {
            if (countIsMax(sleepCount.getOrDefault(hour, 0), runningCount.getOrDefault(hour, 0), learnCount.getOrDefault(hour, 0)) &&
                    speedIsMax(sleepSpeed.getOrDefault(hour, 0), runningSpeed.getOrDefault(hour, 0), learnSpeed.getOrDefault(hour, 0))) {
                sleepStr.append(hour).append(",");
            }
        }

        ((TextView) view.findViewById(R.id.fre_learn)).setText(learnStr.toString());
        ((TextView) view.findViewById(R.id.fre_running)).setText(runningStr.toString());
        ((TextView) view.findViewById(R.id.fre_sleep)).setText(sleepStr.toString());
    }

    private boolean countIsMax(Integer v1, Integer v2, Integer v3) {
        return isMax(v1, v2, v3);
    }

    private boolean speedIsMax(Integer v1, Integer v2, Integer v3) {
        return isMax(v1, v2, v3);
    }

    private boolean isMax(Integer v1, Integer v2, Integer v3) {
        return v1 > v2 && v1 > v3;
    }

    private List<Integer> hourFre(Map<Integer, Integer> hoursCount, Map<Integer, Integer> hourSpeed) {
        int countAmount = 0;
        int speedAmount = 0;
        for (int hour: hoursCount.keySet()) {
            countAmount += hoursCount.getOrDefault(hour, 0);
            speedAmount += hourSpeed.getOrDefault(hour, 0);
        }
        int countAverage = countAmount / 24;
        int speedAverage = speedAmount / 24;

        List<Integer> fre = new ArrayList<>(24);
        for (int hour: hoursCount.keySet()) {
            if (hoursCount.getOrDefault(hour, 0) >= countAverage && hourSpeed.getOrDefault(hour, 0) >= speedAverage) {
                fre.add(hour);
            }
        }
        return fre;
    }

    private void initHourSpeedBar(View view, DashboardResult result) {
        BarChart chart = view.findViewById(R.id.app_time_bar);
        chart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(24);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setTextColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        xLabels.setTextColor(Color.WHITE);
        xLabels.setSpaceMax(1f);

        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setTextColor(Color.WHITE);

        setHourSpeedData(100, 100, chart, result);
    }

    private void setHourSpeedData(final int count, final float range, final BarChart chart, final DashboardResult result) {
        final Map<Integer, Integer> learnHourSpeed = result.getLearnHourSpeed();
        final Map<Integer, Integer> runningHourSpeed = result.getRunningHourSpeed();
        final Map<Integer, Integer> sleepHourSpeed = result.getSleepHourSpeed();
        final ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            final int learnValue = learnHourSpeed.getOrDefault(i, 0);
            final int runningValue = runningHourSpeed.getOrDefault(i, 0);
            final int sleepValue = sleepHourSpeed.getOrDefault(i, 0);
            values.add(new BarEntry(i, new float[]{learnValue, runningValue, sleepValue}));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "时间分布");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"学习", "运动", "睡觉"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
//            data.setValueFormatter(myValueFormatter());
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }

        chart.setFitBars(true);
        chart.invalidate();
    }

    private void initHourCountBar(final View view, final DashboardResult result) {
        BarChart chart = view.findViewById(R.id.app_num_bar);
        chart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(24);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setTextColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        xLabels.setTextColor(Color.WHITE);
        xLabels.setSpaceMax(1f);

        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(2f);
        l.setXEntrySpace(6f);
        l.setTextColor(Color.WHITE);

        setHourNumData(100, 100, chart, result);
    }

    private void setHourNumData(final int count, final float range, final BarChart chart, final DashboardResult result) {
        final Map<Integer, Integer> learnHourCount = result.getLearnHoursCount();
        final Map<Integer, Integer> runningHourCount = result.getRunningHoursCount();
        final Map<Integer, Integer> sleepHourCount = result.getSleepHoursCount();
        final ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            final int learnValue = learnHourCount.getOrDefault(i, 0);
            final int runningValue = runningHourCount.getOrDefault(i, 0);
            final int sleepValue = sleepHourCount.getOrDefault(i, 0);
            values.add(new BarEntry(i, new float[]{learnValue, runningValue, sleepValue}));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "时间分布");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"学习", "运动", "睡觉"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
//            data.setValueFormatter(myValueFormatter());
            data.setValueTextColor(Color.WHITE);

            chart.setData(data);
        }

        chart.setFitBars(true);
        chart.invalidate();
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }

    public static class MyAxisValueFormatter extends ValueFormatter {

        private final DecimalFormat mFormat;

        public MyAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " $";
        }
    }
}
