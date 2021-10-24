package com.example.selfgrowth.http.api;

import com.example.selfgrowth.http.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ActivityApi {

    /**
     * 上传手机使用记录到服务器
     *
     **/
    @Multipart
    @POST("v1/phone/useRecord")
    Call<ApiResponse> uploadRecord(@Part("activity") final String activity);

    @GET("v1/phone/overview")
    Call<ApiResponse> overview();
}
