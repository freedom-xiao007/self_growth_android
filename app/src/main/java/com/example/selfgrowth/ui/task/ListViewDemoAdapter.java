package com.example.selfgrowth.ui.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ListViewDemoAdapter extends BaseAdapter {

    private final TaskRequest taskRequest = new TaskRequest();
    private final Context context;//上下文对象
    private final List<TaskConfig> dataList;//ListView显示的数据
    private final TaskFragment taskFragment;
    private final String groupName;

    /**
     * 构造器
     * @param context 上下文对象
     * @param dataList 数据
     * @param taskFragment
     * @param groupName
     */
    public ListViewDemoAdapter(Context context, List<TaskConfig> dataList, TaskFragment taskFragment, String groupName) {
        this.context = context;
        this.dataList = dataList;
        this.taskFragment = taskFragment;
        this.groupName = groupName;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.task_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.id.setText(dataList.get(position).getId());
        viewHolder.name.setText(dataList.get(position).getName());
//        viewHolder.label.setText(dataList.get(position).getLabel());
//        viewHolder.cycle.setText(CycleTypeConvert.convertToKey(dataList.get(position).getCycleType()));
//        viewHolder.type.setText(TaskTypeConvert.convertToKey(dataList.get(position).getType()));
//        viewHolder.isComplete.setText(dataList.get(position).isComplete() ? "已完成" : "未完成");
        View finalConvertView = convertView;
        viewHolder.complete.setOnClickListener(v -> {
            if (!viewHolder.complete.isChecked()) {
                return;
            }
            taskRequest.complete(viewHolder.id.getText().toString(), success -> {
                // todo
                // 每次完成任务后，以这样的方式刷新会不会有问题，总感觉有点怪，不怎么优雅
                taskFragment.initTaskListOfGroup(groupName);
                Snackbar.make(finalConvertView, "任务请求完成:" + success, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }, failed -> {
                Snackbar.make(finalConvertView, "任务完成请求失败:" + failed, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
        });
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView id;
        private final TextView name;
//        private final TextView label;
//        private final TextView cycle;
//        private final TextView type;
//        private final TextView isComplete;
        private final CheckBox complete;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            id = view.findViewById(R.id.task_id);
            name = view.findViewById(R.id.task_name);
//            label = view.findViewById(R.id.task_label);
//            cycle = view.findViewById(R.id.task_cycle);
//            type = view.findViewById(R.id.task_type);
//            isComplete = view.findViewById(R.id.task_isComplete);
            complete = view.findViewById(R.id.task_complete);
        }
    }
}
