package com.example.selfgrowth.http.request;

import android.util.Log;

import com.example.selfgrowth.http.RetrofitClient;
import com.example.selfgrowth.http.model.ApiResponse;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public abstract class Request {

    final Retrofit retrofit;

    public Request() {
        this.retrofit = RetrofitClient.getInstance().getRetrofit();
    }

    /**
     * 发送网络请求(异步)
     * @param call call
     */
    void sendRequest(Call<ApiResponse> call, Consumer<? super Object> success, Consumer<? super Object> failed) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() != 200) {
                    Log.w("Http Response", "请求响应错误");
                    failed.accept(response.raw().message());
                    return;
                }
                if (response.body() == null || response.body().getData() == null) {
                    success.accept("");
                    return;
                }
                String res = response.body().getData().toString();
                Log.d("http response", res);
                success.accept(res);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println("GetOutWarehouseList->onFailure(MainActivity.java): "+t.toString() );
            }
        });
    }
}
