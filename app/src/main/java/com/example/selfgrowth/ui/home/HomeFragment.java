package com.example.selfgrowth.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.databinding.FragmentHomeBinding;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.request.UserRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private final UserRequest userRequest = new UserRequest();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        final String userName = preferences.getString("username", "");
        final String password = preferences.getString("password", "");
        if (userName.isEmpty() || password.isEmpty()) {
            Snackbar.make(getView(), "无用户，请登录", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if (UserCache.getInstance().isLogin()) {
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
        }, failedMessage -> {
            Snackbar.make(getView(), "登录失败:" + failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}