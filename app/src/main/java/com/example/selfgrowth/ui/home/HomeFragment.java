package com.example.selfgrowth.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
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
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;

public class HomeFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();
    private final TaskRequest taskRequest = new TaskRequest();

    private ListView list;
    private ListViewDemoAdapter listViewDemoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        autoLogin();
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @SneakyThrows
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = requireView().findViewById(R.id.task_list_view);
        initTaskData();
    }

    /**
     * todo
     * 目前的登录有问题
     * 不是在应用启动前进行登录
     * 但放到Main中，取不到想要的数据
     */
    private void autoLogin() {
        if (UserCache.getInstance().isLogin()) {
            return;
        }

        final SharedPreferences preferences = requireActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        final String userName = preferences.getString("username", "");
        final String password = preferences.getString("password", "");
        if (userName.isEmpty() || password.isEmpty()) {
            Snackbar.make(requireView(), "无用户，请登录", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        final LoginUser user = LoginUser.builder()
                .email(userName)
                .password(password)
                .applications(AppUtils.getInstallSoftware(this.requireContext()))
                .build();
        userRequest.login(user, (token) -> {
            UserCache.getInstance().initUser(userName, token.toString());
            Snackbar.make(requireView(), "登录成功", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("用户登录：", token.toString());
        }, failedMessage -> {
            Snackbar.make(requireView(), "登录失败:" + failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("用户登录：", "失败");
        });
    }

    /**
     * 初始化数据
     */
    private void initTaskData() {
        taskRequest.list(success -> {
            if (success == null) {
                Snackbar.make(requireView(), "获取列表为空:", Snackbar.LENGTH_LONG)
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
            list.setAdapter(listViewDemoAdapter);
            list.setSelection(4);
        }, failed -> {
            Snackbar.make(requireView(), "获取列表失败:" + failed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("获取任务列表：", "失败");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}