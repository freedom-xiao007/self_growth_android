package com.example.selfgrowth.http;

import lombok.Data;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Data
public class RetrofitClient {

    private static final RetrofitClient instance = new RetrofitClient();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(HttpConfig.ADDRESS) //基础url,其他部分在GetRequestInterface里
            .addConverterFactory(GsonConverterFactory.create()) //Gson数据转换器
            .build();

    public static RetrofitClient getInstance() {
        return instance;
    }
}
