package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.PhoneUseRecordApi;
import com.example.selfgrowth.http.model.ApiResponse;

import java.util.function.Consumer;

import retrofit2.Call;

public class PhoneUseRecordRequest extends Request {

    public void uploadRecord(String activity, Consumer<? super Object> success, Consumer<? super Object> failed) {
        PhoneUseRecordApi request = retrofit.create(PhoneUseRecordApi.class);
        Call<ApiResponse> call = request.uploadRecord(activity);
        sendRequest(call, success, failed);
    }
}
