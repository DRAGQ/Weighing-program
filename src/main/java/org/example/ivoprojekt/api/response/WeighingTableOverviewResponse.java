package org.example.ivoprojekt.api.response;

import java.math.BigDecimal;

public class WeighingTableOverviewResponse {
    private String dateTime;
    private String entry;
    private String departure;
    private int number;
    private String type;
    private String partner;
    private String vehicle;
    private String material;
    private BigDecimal gross;
    private BigDecimal tara;
    private BigDecimal nett;

    public WeighingTableOverviewResponse() {}

    public WeighingTableOverviewResponse(String dateTime, String entry, String departure, int number, String type, String partner, String vehicle, String material, BigDecimal gross, BigDecimal tara, BigDecimal nett) {
        this.dateTime = dateTime;
        this.entry = entry;
        this.departure = departure;
        this.number = number;
        this.type = type;
        this.partner = partner;
        this.vehicle = vehicle;
        this.material = material;
        this.gross = gross;
        this.tara = tara;
        this.nett = nett;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getGross() {
        return gross;
    }

    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }

    public BigDecimal getTara() {
        return tara;
    }

    public void setTara(BigDecimal tara) {
        this.tara = tara;
    }

    public BigDecimal getNett() {
        return nett;
    }

    public void setNett(BigDecimal nett) {
        this.nett = nett;
    }

    @Override
    public String toString() {
        return "WeighingTableOverviewResponse{" +
                "dateTime='" + dateTime + '\'' +
                ", entry='" + entry + '\'' +
                ", departure='" + departure + '\'' +
                ", number=" + number +
                ", type='" + type + '\'' +
                ", partner='" + partner + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", material='" + material + '\'' +
                ", gross=" + gross +
                ", tara=" + tara +
                ", nett=" + nett +
                '}';
    }
}
