package com.ardayucesan.gezderi_uretim.data.network.model.ticket.ticket_list_data;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketListResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<String> barcodeList = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public TicketListResponse() {
    }

    /**
     *
     * @param barcodeList
     * @param success
     * @param message
     */
    public TicketListResponse(Boolean success, String message, List<String> barcodeList) {
        super();
        this.success = success;
        this.message = message;
        this.barcodeList = barcodeList;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<String> barcodeList) {
        this.barcodeList = barcodeList;
    }

}
