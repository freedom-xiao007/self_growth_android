package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.DashboardResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DashboardApi {

    @POST("/api/record/upload")
    Call<ApiResponse> upload(@Body List<DashboardResult> data);
}
