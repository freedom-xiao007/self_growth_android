package com.example.selfgrowth.ui.addTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.model.CycleTypeConvert;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.model.TaskTypeConvert;
import com.example.selfgrowth.http.request.TaskRequest;
import com.google.android.material.snackbar.Snackbar;

public class AddTaskFragment extends Fragment {

    private final TaskRequest taskRequest = new TaskRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        Button addTaskButton = rootView.findViewById(R.id.add_task_button_confirm);
        addTaskButton.setOnClickListener(view -> {
            EditText taskNameEdit = rootView.findViewById(R.id.add_task_name_edit);
            EditText taskDescEdit = rootView.findViewById(R.id.add_task_desc_edit);
            Spinner taskLabelSpinner = rootView.findViewById(R.id.add_task_label_spinner);
            Spinner taskCycleSpinner = rootView.findViewById(R.id.add_task_cycle_spinner);
            Spinner taskTypeSpinner = rootView.findViewById(R.id.add_task_type_spinner);

            final TaskConfig taskConfig = TaskConfig.builder()
                    .name(taskNameEdit.getText().toString())
                    .description(taskDescEdit.getText().toString())
                    .label(taskLabelSpinner.getSelectedItem().toString())
                    .cycleType(CycleTypeConvert.convertToValue(taskCycleSpinner.getSelectedItem().toString()))
                    .type(TaskTypeConvert.convertToValue(taskTypeSpinner.getSelectedItem().toString()))
                    .build();
            taskRequest.add(taskConfig, success -> {
                    Snackbar.make(view, "新增成功:" + success, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }, failed -> {
                    Snackbar.make(view, "新增失败：" + failed, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            });
        });
        return rootView;
    }
}
