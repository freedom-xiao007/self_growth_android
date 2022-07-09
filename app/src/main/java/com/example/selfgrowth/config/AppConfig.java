package com.example.selfgrowth.config;

public class AppConfig {

    private static boolean showTourist = true;

    public static boolean isShowTourist() {
        return showTourist;
    }

    public static void closeShowTourist() {
        showTourist = false;
    }
}
