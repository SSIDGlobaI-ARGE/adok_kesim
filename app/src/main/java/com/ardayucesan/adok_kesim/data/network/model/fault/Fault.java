package com.ardayucesan.adok_kesim.data.network.model.fault;

import java.util.ArrayList;

public class Fault {
    private String faultId;
    private String faultDesc;

    private ArrayList<String> faultIds;

    public Fault(String faultId,String faultDesc){
        this.faultId = faultId;
        this.faultDesc = faultDesc;
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

}
/*
 {
 "id": "2",
 "desc": "800 - Kap. İnce Kalın",
 "type": "quality"
 }
 ***/