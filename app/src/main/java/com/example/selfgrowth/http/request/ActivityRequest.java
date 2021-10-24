package com.example.selfgrowth.http.request;

import com.example.selfgrowth.http.api.ActivityApi;
import com.example.selfgrowth.http.model.ActivityModel;
import com.example.selfgrowth.http.model.ApiResponse;

import java.util.function.Consumer;

import retrofit2.Call;

public class ActivityRequest extends Request {

    public void uploadRecord(String activity, Consumer<? super Object> success, Consumer<? super Object> failed) {
        ActivityApi request = retrofit.create(ActivityApi.class);
        Call<ApiResponse> call = request.uploadRecord(activity);
        sendRequest(call, success, failed);
    }

    public void overview(Consumer<? super Object> success, Consumer<? super Object> failed) {
        ActivityApi request = retrofit.create(ActivityApi.class);
        Call<ApiResponse> call = request.overview();
        sendRequest(call, success, failed);
    }

    public void updateActivityModel(ActivityModel activityModel, Consumer<? super Object> success, Consumer<? super Object> failed) {
        ActivityApi request = retrofit.create(ActivityApi.class);
        Call<ApiResponse> call = request.updateActivityModel(activityModel);
        sendRequest(call, success, failed);
    }

    public void activityHistory(String activity, Consumer<? super Object> success, Consumer<? super Object> failed) {
        ActivityApi request = retrofit.create(ActivityApi.class);
        Call<ApiResponse> call = request.activityHistory(activity, 0, 0);
        sendRequest(call, success, failed);
    }
}
