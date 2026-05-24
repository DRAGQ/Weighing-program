package org.example.ivoprojekt.controller.weighing;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;

public class PrintTemplateController {
    @FXML
    private Label arrivalDeparture;

    @FXML
    private VBox box;

    @FXML
    private HBox container;

    @FXML
    private Label customerName;

    @FXML
    private Label customerPostCode;

    @FXML
    private Label customerStreet;

    @FXML
    private Label description;

    @FXML
    private Label gross;

    @FXML
    private Label issued;

    @FXML
    private Label material;

    @FXML
    private Label net;

    @FXML
    private Label registrationNumber;

    @FXML
    private Label supplierName;

    @FXML
    private Label supplierPostCode;

    @FXML
    private Label supplierStreet;

    @FXML
    private Label tara;

    @FXML
    private Label weighingDate;

    @FXML
    private Label weighingNumber;

    public void setValuesToPrint(WeighingPrintResponse weighingPrintResponse, String issued) {
        setSupplierName(weighingPrintResponse.getUserName());
        setSupplierStreet(weighingPrintResponse.getUserStreet());
        setSupplierPostCode(weighingPrintResponse.getUserPostcode() + " " + weighingPrintResponse.getUserTownship());

        setCustomerName(weighingPrintResponse.getPartnerName());
        setCustomerStreet(weighingPrintResponse.getPartnerStreet());
        setCustomerPostCode(weighingPrintResponse.getPartnerPostcode() + " "  + weighingPrintResponse.getPartnerTownship());

        setWeighingDate(weighingPrintResponse.getLocalDate());
        setArrivalDeparture(weighingPrintResponse.getLocalTimeDeparture());
        setRegistrationNumber(weighingPrintResponse.getIdentificationNumber());
        setMaterial(weighingPrintResponse.getMaterial());

        setIssued(issued);
        setGross(weighingPrintResponse.getGross());
        setTara(weighingPrintResponse.getTara());
        setNet(weighingPrintResponse.getNett());

        setDescription(weighingPrintResponse.getDescription());
        setWeighingNumber("Vážny lístok č. " + weighingPrintResponse.getNumber());
    }

    public void setArrivalDeparture(String arrivalDeparture) {
        this.arrivalDeparture.setText(arrivalDeparture);
    }

    public void setCustomerName(String customerName) {
        this.customerName.setText(customerName);
    }

    public void setCustomerPostCode(String customerPostCode) {
        this.customerPostCode.setText(customerPostCode);
    }

    public void setCustomerStreet(String customerStreet) {
        this.customerStreet.setText(customerStreet);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setGross(String gross) {
        this.gross.setText(gross);
    }

    public void setIssued(String issued) {
        this.issued.setText(issued);
    }

    public void setMaterial(String material) {
        this.material.setText(material);
    }

    public void setNet(String net) {
        this.net.setText(net);
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber.setText(registrationNumber);
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.setText(supplierName);
    }

    public void setSupplierPostCode(String supplierPostCodeStreet) {
        this.supplierPostCode.setText(supplierPostCodeStreet);
    }

    public void setSupplierStreet(String supplierStreet) {
        this.supplierStreet.setText(supplierStreet);
    }

    public void setTara(String tara) {
        this.tara.setText(tara);
    }

    public void setWeighingDate(String weighingDate) {
        this.weighingDate.setText(weighingDate);
    }
    public void setWeighingNumber(String weighingNumber) {
        this.weighingNumber.setText(weighingNumber);
    }
}
