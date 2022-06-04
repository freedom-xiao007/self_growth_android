package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.TaskConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TaskApi {

    @POST("/api/task/sync")
    Call<ApiResponse> sync(@Body List<TaskConfig> configs);
}
