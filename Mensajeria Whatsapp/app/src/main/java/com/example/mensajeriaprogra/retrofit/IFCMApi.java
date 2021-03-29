package com.example.mensajeriaprogra.retrofit;

import com.example.mensajeriaprogra.models.FCMBody;
import com.example.mensajeriaprogra.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAZV9KaZI:APA91bEWQ5-R7XSG53edn3vyJbNt2GiV01PcWzaqaxHDx3vqZp3NGdgXgFnraCdwyKAi1XC0Kw9C-2s9Qx40yCn_Mnkv58IaKJzfrYXKWXTKynQYufQVCUD0whC22vMCS3OiIuXWZya1"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}