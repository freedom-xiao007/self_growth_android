package com.example.selfgrowth.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.TaskCycleEnum;
import com.example.selfgrowth.enums.TaskLearnTypeEnum;
import com.example.selfgrowth.enums.TaskTypeEnum;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.service.foregroud.TaskService;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

public class AddTaskFragment extends Fragment {

    private final TaskService taskService = TaskService.getInstance();
    private LabelEnum label = LabelEnum.DEFAULT;
    private TaskCycleEnum cycle = TaskCycleEnum.DEFAULT;
    private TaskLearnTypeEnum learnType = TaskLearnTypeEnum.DEFAULT;
    private TaskTypeEnum taskType = TaskTypeEnum.DEFAULT;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_add, container, false);
        getAllGroups(view);
        init(view);
        return view;
    }

    private void getAllGroups(View view) {
        List<String> groups = taskService.getAllGroup();
        AutoCompleteTextView taskGroupEdit = view.findViewById(R.id.add_task_group);
        taskGroupEdit.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.root_text_view, new ArrayList<>(groups)));
    }

    private void init(View view) {
        NiceSpinner labels = view.findViewById(R.id.add_task_label);
        labels.attachDataSource(LabelEnum.names());
        labels.setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            label = LabelEnum.fromString((String) parent.getItemAtPosition(position));
            ((EditText)view.findViewById(R.id.add_task_label_show)).setText("任务标签    " + label.getName());
        });

        NiceSpinner cycleTypes = view.findViewById(R.id.add_task_cycleType);
        cycleTypes.attachDataSource(TaskCycleEnum.names());
        cycleTypes.setOnSpinnerItemSelectedListener(((parent, view1, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            cycle = TaskCycleEnum.fromString(item);
            ((EditText)view.findViewById(R.id.add_task_cycleType_show)).setText("任务周期    " + item);
        }));

        NiceSpinner learnTypes = view.findViewById(R.id.add_task_learnType);
        learnTypes.attachDataSource(TaskLearnTypeEnum.names());
        learnTypes.setOnSpinnerItemSelectedListener(((parent, view1, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            learnType = TaskLearnTypeEnum.fromString(item);
            ((EditText)view.findViewById(R.id.add_task_learnType_show)).setText("任务类型    " + item);
        }));

        NiceSpinner taskTypes = view.findViewById(R.id.add_task_outputType);
        taskTypes.attachDataSource(TaskTypeEnum.names());
        taskTypes.setOnSpinnerItemSelectedListener(((parent, view1, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            taskType = TaskTypeEnum.fromString(item);
            ((EditText)view.findViewById(R.id.add_task_outputType_show)).setText("任务周期    " + item);
        }));

        view.findViewById(R.id.add_task_button).setOnClickListener(v -> {
            final String taskGroup = ((EditText)view.findViewById(R.id.add_task_group)).getText().toString();
            final String taskName = ((EditText)view.findViewById(R.id.add_task_name)).getText().toString();
            final String desc = ((EditText)view.findViewById(R.id.add_task_dsc)).getText().toString();
            final TaskConfig taskConfig = TaskConfig.builder()
                    .name(taskName)
                    .description(desc)
                    .label(label)
                    .cycleType(cycle)
                    .learnType(learnType)
                    .group(taskGroup)
                    .taskTypeEnum(taskType)
                    .isComplete(false)
                    .build();
            taskService.add(taskConfig, view);
        });
    }
}
