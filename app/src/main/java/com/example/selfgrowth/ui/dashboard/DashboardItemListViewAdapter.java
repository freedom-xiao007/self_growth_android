package com.example.selfgrowth.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.codingending.popuplayout.PopupLayout;
import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.ui.activity.AppUseLogListViewAdapter;
import com.example.selfgrowth.ui.task.TaskFragment;
import com.example.selfgrowth.utils.MyTimeUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;

import java.util.List;

public class DashboardItemListViewAdapter extends BaseAdapter {

    private final TaskRequest taskRequest = new TaskRequest();
    private final Context context;
    private final List<DashboardStatistics.DashboardApp> dataList;

    public DashboardItemListViewAdapter(Context context, List<DashboardStatistics.DashboardApp> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.dashboard_item_list_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(dataList.get(position).getName());
        viewHolder.time.setText(MyTimeUtils.toString(dataList.get(position).getMinutes()));
        viewHolder.time.setOnClickListener(v -> {
            View view = View.inflate(context, R.layout.app_use_log, null);
            AppUseLogListViewAdapter adapter = new AppUseLogListViewAdapter(view.getContext(), dataList.get(position).getLogs());
            ((ListView)view.findViewById(R.id.app_use_log)).setAdapter(adapter);
            PopupLayout popupLayout= PopupLayout.init(context, view);
            popupLayout.show();
        });
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView name;
        private final TextView time;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            name = view.findViewById(R.id.application_name);
            time = view.findViewById(R.id.application_time);
        }
    }
}
