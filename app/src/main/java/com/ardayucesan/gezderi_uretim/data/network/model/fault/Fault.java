package com.ardayucesan.gezderi_uretim.data.network.model.fault;

import java.util.ArrayList;

public class Fault {
    private String faultId;
    private String faultDesc;
    private String faultType;

    private ArrayList<String> faultIds;

    public Fault(String faultId,String faultDesc,String faultType){
        this.faultId = faultId;
        this.faultDesc = faultDesc;
        this.faultType = faultType;
    }

    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }
    public String getFaultDesc() {
        return faultDesc;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }
    public String getFaultId() {
        return faultId;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }
    public String getFaultType() {
        return faultType;
    }

}
/*
 {
 "id": "2",
 "desc": "800 - Kap. İnce Kalın",
 "type": "quality"
 }
 ***/