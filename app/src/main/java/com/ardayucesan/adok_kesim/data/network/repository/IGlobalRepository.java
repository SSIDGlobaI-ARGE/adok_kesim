package com.ardayucesan.adok_kesim.data.network.repository;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGlobalRepository {

    @POST("getmachinename")
    Observable<JsonObject> getMachineName(@Body JsonObject body);
}
