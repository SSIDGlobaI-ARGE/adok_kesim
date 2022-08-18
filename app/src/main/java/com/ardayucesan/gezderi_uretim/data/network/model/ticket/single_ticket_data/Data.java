package com.ardayucesan.gezderi_uretim.data.network.model.ticket.single_ticket_data;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("rows")
    @Expose
    private List<Ticket> tickets = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param tickets
     */
    public Data(List<Ticket> tickets) {
        super();
        this.tickets = tickets;
    }

    public List<Ticket> getRows() {
        return tickets;
    }

    public void setRows(List<Ticket> tickets) {
        this.tickets = tickets;
    }

}