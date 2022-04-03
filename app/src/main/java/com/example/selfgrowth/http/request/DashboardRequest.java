package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.DashboardApi;
import com.example.selfgrowth.model.ApiResponse;

import java.util.function.Consumer;

import retrofit2.Call;

public class DashboardRequest extends Request {

    public void statistics(Consumer<? super Object> success, Consumer<? super Object> failed) {
        DashboardApi request = retrofit.create(DashboardApi.class);
        Call<ApiResponse> call = request.statistics();
        sendRequest(call, success, failed);
    }
}
