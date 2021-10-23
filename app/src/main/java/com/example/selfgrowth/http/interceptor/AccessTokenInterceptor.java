package com.example.selfgrowth.http.interceptor;

import androidx.annotation.NonNull;

import com.example.selfgrowth.cache.UserCache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (UserCache.getInstance().getToken() == null) {
            return chain.proceed(chain.request());
        }
        
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .addHeader("Authorization", UserCache.getInstance().getToken());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
