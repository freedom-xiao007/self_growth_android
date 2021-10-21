package com.example.selfgrowth.http.api;

import com.example.selfgrowth.http.model.NewTask;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface TaskApi {

    /**
     * 上传手机使用记录到服务器
     *
     **/
    @Multipart
    @POST("v1/task/add")
    Call<String> uploadRecord(@Body NewTask newTask);
}
