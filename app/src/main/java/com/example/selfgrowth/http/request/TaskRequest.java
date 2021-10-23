package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.TaskApi;
import com.example.selfgrowth.http.api.UserApi;
import com.example.selfgrowth.http.model.ApiResponse;
import com.example.selfgrowth.http.model.LoginUser;
import com.example.selfgrowth.http.model.NewTask;

import java.util.function.Consumer;

import retrofit2.Call;

public class TaskRequest extends Request {

    public void add(NewTask task, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.add(task);
        sendRequest(call, success, failed);
    }
}
