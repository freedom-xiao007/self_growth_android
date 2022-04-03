package com.example.selfgrowth.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.model.AppInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppUtils {

    public static List<String> getInstallSoftware(final Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<String> installAppNames = new ArrayList<>(packages.size());
        for(PackageInfo packageInfo: packages) {
            installAppNames.add(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
        }
        return installAppNames;
    }

    public static List<AppInfo> getApps(final Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(AppInfo.APP_INFO, Context.MODE_PRIVATE);
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<AppInfo> apps = new ArrayList<>(packages.size());
        for(PackageInfo packageInfo: packages) {
            final String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            apps.add(AppInfo.builder()
                    .appName(appName)
                    .packageName(packageInfo.packageName)
                    .label(preferences.getString(appName, "其他"))
                    .build());
        }
        return apps;
    }

    public static Map<String, AppInfo> getApps(final Context context, final LabelEnum label) {
        final SharedPreferences preferences = context.getSharedPreferences(AppInfo.APP_INFO, Context.MODE_PRIVATE);
        return context.getPackageManager().getInstalledPackages(0)
                .stream()
                .map(packageInfo -> createAppInfo(packageInfo, preferences, context))
                .filter(appInfo -> appInfo.getLabel().equals(label.getName()))
                .collect(Collectors.toMap(AppInfo::getAppName, appInfo -> appInfo));
    }

    private static AppInfo createAppInfo(final PackageInfo packageInfo, final SharedPreferences preferences, final Context context) {
        final String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        return AppInfo.builder()
                .appName(appName)
                .packageName(packageInfo.packageName)
                .label(preferences.getString(appName, "其他"))
                .build();
    }
}
