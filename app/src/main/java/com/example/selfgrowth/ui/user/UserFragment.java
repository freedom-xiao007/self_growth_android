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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.enums.LabelEnum;
import com.example.selfgrowth.http.model.DashboardStatistics;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.service.foregroud.AppStatisticsService;
import com.example.selfgrowth.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class UserFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initOverview(view);
        return view;
//        if (UserCache.getInstance().isLogin()) {
//            return userInfo(inflater, container);
//        } else {
//            return loadLoginFragment(inflater, container);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initOverview(View view) {
        DashboardStatistics statistics = AppStatisticsService.getInstance().statistics(new Date(), view.getContext());
        long learnMinutes = statistics.getGroups().getOrDefault(LabelEnum.LEARN.getName(), DashboardStatistics.DashboardGroup.builder().build()).getMinutes();
        long runningMinutes = statistics.getGroups().getOrDefault(LabelEnum.RUNNING.getName(), DashboardStatistics.DashboardGroup.builder().build()).getMinutes();
        long sleepMinutes = statistics.getGroups().getOrDefault(LabelEnum.SLEEP.getName(), DashboardStatistics.DashboardGroup.builder().build()).getMinutes();
        ((TextView)view.findViewById(R.id.learn_minutes)).setText(String.valueOf(learnMinutes));
        ((TextView)view.findViewById(R.id.running_minutes)).setText(String.valueOf(runningMinutes));
        ((TextView)view.findViewById(R.id.sleep_minutes)).setText(String.valueOf(sleepMinutes));
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