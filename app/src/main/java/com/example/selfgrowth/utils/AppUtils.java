package com.example.selfgrowth.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

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
}
