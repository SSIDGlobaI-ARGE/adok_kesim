package com.ardayucesan.gezderi_uretim.data.network.model.workorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    private static final long serialVersionUID = -7060210544300464481L;
    @SerializedName("production_id")
    @Expose
    private String productionId;
    @SerializedName("workorder_code")
    @Expose
    private String workorderCode;
    @SerializedName("workorder_qty")
    @Expose
    private String workorderQty;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("rulo")
    @Expose
    private Integer roll;
    @SerializedName("product_color")
    @Expose
    private String productColor;
    @SerializedName("kesim_miktari")
    @Expose
    private String totalCutValue;
    @SerializedName("K_miktar")
    @Expose
    private float kQuantity;
    @SerializedName("K_cuts")
    @Expose
    private ArrayList<String> cutIds1K = new ArrayList<>();
    private String speed;
    private String thickness;
    private String weight;
    private String description;


    /**
     * No args constructor for use in serialization
     */
    public Order() {
    }


    /**
     * @param workorderCode
     * @param roll
     * @param workorderQty
     * @param totalCutValue
     * @param productionId
     * @param productColor
     * @param kQuantity
     * @param productName
     * @param customerName
     */
    public Order(String productionId, String workorderCode, String workorderQty, String productName, String customerName, Integer roll, String productColor, String totalCutValue, float kQuantity, ArrayList<String> cutIds1K, String speed, String thickness, String weight, String description) {
        super();
        this.productionId = productionId;
        this.workorderCode = workorderCode;
        this.workorderQty = workorderQty;
        this.productName = productName;
        this.customerName = customerName;
        this.roll = roll;
        this.productColor = productColor;
        this.totalCutValue = totalCutValue;
        this.kQuantity = kQuantity;
        this.cutIds1K = cutIds1K;
        this.speed = speed;
        this.thickness = thickness;
        this.weight = weight;
        this.description = description;
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

    public Integer getRoll() {
        return roll;
    }

    public void setRoll(Integer roll) {
        this.roll = roll;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getCutQuantity() {
        return totalCutValue;
    }

    public void setCutQuantity(String kesimMiktari) {
        this.totalCutValue = kesimMiktari;
    }

    public float getKMiktar() {
        return kQuantity;
    }

    public void setKMiktar(float kMiktar) {
        this.kQuantity = kMiktar;
    }

    public ArrayList<String> getCutIds1K() {
        return cutIds1K;
    }

    public void setCutIds1K(ArrayList<String> cutIds1K) {
        this.cutIds1K = cutIds1K;
    }

    public void addCutIds1K(ArrayList<String> cutIds1K){

        for (String c:cutIds1K) {
            this.cutIds1K.add(c);
        }
    }

    public void deleteCutIds1K() {
        this.cutIds1K.clear();
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
