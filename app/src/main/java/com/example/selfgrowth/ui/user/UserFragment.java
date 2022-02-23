package com.example.selfgrowth.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.codingending.popuplayout.PopupLayout;
import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.http.HttpConfig;
import com.example.selfgrowth.http.RetrofitClient;
import com.example.selfgrowth.http.model.AppInfo;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.service.foregroud.AppStatisticsService;
import com.example.selfgrowth.service.foregroud.TaskLogService;
import com.example.selfgrowth.ui.activity.AppFragment;
import com.example.selfgrowth.ui.activity.AppHistoryFragment;
import com.example.selfgrowth.ui.activity.AppUseLogListViewAdapter;
import com.example.selfgrowth.ui.dashboard.DailyDashboardFragment;
import com.example.selfgrowth.ui.task.AddTaskFragment;
import com.example.selfgrowth.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.Map;

public class UserFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();
    private final TaskLogService taskLogService = TaskLogService.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initUserInfo(view);
        initOverview(view);
        initServerUrlSetting(view);
        setRoute(view, R.id.app_setting, new AppFragment());
        setRoute(view, R.id.app_history, new AppHistoryFragment());
        setRoute(view, R.id.daily_dashboard, new DailyDashboardFragment());
        return view;
    }

    private void setRoute(View view, int buttonId, Fragment fragment) {
        view.findViewById(buttonId).setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.user_info, fragment)
                .addToBackStack(null)
                .commit());
    }

    private void initUserInfo(View view) {
        TextView userEmail = view.findViewById(R.id.login_user_email);
        if (UserCache.getInstance().isLogin()) {
            userEmail.setText(UserCache.getInstance().getUserName());
        } else {
            userEmail.setText("未登录");
        }
    }

    private void initServerUrlSetting(View view) {
        view.findViewById(R.id.server_url_setting).setOnClickListener(v -> {
            View settingView = View.inflate(view.getContext(), R.layout.server_url_setting, null);

            settingView.findViewById(R.id.server_url_setting_button).setOnClickListener(ignore -> {
                EditText serverUrl = settingView.findViewById(R.id.server_url);
                HttpConfig.setServerUrl(serverUrl.getText().toString());
                try {
                    RetrofitClient.getInstance().httpClientReload();
                    Snackbar.make(view, "设置成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    Snackbar.make(view, "设置失败:" + e.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            PopupLayout popupLayout= PopupLayout.init(view.getContext(), settingView);
            popupLayout.show(PopupLayout.POSITION_TOP);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initOverview(View view) {
        DashboardStatistics statistics = AppStatisticsService.getInstance().statistics(new Date(), view.getContext());
        Map<String, DashboardStatistics.DashboardGroup> groups = statistics.getGroups();
        if (groups == null) {
            return;
        }
        long learnMinutes = groups.getOrDefault(LabelEnum.LEARN.getName(), DashboardStatistics.DashboardGroup.builder().build()).getMinutes();
        long runningMinutes = groups.getOrDefault(LabelEnum.RUNNING.getName(), DashboardStatistics.DashboardGroup.builder().build()).getMinutes();
        long sleepMinutes = groups.getOrDefault(LabelEnum.SLEEP.getName(), DashboardStatistics.DashboardGroup.builder().build()).getMinutes();
        int taskComplete = taskLogService.list(new Date()).size();
        ((TextView)view.findViewById(R.id.learn_minutes)).setText(String.valueOf(learnMinutes));
        ((TextView)view.findViewById(R.id.running_minutes)).setText(String.valueOf(runningMinutes));
        ((TextView)view.findViewById(R.id.sleep_minutes)).setText(String.valueOf(sleepMinutes));
        ((TextView)view.findViewById(R.id.task_complete)).setText(String.valueOf(taskComplete));
    }

    private View userInfo(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        TextView userEmail = rootView.findViewById(R.id.login_user_email);
        userEmail.setText(UserCache.getInstance().getUserName());
        return rootView;
    }

    private View loadLoginFragment(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            EditText email = rootView.findViewById(R.id.login_user_email);
            EditText password = rootView.findViewById(R.id.login_password);

            final LoginUser user = LoginUser.builder()
                    .email(email.getText().toString())
                    .password(password.getText().toString())
                    .applications(AppUtils.getInstallSoftware(this.requireContext()))
                    .build();

            userRequest.login(user, (token) -> {
                UserCache.getInstance().initUser(email.getText().toString(), token.toString());
                final SharedPreferences preferences = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                final SharedPreferences.Editor edit = preferences.edit();
                edit.putString("username", email.getText().toString());
                edit.putString("password", password.getText().toString());
                edit.apply();
                Snackbar.make(view, "登录成功:" + token.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }, failedMessage -> Snackbar.make(view, "登录失败:" + failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}