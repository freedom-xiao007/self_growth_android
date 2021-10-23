package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.TaskApi;
import com.example.selfgrowth.http.model.ApiResponse;
import com.example.selfgrowth.http.model.TaskConfig;

import java.util.function.Consumer;

import retrofit2.Call;

public class TaskRequest extends Request {

    public void add(TaskConfig task, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.add(task);
        sendRequest(call, success, failed);
    }

    public void list(Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.list();
        sendRequest(call, success, failed);
    }
}
