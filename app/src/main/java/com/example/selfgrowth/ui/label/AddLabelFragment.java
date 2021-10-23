package com.example.selfgrowth.ui.label;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.http.request.TaskLabelRequest;
import com.google.android.material.snackbar.Snackbar;

public class AddLabelFragment extends Fragment {

    private final TaskLabelRequest taskLabelRequest = new TaskLabelRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_label, container, false);
        final Button addLabelButton = rootView.findViewById(R.id.add_label_button_confirm);
        addLabelButton.setOnClickListener(view -> {
            EditText labelName = rootView.findViewById(R.id.add_task_label_edit);
            taskLabelRequest.addLabel(labelName.getText().toString(),
                    success -> {
                        Snackbar.make(view, "新增成功:" + success, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
            },
                    failed -> {
                        Snackbar.make(view, "新增失败：" + failed, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
            });
        });
        return rootView;
    }
}
