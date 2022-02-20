package com.example.selfgrowth.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.utils.AppUtils;

public class AppFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_info, container, false);
        ListView listView = view.findViewById(R.id.app_info_list_view);
        final AppInfoListViewAdapter adapter = new AppInfoListViewAdapter(view.getContext(), AppUtils.getApps(view.getContext()));
        listView.setAdapter(adapter);
        return view;
    }
}
