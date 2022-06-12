package com.example.selfgrowth.ui.user;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.codingending.popuplayout.PopupLayout;
import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.enums.StatisticsTypeEnum;
import com.example.selfgrowth.http.HttpConfig;
import com.example.selfgrowth.http.RetrofitClient;
import com.example.selfgrowth.http.request.AppRequest;
import com.example.selfgrowth.model.DashboardStatistics;
import com.example.selfgrowth.model.Feedback;
import com.example.selfgrowth.service.backend.AppStatisticsService;
import com.example.selfgrowth.service.backend.TaskLogService;
import com.example.selfgrowth.ui.activity.AppFragment;
import com.example.selfgrowth.ui.activity.AppHistoryFragment;
import com.example.selfgrowth.ui.dashboard.DailyDashboardFragment;
import com.example.selfgrowth.ui.dashboard.PeriodDashboardFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class UserFragment extends Fragment {

    private final TaskLogService taskLogService = TaskLogService.getInstance();
    private final AppRequest appRequest = new AppRequest();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initUserInfo(view);
        initOverview(view);
        initVersionCheck(view);
        initFeedback(view);
        setRoute(view, R.id.app_setting, new AppFragment());
        setRoute(view, R.id.daily_dashboard, new DailyDashboardFragment());
        setRoute(view, R.id.week_dashboard, new PeriodDashboardFragment(StatisticsTypeEnum.WEEK));
        setRoute(view, R.id.month_dashboard, new PeriodDashboardFragment(StatisticsTypeEnum.MONTH));
        setRoute(view, R.id.year_dashboard, new PeriodDashboardFragment(StatisticsTypeEnum.YEAR));
        setRoute(view, R.id.data_sync_setting, new SettingFragment());
        return view;
    }

    private void initVersionCheck(View view) {
        view.findViewById(R.id.app_version_check).setOnClickListener(v -> {
            View settingView = View.inflate(view.getContext(), R.layout.server_url_setting, null);

            settingView.findViewById(R.id.server_url_setting_button).setOnClickListener(ignore -> {
                EditText serverUrl = settingView.findViewById(R.id.server_url);
                HttpConfig.setServerUrl(serverUrl.getText().toString());
                try {
                    RetrofitClient.getInstance().httpClientReload();
                    Snackbar.make(settingView, "设置成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    Snackbar.make(settingView, "设置失败:" + e.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            PopupLayout popupLayout= PopupLayout.init(view.getContext(), settingView);
            popupLayout.show(PopupLayout.POSITION_TOP);
        });
    }

    private void initFeedback(View view) {
        view.findViewById(R.id.app_feedback).setOnClickListener(v -> {
            View settingView = View.inflate(view.getContext(), R.layout.feedback, null);
            PopupLayout popupLayout= PopupLayout.init(view.getContext(), settingView);
            settingView.findViewById(R.id.confirm).setOnClickListener(bv -> {
                String email = ((EditText) settingView.findViewById(R.id.email)).getText().toString();
                String feedback = ((EditText) settingView.findViewById(R.id.feedback)).getText().toString();
                if (feedback.isEmpty()) {
                    Snackbar.make(view, "请填写反馈内容", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                appRequest.feedback(Feedback.builder().email(email).msg(feedback).build(),
                        success -> {
                            popupLayout.dismiss();
                            Snackbar.make(view, "感谢您的反馈", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        },
                        failed -> {
                            Snackbar.make(settingView, "网络故障，请其他时间重试", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        });
            });

            settingView.findViewById(R.id.cancel).setOnClickListener(bv -> {
                popupLayout.dismiss();
            });

            popupLayout.show(PopupLayout.POSITION_BOTTOM);
        });
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

        userEmail.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.user_info, new LoginFragment())
                .addToBackStack(null)
                .commit());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initOverview(View view) {
        DashboardStatistics statistics = AppStatisticsService.getInstance().statistics(new Date(), view.getContext());
        Map<String, DashboardStatistics.DashboardGroup> groups = statistics.getGroups();
        if (groups == null) {
            return;
        }
        long learnMinutes = Objects.requireNonNull(groups.getOrDefault(LabelEnum.LEARN.getName(), DashboardStatistics.DashboardGroup.builder().build())).getMinutes();
        long runningMinutes = Objects.requireNonNull(groups.getOrDefault(LabelEnum.RUNNING.getName(), DashboardStatistics.DashboardGroup.builder().build())).getMinutes();
        long sleepMinutes = Objects.requireNonNull(groups.getOrDefault(LabelEnum.SLEEP.getName(), DashboardStatistics.DashboardGroup.builder().build())).getMinutes();
        int taskComplete = taskLogService.list(new Date()).size();
        ((TextView)view.findViewById(R.id.learn_minutes)).setText(String.valueOf(learnMinutes));
        ((TextView)view.findViewById(R.id.running_minutes)).setText(String.valueOf(runningMinutes));
        ((TextView)view.findViewById(R.id.sleep_minutes)).setText(String.valueOf(sleepMinutes));
        ((TextView)view.findViewById(R.id.task_complete)).setText(String.valueOf(taskComplete));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}