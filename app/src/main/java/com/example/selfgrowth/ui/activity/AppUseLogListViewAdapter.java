package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.AppInfo;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.utils.DateUtils;

import org.angmarch.views.NiceSpinner;

import java.util.List;

public class AppUseLogListViewAdapter extends BaseAdapter {

    private final TaskRequest taskRequest = new TaskRequest();
    private final Context context;
    private final List<DashboardStatistics.AppUseLog> dataList;

    public AppUseLogListViewAdapter(Context context, List<DashboardStatistics.AppUseLog> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.app_use_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(showTime(dataList.get(position)));
        return convertView;
    }

    private String showTime(DashboardStatistics.AppUseLog appUseLog) {
        final String start = DateUtils.dateString(appUseLog.getStartTime());
        final String end = DateUtils.dateString(appUseLog.getEndTime());
        return String.format("%s -- %s", start, end);
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView content;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            content = view.findViewById(R.id.content);
        }
    }
}
