package com.example.selfgrowth.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.model.AppInfo;
import com.example.selfgrowth.utils.AppUtils;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

public class AppFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_info, container, false);

        final SharedPreferences preferences = requireContext().getSharedPreferences(AppInfo.APP_INFO, Context.MODE_PRIVATE);
        List<AppInfo> apps = AppUtils.getApps(view.getContext());
        apps.forEach(app -> app.setLabel(preferences));

        ListView listView = view.findViewById(R.id.app_info_list_view);
        final AppInfoListViewAdapter adapter = new AppInfoListViewAdapter(view.getContext(), apps);
        listView.setAdapter(adapter);

        ((NiceSpinner)view.findViewById(R.id.type_spinner)).setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            String type = (String) parent.getItemAtPosition(position);
            ((TextView)view.findViewById(R.id.type_text)).setText("类型： " + type);
            List<AppInfo> searchApp = new ArrayList<>();
            for (AppInfo appInfo: apps) {
                if (appInfo.getLabel().equals(type)) {
                    searchApp.add(appInfo);
                }
            }
            listView.setAdapter(new AppInfoListViewAdapter(view.getContext(), searchApp));
        });

        final EditText searchText = view.findViewById(R.id.search);
        view.findViewById(R.id.search_button).setOnClickListener(v -> {
            final String content = searchText.getText().toString();
            List<AppInfo> searchApp = new ArrayList<>();
            for (AppInfo appInfo: apps) {
                if (content.isEmpty() || appInfo.getAppName().startsWith(content)) {
                    searchApp.add(appInfo);
                }
            }
            listView.setAdapter(new AppInfoListViewAdapter(view.getContext(), searchApp));
        });

        return view;
    }
}
