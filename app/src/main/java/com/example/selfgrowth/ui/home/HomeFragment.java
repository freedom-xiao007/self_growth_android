package com.example.selfgrowth.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.databinding.FragmentHomeBinding;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.http.request.UserRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final UserRequest userRequest = new UserRequest();
    private final TaskRequest taskRequest = new TaskRequest();
    private ListView testLv;//ListView组件
    private ListViewDemoAdapter listViewDemoAdapter;//ListView的数据适配器

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();//初始化组件
        autoLogin();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        testLv = (ListView) getView().findViewById(R.id.task_list_view);
    }
    /**
     * 初始化数据
     */
    private void getTaskData() {
        taskRequest.list(success -> {
            if (success == null) {
                Snackbar.make(getView(), "获取列表为空:", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            Log.d("获取任务列表：", "成功");
            List<Map<String, Object>> taskConfigs = (List<Map<String, Object>>) success;
            final List<TaskConfig> dataList = new ArrayList<>(taskConfigs.size());
            taskConfigs.forEach(task -> {
                final String s = new Gson().toJson(task);
                dataList.add(new Gson().fromJson(s, TaskConfig.class));
            });
            //设置ListView的适配器
            listViewDemoAdapter = new ListViewDemoAdapter(this.getContext(), dataList);
            testLv.setAdapter(listViewDemoAdapter);
            testLv.setSelection(4);
        }, failed -> {
            Snackbar.make(getView(), "获取列表失败:" + failed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("获取任务列表：", "失败");
        });
    }

    private void autoLogin() {
        final SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        final String userName = preferences.getString("username", "");
        final String password = preferences.getString("password", "");
        if (userName.isEmpty() || password.isEmpty()) {
            Snackbar.make(getView(), "无用户，请登录", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if (UserCache.getInstance().isLogin()) {
            getTaskData();
            return;
        }

        final LoginUser user = LoginUser.builder()
                .email(userName)
                .password(password)
                .build();
        userRequest.login(user, (token) -> {
            UserCache.getInstance().initUser(userName, token.toString());
            Snackbar.make(getView(), "登录成功:" + token.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("用户登录：", token.toString());
            getTaskData();
        }, failedMessage -> {
            Snackbar.make(getView(), "登录失败:" + failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("用户登录：", "失败");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}