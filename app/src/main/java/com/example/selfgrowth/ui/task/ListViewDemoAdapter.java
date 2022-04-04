package com.example.selfgrowth.ui.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.selfgrowth.R;
import com.example.selfgrowth.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.service.foregroud.TaskService;

import java.util.List;

public class ListViewDemoAdapter extends BaseAdapter {

    private final TaskRequest taskRequest = new TaskRequest();
    private final Context context;//上下文对象
    private final List<TaskConfig> dataList;//ListView显示的数据
    private final TaskFragment taskFragment;
    private final String groupName;
    private final TaskService taskService = TaskService.getInstance();

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

        boolean isComplete = dataList.get(position).getIsComplete();
        viewHolder.id.setText(dataList.get(position).getId());
        viewHolder.name.setText(dataList.get(position).getName());
        viewHolder.status.setText(isComplete ? "已完成" : "未完成");
//        viewHolder.label.setText(dataList.get(position).getLabel());
//        viewHolder.cycle.setText(CycleTypeConvert.convertToKey(dataList.get(position).getCycleType()));
//        viewHolder.type.setText(TaskTypeConvert.convertToKey(dataList.get(position).getType()));
//        viewHolder.isComplete.setText(dataList.get(position).isComplete() ? "已完成" : "未完成");
        viewHolder.complete.setVisibility(isComplete ? View.GONE : View.VISIBLE);
        viewHolder.complete.setOnClickListener(v -> {
            taskService.complete(groupName, dataList.get(position).getId());
            taskFragment.initTaskListOfGroup(groupName);
        });
//        viewHolder.delete.setOnClickListener(v -> {
//            taskService.delete(groupName, dataList.get(position).getId());
//            taskFragment.initTaskListOfGroup(groupName);
//        });
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView id;
        private final TextView name;
        private final TextView status;
//        private final TextView label;
//        private final TextView cycle;
//        private final TextView type;
//        private final TextView isComplete;
        private final CheckBox complete;
//        private final CheckBox delete;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            id = view.findViewById(R.id.task_id);
            name = view.findViewById(R.id.task_name);
            status = view.findViewById(R.id.task_status);
//            label = view.findViewById(R.id.task_label);
//            cycle = view.findViewById(R.id.task_cycle);
//            type = view.findViewById(R.id.task_type);
//            isComplete = view.findViewById(R.id.task_isComplete);
            complete = view.findViewById(R.id.task_complete);
//            delete = view.findViewById(R.id.task_delete);
        }
    }
}
