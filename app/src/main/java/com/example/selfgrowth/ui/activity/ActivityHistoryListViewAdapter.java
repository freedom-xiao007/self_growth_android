package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.ActivityRecordModel;
import com.example.selfgrowth.http.request.ActivityRequest;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistoryListViewAdapter extends BaseAdapter {

    private final Context context;//上下文对象
    private final List<ActivityRecordModel> dataList;//ListView显示的数据
    private final ActivityRequest activityRequest = new ActivityRequest();
    private final List<String> installAppNames;

    /**
     * 构造器
     *
     * @param context 上下文对象
     * @param dataList 数据
     */
    public ActivityHistoryListViewAdapter(Context context, List<ActivityRecordModel> dataList, List<String> installAppNames) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_history_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        List<String> apps = new ArrayList<>(installAppNames.size() + 1);
        final String application = dataList.get(position).getApplication();
        apps.add(application == null || application.isEmpty() ? "" : application);
        apps.addAll(installAppNames);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, apps);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.application.setAdapter(spinnerAdapter);
        viewHolder.application.setSelection(0, true);

        viewHolder.updateButton.setOnClickListener(view -> {
            final String activityName = viewHolder.name.getText().toString();
            String appName = viewHolder.application.getSelectedItem().toString();//获取i所在的文本
            final String activityLabel = viewHolder.application.getSelectedItem().toString();
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

        viewHolder.name.setText(dataList.get(position).getActivity());
        viewHolder.date.setText(dataList.get(position).getDate().toString());
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {

        private final TextView name;
        private final TextView date;
        private final SearchableSpinner application;
        private final Button updateButton;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.activity_name);
            date = (TextView) view.findViewById(R.id.date);
            application = (SearchableSpinner) view.findViewById(R.id.application_for_activity);
            updateButton = (Button) view.findViewById(R.id.application_for_activity_update);
        }
    }
}
