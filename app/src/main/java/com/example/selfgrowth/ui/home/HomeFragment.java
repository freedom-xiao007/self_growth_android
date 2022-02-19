package com.example.selfgrowth.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        autoLogin();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}