package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityRecordModel;

import java.util.List;

public class ActivityHistoryListViewAdapter extends BaseAdapter {

    private final Context context;//上下文对象
    private final List<ActivityRecordModel> dataList;//ListView显示的数据

    /**
     * 构造器
     *
     * @param context 上下文对象
     * @param dataList 数据
     */
    public ActivityHistoryListViewAdapter(Context context, List<ActivityRecordModel> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_history_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

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

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.activity_name);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
}
