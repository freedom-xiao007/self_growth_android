package com.example.selfgrowth.http.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRequestInterface {

    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里

    /**
     * 获取列表
     * */
    @GET("v1/hello/")
    Call<String> getList();
}
