package com.example.selfgrowth.ui.task;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.request.TaskRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AddTaskFragment extends Fragment {

    private final TaskRequest taskRequest = new TaskRequest();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_add, container, false);
        getAllGroups(view);
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
}
