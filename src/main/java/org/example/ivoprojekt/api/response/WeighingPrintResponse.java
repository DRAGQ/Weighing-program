package org.example.ivoprojekt.api.response;

import java.math.BigDecimal;

public class WeighingPrintResponse {
    private String userName;
    private String userStreet;
    private String userPostcode;
    private String userTownship;

    private String partnerName;
    private String partnerStreet;
    private String partnerPostcode;
    private String partnerTownship;

    private String localDate;
    private String localTimeEntry;
    private String localTimeDeparture;

    private String identificationNumber;
    private String material;
    private String number;

    private String gross;
    private String tara;
    private String nett;
    private String description;
    private String type;

    public WeighingPrintResponse() {}

    public WeighingPrintResponse(String userName, String userStreet, String userPostcode, String userTownship, String partnerName, String partnerStreet, String partnerPostcode, String partnerTownship, String localDate, String localTimeEntry, String localTimeDeparture, String identificationNumber, String material, String number, String gross, String tara, String nett, String description, String type) {
        this.userName = userName;
        this.userStreet = userStreet;
        this.userPostcode = userPostcode;
        this.userTownship = userTownship;
        this.partnerName = partnerName;
        this.partnerStreet = partnerStreet;
        this.partnerPostcode = partnerPostcode;
        this.partnerTownship = partnerTownship;
        this.localDate = localDate;
        this.localTimeEntry = localTimeEntry;
        this.localTimeDeparture = localTimeDeparture;
        this.identificationNumber = identificationNumber;
        this.material = material;
        this.number = number;
        this.gross = gross;
        this.tara = tara;
        this.nett = nett;
        this.description = description;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public void setUserStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public String getUserPostcode() {
        return userPostcode;
    }

    public void setUserPostcode(String userPostcode) {
        this.userPostcode = userPostcode;
    }

    public String getUserTownship() {
        return userTownship;
    }

    public void setUserTownship(String userTownship) {
        this.userTownship = userTownship;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerStreet() {
        return partnerStreet;
    }

    public void setPartnerStreet(String partnerStreet) {
        this.partnerStreet = partnerStreet;
    }

    public String getPartnerPostcode() {
        return partnerPostcode;
    }

    public void setPartnerPostcode(String partnerPostcode) {
        this.partnerPostcode = partnerPostcode;
    }

    public String getPartnerTownship() {
        return partnerTownship;
    }

    public void setPartnerTownship(String partnerTownship) {
        this.partnerTownship = partnerTownship;
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

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public String getNett() {
        return nett;
    }

    public void setNett(String nett) {
        this.nett = nett;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WeighingPrintResponse{" +
                "userName='" + userName + '\'' +
                ", userStreet='" + userStreet + '\'' +
                ", userPostcode='" + userPostcode + '\'' +
                ", userTownship='" + userTownship + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", partnerStreet='" + partnerStreet + '\'' +
                ", partnerPostcode='" + partnerPostcode + '\'' +
                ", partnerTownship='" + partnerTownship + '\'' +
                ", localDate='" + localDate + '\'' +
                ", localTimeEntry='" + localTimeEntry + '\'' +
                ", localTimeDeparture='" + localTimeDeparture + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", material='" + material + '\'' +
                ", number='" + number + '\'' +
                ", gross='" + gross + '\'' +
                ", tara='" + tara + '\'' +
                ", nett='" + nett + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
