package com.example.selfgrowth.ui.task;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.model.TaskConfig;
import com.example.selfgrowth.service.foregroud.TaskService;

import java.util.List;

public class TaskListFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    private final TaskService taskService = TaskService.getInstance();
    private final String groupName;
    private ListView list;

    public TaskListFragment(String groupName) {
        this.groupName = groupName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        view.findViewById(R.id.add_task_fab).setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.task_list_manage, new AddTaskFragment(groupName))
                .addToBackStack(null)
                .commit());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        list = requireView().findViewById(R.id.task_list_view);
        Log.d("current group", groupName);
        initTaskListOfGroup(groupName);
    }

    public void initTaskListOfGroup(String groupName) {
        List<TaskConfig> tasks = taskService.query(groupName, null);
        ListViewDemoAdapter listViewDemoAdapter = new ListViewDemoAdapter(this.getContext(), tasks, this, groupName);
        list.setAdapter(listViewDemoAdapter);
        list.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
