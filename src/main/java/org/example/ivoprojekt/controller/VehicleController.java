package org.example.ivoprojekt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.service.VehicleService;

public class VehicleController {
    private VehicleService vehicleService;
    private boolean isEdit;
    private Integer choosedId;
    private String choosedText;
    private String choosedNumber;
    private Double choosedTara;
    private Runnable onVehicleSuccess;

    @FXML
    private TextField textFieldNumber;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private TextField textFieldTara;

    @FXML
    private Button closeButton;

    @FXML
    void saveVehicle(ActionEvent event) {
        String inputText = textFieldDescription.getText();
        String inputNumber = textFieldNumber.getText();
        Double inputTara;
        try {
            inputTara = Double.parseDouble(this.textFieldTara.getText());
        } catch (NumberFormatException e) {
            this.textFieldTara.requestFocus();
            return;
        }

        if (checkValidInput(inputText, inputNumber)) {
            if (isEdit) {
                System.out.println("EDIT");
                //urobit update vehicle na zaklade
                //najskor zisitim ci su tam nejake zmeny
                //ak su zmeny tak to updatnem ak nie tak len zatvorim
                //neni tam nic unique

                if (!inputText.equals(this.choosedText) || !inputNumber.equals(this.choosedNumber) || !inputTara.equals(this.choosedTara)) {
                    this.vehicleService.updateVehicle(this.choosedId, inputNumber, inputText, inputTara);
                    if (onVehicleSuccess != null) {
                        onVehicleSuccess.run();
                    }
                } else {
                    System.out.println("Inputs are equal");
                }

            } else {
                this.vehicleService.saveVehicle(
                        new Vehicle(null, inputNumber, inputText, inputTara)

                );

                this.choosedNumber = inputNumber;
                this.choosedTara = inputTara;

                if (onVehicleSuccess != null) {
                    onVehicleSuccess.run();
                }
            }
            closeWindow(null);
        }
        else {
            System.out.println("bad inputs");
        }
    }

    public Vehicle getVehicleForChoiceBox() {
        return new Vehicle(null, this.choosedNumber, null, this.choosedTara);
    }

    private boolean checkValidInput(String selectedText, String selectedNumber) {
        if ( selectedNumber.isEmpty()) {
            this.textFieldNumber.requestFocus();
        } else if ( selectedText.isEmpty()) {
            this.textFieldDescription.requestFocus();
        } else if ( this.textFieldTara.getText().isEmpty()) {
            this.textFieldTara.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    @FXML
    void closeWindow(ActionEvent event) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void setChosenVehicle(Integer id, String number, String text) {
        this.choosedId = id;
        this.choosedNumber = number;
        this.choosedText = text;
        this.choosedTara = vehicleService.getTaraById(id);

        this.textFieldNumber.setText(number);
        this.textFieldDescription.setText(text);
        this.textFieldTara.setText(choosedTara.toString());
    }

    public void setOnVehicleSuccess(Runnable onVehicleSuccess) {
        this.onVehicleSuccess = onVehicleSuccess;
    }
}
