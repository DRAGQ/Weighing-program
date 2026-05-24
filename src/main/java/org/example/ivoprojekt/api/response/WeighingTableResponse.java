package org.example.ivoprojekt.api.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeighingTableResponse {
    private int number;
    private String type;
    private String dateTime;
    private String partnerName;
    private String vehicleIdentificationNumber;
    private String issuedName;
    private String material;
    private BigDecimal gross;
    private BigDecimal tara;
    private BigDecimal nett;
    private String description;

    public WeighingTableResponse() {}

    public WeighingTableResponse(int number, String type, String dateTime, String partnerName, String vehicleIdentificationNumber, String issuedName, String material, BigDecimal gross, BigDecimal tara, BigDecimal nett, String description) {
        this.number = number;
        this.type = type;
        this.dateTime = dateTime;
        this.partnerName = partnerName;
        this.vehicleIdentificationNumber = vehicleIdentificationNumber;
        this.issuedName = issuedName;
        this.material = material;
        this.gross = gross;
        this.tara = tara;
        this.nett = nett;
        this.description = description;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getVehicleIdentificationNumber() {
        return vehicleIdentificationNumber;
    }

    public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
        this.vehicleIdentificationNumber = vehicleIdentificationNumber;
    }

    public String getIssuedName() {
        return issuedName;
    }

    public void setIssuedName(String issuedName) {
        this.issuedName = issuedName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "WeighingTableResponse{" +
                "number=" + number +
                ", type='" + type + '\'' +
                ", dateTime=" + dateTime +
                ", partnerName='" + partnerName + '\'' +
                ", vehicleIdentificationNumber='" + vehicleIdentificationNumber + '\'' +
                ", issuedName='" + issuedName + '\'' +
                ", material='" + material + '\'' +
                ", gross=" + gross +
                ", tara=" + tara +
                ", nett=" + nett +
                ", description='" + description + '\'' +
                '}';
    }
}
