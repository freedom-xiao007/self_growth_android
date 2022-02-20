package com.example.selfgrowth.service.foregroud;

public class AppLogService {

    private static final AppLogService instance = new AppLogService();

    public static AppLogService getInstance() {
        return instance;
    }
}
