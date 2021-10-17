package com.example.selfgrowth.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.selfgrowth.http.GetRequestInterface;
import com.example.selfgrowth.ssl.SSLSocketFactoryUtils;

import java.io.IOException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public String url= "https://reqres.in/api/users/2";

    public HomeViewModel() throws IOException {
//        final String text = get("https://192.168.1.3:8443/v1/hello");
//        final String text = get("https://www.baidu.com");
//        final String text = get("http://192.168.1.3:8080/v1/hello");
        mText = new MutableLiveData<>();
        mText.setValue("home init");

        getSuperHeroes();
    }

    private void getSuperHeroes() {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.3:8080/") //基础url,其他部分在GetRequestInterface里
                .addConverterFactory(GsonConverterFactory.create()) //Gson数据转换器
                .build();

        //创建网络请求接口实例
        GetRequestInterface request = retrofit.create(GetRequestInterface.class);
        Call<String> call = request.getList();

        //发送网络请求(异步)
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String outWarehouseList = response.body();
                System.out.println(outWarehouseList);
                mText.setValue("get web:" + outWarehouseList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("GetOutWarehouseList->onFailure(MainActivity.java): "+t.toString() );
            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }
}