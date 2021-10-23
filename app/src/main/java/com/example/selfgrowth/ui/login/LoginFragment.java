package com.example.selfgrowth.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.request.UserRequest;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            EditText email = rootView.findViewById(R.id.login_email_edit);
            EditText password = rootView.findViewById(R.id.login_password_edit);

            final LoginUser user = LoginUser.builder()
                    .email(email.getText().toString())
                    .password(password.getText().toString())
                    .build();

            userRequest.login(user, success -> {
                UserCache.getInstance().initUser(email.getText().toString());
                Snackbar.make(view, "登录成功", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }, failed -> {
                Snackbar.make(view, "登录失败", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
        });
        return rootView;
    }
}
