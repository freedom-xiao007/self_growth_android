package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.CycleTypeConvert;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.model.TaskTypeConvert;
import com.example.selfgrowth.http.request.TaskRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityListViewAdapter extends BaseAdapter {

    private final Context context;//上下文对象
    private final List<ActivityModel> dataList;//ListView显示的数据
    private final List<String> installAppNames;
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
        viewHolder.times.setText(String.valueOf(dataList.get(position).getTimes()));

        List<String> apps = new ArrayList<>(installAppNames.size() + 1);
        apps.add(dataList.get(position).getApplication());
        apps.addAll(installAppNames);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, apps);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.application.setAdapter(spinnerAdapter);
        viewHolder.application.setSelection(0, true);
        viewHolder.application.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String appName = parent.getItemAtPosition(position).toString();//获取i所在的文本
            }
        });

        return convertView;
    }



    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView name;
        private final Spinner application;
        private final TextView times;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.activity_name);
            application = (Spinner) view.findViewById(R.id.application_for_activity);
            times = (TextView) view.findViewById(R.id.activity_times);
        }
    }
}
