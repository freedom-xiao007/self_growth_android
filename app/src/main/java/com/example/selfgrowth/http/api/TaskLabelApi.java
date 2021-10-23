package com.example.selfgrowth.http.api;

import com.example.selfgrowth.http.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TaskLabelApi {

    /**
     * 新增任务标签
     **/
    @Multipart
    @POST("v1/label/add")
    Call<ApiResponse> addLabel(@Part("name") final String name);
}
