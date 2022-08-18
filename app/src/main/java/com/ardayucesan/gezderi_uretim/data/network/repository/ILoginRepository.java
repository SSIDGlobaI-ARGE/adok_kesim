package com.ardayucesan.gezderi_uretim.data.network.repository;

import com.google.gson.JsonObject;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;



public interface ILoginRepository {
    @POST("login")
    Observable<JsonObject> getUser(@Body JsonObject body);

    @POST("appversion")
    Call<JsonObject> checkUpdates(@Body JsonObject body);
}