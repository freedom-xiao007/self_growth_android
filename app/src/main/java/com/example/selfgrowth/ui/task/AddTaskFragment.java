package com.example.selfgrowth.ui.task;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.enums.TaskLearnTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.google.android.material.snackbar.Snackbar;

import org.angmarch.views.NiceSpinner;

import java.util.List;

public class AddTaskFragment extends Fragment {

    private final TaskRequest taskRequest = new TaskRequest();
    private String label = "学习";
    private int cycle = 0;
    private int learnType = -1;
    private int taskType = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_add, container, false);
        getAllGroups(view);
        init(view);
        return view;
    }

    private void getAllGroups(View view) {
        taskRequest.allGroups(success -> {
            if (success == null) {
                Snackbar.make(requireView(), "获取列表为空:", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            List<String> dataset = (List<String>) success;
            String[] groups = new String[dataset.size()];
            for (int i=0; i<dataset.size(); i++) {
                groups[i] = dataset.get(i);
            }
            AutoCompleteTextView taskGroupEdit = view.findViewById(R.id.add_task_group);
            taskGroupEdit.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.root_text_view, groups));
        }, failed -> {
            Snackbar.make(requireView(), "获取任务组列表失败:" + failed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("获取任务组任务列表：", "失败");
        });
    }

    private void init(View view) {
        ((NiceSpinner)view.findViewById(R.id.add_task_label)).setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            label = (String) parent.getItemAtPosition(position);
            ((EditText)view.findViewById(R.id.add_task_label_show)).setText("任务标签    " + label);
        });

        ((NiceSpinner)view.findViewById(R.id.add_task_cycleType)).setOnSpinnerItemSelectedListener(((parent, view1, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            cycle = TaskCycleEnum.getIndexByName(item);
            ((EditText)view.findViewById(R.id.add_task_cycleType_show)).setText("任务周期    " + item);
        }));

        ((NiceSpinner)view.findViewById(R.id.add_task_learnType)).setOnSpinnerItemSelectedListener(((parent, view1, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            learnType = TaskLearnTypeEnum.getIndexByName(item);
            ((EditText)view.findViewById(R.id.add_task_learnType_show)).setText("任务类型    " + item);
        }));

        ((NiceSpinner)view.findViewById(R.id.add_task_outputType)).setOnSpinnerItemSelectedListener(((parent, view1, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            taskType = TaskTypeEnum.getIndexByName(item);
            ((EditText)view.findViewById(R.id.add_task_outputType_show)).setText("任务周期    " + item);
        }));

        view.findViewById(R.id.add_task_button).setOnClickListener(v -> {
            final String taskGroup = ((EditText)view.findViewById(R.id.add_task_group)).getText().toString();
            final String taskName = ((EditText)view.findViewById(R.id.add_task_name)).getText().toString();
            final String desc = ((EditText)view.findViewById(R.id.add_task_dsc)).getText().toString();
            final TaskConfig taskConfig = TaskConfig.builder()
                    .name(taskName)
                    .description(desc)
                    .cycleType(cycle)
                    .learnType(learnType)
                    .group(taskGroup)
                    .outputType(taskType)
                    .build();
            taskRequest.add(taskConfig,
                    success -> Snackbar.make(view, "任务添加成功", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show(),

                    failed -> Snackbar.make(view, "添加失败：" + failed, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
            );
        });
    }
}
