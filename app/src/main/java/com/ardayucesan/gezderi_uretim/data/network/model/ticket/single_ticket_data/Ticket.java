package com.ardayucesan.gezderi_uretim.data.network.model.ticket.single_ticket_data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticket {

    @SerializedName("sirket_adi")
    @Expose
    private String sirketAdi;
    @SerializedName("m_urun_adi")
    @Expose
    private String mUrunAdi;
    @SerializedName("m_renk_adi")
    @Expose
    private String mRenkAdi;
    @SerializedName("barkod")
    @Expose
    private String barkod;
    @SerializedName("urun_kodu")
    @Expose
    private String urunKodu;
    @SerializedName("urun_adi")
    @Expose
    private String urunAdi;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("spy_kod")
    @Expose
    private String spyKod;
    @SerializedName("m_siparis_no")
    @Expose
    private String mSiparisNo;
    @SerializedName("renk_no")
    @Expose
    private String renkNo;
    @SerializedName("siparis_no")
    @Expose
    private String siparisNo;
    @SerializedName("tarih")
    @Expose
    private String tarih;
    @SerializedName("metraj")
    @Expose
    private String metraj;
    @SerializedName("lot_barkod")
    @Expose
    private String lotBarkod;
    @SerializedName("top_boyu")
    @Expose
    private String topBoyu;
    @SerializedName("is_emri")
    @Expose
    private String isEmri;
    @SerializedName("kalite")
    @Expose
    private String kalite;
    @SerializedName("aciklama1")
    @Expose
    private String aciklama1;
    @SerializedName("aciklama2")
    @Expose
    private String aciklama2;
    @SerializedName("lot_adet")
    @Expose
    private String lotAdet;
    @SerializedName("etiket_baslik")
    @Expose
    private String etiketBaslik;
    @SerializedName("top_adet")
    @Expose
    private String topAdet;
    @SerializedName("parca_adet")
    @Expose
    private String parcaAdet;
    @SerializedName("bonus")
    @Expose
    private String bonus;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("p0")
    @Expose
    private String p0;
    @SerializedName("p1")
    @Expose
    private String p1;
    @SerializedName("p2")
    @Expose
    private String p2;
    @SerializedName("p3")
    @Expose
    private String p3;
    @SerializedName("p4")
    @Expose
    private String p4;
    @SerializedName("p5")
    @Expose
    private String p5;
    @SerializedName("makine_adi")
    @Expose
    private String machineName;

    /**
     * No args constructor for use in serialization
     *
     */
    public Ticket() {
    }

    /**
     *
     * @param p0
     * @param topAdet
     * @param p1
     * @param p2
     * @param urunKodu
     * @param p3
     * @param p4
     * @param p5
     * @param bonus
     * @param siparisNo
     * @param barkod
     * @param isEmri
     * @param lotAdet
     * @param mRenkAdi
     * @param lotNo
     * @param tarih
     * @param topBoyu
     * @param renkNo
     * @param tip
     * @param etiketBaslik
     * @param mUrunAdi
     * @param parcaAdet
     * @param mSiparisNo
     * @param aciklama1
     * @param aciklama2
     * @param kalite
     * @param sirketAdi
     * @param urunAdi
     * @param metraj
     * @param spyKod
     * @param lotBarkod
     * @param makineAdi
     */
    public Ticket(String sirketAdi, String mUrunAdi, String mRenkAdi, String barkod, String urunKodu, String urunAdi, String lotNo, String spyKod, String mSiparisNo, String renkNo, String siparisNo, String tarih, String metraj, String lotBarkod, String topBoyu, String isEmri, String kalite, String aciklama1, String aciklama2, String lotAdet, String etiketBaslik, String topAdet, String parcaAdet, String bonus, String tip, String p0, String p1, String p2, String p3, String p4, String p5,String machineName) {
        super();
        this.sirketAdi = sirketAdi;
        this.mUrunAdi = mUrunAdi;
        this.mRenkAdi = mRenkAdi;
        this.barkod = barkod;
        this.urunKodu = urunKodu;
        this.urunAdi = urunAdi;
        this.lotNo = lotNo;
        this.spyKod = spyKod;
        this.mSiparisNo = mSiparisNo;
        this.renkNo = renkNo;
        this.siparisNo = siparisNo;
        this.tarih = tarih;
        this.metraj = metraj;
        this.lotBarkod = lotBarkod;
        this.topBoyu = topBoyu;
        this.isEmri = isEmri;
        this.kalite = kalite;
        this.aciklama1 = aciklama1;
        this.aciklama2 = aciklama2;
        this.lotAdet = lotAdet;
        this.etiketBaslik = etiketBaslik;
        this.topAdet = topAdet;
        this.parcaAdet = parcaAdet;
        this.bonus = bonus;
        this.tip = tip;
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.machineName = machineName;
    }

    public String getSirketAdi() {
        return sirketAdi;
    }

    public void setSirketAdi(String sirketAdi) {
        this.sirketAdi = sirketAdi;
    }

    public String getmUrunAdi() {
        return mUrunAdi;
    }

    public void setmUrunAdi(String mUrunAdi) {
        this.mUrunAdi = mUrunAdi;
    }

    public String getmRenkAdi() {
        return mRenkAdi;
    }

    public void setmRenkAdi(String mRenkAdi) {
        this.mRenkAdi = mRenkAdi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getUrunKodu() {
        return urunKodu;
    }

    public void setUrunKodu(String urunKodu) {
        this.urunKodu = urunKodu;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getSpyKod() {
        return spyKod;
    }

    public void setSpyKod(String spyKod) {
        this.spyKod = spyKod;
    }

    public String getmSiparisNo() {
        return mSiparisNo;
    }

    public void setmSiparisNo(String mSiparisNo) {
        this.mSiparisNo = mSiparisNo;
    }

    public String getRenkNo() {
        return renkNo;
    }

    public void setRenkNo(String renkNo) {
        this.renkNo = renkNo;
    }

    public String getSiparisNo() {
        return siparisNo;
    }

    public void setSiparisNo(String siparisNo) {
        this.siparisNo = siparisNo;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getMetraj() {
        return metraj;
    }

    public void setMetraj(String metraj) {
        this.metraj = metraj;
    }

    public String getLotBarkod() {
        return lotBarkod;
    }

    public void setLotBarkod(String lotBarkod) {
        this.lotBarkod = lotBarkod;
    }

    public String getTopBoyu() {
        return topBoyu;
    }

    public void setTopBoyu(String topBoyu) {
        this.topBoyu = topBoyu;
    }

    public String getIsEmri() {
        return isEmri;
    }

    public void setIsEmri(String isEmri) {
        this.isEmri = isEmri;
    }

    public String getKalite() {
        return kalite;
    }

    public void setKalite(String kalite) {
        this.kalite = kalite;
    }

    public String getAciklama1() {
        return aciklama1;
    }

    public void setAciklama1(String aciklama1) {
        this.aciklama1 = aciklama1;
    }

    public String getAciklama2() {
        return aciklama2;
    }

    public void setAciklama2(String aciklama2) {
        this.aciklama2 = aciklama2;
    }

    public String getLotAdet() {
        return lotAdet;
    }

    public void setLotAdet(String lotAdet) {
        this.lotAdet = lotAdet;
    }

    public String getEtiketBaslik() {
        return etiketBaslik;
    }

    public void setEtiketBaslik(String etiketBaslik) {
        this.etiketBaslik = etiketBaslik;
    }

    public String getTopAdet() {
        return topAdet;
    }

    public void setTopAdet(String topAdet) {
        this.topAdet = topAdet;
    }

    public String getParcaAdet() {
        return parcaAdet;
    }

    public void setParcaAdet(String parcaAdet) {
        this.parcaAdet = parcaAdet;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getP0() {
        return p0;
    }

    public void setP0(String p0) {
        this.p0 = p0;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
}
