package com.example.selfgrowth;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.HttpConfig;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.service.backend.AppLogService;
import com.example.selfgrowth.service.backend.DashboardService;
import com.example.selfgrowth.service.backend.xiuxian.CalService;
import com.example.selfgrowth.service.backend.xiuxian.XiuXianService;
import com.example.selfgrowth.service.foregroud.MonitorActivityService;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.selfgrowth.databinding.ActivityMainBinding;
import com.example.selfgrowth.service.backend.TaskLogService;
import com.example.selfgrowth.service.backend.TaskService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化应用监控相关的服务
        AppLogService.getInstance().initSharedPreferences(this.getApplicationContext());
        // 初始化网络请求配置
        HttpConfig.init(this.getApplicationContext());
        // 初始化任务配置服务
        TaskService.getInstance().init(this.getApplicationContext());
        // 初始任务日志服务
        TaskLogService.getInstance().init(this.getApplicationContext());
        // 初始化仪表盘统计服务
        DashboardService.getInstance().init(this.getApplicationContext());
        XiuXianService.getInstance().init(this.getApplicationContext(), CalService.getInstance());

        com.example.selfgrowth.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_task, R.id.navigation_dashboard, R.id.navigation_calender, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // 获取手机使用情况权限
        if (!isStatAccessPermissionSet()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            this.startActivity(intent);
        }

        // Android 8.0使用startForegroundService在前台启动新服务
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            this.startForegroundService(new Intent(MainActivity.this, MonitorActivityService.class));
        }
        else{
            this.startService(new Intent(MainActivity.this, MonitorActivityService.class));
        }

        if (!isNotificationEnabled()) {
            goToNotificationSetting();
        }

        // 自动登录
        autoLogin();
    }

    private void autoLogin() {
        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);
        if (username == null || password == null) {
            return;
        }
        final UserRequest userRequest = new UserRequest();
        userRequest.login(username, password, (token) -> {
            UserCache.getInstance().setUserName(username);
            UserCache.getInstance().login();
            Log.i("login:", "自动登录成功");
        }, failedMessage -> Log.i("login:", "自动登录失败"));
    }

    /**
     * 判断允许通知，是否已经授权
     * 返回值为true时，通知栏打开，false未打开。
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled() {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = this.getApplicationInfo();
        String pkg = this.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转到app的设置界面--开启通知
     */
    private void goToNotificationSetting() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", this.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", this.getPackageName());
            intent.putExtra("app_uid", this.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", this.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    /**
     * Determine whether the application permission with permission to view usage has been obtained
     */
    public boolean isStatAccessPermissionSet() {
        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(APP_OPS_SERVICE);
            return appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}