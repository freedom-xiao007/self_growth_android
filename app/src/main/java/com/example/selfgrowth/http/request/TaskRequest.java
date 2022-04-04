package com.example.selfgrowth.http.request;

import android.util.Log;

import com.example.selfgrowth.http.api.TaskApi;
import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.TaskConfig;

import java.util.function.Consumer;

import retrofit2.Call;

public class TaskRequest extends Request {

    public void add(TaskConfig task, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.add(task);
        sendRequest(call, success, failed);
    }

    public void list(String groupName, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.list(groupName, "false");
        sendRequest(call, success, failed);
    }

    public void complete(String id, Consumer<? super Object> success, Consumer<? super Object> failed) {
        Log.i("完成任务：", id);
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.complete(id);
        sendRequest(call, success, failed);
    }

    public void history(Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.history();
        sendRequest(call, success, failed);
    }

    public void allGroups(Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.allGroups();
        sendRequest(call, success, failed);
    }

    public void update(TaskConfig task, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskApi request = retrofit.create(TaskApi.class);
        Call<ApiResponse> call = request.update(task);
        sendRequest(call, success, failed);
    }
}
