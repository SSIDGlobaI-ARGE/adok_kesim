package com.ardayucesan.adok_kesim.data.network.model.workorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    private static final long serialVersionUID = -7060210544300464481L;
    @SerializedName("production_id")
    @Expose
    private String productionId;
    private String workOrderId;
    @SerializedName("workorder_code")
    @Expose
    private String workorderCode;
    @SerializedName("workorder_qty")
    @Expose
    private String workorderQty;
    private String orderNumber;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("kesim_miktari")
    @Expose
    private String totalCutValue;
    private float quantity1K = 0;
    private float quantity2K = 0;
    private String description;

    private String deadline;
    private String productId;

    /**
     * No args constructor for use in serialization
     */
    public Order() {
    }

    public Order(String productionId,String workOrderId, String workorderCode,String orderNumber, String workorderQty, String productName, String customerName, String totalCutValue, float quantity1K,float quantity2K ,String description,String productId,String deadline/* ArrayList<String> cutIds1K,ArrayList<String> cutIds2K,*/ ) {
        super();
        this.workOrderId = workOrderId;
        this.productionId = productionId;
        this.workorderCode = workorderCode;
        this.orderNumber = orderNumber;
        this.workorderQty = workorderQty;
        this.productName = productName;
        this.customerName = customerName;
        this.totalCutValue = totalCutValue;
        this.quantity1K = quantity1K;
        this.quantity2K = quantity2K;
        this.description = description;
        this.productId = productId;
        this.deadline = deadline;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getWorkorderCode() {
        return workorderCode;
    }

    public void setWorkorderCode(String workorderCode) {
        this.workorderCode = workorderCode;
    }

    public String getWorkorderQty() {
        return workorderQty;
    }


    public void setWorkorderQty(String workorderQty) {
        this.workorderQty = workorderQty;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCutQuantity() {
        return totalCutValue;
    }

    public void setCutQuantity(String kesimMiktari) {
        this.totalCutValue = kesimMiktari;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getTotalCutValue() {
        return totalCutValue;
    }

    public void setTotalCutValue(String totalCutValue) {
        this.totalCutValue = totalCutValue;
    }

    public float getQuantity1K() {
        return quantity1K;
    }

    public void setQuantity1K(float quantity1K) {
        this.quantity1K = quantity1K;
    }

    public float getQuantity2K() {
        return quantity2K;
    }

    public void setQuantity2K(float quantity2K) {
        this.quantity2K = quantity2K;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
