package com.example.selfgrowth.ui.task;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.model.TaskConfig;
import com.example.selfgrowth.http.request.TaskRequest;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.service.foregroud.TaskService;
import com.example.selfgrowth.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

import org.angmarch.views.NiceSpinner;

import java.util.List;

import lombok.SneakyThrows;

public class TaskFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();
    private final TaskRequest taskRequest = new TaskRequest();
    private final TaskService taskService = TaskService.getInstance();

    private ListView list;
    private ListViewDemoAdapter listViewDemoAdapter;
    private NiceSpinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        autoLogin();
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        view.findViewById(R.id.add_task_jump).setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.task_list_manage, new AddTaskFragment())
                .addToBackStack(null)
                .commit());

        requireActivity().getSupportFragmentManager().addOnBackStackChangedListener(() -> initTaskData(view));
        return view;
    }

    @SneakyThrows
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = requireView().findViewById(R.id.task_list_view);

        spinner = requireView().findViewById(R.id.task_group_spinner);
        spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            ((TextView)requireView().findViewById(R.id.task_group_name)).setText("任务组：" + item);
            initTaskListOfGroup(item);
        });
        initTaskData(requireView());
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
    private void initTaskData(final View view) {
        List<String> groups = taskService.getAllGroup();
        if (groups == null || groups.isEmpty()) {
            ((TextView) view.findViewById(R.id.task_group_name)).setText("任务组：无任务，请添加");
            return;
        }
        spinner.attachDataSource(groups);
        spinner.hideArrow();
        ((TextView) view.findViewById(R.id.task_group_name)).setText("任务组：" + groups.get(0));
        initTaskListOfGroup(groups.get(0));
    }

    public void initTaskListOfGroup(String groupName) {
        List<TaskConfig> tasks = taskService.query(groupName, null);
        listViewDemoAdapter = new ListViewDemoAdapter(this.getContext(), tasks, this, groupName);
        list.setAdapter(listViewDemoAdapter);
        list.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}