package com.example.selfgrowth;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.selfgrowth.server.forground.MyForeGroundService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.selfgrowth.databinding.ActivityMainBinding;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_sleep)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Button startServiceButton = findViewById(R.id.start_foreground_service_button);
        startServiceButton.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent intent = new Intent(MainActivity.this, MyForeGroundService.class);
            startService(intent);
        });

//        Button stopServiceButton = findViewById(R.id.stop_foreground_service_button);
//        stopServiceButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, MyForeGroundService.class);
//            intent.setAction(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(intent);
//            } else {
//                startService(intent);
//            }
//        });

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        this.startActivity(intent);

        ScheduledExecutorService scheduledExecutorService =  Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getTopActivity();
            }
        },0,5, TimeUnit.SECONDS);

        // Android 8.0使用startForegroundService在前台启动新服务
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            this.startForegroundService(new Intent(MainActivity.this, MyForeGroundService.class));
        }
        else{
            this.startService(new Intent(MainActivity.this, MyForeGroundService.class));
        }
    }

    public void getTopActivity()
    {
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        UsageStatsManager sUsageStatsManager =null;
        if (sUsageStatsManager == null) {
            sUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        String result = "";
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.getPackageName()+"/"+event.getClassName();
            }
        }
        if (!android.text.TextUtils.isEmpty(result)) {
            Log.i("Service", result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}