package org.example.ivoprojekt.controller.weighing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ivoprojekt.controller.VehicleController;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.service.VehicleService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class TaraController {
    private Stage stage;
    private VehicleService vehicleService;
    private List<Vehicle> vehicles;

    @FXML
    private Button closeButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField textFieldTara;

    @FXML
    void closeWindow(ActionEvent event) {
        this.stage.close();
    }

    @FXML
    void addVehicleButton(ActionEvent event) throws IOException {
        //FXMLLoader loader = NewWindow.openNewWindow("/fxml/user/Vehicle.fxml", "Údaje o vozidle");
        VehicleController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Vehicle.fxml", "Údaje o vozidle");
        controller.setVehicleService(this.vehicleService);
        controller.setIsEdit(false);
        controller.setOnVehicleSuccess(this::initTara);
    }

    @FXML
    void saveTara(ActionEvent event) {
        Vehicle vehicle = this.vehicles.stream().filter(v -> v.getIdentificationNumber().equals(choiceBox.getValue())).findFirst().orElse(null);
        if (vehicle != null) {
            double changedTara = Double.parseDouble(textFieldTara.getText());
            if (changedTara != vehicle.getTara()) {
                System.out.println(changedTara);
                this.vehicleService.updateTara(vehicle.getId(), changedTara);
                initTara();
                this.choiceBox.setValue(vehicle.getIdentificationNumber());
            } else {
                System.out.println("SAME");
            }

        } else {
            System.out.println("response not found!");
        }

    }

    public void initTara() {
        this.stage = (Stage) closeButton.getScene().getWindow();
        getAllVehicles();
        setChoiceBox();
    }

    private void getAllVehicles() {
        this.vehicles = vehicleService.getAllVehicles();
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    private void setChoiceBox() {
        choiceBox.getItems().clear();
        this.vehicles.forEach((vehicle) -> {
           choiceBox.getItems().add(vehicle.getIdentificationNumber());
        });

        choiceBox.setOnAction(this::showActualTara);
    }

    private void showActualTara(ActionEvent event) {
        DecimalFormat df = new DecimalFormat("#.##");
        //this.textFieldTara.setText(df.format(choiceBoxVehicle.getValue().getTara()));

        Double actualTara = vehicles.stream()
                .filter(vehicle -> vehicle.getIdentificationNumber().equals(choiceBox.getValue()))
                .map(Vehicle::getTara)
                .findFirst()
                .orElse(0.0);
        textFieldTara.setText(df.format(actualTara));
    }
}
