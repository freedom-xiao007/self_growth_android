package com.example.selfgrowth.service.backend.xiuxian;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.service.backend.DashboardService;

import java.util.Calendar;

public class CalService {

    private static final CalService instance = new CalService();
    public static CalService getInstance() {
        return instance;
    }

    private final DashboardService dashboardService = DashboardService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DashboardResult getYesterdayData(View view) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        return dashboardService.getPeriodData(yesterday.getTime(), StatisticsTypeEnum.DAY, view, false);
    }
}
