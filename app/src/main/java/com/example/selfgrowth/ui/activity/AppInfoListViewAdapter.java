package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.AppInfo;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.request.TaskRequest;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.List;

public class AppInfoListViewAdapter extends BaseAdapter {

    private final TaskRequest taskRequest = new TaskRequest();
    private final Context context;
    private final List<AppInfo> dataList;

    public AppInfoListViewAdapter(Context context, List<AppInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.app_info_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.appName.setText(dataList.get(position).getAppName());
        viewHolder.packageName.setText(dataList.get(position).getPackageName());
        viewHolder.labelSpinner.setOnSpinnerItemSelectedListener((parent1, view, position1, id) -> {
            String label =parent1.getItemAtPosition(position1).toString();
            viewHolder.appLabel.setText("标签： " + label);
        });
        return convertView;
    }

    private String showTime(int minutes) {
        int hours = minutes / 60;
        if (hours > 0) {
            return String.format("%d 小时 %d 分钟", hours, minutes % 60);
        }
        return String.format("%d 分钟", minutes);
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView appName;
        private final TextView packageName;
        private final TextView appLabel;
        private final NiceSpinner labelSpinner;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            appName = view.findViewById(R.id.app_info_name);
            packageName = view.findViewById(R.id.app_info_package);
            appLabel = view.findViewById(R.id.app_info_label);
            labelSpinner = view.findViewById(R.id.app_info_label_spinner);
        }
    }
}
