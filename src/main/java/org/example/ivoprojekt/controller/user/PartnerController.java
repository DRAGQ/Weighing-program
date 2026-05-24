package org.example.ivoprojekt.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.utill.PartnerType;
import org.example.ivoprojekt.domain.Partner;
import org.example.ivoprojekt.service.PartnerService;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class PartnerController implements Initializable {
    private PartnerService partnerService;
    private Partner chosenPartner;
    private boolean isEdit;
    private boolean shouldSaveToDatabase;
    private Consumer<Partner> onPartnerCreated;
    private List<TextField> textFields;
    private Runnable onPartnerSaved;
    private PartnerType type;
    private TextField errorTextField;
    private CheckBox errorCheckBox;

    @FXML
    private Button saveButton, closeButton;

    @FXML
    private CheckBox customerCheckBox, supplierCheckBox;

    @FXML
    private TextField businessIdTextField, nameTextField, postalCodeTextField, streetTextField, taxIdTextField, townShipTextField, vatTextField;

    @FXML
    public void closeWindow(ActionEvent event) {
        ((Stage) this.closeButton.getScene().getWindow()).close();
    }

    @FXML
    public void savePartner(ActionEvent event) {
        if (!isEmptyInput()) {
            Partner partner = createNewPartner();
            if (this.isEdit) {
                updatePartnerIfChanged(partner);
            }
            else if (this.shouldSaveToDatabase) {
                savePartnerToDatabase(partner);
            } else {
                sendPartnerBackToRegistration(partner);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.textFields = List.of(
                this.nameTextField, this.streetTextField, this.townShipTextField, this.postalCodeTextField, this.businessIdTextField, this.taxIdTextField, this.vatTextField
        );
        setTextFieldLengths();
    }

    private Partner createNewPartner() {
        setPartnerType();
        return new Partner(
                null, nameTextField.getText(), this.type.toString(), this.streetTextField.getText(), this.townShipTextField.getText(),
                this.postalCodeTextField.getText(), this.businessIdTextField.getText(), this.taxIdTextField.getText(), this.vatTextField.getText()
        );
    }

    private void setPartnerType() {
        if (this.supplierCheckBox.isSelected() && customerCheckBox.isSelected()) {
            this.type = PartnerType.BOTH;
        } else if (this.supplierCheckBox.isSelected()) {
            this.type = PartnerType.SUPPLIER;
        } else if (this.customerCheckBox.isSelected()) {
            this.type = PartnerType.CUSTOMER;
        } else {
            this.type = PartnerType.USER;
        }
    }

    private void updatePartnerIfChanged(Partner partner) {
        try {
            if (!partner.equals(this.chosenPartner)) {
                partner.setId(this.chosenPartner.getId());
                this.partnerService.updatePartner(partner);
                if (this.onPartnerSaved != null) {
                    this.onPartnerSaved.run();
                }
                closeWindow(null);
            }
        } catch (ValidationException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR,"Partner je null", e.getMessage());
        } catch (DatabaseException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR,"Nepodarilo sa upraviť partnera", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Neočakávaná chyba", "Failed to save user into database");
        }
    }

    private void savePartnerToDatabase(Partner partner) {
        try {
            this.partnerService.savePartner(partner);
            this.chosenPartner = partner;
            if (this.onPartnerSaved != null) {
                this.onPartnerSaved.run();
            }
            closeWindow(null);
        } catch (ValidationException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Partner je null", e.getMessage());
        }  catch (DatabaseException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nepodarilo sa uložiť partnera", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Neočakávaná chyba", "Failed to save user into database");
        }
    }

    private void  sendPartnerBackToRegistration(Partner partner) {
        if (this.onPartnerCreated != null) {
            this.onPartnerCreated.accept(partner);
        }
        closeWindow(null);
    }

    public void setPartnerService (PartnerService service) {
        this.partnerService = service;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void setSaveToDatabase(boolean saveToDatabase) {
        this.shouldSaveToDatabase = saveToDatabase;
    }

    public String getChosenPartnerName() {
        return this.chosenPartner.getName();
    }

    public void setChosenPartner(Integer id, Partner partner) {
        try {
            this.chosenPartner = id == null ? partner : this.partnerService.getPartnerById(id);
        } catch (DatabaseException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nepodarilo sa nájsť partnera podľa id", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Neočakávaná chyba", e.getMessage());
        }

        this.nameTextField.setText(this.chosenPartner.getName());
        this.streetTextField.setText(this.chosenPartner.getStreet());
        this.townShipTextField.setText(this.chosenPartner.getTownship());
        this.postalCodeTextField.setText(this.chosenPartner.getPostcode());
        this.businessIdTextField.setText(this.chosenPartner.getBusinessId());
        this.taxIdTextField.setText(this.chosenPartner.getTaxId());
        this.vatTextField.setText(this.chosenPartner.getVat());

        this.type = PartnerType.fromDatabase(this.chosenPartner.getType());
        switch(this.type) {
            case BOTH -> {
                this.supplierCheckBox.setSelected(true);
                this.customerCheckBox.setSelected(true);
            }
            case SUPPLIER ->
                this.supplierCheckBox.setSelected(true);
            case CUSTOMER ->
                this.customerCheckBox.setSelected(true);
            case USER ->
                disableCheckBoxes();
        }
    }

    public void disableCheckBoxes() {
        this.customerCheckBox.setDisable(true);
        this.supplierCheckBox.setDisable(true);
    }

    private void setTextFieldLengths() throws IllegalArgumentException {
        Map<TextField, Integer> limits = Map.of(
                this.nameTextField, 40,
                this.streetTextField, 30,
                this.townShipTextField, 30,
                this.postalCodeTextField, 10,
                this.businessIdTextField, 10,
                this.taxIdTextField, 10,
                this.vatTextField, 20
        );

        limits.forEach((field, limit) ->
                field.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.length() > limit) {
                        field.setText(newValue.substring(0, limit));
                    }
                })
        );
    }

    private boolean isEmptyInput() {
        if (this.errorTextField != null) {
            this.errorTextField.setStyle("-fx-focus-color: #039ED3;");
        }
        if (this.errorCheckBox != null) {
            this.errorCheckBox.setStyle("-fx-focus-color: #039ED3;");
        }

        for (TextField textField : this.textFields) {
            if (textField.getText().isEmpty()) {
                this.errorTextField = textField;
                textField.setStyle("-fx-focus-color:rgba(255,0,0,1);");
                textField.requestFocus();
                return true;
            }
        }
        if (!this.supplierCheckBox.isSelected() && !this.customerCheckBox.isSelected() && !this.supplierCheckBox.isDisabled()) {
            this.errorCheckBox = this.supplierCheckBox;
            this.supplierCheckBox.setStyle("-fx-focus-color:rgba(255,0,0,1);");
            this.supplierCheckBox.requestFocus();
            return true;
        }
        return false;
    }

    public void setOnCreateCallback(Consumer<Partner> callback) {
        this.onPartnerCreated = callback;
    }

    public void setOnPartnerSuccess(Runnable onPartnerSuccess) {
        this.onPartnerSaved = onPartnerSuccess;
    }
}
