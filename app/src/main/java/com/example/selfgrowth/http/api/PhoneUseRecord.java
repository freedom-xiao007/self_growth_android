package com.example.selfgrowth.http.api;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PhoneUseRecord {

    /**
     * 上传手机使用记录到服务器
     *
     **/
    @Multipart
    @POST("v1/phone/useRecord")
    Call<String> uploadRecord(@Part("activity") final String activity);
}
