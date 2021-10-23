package com.example.selfgrowth.http.api;

import com.example.selfgrowth.http.model.ApiResponse;
import com.example.selfgrowth.http.model.LoginUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApi {

    /**
     * 用户登录
     **/
    @POST("auth/user/login")
    Call<ApiResponse> login(@Body LoginUser user);
}
