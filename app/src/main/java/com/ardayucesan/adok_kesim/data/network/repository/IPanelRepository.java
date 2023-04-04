package com.ardayucesan.adok_kesim.data.network.repository;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@SuppressWarnings("ALL")
public interface IPanelRepository {

    @POST("savetomemory")
    Observable<JsonObject> postMemory(@Body JsonObject body);

    @POST("productionstatus")
    Observable<JsonObject> postProductionStatus(@Body JsonObject body);

    @GET("prevbarcodelist")
    Observable<JsonObject> getPreviousTicketList(@Query("point_id") String point_id );

    @POST("screenquit")
    Observable<JsonObject> postQuit(@Body JsonObject body);

    @GET("getstoplist")
    Observable<JsonObject> getStopList(@Query("point_id") String point_id);

    @GET("getfaultlist")
    Observable<JsonObject> getFaultList(@Query("product_id") String product_id);

    @POST("printmemory")
    Observable<JsonObject> getTicketData(@Body JsonObject body);

    @GET("gettime")
    Observable<JsonObject> getTime();

    @POST("getworkorders")
    Observable<JsonObject> getWorkOrderId(@Body JsonObject body);

    @POST("getproductlist")
    Observable<JsonObject> getProductList(@Body JsonObject body);

    @POST("startworkorder")
    Observable<JsonObject> startWorkOrder(@Body JsonObject body);

    @POST("newworkorder")
    Observable<JsonObject> newWorkOrder(@Body JsonObject body);

}
