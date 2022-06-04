package com.example.selfgrowth.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.selfgrowth.R;
import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.request.UserRequest;
import com.example.selfgrowth.model.LoginUser;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private final UserRequest userRequest = new UserRequest();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            EditText username = rootView.findViewById(R.id.login_user_email);
            EditText password = rootView.findViewById(R.id.login_password);

            userRequest.login(username.getText().toString(), password.getText().toString(), (token) -> {
                final SharedPreferences preferences = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                final SharedPreferences.Editor edit = preferences.edit();
                edit.putString("username", username.getText().toString());
                edit.putString("password", password.getText().toString());
                edit.apply();
                Snackbar.make(view, "登录成功:" + token, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }, failedMessage -> Snackbar.make(view, "登录失败:" + failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
        });
        return rootView;
    }
}
