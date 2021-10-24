package com.example.selfgrowth.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.CycleTypeConvert;
import com.example.selfgrowth.http.model.LabelType;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.model.TaskTypeConvert;
import com.example.selfgrowth.http.request.ActivityRequest;
import com.example.selfgrowth.http.request.TaskRequest;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityListViewAdapter extends BaseAdapter {

    private final Context context;//上下文对象
    private final List<ActivityModel> dataList;//ListView显示的数据
    private final List<String> installAppNames;
    private final ActivityRequest activityRequest = new ActivityRequest();

    /**
     * 构造器
     *
     * @param context 上下文对象
     * @param dataList 数据
     */
    public ActivityListViewAdapter(Context context, List<ActivityModel>  dataList, List<String> installAppNames) {
        this.context = context;
        this.dataList = dataList;
        this.installAppNames = installAppNames;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //判断是否有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(dataList.get(position).getName());
        viewHolder.name.setOnClickListener(v -> {
            final String activityName = viewHolder.name.getText().toString();
            activityRequest.activityHistory(activityName, success -> {
                if (success == null) {
                    return;
                }

                final List<Map<String, Object>> data = (List<Map<String, Object>>) success;
                final String[] dates = new String[data.size()];
                for (int i=0; i<data.size(); i++) {
                    dates[i] = data.get(i).getOrDefault("date", "未知").toString();
                }
                AlertDialog alertDialog3 = new AlertDialog.Builder(context)
                        .setTitle("活动上报时间列表")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(dates, (dialog, which) -> {
                        })
                        .create();
                alertDialog3.show();
            }, failed -> {
                Snackbar.make(v, "更新活动历史失败:" + failed, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });

        });

        viewHolder.times.setText(String.valueOf(dataList.get(position).getTimes()));
        final double timeAmount = (dataList.get(position).getTimes().doubleValue() * 10) / 3600;
        BigDecimal bigDecimal = new BigDecimal(timeAmount);
        viewHolder.timeAmount.setText(String.valueOf(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue()));

        List<String> apps = new ArrayList<>(installAppNames.size() + 1);
        apps.add(dataList.get(position).getApplication());
        apps.addAll(installAppNames);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, apps);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.application.setAdapter(spinnerAdapter);
        viewHolder.application.setSelection(0, true);

        viewHolder.updateButton.setOnClickListener(view -> {
            final String activityName = viewHolder.name.getText().toString();
            String appName = viewHolder.application.getSelectedItem().toString();//获取i所在的文本
            final String activityLabel = viewHolder.label.getSelectedItem().toString();
            final ActivityModel activityModel = ActivityModel.builder()
                    .application(appName)
                    .activity(activityName)
                    .label(activityLabel)
                    .build();
            activityRequest.updateActivityModel(activityModel, success -> {
                if (success == null) {
                    return;
                }
                Snackbar.make(view, "更新获取应用名成功:" + success, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }, failed -> {
                Snackbar.make(view, "更新获取应用名失败:" + failed, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
        });

        final String activityLabel = dataList.get(position).getLabel();
        if (activityLabel == null || activityLabel.isEmpty()) {
            viewHolder.label.setSelection(4, true);
        } else {
            viewHolder.label.setSelection(LabelType.convertToValue(activityLabel), true);
        }

        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView name;
        private final SearchableSpinner application;
        private final TextView times;
        private final Button updateButton;
        private final TextView timeAmount;
        private final Spinner label;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.activity_name);
            application = (SearchableSpinner) view.findViewById(R.id.application_for_activity);
            times = (TextView) view.findViewById(R.id.activity_times);
            updateButton = (Button) view.findViewById(R.id.application_for_activity_update);
            timeAmount = (TextView) view.findViewById(R.id.activity_time_amount);
            label = (Spinner) view.findViewById(R.id.application_label);
        }
    }
}
