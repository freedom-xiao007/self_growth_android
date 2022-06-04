package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.DashboardResult;
import com.example.selfgrowth.model.TaskConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DashboardApi {

    /**
     * 仪表盘统计
     **/
    @GET("v1/dashboard/statistics")
    Call<ApiResponse> statistics();

    @POST("/api/record/upload")
    Call<ApiResponse> upload(@Body List<DashboardResult> data);
}
