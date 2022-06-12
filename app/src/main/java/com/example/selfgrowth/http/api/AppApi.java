package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.Feedback;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppApi {

    @POST("/api/app/feedback")
    Call<ApiResponse> feedback(@Body Feedback feedback);

    @GET("/api/app/versionCheck")
    Call<ApiResponse> versionCheck(@Query("version") int version);
}
