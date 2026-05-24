package org.example.ivoprojekt.api.response;

import java.math.BigDecimal;
import java.util.Objects;

public class WeighingUpdateResponse {
    private int number;
    private String localDate;
    private String localTimeEntry;
    private String localTimeDeparture;
    private String partnerName;
    private String vehicleIdentificationNumber;
    private String issuedName;
    private String material;
    private BigDecimal gross;
    private BigDecimal tara;
    private BigDecimal nett;
    private String description;

    public WeighingUpdateResponse() {}

    public WeighingUpdateResponse(int number, String localDate, String localTimeEntry, String localTimeDeparture, String partnerName, String vehicleIdentificationNumber, String issuedName, String material, BigDecimal gross, BigDecimal tara, BigDecimal nett, String description) {
        this.number = number;
        this.localDate = localDate;
        this.localTimeEntry = localTimeEntry;
        this.localTimeDeparture = localTimeDeparture;
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
        return "WeighingUpdateResponse{" +
                "number=" + number +
                ", localDate='" + localDate + '\'' +
                ", localTimeEntry='" + localTimeEntry + '\'' +
                ", localTimeDeparture='" + localTimeDeparture + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WeighingUpdateResponse that = (WeighingUpdateResponse) o;
        return number == that.number && Objects.equals(localDate, that.localDate) && Objects.equals(localTimeEntry, that.localTimeEntry) && Objects.equals(localTimeDeparture, that.localTimeDeparture) && Objects.equals(partnerName, that.partnerName) && Objects.equals(vehicleIdentificationNumber, that.vehicleIdentificationNumber) && Objects.equals(issuedName, that.issuedName) && Objects.equals(material, that.material) && Objects.equals(gross, that.gross) && Objects.equals(tara, that.tara) && Objects.equals(nett, that.nett) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, localDate, localTimeEntry, localTimeDeparture, partnerName, vehicleIdentificationNumber, issuedName, material, gross, tara, nett, description);
    }
}
