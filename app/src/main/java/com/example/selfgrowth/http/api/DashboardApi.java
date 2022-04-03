package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DashboardApi {

    /**
     * 仪表盘统计
     **/
    @GET("v1/dashboard/statistics")
    Call<ApiResponse> statistics();
}
