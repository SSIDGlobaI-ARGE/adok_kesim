package com.ardayucesan.adok_kesim.data.network.model.workorder;

import android.view.View;

public class WorkHolder {
    String workOrderId;
    String workOrderName;
    int visiblity = View.INVISIBLE;
    boolean cbVisibility = false;

    public WorkHolder(String productionId, String productionName) {
        this.workOrderId = productionId;
        this.workOrderName = productionName;
//        this.visiblity = visiblity;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderName() {
        return workOrderName;
    }

    public void setWorkOrderName(String workOrderName) {
        this.workOrderName = workOrderName;
    }

    public int getVisiblity() {
        return visiblity;
    }

    public void setVisiblity(int visiblity) {
        this.visiblity = visiblity;
    }

    public boolean isCbVisibility() {
        return cbVisibility;
    }

    public void setCbVisibility(boolean cbVisibility) {
        this.cbVisibility = cbVisibility;
    }
}