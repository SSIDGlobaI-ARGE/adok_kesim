package com.ardayucesan.adok_kesim.common;

public class Constants {

    public static final String BASE_URL = "http://78.186.71.81/deviceapi/screen/";
    public static final String IMAGE_URL = "http://78.186.71.81/images/generate/";
//    public static final String BASE_URL = "http://10.175.18.154:8081/gezderi-iot/kaplama-debug/";
    //    public static final String BASE_URL = "http://192.168.3.230/gezderi-iot/gofraj/";
    public static final String TYPE_URL = "http://212.175.18.154:8081/gezderi-iot/uretim-debug/";
//    public static final String TYPE_URL = "http://192.168.3.230/gezderi-iot/uretim/";
//    public static final String TYPE_URL = "http://192.168.3.230/gezderi-iot/uretim/";
    public static final String SALT = "OiUuTEhPN9T4YQRlTr09EikH2ZAxnYMnI&cF6fAMXWC-5TD*EeqDVBv*OFuvkPWZvX2U6iv&Mh2DinbdxcmugmmiUM4dpqOLo8oFqquexRs3pQ+5Q-r0G+Uzp1RnFwQ-";
    public static final String CHECK_ETH = "NETWORK İLE İLETİŞİM KURULAMADI NETWORK KABLOSUNU KONTROL EDİN";
    public static final String STATUS_FAULT_CODE = "6";
    public static final String STATUS_FORCED_FAULT_CODE = "13";
    public static final String STATUS_FINISHED_CODE = "3";
    public static final String STATUS_ACTIVE_CODE = "2";
    public static final String STATUS_ACTIVE_TEXT = "ÇALIŞIYOR";
    public static final String STATUS_NO_ORDER = "İŞ EMRİ YOK";
    public static final String CONNECTION_ERR = "İNTERNET BAĞLANTISI YOK.";
    public static final String ADMIN_PW = "123123";

    public enum MachineStatus {
        ACTIVE,
        FAULT,
        FORCED_FAULT,
        BREAK,
        NO_ORDER
    }
}
