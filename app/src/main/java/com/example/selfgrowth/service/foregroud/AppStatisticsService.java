package com.example.selfgrowth.service.foregroud;

public class AppStatisticsService {

    private static final AppStatisticsService instance = new AppStatisticsService();

    public static AppStatisticsService getInstance() {
        return instance;
    }
}
