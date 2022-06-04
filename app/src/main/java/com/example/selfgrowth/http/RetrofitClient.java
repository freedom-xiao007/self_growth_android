package com.example.selfgrowth.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.selfgrowth.cache.UserCache;
import com.example.selfgrowth.http.interceptor.AccessTokenInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Data;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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
                .addInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    //存入Session
                    if (response.header("set-cookie") != null) {
                        final String originCookie = response.header("set-cookie");
                        String key = originCookie.split(";")[0].split("=")[0];
                        String token = originCookie.split(";")[0].split("=")[1];
                        Log.i("login:", String.format("key: %s, token: %s", key, token));
                        UserCache.getInstance().setAuth(key, token);
                    }
                    return response;
                })
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
