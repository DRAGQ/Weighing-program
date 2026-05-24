package org.example.ivoprojekt.domain;

import java.math.BigDecimal;

public class Weighing {
    private Integer id;
    private int number;
    private boolean type;
    private String localDate;
    private String localTimeEntry;
    private String localTimeDeparture;
//    private String partnerNameSnapshot;
//    private String vehicleIdentificationNumberSnapshot;
//    private String issuedNameSnapshot;
    private BigDecimal gross;
    private BigDecimal tara;
    private BigDecimal nett;
    private String description;
    private Integer userId;
    private Integer partnerId;
    private int vehicleId;
    private int materialId;

    public Weighing() {}

    public Weighing(Integer id, int number, boolean type, String localDate, String localTimeEntry, String localTimeDeparture, BigDecimal gross, BigDecimal tara, BigDecimal nett, String description, Integer userId, Integer partnerId, int vehicleId, int materialId) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.localDate = localDate;
        this.localTimeEntry = localTimeEntry;
        this.localTimeDeparture = localTimeDeparture;
        this.gross = gross;
        this.tara = tara;
        this.nett = nett;
        this.description = description;
        this.userId = userId;
        this.partnerId = partnerId;
        this.vehicleId = vehicleId;
        this.materialId = materialId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getLocalTimeEntry() {
        return localTimeEntry;
    }

    public void setLocalTimeEntry(String localTimeEntry) {
        this.localTimeEntry = localTimeEntry;
    }

    public String getLocalTimeDeparture() {
        return localTimeDeparture;
    }

    public void setLocalTimeDeparture(String localTimeDeparture) {
        this.localTimeDeparture = localTimeDeparture;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    @Override
    public String toString() {
        return "Weighing{" +
                "id=" + id +
                ", number=" + number +
                ", type=" + type +
                ", localDate=" + localDate +
                ", localTimeEntry=" + localTimeEntry +
                ", localTimeDeparture=" + localTimeDeparture +
                ", gross=" + gross +
                ", tara=" + tara +
                ", nett=" + nett +
                ", description='" + description + '\'' +
                ", partnerId=" + partnerId +
                ", vehicleId=" + vehicleId +
                ", materialId=" + materialId +
                '}';
    }
}
