package com.example.selfgrowth.ui.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.codingending.popuplayout.PopupLayout;
import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.service.foregroud.TaskService;

import java.util.List;

public class ListViewDemoAdapter extends BaseAdapter {

    private final TaskRequest taskRequest = new TaskRequest();
    private final Context context;
    private final List<TaskConfig> dataList;
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

        final TaskConfig item = dataList.get(position);
        boolean isComplete = item.getIsComplete();
        viewHolder.id.setText(item.getId());
        viewHolder.name.setText(item.getName());
        viewHolder.name.setOnClickListener(v -> {
            View view = View.inflate(context, R.layout.task_detail, null);
            ((TextView) view.findViewById(R.id.task_label)).setText(item.getLabel().getName());
            ((TextView) view.findViewById(R.id.task_cycle)).setText(item.getCycleType().getName());

            PopupLayout popupLayout= PopupLayout.init(context, view);
            view.findViewById(R.id.task_delete).setOnClickListener(v1 -> {
                taskService.delete(groupName, item.getId());
                taskFragment.initTaskListOfGroup(groupName);
                popupLayout.dismiss();
            });
            popupLayout.show();
        });
        viewHolder.cycle.setText(item.getCycleType().equals(TaskCycleEnum.DEFAULT) ? "" : item.getCycleType().getName());
        viewHolder.status.setText(isComplete ? "已完成" : "未完成");
        viewHolder.complete.setVisibility(isComplete ? View.GONE : View.VISIBLE);
        viewHolder.complete.setOnClickListener(v -> {
            taskService.complete(groupName, item.getId());
            taskFragment.initTaskListOfGroup(groupName);
        });
        return convertView;
    }

    /**
     * ViewHolder类
     */
    private static final class ViewHolder {
        private final TextView id;
        private final TextView name;
        private final TextView status;
        private final TextView cycle;
        private final CheckBox complete;

        /**
         * 构造器
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            id = view.findViewById(R.id.task_id);
            name = view.findViewById(R.id.task_name);
            status = view.findViewById(R.id.task_status);
            cycle = view.findViewById(R.id.task_cycle);
            complete = view.findViewById(R.id.task_complete);
        }
    }
}
