package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.DashboardApi;
import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.DashboardResult;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;

public class DashboardRequest extends Request {

    public void upload(List<DashboardResult> data, Consumer<? super Object> success, Consumer<? super Object> failed) {
        DashboardApi request = retrofit.create(DashboardApi.class);
        Call<ApiResponse> call = request.upload(data);
        sendRequest(call, success, failed);
    }
}
