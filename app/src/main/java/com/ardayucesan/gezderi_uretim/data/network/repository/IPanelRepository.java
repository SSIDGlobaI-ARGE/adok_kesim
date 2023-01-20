package com.ardayucesan.gezderi_uretim.data.network.repository;

import com.ardayucesan.gezderi_uretim.data.network.model.ticket.single_ticket_data.TicketResponse;
import com.ardayucesan.gezderi_uretim.data.network.model.ticket.ticket_list_data.TicketListResponse;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

@SuppressWarnings("ALL")
public interface IPanelRepository {

    @POST("setdatascreen")
    Observable<JsonObject> postQuantity(@Body JsonObject body);

    @POST("productionstatus")
    Observable<JsonObject> postProductionStatus(@Body JsonObject body);

    @POST("getbarcodelist")
    Observable<TicketListResponse> getPreviousTicketList(@Body JsonObject body);

    @POST("screenquit")
    Observable<JsonObject> postQuit(@Body JsonObject body);

    @POST("getfaultlist")
    Observable<JsonObject> getFaultList(@Body JsonObject body);

    @POST("getprint")
    Observable<TicketResponse> getTicketData(@Body JsonObject body);

    @GET("gettime")
    Observable<JsonObject> getTime();


}
