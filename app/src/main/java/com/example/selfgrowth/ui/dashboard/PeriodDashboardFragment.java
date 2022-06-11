package com.example.selfgrowth.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.TaskLogService;
import com.example.selfgrowth.ui.task.TaskListFragment;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// todo 日期选择的部分看怎么抽取一下，形成一个公用的
public class PeriodDashboardFragment extends Fragment {

    enum TabsEnum {

        OVERVIEW("总览"),
        STATISTICS_FIGURE("统计图"),
        LEARN_OVERVIEW("学习总览"),
        BLOGS("博客列表"),
        BOOKS("书籍列表"),
        ;

        TabsEnum(final String name) {
            this.name = name;
        }

        private final String name;

        public String getName() {
            return name;
        }
    }

    private final DashboardService dashboardService = DashboardService.getInstance();
    private final StatisticsTypeEnum statisticsType;
    private int yearCache;
    private int monthCache;
    private int dayCache;

    private final List<String> tabs = Arrays.stream(TabsEnum.values()).map(TabsEnum::getName).collect(Collectors.toList());
    private final int activeColor = Color.parseColor("#FFFFFF");
    private final int normalColor = Color.parseColor("#666666");
    private final int normalSize = 20;

    public PeriodDashboardFragment(final StatisticsTypeEnum type) {
        this.statisticsType = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initDateCache();
        return inflater.inflate(R.layout.period_dashboard_layout, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(new Date(), view, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(Date date, View view, boolean refresh) {
        final DashboardResult result = dashboardService.getPeriodData(date, statisticsType, view, refresh);
        CollectionAdapter collectionAdapter = new CollectionAdapter(this, result);
        ViewPager2 viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(collectionAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            //这里可以自定义TabView
            TextView tabView = new TextView(requireContext());

            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{};

            int[] colors = new int[]{activeColor, normalColor};
            ColorStateList colorStateList = new ColorStateList(states, colors);
            tabView.setText(tabs.get(position));
            tabView.setTextSize(normalSize);
            tabView.setTextColor(colorStateList);

            tab.setCustomView(tabView);
        });
        //要执行这一句才是真正将两者绑定起来
        mediator.attach();

        TextView dateText = view.findViewById(R.id.date);
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
                init(selectDate.getTime(), view, false);
            });
        });
    }

    class CollectionAdapter extends FragmentStateAdapter {

        private final DashboardResult result;

        public CollectionAdapter(Fragment fragment, DashboardResult result) {
            super(fragment);
            this.result = result;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return a NEW fragment instance in createFragment(int)
            Fragment fragment;
            String tabName = tabs.get(position);
            if (tabName.equals(TabsEnum.OVERVIEW.getName())) {
                fragment = new DataOverview(result);
            }
            else if (tabName.equals(TabsEnum.STATISTICS_FIGURE.getName())) {
                fragment = new StatisticsFigure(result);
            }
            else if (tabName.equals(TabsEnum.LEARN_OVERVIEW.getName())) {
                fragment = new LearnOverview(result);
            }
            else if (tabName.equals(TabsEnum.BLOGS.getName())) {
                fragment = new Blogs(result);
            }
            else if (tabName.equals(TabsEnum.BOOKS.getName())) {
                fragment = new Books(result);
            }
            else {
                fragment = new DataOverview(result);
            }
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(TaskListFragment.ARG_OBJECT, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return tabs.size();
        }
    }

    public static class DataOverview extends Fragment {

        private final DashboardResult result;

        public DataOverview(DashboardResult result) {
            this.result = result;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.period_overview, container, false);
            loadData(view);
            return view;
        }

        private void loadData(final View view) {
            ((TextView) view.findViewById(R.id.learn_minutes)).setText(MyTimeUtils.toString(result.getLearnTime()));
            ((TextView) view.findViewById(R.id.learn_average)).setText(MyTimeUtils.toString(result.getLearnAverage()));
            ((TextView) view.findViewById(R.id.running_minutes)).setText(MyTimeUtils.toString(result.getRunningTime()));
            ((TextView) view.findViewById(R.id.running_average)).setText(MyTimeUtils.toString(result.getRunningAverage()));
            ((TextView) view.findViewById(R.id.sleep_minutes)).setText(MyTimeUtils.toString(result.getSleepTime()));
            ((TextView) view.findViewById(R.id.sleep_average)).setText(MyTimeUtils.toString(result.getSleepAverage()));
            ((TextView) view.findViewById(R.id.task_complete)).setText(String.valueOf(result.getTaskComplete()));
            ((TextView) view.findViewById(R.id.blogs)).setText(String.valueOf(result.getBlogs()));
            ((TextView) view.findViewById(R.id.books)).setText(String.valueOf(result.getBooks()));
        }
    }

    public static class LearnOverview extends Fragment {

        private final DashboardResult result;

        public LearnOverview(DashboardResult result) {
            this.result = result;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
            loadData(view);
            return view;
        }

        private void loadData(final View view) {
            List<String> appUserTimes = new ArrayList<>(result.getAppTimes().size());
            for (String appName: result.getAppTimes().keySet()) {
                final long minutes = result.getAppTimes().get(appName);
                appUserTimes.add(String.format("%s 使用时间： %s", appName, MyTimeUtils.toString(minutes)));
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, appUserTimes);
            ((ListView) view.findViewById(R.id.dashboard_group_view_id)).setAdapter(adapter1);
        }
    }

    public static class Blogs extends Fragment {

        private final DashboardResult result;

        public Blogs(DashboardResult result) {
            this.result = result;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
            loadData(view);
            return view;
        }

        private void loadData(final View view) {
            List<String> blogs = result.getWriteBlogs();
            ArrayAdapter<String> blogsAdapter = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, blogs);
            ((ListView) view.findViewById(R.id.dashboard_group_view_id)).setAdapter(blogsAdapter);
        }
    }

    public static class Books extends Fragment {

        private final DashboardResult result;

        public Books(DashboardResult result) {
            this.result = result;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
            loadData(view);
            return view;
        }

        private void loadData(final View view) {
            List<String> books = result.getReadBooks();
            ArrayAdapter<String> booksAdapter = new ArrayAdapter<>(view.getContext(), R.layout.string_listview, R.id.textView, books);
            ((ListView) view.findViewById(R.id.dashboard_group_view_id)).setAdapter(booksAdapter);
        }
    }

    public static class StatisticsFigure extends Fragment {

        private final DashboardResult result;

        public StatisticsFigure(DashboardResult result) {
            this.result = result;
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.period_statistics_figure, container, false);
            loadData(view);
            return view;
        }

        private void loadData(final View view) {
            initHourCountBar(view, result);
            initHourSpeedBar(view, result);
            setFrequentlyInfo(view, result);
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

    private void initDateCache() {
        Calendar calendar=Calendar.getInstance();
        yearCache = calendar.get(Calendar.YEAR);
        monthCache = calendar.get(Calendar.MONTH);
        dayCache = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
