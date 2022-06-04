package com.example.selfgrowth.http.api;

import com.example.selfgrowth.model.ApiResponse;
import com.example.selfgrowth.model.LoginUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    /**
     * 用户登录
     **/
    @POST("/sso/doLogin")
    Call<ApiResponse> login(@Query("name") String name, @Query("pwd") String pwd);
}
