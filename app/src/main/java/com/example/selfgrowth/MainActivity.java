package com.example.selfgrowth;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioPlaybackConfiguration;
import android.media.AudioRecordingConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.selfgrowth.scheduler.MonitorTask;
import com.example.selfgrowth.server.forground.MyForeGroundService;
import com.example.selfgrowth.service.backgroud.MonitorService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.selfgrowth.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        this.startActivity(intent);

        // Android 8.0使用startForegroundService在前台启动新服务
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            this.startForegroundService(new Intent(MainActivity.this, MyForeGroundService.class));
        }
        else{
            this.startService(new Intent(MainActivity.this, MyForeGroundService.class));
        }

        this.startService(new Intent(MainActivity.this, MonitorService.class));

        PeriodicWorkRequest monitorTask = new PeriodicWorkRequest.Builder(MonitorTask.class, 3, TimeUnit.SECONDS).build();
        WorkManager.getInstance(this).enqueue(monitorTask);

        Handler handler=new Handler();
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
// TODO Auto-generated method stub
//要做的事情
                Log.d("Monitor Detect", "定时检测顶层应用");
                getTopActivity();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 3000);//每两秒执行一次runnable.

        Process process = null;
        try {
            ArrayList<String> files = new ArrayList<String>();
            File file = new File("/proc");
            File[] tempList = file.listFiles();

            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    Log.d("文     件：", tempList[i].getAbsolutePath());
                    files.add(tempList[i].toString());
                }
                if (tempList[i].isDirectory()) {
                    String name = tempList[i].getAbsolutePath();
                    Log.d("文件夹：", name);
                    Pattern pattern = Pattern.compile("/proc/[0-9]+");
                    if (pattern.matcher(name).matches()) {
//                        process = Runtime.getRuntime().exec("cat " + name + "/cmdline");
//                        process = Runtime.getRuntime().exec("cat /proc/1/cmdline");
//                        BufferedReader processReader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
//                        // Read from BufferedReader
//                        String s = processReader.readLine();
//                        Log.d("cmd:", s);
                    }
                }
            }

//            process = Runtime.getRuntime().exec("ps -aux");
//            BufferedReader processReader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
//            // Read from BufferedReader
//            String s = processReader.readLine();
//            Log.d("process:", s);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ActivityManager mActivityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            Log.d("process", info.toString());
        }

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        List<AudioPlaybackConfiguration> f = audioManager.getActivePlaybackConfigurations();
        List<AudioRecordingConfiguration> d = audioManager.getActiveRecordingConfigurations();
        for (AudioPlaybackConfiguration playbackConfiguration: f) {
            final String s = playbackConfiguration.toString().split(" ")[1];
            Log.d("audio", s);
            final int piid = Integer.parseInt(s.split(":")[1]);

//            try {
//                process = Runtime.getRuntime().exec("cat /proc/" + piid + "/cmdline");
//                BufferedReader processReader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
//                // Read from BufferedReader
//                String s2 = processReader.readLine();
//                Log.d("cmd:", s2);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == piid) {
                    String procName = appProcess.processName;
                    Log.d("audio process name", procName);
                }
            }
        }
        Log.d("audio", "audio");
//        AudioAttributes playbackAttributes = new AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_GAME)
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .build();
//        AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//                .setAudioAttributes(playbackAttributes)
//                .setAcceptsDelayedFocusGain(true)
//                .setOnAudioFocusChangeListener(afChangeListener, handler)
//                .build();
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

        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        final String topActivity = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        Log.i("top activity:", topActivity);
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