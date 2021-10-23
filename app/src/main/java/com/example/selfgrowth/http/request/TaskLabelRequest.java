package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.TaskLabelApi;
import com.example.selfgrowth.http.model.ApiResponse;

import java.util.function.Consumer;

import retrofit2.Call;

public class TaskLabelRequest extends Request {

    public void addLabel(String name, Consumer<? super Object> success, Consumer<? super Object> failed) {
        TaskLabelApi request = retrofit.create(TaskLabelApi.class);
        Call<ApiResponse> call = request.addLabel(name);
        sendRequest(call, success, failed);
    }
}
