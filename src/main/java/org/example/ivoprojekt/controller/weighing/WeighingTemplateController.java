package org.example.ivoprojekt.controller.weighing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.domain.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public class WeighingTemplateController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelTypeOfPartner, labelEntryVehicle, labelExtradictionVehicle;

    @FXML
    private HBox boxTara, boxGrossWeighing;

    @FXML
    private ChoiceBox<DialPartnerResponse> choiceBoxTypeOfPartner;

    @FXML
    private ChoiceBox<Vehicle> choiceBoxVehicle;

    @FXML
    private ChoiceBox<Material> choiceBoxMaterial;

    @FXML
    private Button addPartnerButton, addVehicleButton, addMaterialButton, addTaraButton, setActualEntryTimeButton,
            addWeightGrossButton, setActualDepartureTimeButton, saveWeighingButton, closeButton;

    @FXML
    private TextField textFieldTara;

    @FXML
    private Spinner<?> vehicleEntryTime;

    @FXML
    private TextField textFieldWeightGross;

    @FXML
    private Spinner<?> vehicleDepartureTime;

    @FXML
    private TextField textFieldWeightNet;

    @FXML
    private TextArea textAreaNote;

    @FXML
    private CheckBox checkBoxPrintWeighingTicket;

    @FXML
    void closeWindow(ActionEvent event) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    public void switchBoxes() {
        HBox taraParent = (HBox) boxTara.getParent();
        HBox grossParent = (HBox) boxGrossWeighing.getParent();

        taraParent.getChildren().remove(boxTara);
        grossParent.getChildren().remove(boxGrossWeighing);

        taraParent.getChildren().add(1,boxGrossWeighing);
        grossParent.getChildren().add(1, boxTara);
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public HBox getBoxTara() {
        return boxTara;
    }

    public HBox getBoxGrossWeighing() {
        return boxGrossWeighing;
    }

    public ChoiceBox<?> getChoiceBoxTypeOfPartner() {
        return choiceBoxTypeOfPartner;
    }

    public ChoiceBox<?> getChoiceBoxVehicle() {
        return choiceBoxVehicle;
    }

    public ChoiceBox<?> getChoiceBoxMaterial() {
        return choiceBoxMaterial;
    }

    public Button getAddPartnerButton() {
        return addPartnerButton;
    }

    public Button getAddVehicleButton() {
        return addVehicleButton;
    }

    public Button getAddMaterialButton() {
        return addMaterialButton;
    }

    public Button getAddTaraButton() {
        return addTaraButton;
    }

    public Button getSetActualEntryTimeButton() {
        return setActualEntryTimeButton;
    }

    public Button getAddWeightGrossButton() {
        return addWeightGrossButton;
    }

    public Button getSetActualDepartureTimeButton() {
        return setActualDepartureTimeButton;
    }

    public Button getSaveWeighingButton() {
        return saveWeighingButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public TextField getTextFieldTara() {
        return textFieldTara;
    }

    public Spinner<?> getVehicleEntryTime() {
        return vehicleEntryTime;
    }

    public TextField getTextFieldWeightGross() {
        return textFieldWeightGross;
    }

    public Spinner<?> getVehicleDepartureTime() {
        return vehicleDepartureTime;
    }

    public TextField getTextFieldWeightNet() {
        return textFieldWeightNet;
    }

    public TextArea getTextAreaNote() {
        return textAreaNote;
    }

    public CheckBox getCheckBoxPrintWeighingTicket() {
        return checkBoxPrintWeighingTicket;
    }

    public void setActualDatePicker() {
        this.datePicker.setValue(LocalDate.now());
    }

    public void setLabelTypeOfPartner(String text) {
        this.labelTypeOfPartner.setText(text);
    }

    public void setLabelEntryVehicle(String text) {
        this.labelEntryVehicle.setText(text);
    }

    public void setLabelExtradictionVehicle(String text) {
        this.labelExtradictionVehicle.setText(text);
    }

    private <T> void genericChoiceBox(ChoiceBox<T> choiceBox, List<T> items, Function<T, String> displayFunction) {
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T object) {
                return object != null ? displayFunction.apply(object) : "";
            }

            @Override
            public T fromString(String s) {
                return null;
            }
        });
        choiceBox.getItems().addAll(items);
    }

    public void setChoiceBoxTypeOfPartner(List<DialPartnerResponse> choices) {
        //takto dorobim aj ostatne choice boxy
        //tie 3 fxml subory partner material a vehicle by som mohol dat asi do 1 porozmyslam nad tym este
        genericChoiceBox(choiceBoxTypeOfPartner, choices, DialPartnerResponse::getName);
    }

    public void setChoiceBoxVehicle(List<Vehicle> choices) {
        genericChoiceBox(choiceBoxVehicle, choices, Vehicle::getIdentificationNumber);
        //choiceBoxVehicle.getItems().addAll(choices);
    }

    public void setChoiceBoxMaterial(List<Material> choices) {
        genericChoiceBox(choiceBoxMaterial, choices, Material::getName);
        //choiceBoxMaterial.getItems().addAll(choices);
    }

    public void setTextFieldTara() {
        this.textFieldTara.setText(Double.toString(choiceBoxVehicle.getValue().getTara()));
    }

    public void setTextFieldWeightNet(double weightingGross, double tara) {
        BigDecimal result = BigDecimal.valueOf(weightingGross - tara);
        System.out.println(result);
        this.textFieldWeightNet.setText(result.toString());
    }

    public void clearChoiceBoxTypeOfPartner() {
        this.choiceBoxTypeOfPartner.getItems().clear();
    }

    public void clearChoiceBoxVehicle() {
        this.choiceBoxVehicle.getItems().clear();
    }

    public void clearChoiceBoxMaterial() {
        this.choiceBoxMaterial.getItems().clear();
    }
}
