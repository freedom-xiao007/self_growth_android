package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.TaskConfig;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskApi {

    /**
     * 上传手机使用记录到服务器
     *
     **/
    @POST("v1/task/add")
    Call<ApiResponse> add(@Body TaskConfig taskConfig);

    @GET("v1/task/list")
    Call<ApiResponse> list(@Query("groupName") String groupName, @Query("isComplete") String isComplete);

    @POST("v1/task/complete/{id}")
    Call<ApiResponse> complete(@Path("id") String id);

    @GET("v1/task/history")
    Call<ApiResponse> history();

    @GET("v1/task/allGroups")
    Call<ApiResponse> allGroups();
}
