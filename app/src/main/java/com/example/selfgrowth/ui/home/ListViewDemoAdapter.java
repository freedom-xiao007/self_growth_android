package com.example.selfgrowth.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.CycleTypeConvert;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.model.TaskTypeConvert;

import java.util.List;

public class ListViewDemoAdapter extends BaseAdapter {

    private final Context context;//上下文对象
    private final List<TaskConfig> dataList;//ListView显示的数据
    /**
     * 构造器
     *
     * @param context 上下文对象
     * @param dataList 数据
     */
    public ListViewDemoAdapter(Context context, List<TaskConfig>  dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_demo, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(dataList.get(position).getName());
        viewHolder.label.setText(dataList.get(position).getLabel());
        viewHolder.cycle.setText(CycleTypeConvert.convertToKey(dataList.get(position).getCycleType()));
        viewHolder.type.setText(TaskTypeConvert.convertToKey(dataList.get(position).getType()));
        viewHolder.isComplete.setText(dataList.get(position).isComplete() ? "已完成" : "未完成");
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView name;
        private final TextView label;
        private final TextView cycle;
        private final TextView type;
        private final TextView isComplete;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.task_name);
            label = (TextView) view.findViewById(R.id.task_label);
            cycle = (TextView) view.findViewById(R.id.task_cycle);
            type = (TextView) view.findViewById(R.id.task_type);
            isComplete = (TextView) view.findViewById(R.id.task_isComplete);
        }
    }
}
