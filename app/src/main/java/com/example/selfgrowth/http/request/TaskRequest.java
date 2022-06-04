package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.TaskApi;
import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.TaskConfig;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;

public class TaskRequest extends Request {

    public void sync(List<TaskConfig> configs, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.sync(configs);
        sendRequest(call, success, failed);
    }
}
