package com.example.selfgrowth.ui.addTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.databinding.FragmentAddTaskBinding;

public class AddTaskFragment extends Fragment {

    private AddTaskViewModel addTaskViewModel;
    private FragmentAddTaskBinding binding;
    private Button addTaskButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        addTaskButton = rootView.findViewById(R.id.add_task_button_confirm);
        addTaskButton.setOnClickListener(v -> {
            EditText taskNameEdit = rootView.findViewById(R.id.add_task_name_edit);
            String taskName = taskNameEdit.getText().toString();
            Toast.makeText(rootView.getContext(), taskName, Toast.LENGTH_SHORT).show();
        });
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }
}
