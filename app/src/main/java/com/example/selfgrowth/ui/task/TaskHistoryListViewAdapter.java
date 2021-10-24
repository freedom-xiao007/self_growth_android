package com.example.selfgrowth.ui.task;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.CycleTypeConvert;
import com.example.selfgrowth.http.model.LabelType;
import com.example.selfgrowth.http.model.TaskRecord;
import com.example.selfgrowth.http.model.TaskTypeConvert;
import com.example.selfgrowth.http.request.ActivityRequest;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskHistoryListViewAdapter extends BaseAdapter {

    private final Context context;//上下文对象
    private final List<TaskRecord> dataList;//ListView显示的数据

    /**
     * 构造器
     *
     * @param context 上下文对象
     * @param dataList 数据
     */
    public TaskHistoryListViewAdapter(Context context, List<TaskRecord>  dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.task_history_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.id.setText(dataList.get(position).getId());
        viewHolder.name.setText(dataList.get(position).getName());
        viewHolder.description.setText(dataList.get(position).getDescription());
        viewHolder.label.setText(dataList.get(position).getLabel());
        viewHolder.cycleType.setText(CycleTypeConvert.convertToKey(dataList.get(position).getCycleType()));
        viewHolder.type.setText(TaskTypeConvert.convertToKey(dataList.get(position).getType()));
        viewHolder.completeDate.setText(dataList.get(position).getCompleteDate().toString());
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView id;
        private final TextView name;
        private final TextView description;
        private final TextView label;
        private final TextView cycleType;
        private final TextView type;
        private final TextView completeDate;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            id = (TextView) view.findViewById(R.id.id);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            label = (TextView) view.findViewById(R.id.label);
            cycleType = (TextView) view.findViewById(R.id.cycle);
            type = (TextView) view.findViewById(R.id.type);
            completeDate = (TextView) view.findViewById(R.id.date);
        }
    }
}
