package com.example.selfgrowth.http;

import android.content.Context;
import android.content.SharedPreferences;

public class HttpConfig {

    private static String address = null;
    private static final String HTTP_CONFIG = "HTTP_CONFIG";
    private static final String SERVER_URL = "SERVER_URL";
    private static final String NET_OPEN = "NET_OPEN";
    private static SharedPreferences preferences;
    private static boolean openNetwork = false;

    public static void setServerUrl(String url) {
        address = url;
        final SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SERVER_URL, url);
        edit.apply();
    }

    public static String getServerUrl() {
        if (address == null) {
            address = preferences.getString(SERVER_URL, "http://127.0.0.1:8080");
        }
        return address;
    }

    public static void init(Context context) {
        preferences = context.getSharedPreferences(HTTP_CONFIG, Context.MODE_PRIVATE);
        address = preferences.getString(SERVER_URL, "http://127.0.0.1:8080");
        openNetwork = preferences.getBoolean(NET_OPEN, false);
    }

    public static boolean isOpenNetwork() {
        return openNetwork;
    }

    public static void openNetwork() {
        openNetwork = true;
        preferences.edit().putBoolean(NET_OPEN, openNetwork).apply();;
    }

    public static void closeNetwork() {
        openNetwork = false;
        preferences.edit().putBoolean(NET_OPEN, openNetwork).apply();;
    }

    public static boolean networkIsOpen() {
        return openNetwork;
    }
}
