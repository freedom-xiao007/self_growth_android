package com.example.selfgrowth.http;

import android.util.Log;

import com.example.selfgrowth.http.interceptor.AccessTokenInterceptor;

import java.util.concurrent.TimeUnit;

import lombok.Data;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Data
public class RetrofitClient {

    private static final RetrofitClient instance = new RetrofitClient();
    public static RetrofitClient getInstance() {
        return instance;
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(HttpConfig.getServerUrl()) //基础url,其他部分在GetRequestInterface里
            .client(httpClient())
            .addConverterFactory(GsonConverterFactory.create()) //Gson数据转换器
            .build();

    private OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AccessTokenInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public void httpClientReload() {
        Log.i("reload http client:", HttpConfig.getServerUrl());
        this.retrofit = new Retrofit.Builder()
                .baseUrl(HttpConfig.getServerUrl()) //基础url,其他部分在GetRequestInterface里
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create()) //Gson数据转换器
                .build();
    }
}
