package com.ardayucesan.adok_kesim.data.network.repository;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IOrderRepository {

    @POST("getworkorders")
    Call<JsonObject> getWorkOrderId(@Body JsonObject body);

    @POST("startworkorder")
    Observable<JsonObject> startWorkOrder(@Body JsonObject body);
}
