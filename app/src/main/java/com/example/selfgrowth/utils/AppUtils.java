package com.example.selfgrowth.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.example.selfgrowth.http.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

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
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<AppInfo> apps = new ArrayList<>(packages.size());
        for(PackageInfo packageInfo: packages) {
            apps.add(AppInfo.builder()
                    .appName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString())
                    .packageName(packageInfo.packageName)
                    .build());
        }

        for (AppInfo app: apps) {
            Log.i("app:", app.toString());
        }
        return apps;
    }}
