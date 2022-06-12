package com.example.selfgrowth.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.codingending.popuplayout.PopupLayout;
import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.model.XiuXianState;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.xiuxian.XiuXianService;
import com.example.selfgrowth.ui.dashboard.DailyDashboardFragment;
import com.example.selfgrowth.ui.dashboard.DashboardFragment;
import com.example.selfgrowth.ui.dashboard.DataShareFragment;
import com.example.selfgrowth.ui.dashboard.PeriodDashboardFragment;

import java.util.Date;
import java.util.Locale;

public class XiuXianFragment extends Fragment {

    private final XiuXianService xiuXianService = XiuXianService.getInstance();
    private final DashboardService dashboardService = DashboardService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xiu_xian, container, false);
        refresh(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refresh(View view) {
        final XiuXianState res = xiuXianService.yesterdaySettlement(view);

        XiuXianState.LianQiState qiXiuState = res.getQiXiuState();
        ((TextView) view.findViewById(R.id.qi_xiu_upgrade_need)).setText(String.format(Locale.CHINA,
                "%s%d层(轮回%d)：升级需%d气力+%d元力", qiXiuState.getState().getName(), qiXiuState.getLevel(),
                res.getReincarnationAmountOfQiXiu(), qiXiuState.getUpgradeNeedQiLi(), qiXiuState.getUpgradeNeedYuanLi()));

        int qiXiuUpgradeProcess = (int) (((double)res.getQiLi() / (double)qiXiuState.getUpgradeNeedQiLi()) * 100);
        ((ProgressBar) view.findViewById(R.id.qi_xiu_upgrade_process)).setProgress(qiXiuUpgradeProcess);

        XiuXianState.TiXiuState tiXiuState = res.getTiXiuState();
        ((TextView) view.findViewById(R.id.ti_xiu_upgrade_need)).setText(String.format(Locale.CHINA,
                "%s%d层(轮回%d)：升级需%d体力+%d元力", tiXiuState.getState().getName(), tiXiuState.getLevel(),
                res.getReincarnationAmountOfTiXiu(), tiXiuState.getUpgradeNeedTiLi(), tiXiuState.getUpgradeNeedYuanLi()));

        int tiXiuUpgradeProcess = (int) (((double)res.getTiLi() / (double)qiXiuState.getUpgradeNeedQiLi()) * 100);
        ((ProgressBar) view.findViewById(R.id.ti_xiu_upgrade_process)).setProgress(tiXiuUpgradeProcess);

        ((TextView) view.findViewById(R.id.current_yuan_li)).setText(String.format(Locale.CHINA,
                "元力：%d", res.getYuanLi()));
        ((TextView) view.findViewById(R.id.current_qi_li)).setText(String.format(Locale.CHINA,
                "气力：%d", res.getQiLi()));
        ((TextView) view.findViewById(R.id.current_ti_li)).setText(String.format(Locale.CHINA,
                "体力：%d", res.getTiLi()));

        DashboardResult stat = dashboardService.getPeriodData(new Date(), StatisticsTypeEnum.DAY, view, true);
        ((TextView) view.findViewById(R.id.today_yuan_li)).setText(String.format(Locale.CHINA,
                "元力：%d", stat.getSleepTime()));
        ((TextView) view.findViewById(R.id.today_qi_li)).setText(String.format(Locale.CHINA,
                "气力：%d", stat.getLearnTime()));
        ((TextView) view.findViewById(R.id.today_ti_li)).setText(String.format(Locale.CHINA,
                "体力：%d", stat.getRunningTime()));

        ((TextView) view.findViewById(R.id.qi_xiu_log)).setText(res.getQiXiuUpgradeMsg());
        ((TextView) view.findViewById(R.id.ti_xiu_log)).setText(res.getTiXiuUpgradeMsg());
        ((TextView) view.findViewById(R.id.xiu_xian_log)).setText(res.getYesterdayLog());

        view.findViewById(R.id.reload_xiu_xian_data).setOnClickListener(v -> {
            xiuXianService.reloadStateFromOldDate(view);
            refresh(view);
        });

        setRoute(view, R.id.xiu_xian_today_data, new DashboardFragment());

        view.findViewById(R.id.xiu_xian_data_overview).setOnClickListener(v -> {
            View settingView = View.inflate(view.getContext(), R.layout.data_overview_menu, null);
            setRoute(settingView, R.id.xiu_xian_daily_dashboard, new DailyDashboardFragment());
            setRoute(settingView, R.id.xiu_xian_week_dashboard, new PeriodDashboardFragment(StatisticsTypeEnum.WEEK));
            setRoute(settingView, R.id.xiu_xian_month_dashboard, new PeriodDashboardFragment(StatisticsTypeEnum.MONTH));
            setRoute(settingView, R.id.xiu_xian_year_dashboard, new PeriodDashboardFragment(StatisticsTypeEnum.YEAR));
            PopupLayout popupLayout= PopupLayout.init(view.getContext(), settingView);
            popupLayout.show(PopupLayout.POSITION_TOP);
        });

        view.findViewById(R.id.share_xiu_xian_data).setOnClickListener(v -> {
            View settingView = View.inflate(view.getContext(), R.layout.data_share_menu, null);
            setRoute(settingView, R.id.xiu_xian_daily_dashboard, new DataShareFragment(StatisticsTypeEnum.DAY));
            setRoute(settingView, R.id.xiu_xian_week_dashboard, new DataShareFragment(StatisticsTypeEnum.WEEK));
            setRoute(settingView, R.id.xiu_xian_month_dashboard, new DataShareFragment(StatisticsTypeEnum.MONTH));
            setRoute(settingView, R.id.xiu_xian_year_dashboard, new DataShareFragment(StatisticsTypeEnum.YEAR));
            PopupLayout popupLayout= PopupLayout.init(view.getContext(), settingView);
            popupLayout.show(PopupLayout.POSITION_TOP);
        });
    }

    private void setRoute(View view, int buttonId, Fragment fragment) {
        view.findViewById(buttonId).setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.xiu_xian, fragment)
                .addToBackStack(null)
                .commit());
    }
}
