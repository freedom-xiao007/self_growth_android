package com.example.selfgrowth.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.model.XiuXianState;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.xiuxian.XiuXianService;
import com.example.selfgrowth.ui.custum.TableView;
import com.example.selfgrowth.utils.DateUtils;
import com.example.selfgrowth.utils.MyTimeUtils;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DataShareFragment extends Fragment {

    private final DashboardService dashboardService = DashboardService.getInstance();
    private final XiuXianService xiuXianService = XiuXianService.getInstance();
    private final StatisticsTypeEnum statisticsType;

    public DataShareFragment(final StatisticsTypeEnum type) {
        this.statisticsType = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_share, container, false);
        refresh(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refresh(View view) {
        final XiuXianState state = xiuXianService.yesterdaySettlement(view);
        ((TextView) view.findViewById(R.id.lun_hui_time))
                .setText(String.format(Locale.CHINA, "气修%d 体修%d", state.getReincarnationAmountOfQiXiu(), state.getReincarnationAmountOfTiXiu()));
        ((TextView) view.findViewById(R.id.qi_xiu_state))
                .setText(String.format(Locale.CHINA, "%s%d层",
                        state.getQiXiuState().getState().getName(),
                        state.getQiXiuState().getLevel()));
        ((TextView) view.findViewById(R.id.ti_xiu_state))
                .setText(String.format(Locale.CHINA, "%s%d层",
                        state.getTiXiuState().getState().getName(),
                        state.getTiXiuState().getLevel()));

        final DashboardResult statisticsData = dashboardService.getPeriodData(new Date(), statisticsType, view, true);
        ((TextView) view.findViewById(R.id.date)).setText(String.format(Locale.CHINA,
                "%s-%s", statisticsData.getStartDate() == null ? DateUtils.dateShow(new Date()) : statisticsData.getStartDate(),
                statisticsData.getEndDate() == null ? DateUtils.dateShow(new Date()) : statisticsData.getEndDate()));

        TableView overviewTable = view.findViewById(R.id.overview_data);
        overviewTable.clearTableContents()
                .setHeader("分类", "总时间/数量", "每天平均时间")
                .addContent("学习", MyTimeUtils.toString(statisticsData.getLearnTime()), MyTimeUtils.toString(statisticsData.getLearnAverage()))
                .addContent("运动", MyTimeUtils.toString(statisticsData.getRunningTime()), MyTimeUtils.toString(statisticsData.getRunningAverage()))
                .addContent("睡觉", MyTimeUtils.toString(statisticsData.getSleepTime()), MyTimeUtils.toString(statisticsData.getSleepAverage()));
        if (statisticsData.getBooks() > 0) {
            overviewTable.addContent("书籍", String.valueOf(statisticsData.getBooks()), "-");
        }
        if (statisticsData.getBlogs() > 0) {
            overviewTable.addContent("博客", String.valueOf(statisticsData.getBlogs()), "-");
        }
        overviewTable.refreshTable();

        TableView detailTable = view.findViewById(R.id.data_detail);
        detailTable.clearTableContents().setHeader("详细名称", "时间统计/具体名称");
        for (Map.Entry<String, Long> app: statisticsData.getAppTimes().entrySet()) {
            detailTable.addContent(app.getKey(), MyTimeUtils.toString(app.getValue()));
        }
        for (String blog: statisticsData.getWriteBlogs()) {
            detailTable.addContent("博客输出", blog);
        }
        for (String book: statisticsData.getReadBooks()) {
            detailTable.addContent("典籍阅读", book);
        }
        detailTable.refreshTable();
    }
}
