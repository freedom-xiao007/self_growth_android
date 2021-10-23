package com.example.selfgrowth.server.foreground;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.HttpConfig;
import com.example.selfgrowth.http.api.PhoneUseRecordApi;
import com.example.selfgrowth.http.request.PhoneUseRecordRequest;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MonitorActivityService extends Service {

    private String beforeActivity;
    private final PhoneUseRecordRequest phoneUseRecordRequest = new PhoneUseRecordRequest();

    /*
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //处理任务
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("foreground", "onCreate");
        //如果API在26以上即版本为O则调用startForefround()方法启动服务
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setForegroundService();
        }
    }

    /**
     *通过通知启动服务
     */
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public void  setForegroundService()
    {
        //设定的通知渠道名称
        String channelName = "test";
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel("232", channelName, importance);
        channel.setDescription("test");
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "232");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground) //设置通知图标
                .setContentTitle("正在监控手机活动并上报")//设置通知标题
                .setContentText("正在监控手机活动并上报")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(232,builder.build());

        Handler handler=new Handler();
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                Log.d("Monitor Detect", "定时检测顶层应用");
                getTopActivity();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 10000);//每两秒执行一次runnable.
    }

    public void getTopActivity()
    {
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        UsageStatsManager sUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
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
            Log.d("Service", result);
            beforeActivity = result;
        } else {
            Log.d("Before Service", beforeActivity == null ? "null" : beforeActivity);
        }

        if (beforeActivity == null) {
            Toast.makeText(MonitorActivityService.this.getApplicationContext(),"活动为空",Toast.LENGTH_SHORT).show();
            return;
        }

        phoneUseRecordRequest.uploadRecord(beforeActivity, success -> {
            Toast.makeText(MonitorActivityService.this.getApplicationContext(),"上传成功",Toast.LENGTH_SHORT).show();
        }, failed -> {
            Toast.makeText(MonitorActivityService.this.getApplicationContext(),"上传失败",Toast.LENGTH_SHORT).show();
            Log.w("Activity", "上传失败");
        });
    }
}
