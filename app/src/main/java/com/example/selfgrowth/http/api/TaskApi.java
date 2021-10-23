package com.example.selfgrowth.http.api;

import com.example.selfgrowth.http.model.ApiResponse;
import com.example.selfgrowth.http.model.TaskConfig;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaskApi {

    /**
     * 上传手机使用记录到服务器
     *
     **/
    @POST("v1/task/add")
    Call<ApiResponse> add(@Body TaskConfig taskConfig);

    @GET("v1/task/list")
    Call<ApiResponse> list();
}
