package com.example.selfgrowth.ui.addTask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.databinding.FragmentAddTaskBinding;
import com.google.android.material.snackbar.Snackbar;

public class AddTaskFragment extends Fragment {

    private Button addTaskButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        addTaskButton = rootView.findViewById(R.id.add_task_button_confirm);
        addTaskButton.setOnClickListener(view -> {
            EditText taskNameEdit = rootView.findViewById(R.id.add_task_name_edit);
            String taskName = taskNameEdit.getText().toString();

            EditText taskDescEdit = rootView.findViewById(R.id.add_task_desc_edit);
            String taskDesc = taskDescEdit.getText().toString();

            Spinner taskLabelSpinner = rootView.findViewById(R.id.add_task_label_spinner);
            String taskLabel = taskLabelSpinner.getSelectedItem().toString();

            Spinner taskCycleSpinner = rootView.findViewById(R.id.add_task_cycle_spinner);
            String taskCycle = taskCycleSpinner.getSelectedItem().toString();

            Spinner taskTypeSpinner = rootView.findViewById(R.id.add_task_type_spinner);
            String taskType = taskTypeSpinner.getSelectedItem().toString();

            Snackbar.make(view, "add Task success", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
        return rootView;
    }
}
